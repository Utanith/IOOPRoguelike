package name.blah;

import java.util.ArrayList;

import android.widget.TextView;

public class Notifier {

	private TextView display;
	private ArrayList<String> items;
	
	public Notifier(TextView display)
	{
		this.display = display;
		this.items = new ArrayList<String>();
	}
	
	public void addItem(String text, Player plr)
	{
		this.items.add(text);
		this.display.setText("");
		
		for(String s : this.items)
		{
			this.display.append(s);
		}
		this.display.append("Health: " + plr.getHealth());
	}
	
	public void clear()
	{
		this.items.clear();
	}
}
