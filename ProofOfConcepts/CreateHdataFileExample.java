
import java.io.File;
import java.io.IOException;
import java.net.*;
import java.util.Date;
import java.util.logging.*;
import javax.xml.datatype.DatatypeConfigurationException;
import org.mitre.hdata.hrf.*;
import org.mitre.hdata.hrf.Section;
import org.mitre.hdata.hrf.adversereactions.*;
import org.mitre.hdata.hrf.patientinformation.Patient;
import org.mitre.hdata.hrf.serialization.*;
import org.mitre.hdata.hrf.util.*;
import org.projecthdata.hdata.schemas._2009._06.allergy.Severity;
import org.projecthdata.hdata.schemas._2009._06.core.*;
import org.projecthdata.hdata.schemas._2009._06.patient_information.*;


/**
 *  This class is an example of using the hData java parser code to create
 *  an hdata file
 */
public class CreateHdataFileExample
{
    public static void main(String[] args)
        throws Exception
    {
        HRFFactory fac = new HRFFactory();
        HRF hrf = fac.getHRFInstance();

                Section patientinformation = new Section("Patient Information", new URI(Patient.TYPEID), "patientinformation");

                Patient p = new Patient();
                Name n = new Name();

                n.setTitle("Dr");
                n.getGiven().add("Robert");
                n.setLastname("Bruce");
                n.setSuffix("Esquire");
                p.setName(n);

                Address ad = new Address();

                ad.getStreetAddress().add("1 Castle Rd");
                ad.setCity("Edinburgh");
                ad.setCountry("Scotland");
                ad.setStateOrProvince("Central Belt");
                ad.setZip("00000");

                p.getAddress().add(ad);

                Telecom tel = new Telecom();
                tel.setValue("44-131-1234");
                tel.setUse("yes");

                p.getTelecom().add(tel);

                BirthPlace bp = new BirthPlace();
                Address bpad = new Address();
                bpad.getStreetAddress().add("1 Castle Rd");
                bpad.setCity("Edinburgh");
                bpad.setCountry("Scotland");
                bpad.setStateOrProvince("Central Belt");
                bpad.setZip("00000");
                bp.setAddress(bpad);
                p.setBirthPlace(bp);

                try {
                    p.setBirthtime(DateConverter.getXLMDateFromUtilsDate(new Date(72, 3, 6)));
                } catch (DatatypeConfigurationException ex) {
                }

                p.setId("00000000001");
                p.setAdministrativeGender(Patient.Gender.MALE);

                Language lang = new Language();
                lang.setCode("SCO");
                lang.setCodeSystem("ISO 639-2");
                lang.setDisplayName("Scots");

                p.getLanguage().add(lang);

                MaritalStatus ms = new MaritalStatus();
                ms.setCodeSystem("HL7");
                ms.setCode("M");
                ms.setDisplayName("Married");

                p.setMaritalStatus(ms);

                Race race = new Race();
                race.setCodeSystem("HL7");
                race.setCode("2L2116-2");
                race.setDisplayName("Scottish");

                p.getRace().add(race);

                Description desc = new Description();
                desc.setText("This is a description!");
                CodedValue cv = new CodedValue();
                cv.setCode("12");
                cv.setCodeSystem("HL7");
                cv.setDisplayName("Hello!");
                desc.getCodedValue().add(cv);
                p.setDescription(desc);

                InformationSource is = new InformationSource();
                try {
                    is.setDate(DateConverter.getXLMDateFromUtilsDate(new Date(72, 3, 6)));
                } catch (DatatypeConfigurationException ex) {
                }
                p.setInformationSource(is);

                patientinformation.addSectionDocument(p);
                hrf.addSection(patientinformation, "/");

                Section adverse = new Section("Adverse Reactions", new URI(Allergy.TYPEID), "adversereactions");
                hrf.addSection(adverse, "/");

                Section allergies = new Section("Allergies", new URI(Allergy.TYPEID), "allergies");

                Allergy a = new Allergy();
                a.setUtilDate(new Date());
                a.setProduct("Penicillin", "1.2.3.4.5.6.7", "urn:some:system");
                a.setReaction("pimples");

                Severity s = new Severity();
                s.setCode("1");
                s.setCodeSystem("blah");
                s.setDisplayName("ouch");
                a.setSeverity(s);

                Allergy b = new Allergy();
                b.setUtilDate(new Date(98, 4, 9));
                b.setProduct("Red 40", "465451.2.3.4.5.6.7", "urn:some:system");
                b.setReaction("unwellness");
                b.setSeverity(s);

                allergies.addSectionDocument(a);
                allergies.addSectionDocument(b);

                hrf.addSection(allergies, "/adversereactions");



       HRFFileSystemSerializer out = new HRFFileSystemSerializer();
       try
       {
           out.serialize( new File( "hdataexample.zip"), hrf );   //this is a directory, not a file
       }
       catch (IOException e)
       {
       }

    }

}
