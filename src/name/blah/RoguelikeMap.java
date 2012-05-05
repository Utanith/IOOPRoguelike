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
	LinkedList<String[][]> map = new LinkedList<String[][]>();
	String message = " ";
	int turn;
	int level;
	int maxlevel;
	int playerX;
	int playerY;
	private long oldseed = 0;
	//a linkedlist that stores the locations of all items and ladders
	LinkedList<Item> locations = new LinkedList<Item>();
	// a linkedlist that stores the locations of all enemies
	LinkedList<Enemy> monsters = new LinkedList<Enemy>();
	
	/**
	 * a public function so that everyone can use one rand function and it's seed will vary
	 * @param min the minimum value we want to get
	 * @param max the maximum value plus 1 one that we can get
	 * @return a random number from min to max - 1
	 */
	public int getRand(int min, int max){
		//instantiate a new date function
		Date now = new Date();
		//make a new seed based on the previous seed and the time since the epoch
		long seed = now.getTime()	+ oldseed;
		//set this to be the new oldseed
		oldseed = seed;
 
		//make a new randomizer seeded with the new seed
		Random randomizer = new Random(seed);
		int n = max - min;
		int i = randomizer.nextInt(n);
		//if for some reason it ends up negative turn it positive
		if (i < 0)
			i = -i;
		//return the random value plus the minimum value
		return min + i;
	}
 
	
	/**
	 * a constructor to make the first floor of the dungeon
	 */
	RoguelikeMap()
	{
		generateFloor();
		level = 0;
		maxlevel = 0;
		turn = 0;
		
	}
	
	/* a function that will make the array into something we can easily print out
	 */
	public String toString()
	{
		String out = "";
		for(int i = 0; i < 24; i++)
		{
			for(int j = 0; j < 24; j++)
			{
				//if the value is within a five block radius from the player then
				//we add it in otherwise we just add a space
				if((Math.pow(i - playerX, 2) + Math.pow(j - playerY, 2)) <= 25 && (Math.pow(i - playerX, 2) + Math.pow(j - playerY, 2)) >= -25){
					//if it's the player's space then print out the player sign instead
					if(i == playerX && j == playerY)
						out += "@";
					//if there's a monster or item there then print that out
					else if(isItem(i, j, level) || isMonster(i, j, level)){
						if(isMonster(i, j, level))
							out += monsterAt(i, j, level);
						else if(isItem(i, j, level))
							out += itemAt(i, j, level);
						
					}
					//if there's nothing there then just print out the map for that space
					else
						out += map.get(level)[i][j];
				}else{
					out += " ";
				}
			}
			//when we reach the end of an x line then we add in a newline character
			//and continue onward
			out += "\n";
		}
		return out;
	}
	
	/**
	 * checks if there is an open space adjacent
	 * @param x the x value to check
	 * @param y the y value to check
	 * @return returns 0 or greater on success -1 on failure
	 */
	private int checkAdjacent(int x, int y)
	{
		if((x-1 > 0) && map.get(level)[x-1][y].equals("."))
		{
			return 0;
		}	
		else if((x+1 < 24) && map.get(level)[x+1][y].equals("."))
		{
			return 1;
		}
		else if((y-1 > 0) && map.get(level)[x][y-1].equals("."))
		{
			return 2;
		}
		else if((y+1 < 24) && map.get(level)[x][y+1].equals("."))
		{
			return 3;
		}
		return -1;
	}
	
	/**
	 * checks to see if a new feature can be put there
	 * @param x the x value to check
	 * @param y the y value to check
	 * @return true if it is a valid location false otherwise
	 */
	public boolean checkOpen(int x, int y)
	{
		// if it's outside the boundaries then it fails
		if(x < 0 || x > 23 || y < 0 || y > 23)
			return false;
		
		//if theres empty space there then return true
		if(!map.get(level)[x][y].equals(" "))
			return true;
		//otherwise it's false
		return false;
	}
	
	/**
	 * set's the player to those values
	 * @param x the x-value to be set to
	 * @param y the y-value to be set to
	 */
	public void setPlayer(int x, int y)
	{
		playerX = x;
		playerY = y;
	}
	
	/**
	 * check if there's an item in the given location
	 * @param x the x value to check
	 * @param y the y value to check
	 * @param floor the floor to check
	 * @return true if there's an item false otherwise
	 */
	public boolean isItem(int x, int y, int floor){
		//go through the entire linkedlist of items and check if each of them meets the requirements
		for(int i = 0; i < locations.size(); i++){
			//if there's a valid item with the right attributes then return true
			if(locations.get(i).getX() == x && locations.get(i).getY() == y && locations.get(i).floor == floor)
				return true;
		}
		return false;
	}
	
	/**
	 * returns what items at the given location
	 * @param x the x-value to be checked
	 * @param y the y-value to be checked
	 * @param floor the floor to check
	 * @return the items id on success a ? on failure
	 */
	public String itemAt(int x, int y, int floor){
		//if there's an item in the given location then return the item's id
		for(int i = 0; i < locations.size(); i++){
			if(locations.get(i).getX() == x && locations.get(i).getY() == y && locations.get(i).floor == floor)
				return locations.get(i).getId();
		}
		return "?";
	}
	
	/**
	 * returns what tile is at the given location
	 * @param x the x value to check
	 * @param y the y -value to check
	 * @return the tile at that location
	 */
	public String location(int x, int y){
		//if it's outside the bounds return a ? 
		if(x > 23 || y > 23 || x < 0 || y < 0)
			return "?";
		//otherwise return the item
		return map.get(level)[x][y];
	}
	/**
	 * checks for a monster on the given location
	 * @param x x -value to be checked
	 * @param y y-value to be checked
	 * @param floor the floor to be checked
	 * @return true or false
	 */
	public boolean isMonster(int x, int y, int floor){
		for(int i = 0; i < monsters.size(); i++){
			if(monsters.get(i).x == x && monsters.get(i).y == y && monsters.get(i).floor == floor)
				return true;
		}
		return false;
	}
	
	/**
	 * returns the id of the monster there
	 * @param x x to be checked
	 * @param y y to be checked
	 * @param floor floor to be checked
	 * @return id of the monster or ? on failure
	 */
	public String monsterAt(int x, int y, int floor){
		for(int i = 0; i < monsters.size(); i++){
			if(monsters.get(i).x == x && monsters.get(i).y == y && monsters.get(i).floor == floor)
				return monsters.get(i).id;
		}
		return "?";
	}
	
	/**
	 * on game over remove the tiles
	 * currently keeps the monsters items and the player though
	 */
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

	/**
	 * generates a floor
	 */
	public void generateFloor(){
		String[][] floor = new String[24][24];
		int x = getRand(8, 16);
		int y = getRand(8, 16);
		//Fill the map with the base tile - Impassable terrain
		for(int i = 0; i < 24; i++)
		{
			for(int j = 0; j < 24; j++)
			{
				//set's up a few hallways of walkable tile
				if((i > 2 && i < 21) && j == 11 + x)
					floor[i][j] = ".";
				else if((j > 3 && j < 20) && i == 11 + y)
					floor[i][j] = ".";
				else
					floor[i][j] = " ";
			}
		}
		
		//adds the floor onto the linkedlist
		map.add(floor);
		
		int halls, length, dir;
		halls = 2;
		
		//generate some more hallways adjacent to the ones currently there
		while(halls > 0)
		{
			x = getRand(0, 24);
			y = getRand(0, 24);
			length = getRand(4, 8);
			if(checkOpen(x,y))
			{
				//switch on the random value  to determine direction
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
		//to generate something that looks roughly like a cave we just pick random spots
		//then if they're adjacent to a walkable tile we make them walkable too
		for(int i = 1500; i > 1; i--)
		{
			x = getRand(0, 24);
			y = getRand(0, 24);
			
			if(x < 0)
				x *= -1;
			if(y < 0)
				y *= -1;
			
			
			if(this.checkAdjacent(x, y) >= 0)
			{
				map.get(level)[x][y] = ".";
			}
		}
		
		//this will find places to put health potions
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
		
		//find a location for a ladder down
		x = getRand(0, 24);
		y = getRand(0, 24);
		while(!checkOpen(x, y)){
			x = getRand(0, 24);
			y = getRand(0, 24);
		}
		Item newitem = new Item(x, y, ">", level);
		locations.add(newitem);
		
		//if we're not on the first floor then we make a ladder upward as well
		if(level != 0){
			x = getRand(0, 24);
			y = getRand(0, 24);
			while(map.get(level)[x][y] != "."){
				x = getRand(0, 24);
				y = getRand(0, 24);
			}
		newitem = new Item(x, y, "<", level);
		locations.add(newitem);
		}
		
		//generate six monsters for the level
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
	
	//not currently used
	/*public boolean isLit(int startx, int starty, int endx, int endy){
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
		
	}*/
	
	/**
	 * @param message the message to be sent to the player
	 */
	public void setMessage(String message){
		this.message = (this.message + message);
	}
}
