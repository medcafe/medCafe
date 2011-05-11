/*
 * Copyright (C) 2009  Medsphere Systems Corporation
 * All rights reserved.
 *
 * This source code contains the intellectual property
 * of its copyright holder(s), and is made available
 * under a license. If you do not know the terms of
 * the license, please stop and do not read further.
 *
 * Please read LICENSES for detailed information about
 * the license this source code file is available under.
 * Questions should be directed to legal@medsphere.com
 *
 */
package com.medsphere.cia;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.Calendar;
import java.util.Map;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import com.medsphere.common.util.TimeKeeper;
import com.medsphere.common.util.TimeKeeperException;
import com.medsphere.vistarpc.RPCArray;
import com.medsphere.vistarpc.RPCConnection;
import com.medsphere.vistarpc.RPCException;
import com.medsphere.vistarpc.RPCResponse;
import com.medsphere.vistarpc.VistaRPC;
import com.medsphere.vistarpc.RPCResponse.ResponseType;

public class CIABrokerConnection implements RPCConnection {

    private final static byte EOD = (byte) 255;
    private final static String HEADER = "{CIA}";
    private Logger logger = Logger.getLogger(CIABrokerConnection.class);
    private Socket socket = null;
    private int sequence = 0;
    private ByteBuffer writeBuffer = null;
    private String uci = "0";
    private String context = "CIAV VUECENTRIC";
    private String duz = null;
    private StackTraceElement ste[] = null;
    private long lastExecute = 0;
    private boolean isClosed = false;
    private static final long PING_TIME = 45000;

    public CIABrokerConnection(String server, int port, String access, String verify, String token, String uci) throws RPCException {

        logger.debug("instatiating new CIABrokerConnection to " + server + ", " + port + " as " + access);
        if (server == null) {
            throw new RPCException("No server specified");
        }

        if (token == null) {
            if (access == null) {
                throw new RPCException("No access code specified");
            }
            if (verify == null) {
                throw new RPCException("No verify code specified");
            }
        }

        if (uci == null) {
            this.uci = "";
        } else {
            this.uci = uci;
        }
        InetAddress addr;
        String computername;
        String myAddress;
        try {
            addr = InetAddress.getByName(server);
            socket = new Socket(addr, port);
            socket.setTcpNoDelay(true);
            computername = InetAddress.getLocalHost().getHostName();
            // CIA Broker uses the address to call LOG^XUS1, which in turn
            //  calls COOKIE^XUS1, which calls CMD^XWBCAGNT, which tries to
            //  open a socket connection back to this address. The 3-second
            //  timeout when this fails is bad enough, but when the client
            //  is a Windows machine with the firewall up, it takes roughly
            //  three minutes timeout. (GTM/Linux) So, we give it an invalid
            //  address so it bounces straight away.
            // myAddress = socket.getLocalAddress().getHostAddress();
            myAddress = "NOTVALID";
        } catch (IOException ex) {
            socket = null;
            throw new RPCException(ex.getLocalizedMessage(), ex);
        }
        execute(new CIAConnectAction(myAddress, uci));

        String authToken = token;
        if (token == null || token.isEmpty()) {
            logger.debug("No token so using access;verify: " + access + ";" + verify);
            authToken = VistaRPC.encrypt(access + ";" + verify);
        } else {
            logger.debug ("Using token: " + token);
        }

        VistaRPC authRPC = new VistaRPC("CIANBRPC AUTH", ResponseType.ARRAY);
        authRPC.setParam(1, context);
        authRPC.setParam(2, computername);
        authRPC.setParam(3, "");
        authRPC.setParam(4, authToken);
        authRPC.setParam(5, myAddress);
        RPCResponse authResponse = execute(authRPC);
        String[] lines = authResponse.getArray();
        String[] status = lines[0].split(("\\^"));
        if (!status[0].equals("0")) {
            logger.debug("Failed to log in" + (status.length > 1 ? ": " + status[status.length-1] : ""));
            throw new RPCException("Could not log in" + (status.length > 1 ? ": " + status[status.length-1] : ""));
        }
        this.uci = lines[1].substring(0, lines[1].indexOf("^"));
        new Pinger(this, PING_TIME);
    }

