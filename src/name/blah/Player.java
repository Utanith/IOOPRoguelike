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
			if(map.checkOpen(x, y+1))
			{
				y++;
				map.setPlayer(x, y);
				return true;
			}
			break;
			
		case SOUTH:
			if(map.checkOpen(x, y-1))
			{
				y--;
				map.setPlayer(x, y);
				return true;
			}
			break;
			
		case EAST:
			if(map.checkOpen(x+1, y))
			{
				x++;
				map.setPlayer(x, y);
				return true;
			}
			break;
			
		case WEST:
			if(map.checkOpen(x-1, y))
			{
				x--;
				map.setPlayer(x, y);
				return true;
			}
			break;
		}
		return false;
	}
}
