// <editor-fold defaultstate="collapsed">
/*
 * Copyright (C) 2011  Medsphere Systems Corporation
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
package com.medsphere.ovid.domain.lab;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.medsphere.fileman.FMUtil;
import com.medsphere.fmdomain.FMLaboratoryTest;
import com.medsphere.ovid.domain.ov.OvidDomainException;
import com.medsphere.ovid.model.domain.patient.IsAPatientItem;
import com.medsphere.ovid.model.domain.patient.PatientResult;
import com.medsphere.ovid.model.domain.patient.ResultDetail;
import com.medsphere.vistarpc.RPCBrokerConnection;
import com.medsphere.vistarpc.RPCConnection;
import com.medsphere.vistarpc.RPCException;
import com.medsphere.vistarpc.RPCResponse;
import com.medsphere.vistarpc.VistaRPC;
import com.medsphere.vistarpc.RPCResponse.ResponseType;
import java.util.logging.Level;

/**
 * Get lab results using MSC KMR LABS DETAIL, if available, or ORRC RESULTS BY DATE if not.
 * 
 */
public class LabResultRepository extends LabRepository {

    private Logger logger = LoggerFactory.getLogger(LabResultRepository.class);
    private static String resultsRPCName = null;
    
	public LabResultRepository(RPCConnection connection, RPCConnection serverConnection) {
		super(connection, serverConnection);
		
	}
	
	/**
	 * get lab results for a dfn within a date range.
	 * @param dfn
	 * @param beginDate
	 * @param endDate
	 * @return
	 * @throws OvidDomainException
	 */
    @Override
    public Collection<IsAnOVLabResult> getResults(String patientDfn, Date startDate, Date stopDate) throws OvidDomainException {
    	return getResults(patientDfn, startDate, stopDate, false);
    }
	
    /**
     * get lab results for a dfn within a date range.
	 * @param dfn
	 * @param beginDate
	 * @param endDate
	 * @return
	 * @throws OvidDomainException
     */
	@Override
    public Collection<IsAnOVLabResult> getResults(String patientDfn, Date startDate, Date stopDate, boolean resolveLabTests) throws OvidDomainException {
        Collection<IsAnOVLabResult> collection = new ArrayList<IsAnOVLabResult>();
        String fmContext = "OR CPRS GUI CHART";
        RPCConnection rpcConnection;
        try {
            rpcConnection = getConnection();
            rpcConnection.setContext(fmContext);
        } catch (RPCException e) {
            throw new OvidDomainException(e);
        }
        
        VistaRPC rpc = new VistaRPC(getResultsRPCName(), ResponseType.ARRAY);        
        rpc.setParam(1, patientDfn);        
        rpc.setParam(2, FMUtil.dateToFMDate(startDate));
        rpc.setParam(3, FMUtil.dateToFMDate(stopDate));
        
        String items[] = null;
        try {
            RPCResponse response = rpcConnection.execute(rpc);
            items = response.getArray();
            for (String s : response.getArray()) {
            	System.out.println(s);
            }
            if (isEmptyResult(items)) {
                return collection;
            }
            IsAPatientItem item = null;

            for (int idx = 0; idx<items.length; ++idx) {                
                if (items[idx].startsWith("Item=")) {
                    if (item != null) {
                        collection.add((IsAnOVLabResult)item);
                        item = null;
                    }
                    String parts[] = items[idx].split("\\^",-1);
                    String id = parts[0].substring(5, parts[0].length());
                    String message = parts[1];
                    String datePart = parts[2];

                    if ("-1".equals(datePart)) {

                    } else if (datePart.length()!=19) {
                        // Pad zeros for missing HH, mm, or ss
                        StringBuilder sb = new StringBuilder(datePart.substring(0, datePart.length()-5));
                        for (int i=19-datePart.length(); i>0; --i) {
                            sb.append("0");
                        }
                        sb.append(datePart.substring(datePart.length()-5));
                        datePart = sb.toString();
                    }

                    Date dateTime;
                    try {
                        dateTime = parseHL7TimeFormat(datePart);
                    } catch (ParseException ex) {
                        dateTime = null;
                    }
                    item = new OVLabResult(id, message, "", dateTime);
                    ((OVLabResult)item).addPanelInfo(parts);
                    
                } else if (items[idx].startsWith("Text=")) {
                    ((PatientResult) item).addText(items[idx].substring(5, items[idx].length()));
                } else if (items[idx].startsWith("Cmnt=")) {
                    ((PatientResult) item).addComment(items[idx].substring(5, items[idx].length()));
                }  else if (items[idx].startsWith("Data=")) {
                    String parts[] = items[idx].split("\\^",-1);
                    String testName = parts[0].substring(5, parts[0].length());
                    String value = parts[1];
                    String units = parts[2];
                    String referenceRange = parts[3];
                    String indicator = parts[4];
                    String labTestIEN = null;
                    String labTestLoincCode = null;
                    if (parts.length > 5) {
                    	String[] tstParts = parts[5].split(";", -1);
                    	if (tstParts.length > 0) {
                    		labTestIEN = tstParts[0];
                    	}
                    	if (tstParts.length > 1) {
                    		labTestLoincCode = tstParts[1];
                    	}
                    }
                    ((PatientResult) item).addDetail(new ResultDetail(testName, value, units, referenceRange, indicator, labTestIEN, labTestLoincCode));
                }
            }
            if (item != null) {
                collection.add((IsAnOVLabResult)item);
            }

        } catch (RPCException ex) {
            throw new OvidDomainException(ex);
        }

        if (resolveLabTests) {
        	resolveLabTests(collection);
        }
        return collection;
    }

