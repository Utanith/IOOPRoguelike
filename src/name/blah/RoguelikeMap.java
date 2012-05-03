/**
 * 
 */
package name.blah;

import java.util.LinkedList;
import java.util.Random;

enum Direction {
	NORTH, SOUTH, EAST, WEST
}

/**
 * @author Jett Peterson
 *
 */
public class RoguelikeMap {
	
	Random rand = new Random();
	String[][] map;
	int playerX;
	int playerY;
	LinkedList<Item> locations = new LinkedList<Item>();
	LinkedList<Enemy> monsters = new LinkedList<Enemy>();
	
	RoguelikeMap()
	{
		map = new String[24][24];
		int x = rand.nextInt(16)-8;
		int y = rand.nextInt(16)-8;
		//Fill the map with the base tile - Impassable terrain
		for(int i = 0; i < 24; i++)
		{
			for(int j = 0; j < 24; j++)
			{
				if((i > 2 && i < 21) && j == 11 + x)
					map[i][j] = ".";
				else if((j > 3 && j < 20) && i == 11 + y)
					map[i][j] = ".";
				else
					map[i][j] = " ";
			}
		}
		

		
		int halls, length, dir;
		halls = 2;
		
		while(halls > 0)
		{
			x = rand.nextInt(24);
			y = rand.nextInt(24);
			length = rand.nextInt(8) + 4;
			if(map[x][y].equals("."))
			{
				dir = rand.nextInt(4);
				switch(dir)
				{
				case 0:	//Go north
					for(int j = 0; j < length; j++)
					{
						if(x+j < 24)
							map[x+j][y] = ".";
					}
					break;

				case 1:	//Go south
					for(int j = 0; j < length; j++)
					{
						if(x-j >= 0)
							map[x-j][y] = ".";
					}
					break;

				case 2: //Go east
					for(int j = 0; j < length; j++)
					{
						if(y+j < 24)
							map[x][y+j] = ".";
					}
					break;

				case 3:	//Go west
					for(int j = 0; j < length; j++)
					{
						if(y-j >= 0)
							map[x][y-j] = ".";
					}
					break;
				}
				halls--;
			}
		}
		
		/*
		for(int i = 11; i < 14; i++)
		{
			for(int j = 11; j < 14; j++)
			{
				map[i][j] = ".";
			}
		}
		*/
		
		for(int i = 1500; i > 1; i--)
		{
			x = rand.nextInt(24);
			y = rand.nextInt(24);
			
			if(x < 0)
				x *= -1;
			if(y < 0)
				y *= -1;
			System.out.println("X: " + x + ", Y: " + y);
			
			
			if(this.checkAdjacent(x, y) > 0)
			{
				map[x][y] = ".";
			}
		};
		
		for(int objects = 0; objects < 3; objects++){
			x = rand.nextInt(24);
			y = rand.nextInt(24);
			
			while(map[x][y] != "."){
				x = rand.nextInt(24);
				y = rand.nextInt(24);
			}
			
			Item newitem = new Item(x, y, "k");
			locations.add(newitem);
			
		}
		x = rand.nextInt(24);
		y = rand.nextInt(24);
		while(map[x][y] != "."){
			x = rand.nextInt(24);
			y = rand.nextInt(24);
		}
		Item newitem = new Item(x, y, ">");
		locations.add(newitem);
		
		for(int mon = 0; mon < 6; mon++){
			x = rand.nextInt(24);
			y = rand.nextInt(24);
			
			while(map[x][y] != "."){
				x = rand.nextInt(24);
				y = rand.nextInt(24);
			}
			
			Enemy newenemy = new Enemy(x, y, rand.nextInt(3), rand.nextInt(2), rand.nextInt(2)+2, "&");
			monsters.add(newenemy);
			
		}
		
	}
	
	public String toString()
	{
		String out = "";
		for(int i = 0; i < 24; i++)
		{
			for(int j = 0; j < 24; j++)
			{
				if(i == playerX && j == playerY)
					out += "@";
				else if(isItem(i, j) || isMonster(i, j)){
					if(isMonster(i, j))
						out += monsterAt(i, j);
					if(isItem(i, j))
						out += itemAt(i, j);
					
				}
				else
					out += map[i][j];
			}
			out += "\n";
		}
		return out;
	}
	
	private int checkAdjacent(int x, int y)
	{
		if((x-1 > 0) && map[x-1][y].equals("."))
		{
			//this.pickFeature(x, y, Direction.EAST);
			return 0;
		}	
		else if((x+1 < 24) && map[x+1][y].equals("."))
		{
			//this.pickFeature(x, y, Direction.WEST);
			return 1;
		}
		else if((y-1 > 0) && map[x][y-1].equals("."))
		{
			//this.pickFeature(x, y, Direction.SOUTH);
			return 2;
		}
		else if((y+1 < 24) && map[x][y+1].equals("."))
		{
			//this.pickFeature(x, y, Direction.NORTH);
			return 3;
		}
		return -1;
	}
	
	public boolean checkOpen(int x, int y)
	{
		if(map[x][y].equals("."))
			return true;
		return false;
	}
	
	public void setPlayer(int x, int y)
	{
		playerX = x;
		playerY = y;
	}
	
	public boolean isItem(int x, int y){
		for(int i = 0; i < locations.size(); i++){
			if(locations.get(i).getX() == x && locations.get(i).getY() == y)
				return true;
		}
		return false;
	}
	
	public String itemAt(int x, int y){
		for(int i = 0; i < locations.size(); i++){
			if(locations.get(i).getX() == x && locations.get(i).getY() == y)
				return locations.get(i).getId();
		}
		return "?";
	}
	
	public String location(int x, int y){
		if(x > 23 || y > 23)
			return "?";
		return map[x][y];
	}
	public boolean isMonster(int x, int y){
		for(int i = 0; i < monsters.size(); i++){
			if(monsters.get(i).x == x && monsters.get(i).y == y)
				return true;
		}
		return false;
	}
	
	public String monsterAt(int x, int y){
		for(int i = 0; i < monsters.size(); i++){
			if(monsters.get(i).x == x && monsters.get(i).y == y)
				return monsters.get(i).id;
		}
		return "?";
	}
	
	public void setGameOver(){
		for(int i = 0; i < 24; i++)
		{
			for(int j = 0; j < 24; j++)
			{
					map[i][j] = " ";
			}
		}
		map[1][1] = "GAME OVER";
	}

}
