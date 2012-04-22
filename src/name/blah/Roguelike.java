package name.blah;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.TextView;

public class Roguelike extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        TextView tv = new TextView(this);
        RoguelikeMap map = new RoguelikeMap();
        
        tv.setText(map.toString());
        tv.setTypeface(Typeface.MONOSPACE);
        setContentView(tv);
    }
}