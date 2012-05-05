/**
 * 
 */
package name.blah;


import android.widget.ScrollView;

/**
 * @author Jett Peterson
 *
 */
public class Player {
	
	private int x;
	private int y;
	private int health;
	private int minDamage;
	private int maxDamage;
	private int dotdamage;
	private int dotlength;
	
	
	/**
	 * standard getter
	 */
	public int getDotdamage() {
		return dotdamage;
	}

	/**
	 * @param dotdamage sets dotdamage
	 */
	public void setDotdamage(int dotdamage) {
		this.dotdamage = dotdamage;
	}

	/**
	 * @return returns how long the dot has left
	 */
	public int getDotlength() {
		return dotlength;
	}

	/**
	 * @param dotlength sets the length of the damage over time effect
	 */
	public void setDotlength(int dotlength) {
		this.dotlength = dotlength;
	}


	
	
	/**
	 * Creates the player and set his starting stats
	 * @param map to determine where to start the player
	 */
	Player(RoguelikeMap map)
	{
		//set the players health to the standard starting amount 30
		this.setHealth(30);
		
		minDamage = 3;
		maxDamage = 6;
		int x, y;
		x = map.getRand(0, 24);
		y = map.getRand(0, 24);
		
		while(!map.checkOpen(x, y))
		{
			x = map.getRand(0, 24);
			y = map.getRand(0, 24);
		}
		
		this.x = x;
		this.y = y;
		map.setPlayer(x, y);
	}
	
