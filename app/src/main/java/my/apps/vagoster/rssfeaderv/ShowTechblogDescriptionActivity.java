package my.apps.vagoster.rssfeaderv;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.*;

public class ShowTechblogDescriptionActivity extends Activity 
{
	public static ProgressDialog showProgressDesc;
	
	public void onCreate(Bundle icicle) 
    {
        super.onCreate(icicle);
        setContentView(R.layout.showdescription);
        
        String theStory = null;
        String theTitle = null;
        String theImage = null;
        LoadingTask lt = new LoadingTask();
    	
        
        Intent startingIntent = getIntent();
        
        if (startingIntent != null)
        {
        	Bundle b = startingIntent.getBundleExtra("android.intent.extra.INTENT");
        	if (b == null)
        	{
        		theStory = "bad bundle?";
        	}
        	else
    		{
        		theTitle = b.getString("title") + "\n\n";
        		theImage = b.getString("images")+"";
        		theStory = b.getString("pubdate") + "\n\n" + b.getString("description").replace('\n',' ') + "\n\nMore information:\n" + b.getString("link");
    		}
        }
        else
        {
        	theStory = "Information Not Found.";
        }
        
        TextView t= (TextView) findViewById(R.id.title);
        t.setText(theTitle);
        
        if(theImage!=null){
        	showProgressDesc = ProgressDialog.show(ShowTechblogDescriptionActivity.this, "","Loading Description. Please wait...", true);
        	lt.execute(theImage);
        }
        //ShowDescriptionActivity.showProgressDesc.dismiss();
        /*if(theImages!=null){
        	
        	Bitmap bitmap=DownloadImage(theImages.get(0));
        	ImageView img = (ImageView) findViewById(R.id.imageDesc);
        	img.setImageBitmap(bitmap);
        	
        }*/
        TextView db= (TextView) findViewById(R.id.storybox);
        
        if(theStory.contains("]")){// Afairesei ton sxetikwn thematwn apo to telos tou story
        	int indexOfSxetika = theStory.indexOf("]")+1;
        	theStory = theStory.substring(0, indexOfSxetika)+"\n\n";
        }
        db.setText(theStory);
        
        
        Button backbutton = (Button) findViewById(R.id.back);
        backbutton.setOnClickListener(new Button.OnClickListener() 
        {
            public void onClick(View v) 
            {
            	finish();
            }
        });        
    }
    
    public class LoadingTask extends AsyncTask<String, Void, String>{

    	Bitmap bitmap=null;;
    	
		@Override
		protected String doInBackground(String... urls) { 
				Log.d("doInBackground",urls[0]);
				bitmap=DownloadImage(urls[0]);
				//bitmap=DownloadImage("http://www.techgear.gr/wp-content/uploads/2012/09/windows_phone_8_comparison.jpg");			
			return "";
		}

		protected void onPostExecute(String s) {
			Log.d("onPostExecute","asasa");
			ShowTechblogDescriptionActivity.showProgressDesc.dismiss();
			ImageView img = (ImageView) findViewById(R.id.imageDesc);
			img.setImageBitmap(bitmap);
		}
		
		private Bitmap DownloadImage(String URL)
	    {      
	        Bitmap bitmap = null;
	        InputStream in = null;      
	        try {
	            in = OpenHttpConnection(URL);
	            bitmap = BitmapFactory.decodeStream(in);
	            in.close();
	        } catch (IOException e1) {
	            // TODO Auto-generated catch block
	            Log.d("download image error", e1.toString());
	        }
	        return bitmap;              
	    }
		
	    private InputStream OpenHttpConnection(String urlString) 
	    {
	        InputStream in = null;
	        int response = -1;
	              
	        URL url = null;
			try {
				url = new URL(urlString);
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				Log.d("MalformedEx URL", e.getMessage());
			}
	        URLConnection conn = null;
			try {
				conn = url.openConnection();
			} catch (IOException e) {
				// TODO Auto-generated catch block
			//	Log.d("Could not open connection", e.getMessage());
			}
	                
	        if (!(conn instanceof HttpURLConnection))
				try {
					throw new IOException("Not an HTTP connection");
				} catch (IOException e) {
					// TODO Auto-generated catch block
				//	Log.d("not an instance of HTTPL", e.getMessage());
				}
	      
	        try{
	            HttpURLConnection httpConn = (HttpURLConnection) conn;
	            httpConn.setAllowUserInteraction(false);
	            httpConn.setInstanceFollowRedirects(true);
	            httpConn.setRequestMethod("GET");
	            httpConn.connect();

	            response = httpConn.getResponseCode();                
	            if (response == HttpURLConnection.HTTP_OK) {
	                in = httpConn.getInputStream();                                
	            }                    
	        }
	        catch (Exception ex)
	        {
	        	Log.d("Error connecting", ex.getMessage());          
	        }
	        return in;    
	    }
		
    }
    
	/*private Bitmap DownloadImage(String URL)
    {      
        Bitmap bitmap = null;
        InputStream in = null;      
        try {
            in = OpenHttpConnection(URL);
            bitmap = BitmapFactory.decodeStream(in);
            in.close();
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            Log.d("download image error", e1.toString());
        }
        return bitmap;              
    }
	
    private InputStream OpenHttpConnection(String urlString) throws IOException
    {
        InputStream in = null;
        int response = -1;
              
        URL url = new URL(urlString);
        URLConnection conn = url.openConnection();
                
        if (!(conn instanceof HttpURLConnection))                    
            throw new IOException("Not an HTTP connection");
      
        try{
            HttpURLConnection httpConn = (HttpURLConnection) conn;
            httpConn.setAllowUserInteraction(false);
            httpConn.setInstanceFollowRedirects(true);
            httpConn.setRequestMethod("GET");
            httpConn.connect();

            response = httpConn.getResponseCode();                
            if (response == HttpURLConnection.HTTP_OK) {
                in = httpConn.getInputStream();                                
            }                    
        }
        catch (Exception ex)
        {
            throw new IOException("Error connecting");          
        }
        return in;    
    }*/
}


