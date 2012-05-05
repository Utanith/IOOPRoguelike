/**
 * 
 */
package name.blah;

import java.util.Date;
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
	String message = " ";
	int turn;
	int level;
	int maxlevel;
	int playerX;
	int playerY;
	private long oldseed = 0;
	LinkedList<Item> locations = new LinkedList<Item>();
	LinkedList<Enemy> monsters = new LinkedList<Enemy>();
	
	public int getRand(int min, int max){
		Date now = new Date();
		long seed = now.getTime()	+ oldseed;
		oldseed = seed;
 
		Random randomizer = new Random(seed);
		int n = max - min;
		int i = randomizer.nextInt(n);
		if (i < 0)
			i = -i;
		return min + i;
	}
 
	
	RoguelikeMap()
	{
		generateFloor();
		level = 0;
		maxlevel = 0;
		turn = 0;
		
	}
	
	public String toString()
	{
		String out = "";
		for(int i = 0; i < 24; i++)
		{
			for(int j = 0; j < 24; j++)
			{
				if((Math.pow(i - playerX, 2) + Math.pow(j - playerY, 2)) <= 25 && (Math.pow(i - playerX, 2) + Math.pow(j - playerY, 2)) >= -25){
					if(i == playerX && j == playerY)//if(isLit(playerX, playerY, i, j)){
						out += "@";
					else if(isItem(i, j, level) || isMonster(i, j, level)){
						if(isMonster(i, j, level))
							out += monsterAt(i, j, level);
						else if(isItem(i, j, level))
							out += itemAt(i, j, level);
						
					}
					else
						out += map.get(level)[i][j];
				}else{
					out += " ";
				}
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
		if(x > 23 || y > 23 || x < 0 || y < 0)
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
		int x = getRand(8, 16);
		int y = getRand(8, 16);
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
			x = getRand(0, 24);
			y = getRand(0, 24);
			length = getRand(4, 8);
			if(checkOpen(x,y))
			{
				dir = getRand(0, 4);
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
			x = getRand(0, 24);
			y = getRand(0, 24);
			
			if(x < 0)
				x *= -1;
			if(y < 0)
				y *= -1;
			System.out.println("X: " + x + ", Y: " + y);
			
			
			if(this.checkAdjacent(x, y) >= 0)
			{
				map.get(level)[x][y] = ".";
			}
		}
		
		for(int objects = 0; objects < 3; objects++){
			x = getRand(0, 24);
			y = getRand(0, 24);
			
			while(!checkOpen(x, y)){
				x = getRand(0, 24);
				y = getRand(0, 24);
			}
			
			Item newitem = new Item(x, y, "#", level);
			locations.add(newitem);
			
		}
		
		x = getRand(0, 24);
		y = getRand(0, 24);
		while(!checkOpen(x, y)){
			x = getRand(0, 24);
			y = getRand(0, 24);
		}
		Item newitem = new Item(x, y, ">", level);
		locations.add(newitem);
		
		if(level != 0){
			x = getRand(0, 24);
			y = getRand(0, 24);
			while(map.get(level)[x][y] != "."){
				x = getRand(0, 24);
				y = getRand(0, 24);
			}
		}
		newitem = new Item(x, y, "<", level);
		locations.add(newitem);
		
		for(int mon = 0; mon < 6; mon++){
			x = getRand(0, 24);
			y = getRand(0, 24);
			
			while(!checkOpen(x, y)){
				x = getRand(0, 24);
				y = getRand(0, 24);
			}

			Enemy newenemy = new Enemy(x, y, level, this);
			monsters.add(newenemy);
			
		}
	}
	
	public boolean isLit(int startx, int starty, int endx, int endy){
		if(startx == endx){
			if(endy > starty){
				while(location(startx, starty) != " " && endy != starty)
					starty++;
			}else{
				while(location(startx, starty) != " " && endy != starty)
				starty--;
			}
			if(location(startx, starty) == " ")
				return false;
			return true;
		}
		double ratio = (endy - starty)/(endx - startx);

		double ycounter = 0;
		int yvalue = 0;
		if(endx > startx){
			while((startx != endx && yvalue != endy) && location(startx, yvalue) != " "){
				startx++;
				ycounter += ratio;
				yvalue = (int) (ycounter + starty);
			}
		}else{
			while((startx != endx && yvalue != endy) && location(startx, yvalue) != " "){
				startx--;
				ycounter += ratio;
				yvalue = (int) (ycounter + starty);
			}
		}
		if(location(startx, yvalue) == " ")
			return false;
		return true;
		
	}
	
	public void setMessage(String message){
		this.message = (this.message + message);
	}
}
