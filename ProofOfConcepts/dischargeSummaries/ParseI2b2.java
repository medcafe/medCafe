import java.io.*;
import java.util.*;
import java.util.regex.*;

public class ParseI2b2
{

    public static void main(String[] args) throws Exception
    {
       BufferedReader in = new BufferedReader(new FileReader(args[0]));
       String line = in.readLine();
       StringBuilder buf = new StringBuilder(  );
       Summary s = new Summary();
       String[] parts = null;
       while(line != null)
       {
           if( line.startsWith("<RECORD") )
           {
               s = new Summary();
               parts = line.split("\"");
               s.setId( parts[1] );
           }
           if( line.startsWith("<SMOKING") )
           {
               parts = line.split("\"");
               s.setCategory( parts[1] );
           }
           if( line.startsWith("<TEXT") )
           {
               buf = new StringBuilder();
               line = in.readLine();
               continue;
           }
           if( line.startsWith("</TEXT") )
           {
               s.setBody( buf.toString() );
               s.write();
           }
           buf.append(line + "\n");
           line = in.readLine();
       }
    }

}

class Summary
{

    public void write()
        throws Exception
    {
        //write out to file
        FileWriter out = new FileWriter( id + "_" + category + ".txt");
        out.write(body);
        out.close();
    }

  /**
   * Category property.
   */
  protected String category = null;

  /**
   * Get category property.
   *
   *@return Category property.
   */
  public String getCategory() {
  	return this.category;
  }

  /**
   * Set category property.
   *
   *@param category New category property.
   */
  public void setCategory(String category) {
  	this.category = category;
  }


  /**
   * Id property.
   */
  protected String id = null;

  /**
   * Get id property.
   *
   *@return Id property.
   */
  public String getId() {
  	return this.id;
  }

  /**
   * Set id property.
   *
   *@param id New id property.
   */
  public void setId(String id) {
  	this.id = id;
  }


  /**
   * Body property.
   */
  protected String body = null;

  /**
   * Get body property.
   *
   *@return Body property.
   */
  public String getBody() {
  	return this.body;
  }

  /**
   * Set body property.
   *
   *@param body New body property.
   */
  public void setBody(String body) {
  	this.body = body;
  }


}
