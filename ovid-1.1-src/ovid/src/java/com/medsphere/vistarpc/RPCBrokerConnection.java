// <editor-fold defaultstate="collapsed">
/*
 * Copyright (C) 2007-2009  Medsphere Systems Corporation
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
// </editor-fold>

package com.medsphere.vistarpc;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.medsphere.vistarpc.RPCResponse.ResponseType;

public class RPCBrokerConnection extends AbstractRPCConnection {
//  Header chunk types

    private final static String CHUNK_TYPE_HEADER = "1";
    private final static String CHUNK_TYPE_RPC = "2";
    private final static String CHUNK_TYPE_SECURITY = "3";
    private final static String CHUNK_TYPE_COMMAND = "4";
    private final static String CHUNK_TYPE_DATA = "5";
    private final static String XWB_HEADER = "[XWB]";
//  Header and protocol info
    private final static String VISTA_RPC_VERSION = "1";
    private final static String VISTA_RPC_TYPE_CMD = "0";
    private final static String VISTA_RPC_TYPE_RPC = "1";
    private final static int VISTA_RPC_LENV = 3;
    private final static String VISTA_RPC_LENV_STR = Integer.toString(VISTA_RPC_LENV);
    private final static String VISTA_RPC_RETURN_DATA = "0";
    private final static String VISTA_RPC_NO_RETURN_DATA = "1";
    private final static byte END_MARKER = 4;
//  Parameter types
    private final static String PARAM_LITERAL_MARKER = "0";
    private final static String PARAM_REFERENCE_MARKER = "1";
    private final static String PARAM_LIST_MARKER = "2";
    private final static String PARAM_GLOBAL_MARKER = "3";
    private final static String PARAM_EMPTY_MARKER = "4";
    private boolean sentConnect = false;
    private boolean signedOn = false;
    private String host;
    private int port;
    private String access;
    private String verify;
    private String token;
    private Socket socket = null;
    private String encryptedAV = null;
    private ByteBuffer writeBuffer = null;
    private String duz = "0";
    private Logger logger = LoggerFactory.getLogger(RPCBrokerConnection.class);

    public RPCBrokerConnection(String host, int port, String access, String verify) throws RPCException {
        logger.debug("instatiating new RPCBrokerConnection to " + host + ", " + port + " as " + access);
        if (host == null) {
            throw new RPCException("No host specified");
        }
        if (access == null) {
            throw new RPCException("No access code specified");
        }
        if (verify == null) {
            throw new RPCException("No verify code specified");
        }
        connect(host, port, access, verify, null);
    }

    public RPCBrokerConnection(String host, int port, String token) throws RPCException {
        logger.debug("instatiating new RPCBrokerConnection to " + host + ", " + port + " with " + token);
        if (host == null) {
            throw new RPCException("No host specified");
        }
        if (token == null) {
            throw new RPCException("No token specified");
        }

        connect(host, port, null, null, token);
    }

    private void connect(String host, int port, String access, String verify, String token) throws RPCException {
        this.host = host;
        this.port = port;
        this.access = access;
        this.verify = verify;
        this.token = token;
        for (int i = 0; i < 3; ++i) {
            if (connect()) {
                return;
            }
        }
        throw new RPCException("Could not connect");

    }

    private boolean connect() throws RPCException {
        boolean retVal = true;
        if (isConnected()) {
            logger.debug(this + " already connected, closing first");
            close();
        }
        try {
            InetAddress addr = InetAddress.getByName(host);
            socket = new Socket(addr, port);
            socket.setTcpNoDelay(true);
            VistaRPC tcpConnect = new VistaRPC("TCPConnect", ResponseType.SINGLE_VALUE, true);
            tcpConnect.setParam(1, socket.getLocalAddress().getHostAddress());
            tcpConnect.setParam(2, "0"); // callback port ?
            tcpConnect.setParam(3, "OVID"); // ?
            RPCResponse connectResponse = execute(tcpConnect);
            if (connectResponse == null || connectResponse.getString() == null || connectResponse.getString().equals("reject")) {
                throw new RPCException("Handshake error");
            }
            if (connectResponse.getError() != null) {
                throw new RPCException("RPC Error: " + connectResponse.getError());
            }
            sentConnect = true;

            signOn();
            retVal = logIn();
        } catch (UnknownHostException e) {
            socket = null;
            throw new RPCException(e.getLocalizedMessage(), e);
        } catch (IOException e) {
            socket = null; // failed connection, so we may try again.
            retVal = false;
        } catch (SecurityException e) {
            socket = null;
            throw new RPCException(e.getLocalizedMessage(), e);
        }

        return retVal;
    }

    private void signOn() throws RPCException {
        RPCResponse signonResponse = execute(new VistaRPC("XUS SIGNON SETUP", ResponseType.ARRAY));
        if (signonResponse == null || signonResponse.getError() != null) {
            throw new RPCException("XUS SIGNON SETUP failed");
        }
        signedOn = true;
    }

    private boolean logIn() throws RPCException {
        VistaRPC xusAvCode = new VistaRPC("XUS AV CODE", ResponseType.ARRAY);
        duz = "0";

        if (encryptedAV == null) {
            if (token != null) {
                encryptedAV = token;
            } else {
                encryptedAV = VistaRPC.encrypt(access + ";" + verify);
            }

        }
        xusAvCode.setParam(1, encryptedAV);
        RPCResponse response = execute(xusAvCode);
        if (response == null) {
            return false;
        }
        boolean hasErrors = (response.getError() != null);
        String answer[] = response.getArray();
        if (!hasErrors) {
            if (answer.length == 0) {
                hasErrors = true;
            } else {
                hasErrors = (answer[0].equals("0"));
            }
        }
        if (hasErrors) {
            String brokerInfo = extractBrokerErrorInfo(response);
            throw new RPCException("Access denied: " + brokerInfo);
        } else {
            duz = answer[0];
        }

        return true;
    }

    private String extractBrokerErrorInfo(RPCResponse response) {
        StringBuilder sb = new StringBuilder("");
        if (response != null && response.getArray() != null) {
            for (String answer : response.getArray()) {
                if (answer != null && answer.length() > 0 && !answer.matches("^(\\d+)$")) {
                    if (sb.length() != 0) {
                        sb.append(" : ");
                    }
                    sb.append(answer);
                }
            }
        }

        return sb.toString();
    }

    public void close() throws RPCException {
        logger.debug("asked to close " + this + " ... isConnected() == " + isConnected());
        if (isConnected()) {
            try {
                if (sentConnect) {
                    sentConnect = false;
                    VistaRPC bye = new VistaRPC("#BYE#", ResponseType.SINGLE_VALUE, true);
                    execute(bye);
                }
                socket.close();
            } catch (IOException e) {
                // do nothing
            } finally {
                socket = null;
            }
        }
    }

    private boolean isConnected() {
        return socket != null && socket.isConnected();
    }

    public String buildSubscript(String string) {
        return /* "\r" + */ string;
    }

    private synchronized RPCResponse executeOnce(VistaRPC rpc) throws RPCException {
        RPCResponse retVal = null;
        writeProtocol();
        writeCommand(rpc);
        writeParams(rpc);
        write(END_MARKER);
        if (flush()) {
            retVal = getResponse(rpc.getType());
        }
        return retVal;
    }

    private void write(byte data) {
        writeBuffer.put(data);
    }

    private void writeProtocol() {
        write(XWB_HEADER + VISTA_RPC_VERSION + VISTA_RPC_TYPE_RPC + VISTA_RPC_LENV_STR + VISTA_RPC_RETURN_DATA);
    }

    private void writeCommand(VistaRPC rpc) {
        if (rpc.isCommand()) {
            write(CHUNK_TYPE_COMMAND);
        } else {
            write(CHUNK_TYPE_RPC);
            writeSPack(VISTA_RPC_VERSION);
        }
        writeSPack(rpc.getName());
    }

    private void writeSPack(String value) {
        write((byte) value.length());
        write(value);
    }

    private void writeLPack(String string) {
        String len = Integer.toString(string.length());
        while (len.length() < VISTA_RPC_LENV) {
            len = "0" + len;
        }
        write(len);
        write(string);
    }

    private void write(byte[] buf) {
        if (writeBuffer == null) {
            writeBuffer = ByteBuffer.allocate(buf.length + 1024);
        } else if (writeBuffer.remaining() < buf.length) {
            ByteBuffer tmp = ByteBuffer.allocate(writeBuffer.capacity() + buf.length + 1024);
            tmp.put(writeBuffer.array(), 0, writeBuffer.position());
            writeBuffer = tmp;
        }
        writeBuffer.put(buf);
    }

    private void write(String value) {
        try {
            write(value.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            // won't happen
        }
    }

    private void writeParams(VistaRPC rpc) throws RPCException {
        write(CHUNK_TYPE_DATA);

        Map<Integer, Object> params = rpc.getParams();
        if (!params.isEmpty()) {
            int position = 1;
            for (Map.Entry<Integer, Object> entry : params.entrySet()) {
                int paramNumber = entry.getKey();
                Object paramValue = entry.getValue();
                while (position < paramNumber) {
                    writeEmptyParam();
                    ++position;
                }
                if (paramValue instanceof String) {
                    writeStringParam((String) paramValue);
                } else if (paramValue instanceof RPCArray) {
                    writeArrayParam((RPCArray) paramValue);
                } else {
                    // Reference paramters not supported yet
                    throw new RPCException("Unsupported parameter type: " + paramValue.getClass().getName());
                }
            }
        } else {
            writeEmptyParam();
        }
        //No end marker. Chunk 5 is assumed to be the last.
    }

    private void writeArrayParam(RPCArray array) {
        write(PARAM_LIST_MARKER);
        int size = array.size();
        int index = 0;
        for (Map.Entry<String, String> entry : array.entrySet()) {
            String paramIndex = entry.getKey();
            String paramValue = entry.getValue();
            writeLPack(paramIndex);
            writeLPack(paramValue);
            ++index;
            if (index != size) {
                write("t");
            }
        }
        write("f");
    }

    private void writeStringParam(String string) {
        write(PARAM_LITERAL_MARKER);
        writeLPack(string);
        write("f");
    }

    private void writeEmptyParam() {
        write(PARAM_EMPTY_MARKER);
        write("f");
    }

    private boolean flush() throws RPCException {
        boolean retVal = false;
        try {
            if (isConnected() || connect()) {
                int pos = writeBuffer.position();
                byte writeArray[] = new byte[pos];
                writeBuffer.position(0);
                writeBuffer.get(writeArray, 0, pos);
                socket.getOutputStream().write(writeArray);
                writeBuffer = null;
                retVal = true;
            }
        } catch (IOException ex) {
            throw new RPCException("Got IO exception", ex);
        }
        return retVal;
    }

    private RPCResponse getResponse(ResponseType rpcType) {
        Boolean hadError[] = {false};
        String securityError = readSPack(hadError);
        if (hadError[0]) {
            return null;
        }
        if (securityError.isEmpty()) {
            securityError = null;
        }
        String otherError = readSPack(hadError);
        if (hadError[0]) {
            return null;
        }
        if (otherError.isEmpty()) {
            otherError = null;
        }

        RPCResponse retVal = null;
        switch (rpcType) {
            case SINGLE_VALUE:
            case GLOBAL_INSTANCE:
                retVal = new RPCResponse(readString(null, hadError));
                if (hadError[0]) {
                    return null;
                }
                break;
            case GLOBAL_ARRAY:
            case WORD_PROCESSING:
            case ARRAY:
                retVal = new RPCResponse(readArray(hadError));
                if (hadError[0]) {
                    return null;
                }
        }
        if (retVal != null && (otherError != null || securityError != null)) {
            if (otherError != null && securityError != null) {
                retVal.setError(otherError + "^" + securityError);
            } else if (otherError != null) {
                retVal.setError(otherError);
            } else {
                retVal.setError(securityError);
            }
        }
        return retVal;
    }

    private String[] readArray(Boolean[] hadError) {
        if (hadError != null) {
            hadError[0] = false;
        }
        ArrayList<String> arrayList = new ArrayList<String>();
        Boolean endMarker[] = {false};
        for (;;) {
            String newString = readString(endMarker, hadError);
            if (newString == null) {
                if (hadError != null) {
                    hadError[0] = true;
                }
                break;
            }
            if (endMarker[0]) {
                break;
            }
            arrayList.add(newString);
        }
        return arrayList.toArray(new String[arrayList.size()]);
    }

    private String readString(Boolean endMarker[], Boolean hadError[]) {
        if (endMarker != null) {
            endMarker[0] = !isConnected();
        }
        if (hadError != null) {
            hadError[0] = !isConnected();
        }
        if (!isConnected()) {
            return null;
        }
        ByteBuffer buffer = ByteBuffer.allocate(1024);

        try {
            String dbg = new String();
            DataInputStream inStream = new DataInputStream(socket.getInputStream());
            boolean gotCR = false;
            for (;;) {
                byte in = inStream.readByte();
                if (in == END_MARKER) {
                    if (endMarker != null) {
                        endMarker[0] = true;
                    }
                    break;
                }
                if (gotCR && in == 10) {
                    break;
                }
                if (in == 13) {
                    gotCR = true;
                } else {
                    if (gotCR) {
                        buffer.put((byte) 13);
                        gotCR = false;
                    }
                    dbg = dbg + (char) in;
                    buffer.put(in);
                }
            }
        } catch (IOException e) {
            if (endMarker != null) {
                endMarker[0] = true;
            }
            if (hadError != null) {
                hadError[0] = true;
            }
            return null;
        }
        String retVal = null;
        try {
            retVal = new String(buffer.array(), 0, buffer.position(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            // doesn't happen
            return null;
        }
        return retVal;
    }

    private String readSPack(Boolean[] hadError) {
        if (hadError != null) {
            hadError[0] = false;
        }
        try {
            DataInputStream inStream = new DataInputStream(socket.getInputStream());
            byte len = inStream.readByte();
            byte[] data = new byte[len];
            inStream.readFully(data);
            return new String(data, "US-ASCII");
        } catch (IOException e) {
            if (hadError != null) {
                hadError[0] = true;
            }
            return null;
        }
    }

    public synchronized RPCResponse execute(VistaRPC rpc) throws RPCException {
        RPCResponse retVal = executeOnce(rpc);
        if (retVal == null && signedOn) {
            // reconnect
            if (rpc.getName().equals("#BYE#")) {
                // We're disconnecting anyway
                return null;
            }
            // attempt to log back on
            if (connect()) {
                if (currentContext != null) {
                    String context = currentContext;
                    currentContext = null;
                    setContext(context);
                }
                retVal = executeOnce(rpc);
            }
        }
        if (retVal == null) {
            throw new RPCException("Lost connection to server");
        }
        return retVal;
    }
    private String currentContext = null;

    public synchronized void setContext(String context) throws RPCException {

        if (context.equals(currentContext)) {
            logger.debug("context is already set to " + context + "...");
            return;
        }
        if (currentContext != null) {
            logger.debug("changing context from " + currentContext + " to " + context);
        }

        logger.debug("Setting context to: " + context);
        VistaRPC xwbCreateContext = new VistaRPC("XWB CREATE CONTEXT", ResponseType.SINGLE_VALUE);
        String encryptedContext = VistaRPC.encrypt(context);
        
        xwbCreateContext.setParam(1, encryptedContext);
        RPCResponse response = execute(xwbCreateContext);
        if (response == null || response.getError() != null) {
            throw new RPCException("XWB CREATE CONTEXT failed: " + response.getError());
        }
        currentContext = context;
    }

    @Override
    public String getDUZ() {
        return duz;
    }

}
