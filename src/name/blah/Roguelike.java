package name.blah;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;

public class Roguelike extends Activity {
    /** Called when the activity is first created. */
	private TextView tv;
	private RoguelikeMap map;
	private Player plr;
	private TextView dbgMsg;
	
    /* 
     * instantiates the new application
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        tv = (TextView) findViewById(R.id.textView1);
        dbgMsg = (TextView) findViewById(R.id.debugMsg);
        map = new RoguelikeMap();
        plr = new Player(map);
        dbgMsg.setText("Started game...\n");
        dbgMsg.append("Player health: " + plr.getHealth() + "\n");
        
        TextView tmp = new TextView(this);
        tmp.setText("Started.");
        
        
        tv.setText(map.toString());
        tv.setTypeface(Typeface.MONOSPACE);
        tv.setTextSize(13);
        
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle("Start a new game?");
        alertDialog.setMessage("Do you want to start a new game?");
        alertDialog.setButton("Yes", new DialogInterface.OnClickListener() {
        	public void onClick(DialogInterface dialog, int which) {
        		//We're starting a new game, no matter the input, because this is a demo of AlertDialog!
        		dialog.dismiss();
        	}
        });
        
        alertDialog.show();
        //setContentView(tv);
        
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
    
    /**
     * resets the game completely
     */
    public void restart(View v)
    {
    	tv.setText("Regenerating...");
      	map = null;
      	plr = null;
  		map = new RoguelikeMap();
  		plr = new Player(map);
  		dbgMsg.append("Started game...\n");
  		tv.setText(map.toString());
    }
    
    /* This function is called repeatedly when scrolling through debug messages
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent msg) {
    	
    	switch(keyCode)
    	{
    	case KeyEvent.KEYCODE_DPAD_LEFT:
    		this.plr.move(Direction.WEST, this.map);
    		break;
    		
    	case KeyEvent.KEYCODE_DPAD_RIGHT:
    		this.plr.move(Direction.EAST, this.map);
    		break;
    		
    	case KeyEvent.KEYCODE_DPAD_UP:
    		this.plr.move(Direction.NORTH, this.map);
    		break;
    		
    	case KeyEvent.KEYCODE_DPAD_DOWN:
    		this.plr.move(Direction.SOUTH, this.map);
    		break;
    	}
    	this.redraw();
    	dbgMsg.append("Player health: " + plr.getHealth() + "\n");
    	dbgMsg.append("Location: " + plr.getLocation()[0] + "," + plr.getLocation()[1] + "\n");
    	return true;
    }
    */
    
    /**
     * when the buttons are pressed call the appropriate function
     */
    public void movementKeys(View v)
    {
    	switch(v.getId())
    	{
    	case R.id.btndown:
    		this.plr.move(Direction.SOUTH, this.map);
    		break;
    		
    	case R.id.btnleft:
    		this.plr.move(Direction.WEST, this.map);
    		break;
    		
    	case R.id.btnright:
    		this.plr.move(Direction.EAST, this.map);
    		break;
    		
    	case R.id.btnup:
    		this.plr.move(Direction.NORTH, this.map);
    		break;
    	}
    	this.redraw();
    	dbgMsg.setText(map.message + "\nHealth: " + plr.getHealth() + "\n");
    	map.message = " ";
    	
    }
    
    /**
     * redraws the map
     */
    private void redraw()
    {
    	tv.setText(map.toString());
    }
}