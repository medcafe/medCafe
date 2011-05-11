/*
 * Test.java
 *
 * Created on 08. Mai 2007, 15:19
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package at.riemers.velocity2js.test;

import at.riemers.velocity2js.velocity.*;
import java.util.Properties;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.Template;

import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;

import java.io.*;
import java.util.ArrayList;

/**
 *
 * @author tobias
 */
public class Test {
    
    /** Creates a new instance of Test */
    public Test(String templateFile) {
        try {
            /*
             * setup
             */
            
            Properties p = new Properties();
            p.setProperty("resource.loader" , "file");
            
            p.setProperty("file.resource.loader.description", "Velocity File Resource Loader");
            p.setProperty("file.resource.loader.class", "org.apache.velocity.runtime.resource.loader.FileResourceLoader");
            
            p.setProperty( "file.resource.loader.path", "C:\\Dokumente und Einstellungen\\tobias\\Eigene Dateien\\java\\vel2js\\templates");
            Velocity2Js.init(p);
            
            /*
             *  Make a context object and populate with the data.  This
             *  is where the Velocity engine gets the data to resolve the
             *  references (ex. $list) in the template
             */
            
            VelocityContext context = new VelocityContext();
            context.put("list", getNames());
            context.put("list2", getNames());
            context.put("list3", new MyList());
            
            /*
             *  get the Template object.  This is the parsed version of your
             *  template input file.  Note that getTemplate() can throw
             *   ResourceNotFoundException : if it doesn't find the template
             *   ParseErrorException : if there is something wrong with the VTL
             *   Exception : if something else goes wrong (this is generally
             *        indicative of as serious problem...)
             */
            
            Template template =  null;
            
            try {
                template = Velocity.getTemplate(templateFile);
            } catch( ResourceNotFoundException rnfe ) {
                System.out.println("Example : error : cannot find template " + templateFile );
            } catch( ParseErrorException pee ) {
                System.out.println("Example : Syntax error in template " + templateFile + ":" + pee );
            }
            
            /*
             *  Now have the template engine process your template using the
             *  data placed into the context.  Think of it as a  'merge'
             *  of the template and the data to produce the output stream.
             */
            
            BufferedWriter writer = writer = new BufferedWriter(
                    new OutputStreamWriter(System.out));
            
            if ( template != null)
                template.merge(context, writer);
            
            /*
             *  flush and cleanup
             */
            
            writer.flush();
            writer.close();
        } catch( Exception e ) {
            System.out.println(e);
        }
    }
    
    public ArrayList getNames() {
        ArrayList list = new ArrayList();
        
        list.add("ArrayList element 1");
        list.add("ArrayList element 2");
        list.add("ArrayList element 3");
        list.add("ArrayList element 4");
        
        return list;
    }
    
    class MyList {
        public ArrayList find() {
            ArrayList list = new ArrayList();
            
            list.add("x  1");
            list.add("x  2");
            list.add("x  3");
            list.add("x  4");
            
            return list;
        }
    }
    
    public static void main(String[] args) {
        Test t = new Test("test.vm");
    }
}
