package my.apps.vagoster.rssfeaderv;

import java.util.ArrayList;

import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.*;
import android.util.Log;


public class TechgearRSSHandler extends DefaultHandler
{
	String temp;
	boolean wait=false;
	RSSFeed _feed;
	RSSItem _item;
	String _lastElementName = "";
	final int RSS_TITLE = 1;
	final int RSS_LINK = 2;
	final int RSS_DESCRIPTION = 3;
	final int RSS_CATEGORY = 4;
	final int RSS_PUBDATE = 5;
	
	int depth = 0;
	int currentstate = 0;
	/*
	 * Constructor 
	 */
	TechgearRSSHandler()
	{
	}
	
	/*
	 * getFeed - this returns our feed when all of the parsing is complete
	 */
	RSSFeed getFeed()
	{
		return _feed;
	}
	
	
	public void startDocument() throws SAXException
	{
		// initialize our RSSFeed object - this will hold our parsed contents
		_feed = new RSSFeed();
		// initialize the RSSItem object - we will use this as a crutch to grab the info from the channel
		// because the channel and items have very similar entries..
		_item = new RSSItem();

	}
	public void endDocument() throws SAXException
	{
	}
	public void startElement(String namespaceURI, String localName,String qName, Attributes atts) throws SAXException
	{
		Log.d("namespace_start_RSS",namespaceURI);
		Log.d("localname_start_RSS", localName);
		Log.d("qname_start_RSS", qName);
		for(int i=0;i<atts.getLength();i++){
			Log.d("attribute_start", atts.getType(i) + atts.getQName(i));
		}

		depth++;
		if (localName.equals("channel"))
		{
			currentstate = 0;
			return;
		}
		if (localName.equals("image"))
		{
			// record our feed data - we temporarily stored it in the item :)
			_feed.setTitle(_item.getTitle());
			_feed.setPubDate(_item.getPubDate());
		}
		if (localName.equals("item"))
		{
			// create a new item
			_item = new RSSItem();
			return;
		}
		if (localName.equals("title"))
		{
			currentstate = RSS_TITLE;
			return;
		}
		if (localName.equals("encoded"))  // Anti tou description
		{
			currentstate = RSS_DESCRIPTION;
			return;
		}
		if (localName.equals("link"))
		{
			currentstate = RSS_LINK;
			return;
		}
		if (localName.equals("category"))
		{
			currentstate = RSS_CATEGORY;
			return;
		}
		if (localName.equals("pubDate"))
		{
			currentstate = RSS_PUBDATE;
			return;
		}
		// if we don't explicitly handle the element, make sure we don't wind up erroneously 
		// storing a newline or other bogus data into one of our existing elements
		currentstate = 0;
	}
	
	public void endElement(String namespaceURI, String localName, String qName) throws SAXException
	{
		depth--;
		Log.d("namespace_end_RSS",namespaceURI);
		Log.d("localname_end_RSS", localName);
		Log.d("qname_end_RSS", qName);
		if (localName.equals("item"))
		{
			// add our item to the list!
			_feed.addItem(_item);
			return;
		}
		if(localName.equals("encoded")){
			String startOfTheDesc=_item.getDescription().replace(_item.getTitle(),"").replace("null", "").substring(0, 30); // Pairnoume ena kommati apo thn arxh tou keimenou (to replace me to getTitle einai gia thn afairesi tou titlou apo to telos tou description)
			int indexOfStart = _item.getDescription().lastIndexOf(startOfTheDesc);	//anazitoume auto to keimeno apo to telos pros thn arxi kai apothikeuoume to index ths prwtis emfanisis tou					
			String noDuplText = _item.getDescription().substring(indexOfStart);		//Me vasi auto to index pairnoume to subbstring pou arxizei apo auto kai to thetoume san to description
			_item.setDescription(noDuplText.replace("null", ""));  //To kanoume auto gia diwxoume ta duplicates pou dimiourgountai kata tn diarkeia dimiourgias tou description string.
			
			/*int indexOfSxetika = _item.getDescription().indexOf(_item.getTitle());
			_item.setDescription(_item.getDescription().substring(0, indexOfSxetika-1));*/
			
			if(DescHandler.imageURL==null)
				Log.d("URLS","NULL");
			_item.setImage(DescHandler.imageURL);
			Log.d("url", _item.getImage());
			
			currentstate = 0;
			DescHandler.desc="";
			DescHandler.imageURL= "";
			DescHandler.urls=new ArrayList<String>();
			return;
		}
	}
	 
	public void characters(char ch[], int start, int length)
	{
		String theString = new String(ch,start,length);
		Log.i("RSSReader","characters[" + theString + "]");
		
		switch (currentstate)
		{
			case RSS_TITLE:
				_item.addToTheTitle(theString);
				_item.setTitle(_item.getTitle().trim()); // gia na kopsw tis kenes grammes pou emfanizontan stn titlo apo ta stoixeia tis listas
				currentstate = RSS_TITLE;	
				break;
			case RSS_LINK:
				_item.setLink(theString);
				currentstate = 0;
				break;
			case RSS_DESCRIPTION:
				if(!theString.endsWith(">") && theString.startsWith("<")){ //se periptwsi pou ta tags exoun xwristei se diaforetika stigmiotipa tou characters()
					temp = theString;										//perimenw osa stigmiotipa xreiastei kratwntas ta prwta kommatia kai ta enwnw sta epomena mexri na vrw to end tag
					wait=true;												// h metavliti wait mas voithaei na mn mperdeutoun alla Strings mesa se auth tn diadikasia.
					break;
				}
				if(wait && (!theString.contains("<") || !theString.contains(">"))){
					theString = temp+theString;
					wait=true;
				}
				else if(wait){
					theString = temp+theString;
					wait=false;
				}
				if(!theString.startsWith("<") && theString.contains("</p>"))// gia ta kommatia pou arxizoun apo gramma kai einai ommatia ennos p header
				{	
					theString = "<p>" + theString;
				}
					String text = EncodedParser.getFeed(theString.replace("<p><a href=\"http://www.techgear.gr\"></a></p>", ""));
					//theString = replaceUnprosseced(theString); // filtrarisma gia entity references apo html h xml			
						_item.addToTheDesc(text);		// gia na apofugw tn apokopi lexewn logw twn string tis morfis &xxx;(eg &amp; -> &, Ampersand in HTML kai XML)
					currentstate = RSS_DESCRIPTION;
					//DescHandler.desc="";
				Log.d("stringlength", String.valueOf(length));
				break;
			case RSS_CATEGORY:
				_item.setCategory(theString);
				currentstate = 0;
				break;
			case RSS_PUBDATE:
				_item.setPubDate(theString.replace("+0000","")); // afairesi tou +0000 apo thn imerominia
				currentstate = 0;
				break;
			default:
				return;
		}
		
	}
}
