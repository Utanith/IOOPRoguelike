/**
 * 
 */
package name.blah;

import java.util.Random;

/**
 * @author Jett Peterson
 *
 */
public class Player {
	
	private int x;
	private int y;
	private int health;
	
	Player(RoguelikeMap map)
	{
		this.health = 10;
		if(health > 0)
		{
			//This is here to make Java stop complaining, till health does something
		}
		
		int x, y;
		Random rand = new Random();
		x = rand.nextInt(24);
		y = rand.nextInt(24);
		
		while(!map.checkOpen(x, y))
		{
			x = rand.nextInt(24);
			y = rand.nextInt(24);
		}
		
		this.x = x;
		this.y = y;
		map.setPlayer(x, y);
	}
	
	public boolean move(Direction dir, RoguelikeMap map)
	{
		switch(dir)
		{
		case NORTH:
			if(map.checkOpen(this.x-1, this.y))
			{
				this.x--;
				map.setPlayer(this.x, this.y);
				return true;
			}
			break;
			
		case SOUTH:
			if(map.checkOpen(this.x+1, this.y))
			{
				this.x++;
				map.setPlayer(this.x, this.y);
				return true;
			}
			break;
			
		case EAST:
			if(map.checkOpen(this.x, this.y+1))
			{
				this.y++;
				map.setPlayer(this.x, this.y);
				return true;
			}
			break;
			
		case WEST:
			if(map.checkOpen(this.x, this.y-1))
			{
				this.y--;
				map.setPlayer(this.x, this.y);
				return true;
			}
			break;
		}
		return false;
	}
}