    public void close() throws RPCException {
        execute(new CIADisconnectAction(uci));
        ste = Thread.currentThread().getStackTrace();
        isClosed = true;
//        try {
//            socket.close();
//        } catch (IOException ex) {
//            ex.printStackTrace();
//        }
    }

    public void setContext(String context) throws RPCException {
        this.context = context;
    }

    public String buildSubscript(String string) {
        return string;
    }

    public String getDUZ() {
        if (duz==null) {
            duz = "";
            VistaRPC duzRPC = new VistaRPC("CIANBRPC GETINFO", ResponseType.ARRAY);
            duzRPC.setParam(1, "2");
            try {
                RPCResponse response = execute(duzRPC);
                for (String string : response.getArray()) {
                    String[] parts = string.split("\\^");
                    if (parts[1].equals("DUZ")) {
                        duz = parts[2];
                        break;
                    }
                }
            } catch (RPCException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return duz;
    }

    private void writeHeader() {
        write(HEADER);
    }

    private void writeEOD() {
        write(EOD);
    }

    private void writeSequence() {
        if (++sequence == 256) {
            sequence = 1;
        }
        write((byte) sequence);
    }

    private void writeCommand(CIAAction action) {
        write(action.getCommand());
    }

    private void writeParams(CIAAction action) throws RPCException {
        Map<String, Object> params = action.getParams();
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            Object paramValue = entry.getValue();
            String key = entry.getKey();
            if (paramValue instanceof String) {
                writeStringParam(key, (String) paramValue);
            } else if (paramValue instanceof RPCArray) {
                writeArrayParam(key, (RPCArray) paramValue);
            } else {
                // Reference paramters not supported yet
                throw new RPCException("Unsupported parameter type: " + paramValue.getClass().getName());
            }
        }
    }

    private void writeStringParam(String key, String value) {
        lwrite(key);
        write((byte) 0);
        lwrite(value);
    }

    private void writeArrayParam(String key, RPCArray array) {
        for (Map.Entry<String, String> entry : array.entrySet()) {
            lwrite(key);
            String paramIndex = entry.getKey();
            String paramValue = entry.getValue();
            lwrite(paramIndex);
            lwrite(paramValue);
        }
    }

    private void lwrite(String string) {
        int len = string.length();
        int low = len % 16;
        int highCount = 0;
        len = len >>> 4;
        byte[] bytes = new byte[4];
        while (len != 0) {
            bytes[highCount] = (byte) (len & 0xFF);
            ++highCount;
            len = len >>> 8;
        }
        write((byte) ((highCount << 4) + low));
        for (int idx = highCount - 1; idx >= 0; --idx) {
            write(bytes[idx]);
        }
        write(string);
    }

    private ByteBuffer getBuffer(int requestedSize) {
        if (writeBuffer == null) {
            writeBuffer = ByteBuffer.allocate(requestedSize + 1024);
        } else if (writeBuffer.remaining() < requestedSize) {
            ByteBuffer tmp = ByteBuffer.allocate(writeBuffer.capacity() + requestedSize + 1024);
            tmp.put(writeBuffer.array(), 0, writeBuffer.position());
            writeBuffer = tmp;
        }
        return writeBuffer;
    }

    private void write(byte b) {
        getBuffer(1).put(b);
    }

    private void write(String string) {
        byte[] buf;
        try {
            buf = string.getBytes("UTF-8");
            getBuffer(buf.length).put(buf);
        } catch (UnsupportedEncodingException ex) {
            // Won't happen
        }
    }

    private boolean flush() {
        if (ste!=null) {
            System.out.println("################# writing to closed");
            System.out.println("################# This is thread "+Thread.currentThread().getId());
            for (StackTraceElement element : ste) {
                System.out.println("Closed############ " + element.toString());
            }
            for (StackTraceElement element : Thread.currentThread().getStackTrace()) {
                System.out.println("Now   ############ " + element.toString());
            }
        }
        boolean retVal = false;
        try {
            int pos = writeBuffer.position();
            byte writeArray[] = new byte[pos];
            writeBuffer.position(0);
            writeBuffer.get(writeArray, 0, pos);
            socket.getOutputStream().write(writeArray);
            writeBuffer = null;
            retVal = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return retVal;
    }

    long tryPing() {
        try {
            if (isClosed) {
                return -1;
            }
            long now = Calendar.getInstance().getTimeInMillis();
            long waitTime = PING_TIME - (now-lastExecute);
            if (waitTime>=2000) {
                return waitTime;
            }
            CIAAction action = new CIAPingAction();
            execute(action);
            return PING_TIME;
        } catch (RPCException ex) {
            return -1;
        }
    }

    private synchronized String execute(CIAAction action) throws RPCException {
        writeHeader();
        writeEOD();
        writeSequence();
        writeCommand(action);
        writeParams(action);
        writeEOD();
        flush();
        lastExecute = Calendar.getInstance().getTimeInMillis();
        return getResponse();
    }

    public synchronized RPCResponse execute(VistaRPC rpc) throws RPCException {
        String tag = "CIA RPC " + rpc.toString();
        TimeKeeper tk = new TimeKeeper();
        try {
            try {
                if (CIABrokerTimer.canLog()) {
                    tk.start(tag);
                }
            } catch (TimeKeeperException e) {
                e.printStackTrace();
            }
            CIAAction action = new CIARPCAction();
            action.addParam("CTX", context);
            action.addParam("UID", uci);
            action.addParam("VER", "0");
            action.addParam("RPC", rpc.getName());
            for (Map.Entry<Integer, Object> entry : rpc.getParams().entrySet()) {
                int paramNumber = entry.getKey();
                action.addParam(String.valueOf(paramNumber), entry.getValue());
            }
            String result = execute(action);
            RPCResponse retVal = null;
            switch (rpc.getType()) {
                case SINGLE_VALUE:
                case GLOBAL_INSTANCE:
                    retVal = new RPCResponse(result);
                    break;
                case GLOBAL_ARRAY:
                case WORD_PROCESSING:
                case ARRAY:
                    retVal = new RPCResponse(result.split("\r"));
            }
            return retVal;
        } finally {
            try {
                if (CIABrokerTimer.canLog()) {
                    tk.stop(tag);
                    CIABrokerTimer.log(tk.getDisplayStringWithTimeFirst(tag));
                }
            } catch (TimeKeeperException e) {
                e.printStackTrace();
            }

        }
    }

    private String getResponse() throws RPCException {
        DataInputStream stream = null;
        boolean status;
        byte in;
        int sequence_in;
        StringBuffer sb = new StringBuffer();
        try {
            stream = new DataInputStream(socket.getInputStream());
            sequence_in = ((int)stream.readByte()) & 0xFF;
            if (sequence_in!=sequence) {
                logger.warn("Sequence number mismatch. Expected " + sequence + " but got " + sequence_in);
            }
            status = (stream.readByte() == 0);
            if (status || stream.available()!=0) {
                while (true) {
                    in = stream.readByte();
                    if (in == -1) {
                        break;
                    }
                    sb.append((char) in);
                }
            } else {
                sb.append("Status returned 0 with no following message");
            }
        } catch (IOException ex) {
            throw new RPCException("IO error: " + ex.getLocalizedMessage(), ex);
        }
        if (!status) {
            logger.debug("Failed status from RPC: " + sb.toString());
            throw new RPCException("Server error: " + sb.toString());
        }
        return sb.toString();
    }

    public static class CIABrokerTimer {
        private static Logger logger = Logger.getLogger(CIABrokerTimer.class);
        public static void log(String msg) {
            logger.trace(msg);
        }
        private static boolean canLog() {
            return logger.getLevel() == Level.TRACE;
        }
    }

}
