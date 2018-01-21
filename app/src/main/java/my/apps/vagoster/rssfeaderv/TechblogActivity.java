package my.apps.vagoster.rssfeaderv;

import java.net.URL;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import my.apps.vagoster.rssfeaderv.TechgearActivity.LoadingTask;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

public class TechblogActivity extends Activity implements OnItemClickListener
{

public final String RSSFEEDOFCHOICE = "http://feeds.feedburner.com/techblogGR";
	
	public final String tag = "RSSReader";
	private RSSFeed feed = null;
	public LoadingTask lt;
	public static ProgressDialog showProgress;
	
	/** Called when the activity is first created. */

    public void onCreate(Bundle icicle) {
    	super.onCreate(icicle);
        setContentView(R.layout.techblog_layout);
        
        
        if(checkInternetConnection()){
        	showProgress = ProgressDialog.show(TechblogActivity.this, "","Loading. Please wait...", true);
            new LoadingTask().execute(RSSFEEDOFCHOICE);
  		}
  		else{
  			Log.d("network", "No network");
  			Toast.makeText(TechblogActivity.this, "No Network Available", Toast.LENGTH_SHORT).show();
  		}
    }
    
    
    private void UpdateDisplay()
    {
        TextView feedtitle = (TextView) findViewById(R.id.feedtitle);
        TextView feedpubdate = (TextView) findViewById(R.id.feedpubdate);
        ListView itemlist = (ListView) findViewById(R.id.itemlist_tb);
  
        
        if (feed == null)
        {
        	feedtitle.setText("No RSS Feed Available");
        	return;
        }
        
        feedtitle.setText("");
        feedpubdate.setText(feed.getPubDate());

        ArrayAdapter<RSSItem> adapter = new ArrayAdapter<RSSItem>(this,R.layout.complex_list_item,feed.getAllItems());

        itemlist.setAdapter(adapter);
        
        itemlist.setOnItemClickListener(this);
        
        itemlist.setSelection(0);
        
        
    }
    
    
     public void onItemClick(AdapterView parent, View v, int position, long id)
     {
    	 Log.i(tag,"item clicked! [" + feed.getItem(position).getTitle() + "]");

    	 Intent itemintent = new Intent(this,ShowTechblogDescriptionActivity.class);
         
    	 Bundle b = new Bundle();
    	 b.putString("title", feed.getItem(position).getTitle());
    	 b.putString("description", feed.getItem(position).getDescription());
    	 b.putString("link", feed.getItem(position).getLink());
    	 b.putString("pubdate", feed.getItem(position).getPubDate());
    	 b.putString("images", feed.getItem(position).getImage());
    	 
    	 itemintent.putExtra("android.intent.extra.INTENT", b);
         
         startActivity(itemintent);
     }
     
     public class LoadingTask extends AsyncTask<String, Void, String> {

    		@Override
    		protected String doInBackground(String... urls) {    			
    			feed=getFeed(urls[0]);
    			return "";
    		}
    		
    		
    		protected void onPostExecute(String s) {
    			TechblogActivity.showProgress.dismiss();
    			UpdateDisplay();

    		}
    		private RSSFeed getFeed(String urlToRssFeed)
    	    {
    	    	try
    	    	{
    	    		// setup the url
    	    	   URL url = new URL(urlToRssFeed);

    	           // create the factory
    	           SAXParserFactory factory = SAXParserFactory.newInstance();
    	           // create a parser
    	           SAXParser parser = factory.newSAXParser();

    	           // create the reader (scanner)
    	           XMLReader xmlreader = parser.getXMLReader();
    	           // instantiate our handler
    	           TechblogRSSHandler theRssHandler = new TechblogRSSHandler();
    	           // assign our handler
    	           xmlreader.setContentHandler(theRssHandler);
    	           // get our data via the url class
    	           InputSource is = new InputSource(url.openStream());
    	           // perform the synchronous parse           
    	           xmlreader.parse(is);
    	           // get the results - should be a fully populated RSSFeed instance, or null on error
    	           return theRssHandler.getFeed();
    	    	}
    	    	catch (Exception ee)
    	    	{
    	    		// if we have a problem, simply return null
    	    		return null;
    	    	}
    	    }
     }
    //Sinartish elegxou diathesimou diktuou
    private boolean checkInternetConnection() {
    	 ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
    	 // test for connection
    	 if (cm.getActiveNetworkInfo() != null
    	 	 && cm.getActiveNetworkInfo().isAvailable()
    	 	 && cm.getActiveNetworkInfo().isConnected()) {
    	 	        return true;
    	 	 } else {
    	 	        Log.v("Network", "Internet Connection Not Present");
    	 	        return false;
    	 	 }
    }		
    
    @Override
 	public boolean onCreateOptionsMenu(Menu menu) {
 	    MenuInflater inflater = getMenuInflater();
 	    inflater.inflate(R.menu.refresh_menu, menu);
 	    return true;
 	}
 	
 	@Override
 	public boolean onOptionsItemSelected(MenuItem item) {
 	    // Handle item selection
 	    switch (item.getItemId()) {
 	        case R.id.refresh:		//otan einai to koumpi tou resfresh kane ta parakatw
 	        	if(checkInternetConnection()){
 		         	showProgress = ProgressDialog.show(TechblogActivity.this, "","Loading. Please wait...", true);
 		             new LoadingTask().execute(RSSFEEDOFCHOICE);
 		   		}
 		   		else{
 		   			Log.d("network", "No network");
 		   			Toast.makeText(TechblogActivity.this, "No Network Available", Toast.LENGTH_SHORT).show();
 		   		}
 	            return true;
 	        default:
 	            return super.onOptionsItemSelected(item);
 	    }
 	}
    	
}

