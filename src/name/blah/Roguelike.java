package name.blah;


import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Roguelike extends Activity {
    /** Called when the activity is first created. */
	private TextView tv;
	private RoguelikeMap map;
	private Player plr;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        tv = new TextView(this);
        map = new RoguelikeMap();
        plr = new Player(map);
        
        tv.setText(map.toString());
        tv.setTypeface(Typeface.MONOSPACE);
        setContentView(tv);
        
    }
    
    public void regen(View view)
    {
    	map = null;
    	map = new RoguelikeMap();
    	tv.setText(map.toString());
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
     menu.add("Regenerate");
     return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
     
     switch(item.getItemId())
     {
     default:
    	tv.setText("Regenerating...");
      	map = null;
  		map = new RoguelikeMap();
  		tv.setText(map.toString());
  		return true;
     }

    }
}