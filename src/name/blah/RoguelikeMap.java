/**
 * 
 */
package name.blah;

import java.util.Random;

/**
 * @author Jett Peterson
 *
 */
public class RoguelikeMap {
	
	String[][] map;
	
	RoguelikeMap()
	{
		map = new String[24][24];
		
		//Fill the map with the base tile - Impassable terrain
		for(int i = 0; i < 24; i++)
		{
			for(int j = 0; j < 24; j++)
			{
				map[i][j] = "#";
			}
		}
		
		Random rand = new Random();
		
		for(int i = 11; i < 14; i++)
		{
			for(int j = 11; j < 14; j++)
			{
				map[i][j] = ".";
			}
		}
		
	}
	
	public String toString()
	{
		String out = "";
		for(int i = 0; i < 24; i++)
		{
			for(int j = 0; j < 24; j++)
			{
				out += map[i][j];
			}
			out += "\n";
		}
		return out;
	}

}