	/**
	 * Controls the players movements and by extension the rest of the game
	 * @param dir the direction the player is moving
	 * @param map dictates where the player can move and what he can interact with
	 */
	public boolean move(Direction dir, RoguelikeMap map)
	{
		//checks if the character is still alive or if the game has ended
		if(isgameover()){
			map.setGameOver();
			return false;
		}
		
		//advance the turn clock
		map.turn++;
		
		
		//checks if the player is currently suffering from a damage over time effect
		if(dotlength != 0){
			dotlength--;
			this.health -= dotdamage;
			
			//make sure the player doesn't die of poison
			if(this.health < 1)
				this.health = 1;
			
			//tell the player what's happening
			map.setMessage("you're poisoned ");
		}
		switch(dir)
		{
		case NORTH:
			//check if the player is trying to attack a monster
			if(map.isMonster(this.x-1, this.y, map.level)){
				/*if there is a monster at the location the player is trying to move to 
				 * then we have to find out which one is there*/
				
				for(int i = 0; i < map.monsters.size(); i++){
					
					//if the current monsters location is correct then we attack him
					if(map.monsters.get(i).x == x-1 && map.monsters.get(i).y == y){
						
						//use the getRand function that we set up to find the correct values 
						int damage = map.getRand(minDamage , maxDamage);
						
						//tell the player how successful they were and adjust the monster's health accordingly
						map.setMessage("you hit the " + map.monsters.get(i).name + " for " + damage + " damage\n" );
						map.monsters.get(i).health -= damage;
						
						//if the monsters dead then we delete it and congratulate the player
						if(map.monsters.get(i).health <= 0){
							map.setMessage("you vanquish the " + map.monsters.get(i).name + "\n" );
							map.monsters.remove(i);
						}
						
						//have the monsters move before we end this part of the function.
						for(int j = 0; j < map.monsters.size(); j++)
							map.monsters.get(j).move(this.x, this.y, map, this, true);
						return true;
					}
				}
			}
			
			//if the player is trying to interact with an item
			if(map.isItem(this.x-1, this.y, map.level)){
				
				//if the item's a potion we remove the potion tell the player and increase his health
				if(map.itemAt(this.x-1, this.y, map.level) == "#"){
					map.setMessage("You pick up and promptly chug a red potion\n" );
					this.setHealth(this.getHealth() + 5);
					
					//we have to find the item with the correct x and y values and delete that one.
					for(int i = 0; i < map.locations.size(); i++){
						if(map.locations.get(i).getX() == x-1 && map.locations.get(i).getY() == y)
							map.locations.remove(i);
					}
				}
				
				//if the player is trying to climb a ladder
				if(map.itemAt(this.x-1, this.y, map.level) == ">"){
					//we increase the current floor so the game will load the correct level
					map.level++;
					map.setMessage("You climb down the narrow ladder\n" );
					
					//if this floor hasn't been visited yet we generate it then add it to the linkedlist
					if(map.level > map.maxlevel){
						map.generateFloor();
						map.maxlevel++;
					}
					
					//we try and find the correct block to put the player on
					for(int i = 0; i < map.locations.size(); i++){
						if(map.locations.get(i).id == "<" && map.locations.get(i).floor == map.level){
							x = map.locations.get(i).x;
							y = map.locations.get(i).y;
						}
					}
					
					//set the player to the new values
					map.setPlayer(x, y);
					return true;
				}
				
				//if the player is trying to climp up a ladder
				if(map.itemAt(this.x-1, this.y, map.level) == "<"){
					
					//we decrease the level that we're on then load up the old map for this level
					map.level--;
					map.setMessage("You clamber up the narrow ladder\n" );
					
					//we have to set the player to the right area on the new floor
					for(int i = 0; i < map.locations.size(); i++){
						if(map.locations.get(i).id == ">" && map.locations.get(i).floor == map.level){
							x = map.locations.get(i).x;
							y = map.locations.get(i).y;
						}
					}
					map.setPlayer(x, y);
					return true;
				}
			}
			
			//if the player is just moving into a non-occupied space we check if there's a wall there first
			if(map.checkOpen(this.x-1, this.y))
			{
				//if the area is open we move him there then move the monsters around
				this.x--;
				map.setPlayer(this.x, this.y);
				for(int i = 0; i < map.monsters.size(); i++)
					map.monsters.get(i).move(this.x, this.y, map, this, true);
				return true;
			}
			
			//if the player runs into a wall we'll still move the monsters
			for(int i = 0; i < map.monsters.size(); i++)
				map.monsters.get(i).move(this.x, this.y, map, this, true);
			break;
			
		case SOUTH:
			//the only difference on the various other cases is the direction of movement
			if(map.isMonster(this.x+1, this.y, map.level)){
				
				for(int i = 0; i < map.monsters.size(); i++){
					
					if(map.monsters.get(i).x == x+1 && map.monsters.get(i).y == y){
						
						int damage = map.getRand(minDamage, maxDamage);
						map.setMessage("you hit the " + map.monsters.get(i).name + " for " + damage + " damage\n" );
						map.monsters.get(i).health -= damage;
						
						if(map.monsters.get(i).health <= 0){
							map.setMessage("you vanquish the " + map.monsters.get(i).name + "\n" );
							map.monsters.remove(i);
						}
						for(int j = 0; j < map.monsters.size(); j++)
							map.monsters.get(j).move(this.x, this.y, map, this, true);
						return true;
					}
				}
			}
			if(map.isItem(this.x+1, this.y, map.level)){
				
				if(map.itemAt(this.x+1, this.y, map.level) == "#"){
					map.setMessage("You pick up and promptly chug a red potion\n" );
					this.setHealth(this.getHealth() + 5);
					
					for(int i = 0; i < map.locations.size(); i++){
						if(map.locations.get(i).getX() == x+1 && map.locations.get(i).getY() == y)
							map.locations.remove(i);
					}
				}
				if(map.itemAt(this.x+1, this.y, map.level) == ">"){
					map.level++;
					map.setMessage("You climb down the narrow ladder\n" );
					
					if(map.level > map.maxlevel){
						map.generateFloor();
						map.maxlevel++;
					}
					for(int i = 0; i < map.locations.size(); i++){
						if(map.locations.get(i).id == "<" && map.locations.get(i).floor == map.level){
							x = map.locations.get(i).x;
							y = map.locations.get(i).y;
						}
					}
					map.setPlayer(x, y);
					return true;
				}
				if(map.itemAt(this.x+1, this.y, map.level) == "<"){
					map.level--;
					map.setMessage("You clamber up the narrow ladder\n" );
					
					for(int i = 0; i < map.locations.size(); i++){
						if(map.locations.get(i).id == "<" && map.locations.get(i).floor == map.level){
							x = map.locations.get(i).x;
							y = map.locations.get(i).y;
						}
					}
					map.setPlayer(x, y);
					return true;
				}
			}
			if(map.checkOpen(this.x+1, this.y))
			{
				this.x++;
				map.setPlayer(this.x, this.y);
				
				for(int i = 0; i < map.monsters.size(); i++)
					map.monsters.get(i).move(this.x, this.y, map, this, true);
				return true;
			}
			
			for(int i = 0; i < map.monsters.size(); i++)
				map.monsters.get(i).move(this.x, this.y, map, this, true);
			
			break;
			
		case EAST:

			if(map.isMonster(this.x, this.y+1, map.level)){
				
				for(int i = 0; i < map.monsters.size(); i++){
					
					if(map.monsters.get(i).x == x && map.monsters.get(i).y == y+1){
						
						int damage = map.getRand(minDamage, maxDamage);
						map.setMessage("you hit the " + map.monsters.get(i).name + " for " + damage + " damage\n" );
						map.monsters.get(i).health -= damage;
						
						if(map.monsters.get(i).health <= 0){
							map.setMessage("you vanquish the " + map.monsters.get(i).name + "\n" );
							map.monsters.remove(i);
						}
						
						for(int j = 0; j < map.monsters.size(); j++)
							map.monsters.get(j).move(this.x, this.y, map, this, true);
						return true;
					}
				}
			}
			if(map.isItem(this.x, this.y+1, map.level)){
				
				if(map.itemAt(this.x, this.y+1, map.level) == "#"){
					this.setHealth(this.getHealth() + 5);
					map.setMessage("You pick up and promptly chug a red potion\n" );
					
					for(int i = 0; i < map.locations.size(); i++){
						if(map.locations.get(i).getX() == x && map.locations.get(i).getY() == y+1)
							map.locations.remove(i);
					}
				}
				if(map.itemAt(this.x, this.y+1, map.level) == ">"){
					map.level++;
					map.setMessage("You climb down the narrow ladder\n" );
					
					if(map.level > map.maxlevel){
						map.generateFloor();
						map.maxlevel++;
					}
					for(int i = 0; i < map.locations.size(); i++){
						if(map.locations.get(i).id == "<" && map.locations.get(i).floor == map.level){
							x = map.locations.get(i).x;
							y = map.locations.get(i).y;
						}
					}
					map.setPlayer(x, y);
					return true;
				}
				if(map.itemAt(this.x, this.y+1, map.level) == "<"){
					map.level--;
					map.setMessage("You clamber up the narrow ladder\n" );
					
					for(int i = 0; i < map.locations.size(); i++){
						if(map.locations.get(i).id == "<" && map.locations.get(i).floor == map.level){
							x = map.locations.get(i).x;
							y = map.locations.get(i).y;
						}
					}
					map.setPlayer(x, y);
					return true;
				}
			}
			if(map.checkOpen(this.x, this.y+1))
			{
				this.y++;
				map.setPlayer(this.x, this.y);
				
				for(int i = 0; i < map.monsters.size(); i++)
					map.monsters.get(i).move(this.x, this.y, map, this, true);
				return true;
			}
			
			for(int i = 0; i < map.monsters.size(); i++)
				map.monsters.get(i).move(this.x, this.y, map, this, true);
			
			break;
			
		case WEST:
			if(map.isMonster(this.x, this.y-1, map.level)){
				
				for(int i = 0; i < map.monsters.size(); i++){
					
					if(map.monsters.get(i).x == x && map.monsters.get(i).y == y-1){
						int damage = map.getRand(minDamage, maxDamage);
						map.setMessage("you hit the " + map.monsters.get(i).name + " for " + damage + " damage\n" );
						map.monsters.get(i).health -= damage;
						
						if(map.monsters.get(i).health <= 0){
							map.setMessage("you vanquish the " + map.monsters.get(i).name + "\n" );
							map.monsters.remove(i);
						}
						for(int j = 0; j < map.monsters.size(); j++)
							map.monsters.get(j).move(this.x, this.y, map, this, true);
						return true;
					}
				}
			}
			if(map.isItem(this.x, this.y-1, map.level)){
				if(map.itemAt(this.x, this.y-1, map.level) == "#"){
					map.setMessage("You pick up and promptly chug a red potion\n" );
					this.setHealth(this.getHealth() + 5);
					
					for(int i = 0; i < map.locations.size(); i++){
						if(map.locations.get(i).getX() == x && map.locations.get(i).getY() == y-1)
							map.locations.remove(i);
					}
				}
				if(map.itemAt(this.x, this.y-1, map.level) == ">"){
					map.level++;
					if(map.level > map.maxlevel){
						map.generateFloor();
						map.maxlevel++;
					}
					map.setMessage("You climb down the narrow ladder\n" );
					for(int i = 0; i < map.locations.size(); i++){
						if(map.locations.get(i).id == "<" && map.locations.get(i).floor == map.level){
							x = map.locations.get(i).x;
							y = map.locations.get(i).y;
						}
					}
					map.setPlayer(x, y);
					return true; 
				}
				if(map.itemAt(this.x, this.y-1, map.level) == "<"){
					map.level--;
					map.setMessage("You clamber up the narrow ladder\n" );
					for(int i = 0; i < map.locations.size(); i++){
						if(map.locations.get(i).id == "<" && map.locations.get(i).floor == map.level){
							x = map.locations.get(i).x;
							y = map.locations.get(i).y;
						}
					}
					map.setPlayer(x, y);
					return true;
				}
			}
			if(map.checkOpen(this.x, this.y-1))
			{
				this.y--;
				map.setPlayer(this.x, this.y);
				for(int i = 0; i < map.monsters.size(); i++)
					map.monsters.get(i).move(this.x, this.y, map, this, true);
				return true;
			}
			
			for(int i = 0; i < map.monsters.size(); i++)
				map.monsters.get(i).move(this.x, this.y, map, this, true);
			
			break;
		}
		return false;
	}

	/**
	 * @return returns the character's current health
	 */
	public int getHealth() {
		return health;
	}

	/**
	 * @param health set's the player's health to this value
	 */
	public void setHealth(int health) {
		this.health = health;
	}
	
	/**
	 * @return returns wether the player is still alive or not
	 */
	public boolean isgameover(){
		if(health <= 0)
			return true;
		return false;
	}
	
	/**
	 * @return returns the player's current location
	 */
	public int[] getLocation()
	{
		int[] out = {x, y};
		return out;
	}
}
