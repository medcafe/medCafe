package test.org.mitre.medcafe.util;

import java.net.URL;
import java.util.List;

import junit.framework.Assert;

import org.junit.Ignore;
import org.junit.Test;
import org.mitre.medcafe.util.GreenCDAFeedParser;

import com.sun.syndication.feed.synd.SyndLinkImpl;

public class GreenCDAFeedParserTester {

	@Test
	public void testBasicParse() {
		URL url = this.getClass().getResource("/TestAtom.xml");
		
		String firstName = "Tom";
		String lastName = "Smith59";
		String type = "medications";

		String fileName = url.getFile();
		
		// parser.parseAtom("http://1.1.22.110:3000/records", fileName);

		
//	 	List<String> urls = new ArrayList<String>();
//    	List<String> results = new ArrayList<String>();

		List<SyndLinkImpl> foundEntries=  GreenCDAFeedParser.findPatient( firstName,  lastName,  type,  fileName);
    	Assert.assertEquals(foundEntries.size() , 1);
	}
	
	@Test
	public void testPDSParse() {
		
		String firstName = "Tom";
		String lastName = "Smith59";
		String type = "medications";

		GreenCDAFeedParser.parseAtom("http://localhost:3000/records");
	}
	
	@Test
	@Ignore
	public void testParseAtom() {
		URL url = this.getClass().getResource("/TestAtom.xml");
		
		String firstName = "Tom";
		String lastName = "Smith59";
		String type = "medications";

		String fileName = url.getFile();
		
		// parser.parseAtom("http://1.1.22.110:3000/records", fileName);

		List<String> foundList = GreenCDAFeedParser.findPatientDetails(firstName, lastName,
				type, fileName);

		for (String foundVals : foundList) {
			System.out.println("val: " + foundVals );
		}
		
		/*
		 * GreenCDARepository gcda = new GreenCDARepository();
		 * List<Medication> meds = gcda.getMedications("1");
		 * StringBuffer strBuf = new StringBuffer();
		 * Gson gson = new Gson();
		 * for (Medication med: meds)
		 * {
		 * if (med == null)
		 * strBuf.append("is null ");
		 * strBuf.append(gson.toJson(med));
		 * }
		 */

	}

}
