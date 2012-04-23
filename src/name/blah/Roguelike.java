package name.blah;


import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Roguelike extends Activity {
    /** Called when the activity is first created. */
	private TextView tv;
	private RoguelikeMap map;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        tv = new TextView(this);
        map = new RoguelikeMap();
        
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
}