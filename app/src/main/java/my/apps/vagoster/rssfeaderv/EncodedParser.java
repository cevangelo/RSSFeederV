package my.apps.vagoster.rssfeaderv;

import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

public class EncodedParser {

	static public String getFeed(String encodedText)
    {
    	try
    	{
    		
           // create the factory
           SAXParserFactory factory = SAXParserFactory.newInstance();
           factory.setNamespaceAware(true);
           // create a parser
           SAXParser parser = factory.newSAXParser();
           // create the reader (scanner)
           XMLReader xmlreader = parser.getXMLReader();
           // instantiate our handler
           DescHandler Handler = new DescHandler(); 
           // assign our handler
           xmlreader.setContentHandler(Handler); 
           byte[] bytes = encodedText.getBytes();
           ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
           InputStreamReader isr = new InputStreamReader(bais);

           // get our data via the url class
           InputSource is = new InputSource(isr);
           // perform the synchronous parse           
           xmlreader.parse(is);
           // get the results - should be a fully populated RSSFeed instance, or null on error
           return DescHandler.desc;
    	}
    	catch (Exception ee)
    	{
    		// if we have a problem, simply return null
    		return null;
    	}
    }
}
