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
	LinkedList<String[][]> map = new LinkedList<String[][]>();
	int level;
	int maxlevel;
	int playerX;
	int playerY;
	LinkedList<Item> locations = new LinkedList<Item>();
	LinkedList<Enemy> monsters = new LinkedList<Enemy>();
	
	RoguelikeMap()
	{
		for(level = 0; level < 3; level++){
			generateFloor();
		}
		level = 0;
		maxlevel = 2;
		
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
				else if(isItem(i, j, level) || isMonster(i, j, level)){
					if(isMonster(i, j, level))
						out += monsterAt(i, j, level);
					else if(isItem(i, j, level))
						out += itemAt(i, j, level);
					
				}
				else
					out += map.get(level)[i][j];
			}
			out += "\n";
		}
		return out;
	}
	
	private int checkAdjacent(int x, int y)
	{
		if((x-1 > 0) && map.get(level)[x-1][y].equals("."))
		{
			//this.pickFeature(x, y, Direction.EAST);
			return 0;
		}	
		else if((x+1 < 24) && map.get(level)[x+1][y].equals("."))
		{
			//this.pickFeature(x, y, Direction.WEST);
			return 1;
		}
		else if((y-1 > 0) && map.get(level)[x][y-1].equals("."))
		{
			//this.pickFeature(x, y, Direction.SOUTH);
			return 2;
		}
		else if((y+1 < 24) && map.get(level)[x][y+1].equals("."))
		{
			//this.pickFeature(x, y, Direction.NORTH);
			return 3;
		}
		return -1;
	}
	
	public boolean checkOpen(int x, int y)
	{
		if(x < 0 || x > 23 || y < 0 || y > 23)
			return false;
		
		if(!map.get(level)[x][y].equals(" "))
			return true;
		return false;
	}
	
	public void setPlayer(int x, int y)
	{
		playerX = x;
		playerY = y;
	}
	
	public boolean isItem(int x, int y, int floor){
		for(int i = 0; i < locations.size(); i++){
			if(locations.get(i).getX() == x && locations.get(i).getY() == y && locations.get(i).floor == floor)
				return true;
		}
		return false;
	}
	
	public String itemAt(int x, int y, int floor){
		for(int i = 0; i < locations.size(); i++){
			if(locations.get(i).getX() == x && locations.get(i).getY() == y && locations.get(i).floor == floor)
				return locations.get(i).getId();
		}
		return "?";
	}
	
	public String location(int x, int y){
		if(x > 23 || y > 23)
			return "?";
		return map.get(level)[x][y];
	}
	public boolean isMonster(int x, int y, int floor){
		for(int i = 0; i < monsters.size(); i++){
			if(monsters.get(i).x == x && monsters.get(i).y == y && monsters.get(i).floor == floor)
				return true;
		}
		return false;
	}
	
	public String monsterAt(int x, int y, int floor){
		for(int i = 0; i < monsters.size(); i++){
			if(monsters.get(i).x == x && monsters.get(i).y == y && monsters.get(i).floor == floor)
				return monsters.get(i).id;
		}
		return "?";
	}
	
	public void setGameOver(){
		for(int i = 0; i < 24; i++)
		{
			for(int j = 0; j < 24; j++)
			{
					map.get(level)[i][j] = " ";
			}
		}
		map.get(level)[1][1] = "GAME OVER";
	}

	public void generateFloor(){
		String[][] floor = new String[24][24];
		int x = rand.nextInt(16)-8;
		int y = rand.nextInt(16)-8;
		//Fill the map with the base tile - Impassable terrain
		for(int i = 0; i < 24; i++)
		{
			for(int j = 0; j < 24; j++)
			{
				if((i > 2 && i < 21) && j == 11 + x)
					floor[i][j] = ".";
				else if((j > 3 && j < 20) && i == 11 + y)
					floor[i][j] = ".";
				else
					floor[i][j] = " ";
			}
		}
		
		map.add(floor);
		
		int halls, length, dir;
		halls = 2;
		
		while(halls > 0)
		{
			x = rand.nextInt(24);
			y = rand.nextInt(24);
			length = rand.nextInt(8) + 4;
			if(map.get(level)[x][y].equals("."))
			{
				dir = rand.nextInt(4);
				switch(dir)
				{
				case 0:	//Go north
					for(int j = 0; j < length; j++)
					{
						if(x+j < 24)
							map.get(level)[x+j][y] = ".";
					}
					break;

				case 1:	//Go south
					for(int j = 0; j < length; j++)
					{
						if(x-j >= 0)
							map.get(level)[x-j][y] = ".";
					}
					break;

				case 2: //Go east
					for(int j = 0; j < length; j++)
					{
						if(y+j < 24)
							map.get(level)[x][y+j] = ".";
					}
					break;

				case 3:	//Go west
					for(int j = 0; j < length; j++)
					{
						if(y-j >= 0)
							map.get(level)[x][y-j] = ".";
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
				map.get(level)[x][y] = ".";
			}
		};
		
		for(int objects = 0; objects < 3; objects++){
			x = rand.nextInt(24);
			y = rand.nextInt(24);
			
			while(map.get(level)[x][y] != "."){
				x = rand.nextInt(24);
				y = rand.nextInt(24);
			}
			
			Item newitem = new Item(x, y, "k", level);
			locations.add(newitem);
			
		}
		x = rand.nextInt(24);
		y = rand.nextInt(24);
		while(map.get(level)[x][y] != "."){
			x = rand.nextInt(24);
			y = rand.nextInt(24);
		}
		Item newitem = new Item(x, y, ">", level);
		locations.add(newitem);
		
		if(level != 0){
			x = rand.nextInt(24);
			y = rand.nextInt(24);
			while(map.get(level)[x][y] != "."){
				x = rand.nextInt(24);
				y = rand.nextInt(24);
			}
		}
		newitem = new Item(x, y, "<", level);
		locations.add(newitem);
		
		for(int mon = 0; mon < 6; mon++){
			x = rand.nextInt(24);
			y = rand.nextInt(24);
			
			while(map.get(level)[x][y] != "."){
				x = rand.nextInt(24);
				y = rand.nextInt(24);
			}
			
			Enemy newenemy = new Enemy(x, y, rand.nextInt(3)+3, rand.nextInt(2), rand.nextInt(2)+2, "&", level);
			monsters.add(newenemy);
			
		}
	}
}
