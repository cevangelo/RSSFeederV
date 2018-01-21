package my.apps.vagoster.rssfeaderv;

import android.os.Bundle;
import android.app.TabActivity;
import android.content.Intent;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

public class TabLayoutActivity extends TabActivity {
   /** Called when the activity is first created. */
   @Override
   public void onCreate(Bundle savedInstanceState) {
       // Make sure this is before calling super.onCreate
       setTheme(R.style.AppTheme);
       super.onCreate(savedInstanceState);
       setContentView(R.layout.main);

       final TabHost tabHost = getTabHost();
       
       // Tab for Techgear
       TabSpec techgearspec = tabHost.newTabSpec("TechGear");
       // setting Title and Icon for the Tab
       techgearspec.setIndicator("TechGear", getResources().getDrawable(R.drawable.icon_techgear_tab));
       Intent techgearIntent = new Intent(this, TechgearActivity.class);
       techgearspec.setContent(techgearIntent);

       // Tab for Techblog
       TabSpec techblogspec = tabHost.newTabSpec("TechBlog");
       // setting Title and Icon for the Tab
       techblogspec.setIndicator("TechBlog", getResources().getDrawable(R.drawable.icon_techblog_tab));
       Intent techblogIntent = new Intent(this, TechblogActivity.class);
       techblogspec.setContent(techblogIntent);

       // Tab for Engadget
       TabSpec engadgetspec = tabHost.newTabSpec("Engadget");
       engadgetspec.setIndicator("Engadget", getResources().getDrawable(R.drawable.icon_engadget_tab));
       Intent engadgetIntent = new Intent(this, EngadgetActivity.class);
       engadgetspec.setContent(engadgetIntent);

       // Adding all TabSpec to TabHost
       tabHost.addTab(techgearspec); // Adding techgear tab
       tabHost.addTab(techblogspec); // Adding techblog tab
       tabHost.addTab(engadgetspec); // Adding engadget tab

   }
   
   @Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    if ((keyCode == KeyEvent.KEYCODE_BACK)) {
	        Log.d(this.getClass().getName(), "back button pressed");
	       TechgearActivity.showProgress.dismiss();
	    }
	    return true;
	}
}