	private String getResultsRPCName() {		
		if (resultsRPCName == null) {
			resultsRPCName = "ORRC RESULTS BY DATE";
			RPCConnection rpcConnection = getConnection();
			VistaRPC rpc = new VistaRPC("XWB IS RPC AVAILABLE", ResponseType.SINGLE_VALUE);        
	        rpc.setParam(1, "MSC KMR LABS DETAIL");
	        try {
				RPCResponse response = rpcConnection.execute(rpc);
				if (response == null || response.getError() != null) {
					throw new RPCException(response.getError());
				} else {
					String result = response.getString();
					if ("1".equals(result)) {
						resultsRPCName = "MSC KMR LABS DETAIL";
					}
				}
			} catch (RPCException e) {
				e.printStackTrace();
			}
		}
		return resultsRPCName;
	}
	
    private void resolveLabTests(Collection<IsAnOVLabResult> collection) throws OvidDomainException {
    	Collection<String> labIens = new ArrayList<String>();
    	if (collection != null) {
    		for (IsAnOVLabResult item : collection) {    			
    			
    				if (item.getLabTestIEN() != null) {
	    				if (!labIens.contains(item.getLabTestIEN())) {
	    					labIens.add(item.getLabTestIEN());
	    				}
    				}
    				for (ResultDetail detail : item.getDetails()) {
    					if (detail.getLabTestIEN() != null) {
	    					if (!labIens.contains(detail.getLabTestIEN())) {
	    						labIens.add(detail.getLabTestIEN());
	    					}
    					}
    				}
    			    			
    		}
    		for (FMLaboratoryTest l : getLaboratoryTest(labIens)) {
    			for (IsAnOVLabResult item : collection) {
    				
    					if (l.getIEN().equals(item.getLabTestIEN())) {
    						item.setLaboratoryTest(l);
    					}
        				for (ResultDetail detail : item.getDetails()) {
        					if (l.getIEN().equals(detail.getLabTestIEN())) {
    	    					detail.setLaboratoryTest(l);
        					}
        				}
    					
    				}
    			
    		}
    	}
    }
    private Date parseHL7TimeFormat(String dateString) throws ParseException {
        String [] parts= dateString.split("[+-]");
        if (parts.length!=2) {
            throw new ParseException("No timezone or incorrect format [" + dateString + "]", 0);
        }

        StringBuilder sb = new StringBuilder(normalizeDate(parts[0]));
        sb.append(dateString.contains("+") ? "+" : "-");
        sb.append(parts[1]);
        return getHL7TimeFormat().parse(sb.toString());
    }
    private SimpleDateFormat getHL7TimeFormat() {
        return new SimpleDateFormat("yyyyMMddHHmmssZ");
    }

    private String normalizeDate(String dateString) {
        if (dateString.length()>=14) {
            return dateString;
        }
        StringBuilder sb = new StringBuilder(dateString);
        for (int i=14-dateString.length(); i>0; --i) {
            sb.append("0");
        }
        return sb.toString();
    }

     
    public static void main(String[] args) throws OvidDomainException {
        //BasicConfigurator.configure();
        //Logger.getRootLogger().setLevel(Level.ERROR);
        RPCConnection serverConn = null;
        RPCConnection userConn = null;
        try {

            if (args == null || args.length == 0) {
                args = new String[] { "localhost", "9202", "OV1234", "OV1234!!", "PU1234", "PU1234!!"};
            }
            if (args.length < 6) {
                System.err.println("usage: LabResultRepository <host> <port> <ovid-access-code> <ovid-verify-code> <user-access-code> <user-verify-code>");
                return;
            }

            serverConn = new RPCBrokerConnection(args[0], new Integer(args[1]), args[2], args[3]);
            if (serverConn==null) {
                return;
            }

            userConn = new RPCBrokerConnection(args[0], new Integer(args[1]), args[4], args[5]);

            String dfn = "6"; // "15"
            Calendar cal = GregorianCalendar.getInstance();
            cal.set(2000, Calendar.JANUARY, 1);
            Date beginDate = cal.getTime();
            Date endDate = new Date();
            LabResultRepository repo = new LabResultRepository(userConn, serverConn);
            for (IsAnOVLabResult item : repo.getResults(dfn, beginDate, endDate, true)) {
            	System.out.println(item);
            }
            

        } catch (NumberFormatException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (RPCException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            try {
                if (serverConn != null) {
                    serverConn.close();
                }
                if (userConn != null) {
                    userConn.close();
                }
            } catch (RPCException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }
    }
}
