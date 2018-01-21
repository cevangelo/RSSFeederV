package my.apps.vagoster.rssfeaderv;

import java.util.ArrayList;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import android.util.Log;

public class DescHandler extends DefaultHandler {

	int currentstate = 0;
	public static String desc="";
	static ArrayList<String> urls= new ArrayList<String>();
	public static String imageURL="";
	final int RSS_DESC = 1;
	final int RSS_IMG = 2;
	boolean piges=false;
	String href="";

	public void startDocument() throws SAXException
	{
	}
	public void endDocument() throws SAXException
	{
	}
	public void startElement(String namespaceURI, String localName,String qName, Attributes atts) throws SAXException
	{
		Log.d("namespace_start_Desc",namespaceURI);
		Log.d("localname_start_Desc", localName);
		Log.d("qname_start_Desc", qName);
		for(int i=0;i<atts.getLength();i++){
			Log.d("attribute_start_Desc", atts.getType(i) + atts.getQName(i));
		}

		if (localName.equals("p") || localName.equals("a") || localName.equals("strong"))
		{
			currentstate = RSS_DESC;
			href=atts.getValue("href");
			return;
		}
		if(localName.equals("img"))
		{
			currentstate = RSS_IMG;
			urls.add(atts.getValue("src"));
			imageURL=urls.get(0);
			Log.d("imageURLList",imageURL);
			return;
		}
	
		// if we don't explicitly handle the element, make sure we don't wind up erroneously 
		// storing a newline or other bogus data into one of our existing elements
		currentstate = 0;
	}
	
	public void endElement(String namespaceURI, String localName, String qName) throws SAXException
	{
		Log.d("namespace_end_Desc",namespaceURI);
		Log.d("localname_end_Desc", localName);
		Log.d("qname_end_Desc", qName);
		if(localName.equals("p")){
			piges=false;
		}
	}
	 
	public void characters(char ch[], int start, int length)
	{
		String theString = new String(ch,start,length);
		Log.i("DescReader","characters[" + theString + "]");
		
		switch (currentstate)
		{
			case 1:
				if(theString.contains("ÐçãÞ:")){
					theString="\n"+theString;
					piges=true;
				}
				if(theString!=null){
					Log.d("DescReaderElse", theString);
					desc+= theString;		// gia na apofugw tn apokopi lexewn logw twn string tis morfis &xxx;(eg &amp; -> &, Ampersand in HTML kai XML)
				}
				if(piges && !theString.contains(",")){
					desc+= " "+href+"\n";
				}
				currentstate = RSS_DESC;
			default:
				return;
		}
		
	}
}
