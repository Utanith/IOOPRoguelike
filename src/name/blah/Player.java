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
	
	Player(RoguelikeMap map)
	{
		this.setHealth(200);
		
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
	
	public boolean move(Direction dir, RoguelikeMap map)
	{
		if(isgameover()){
			map.setGameOver();
			return false;
		}
		map.turn++;
		switch(dir)
		{
		case NORTH:
			if(map.isMonster(this.x-1, this.y, map.level)){
				for(int i = 0; i < map.monsters.size(); i++){
					if(map.monsters.get(i).x == x-1 && map.monsters.get(i).y == y){
						int damage = map.getRand(minDamage , maxDamage);
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
			if(map.isItem(this.x-1, this.y, map.level)){
				if(map.itemAt(this.x-1, this.y, map.level) == "#"){
					map.setMessage("You pick up and promptly chug a red potion\n" );
					this.setHealth(this.getHealth() + 5);
					for(int i = 0; i < map.locations.size(); i++){
						if(map.locations.get(i).getX() == x-1 && map.locations.get(i).getY() == y)
							map.locations.remove(i);
					}
				}
				if(map.itemAt(this.x-1, this.y, map.level) == ">"){
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
				if(map.itemAt(this.x-1, this.y, map.level) == "<"){
					map.level--;
					map.setMessage("You clamber up the narrow ladder\n" );
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
			if(map.checkOpen(this.x-1, this.y))
			{
				this.x--;
				map.setPlayer(this.x, this.y);
				for(int i = 0; i < map.monsters.size(); i++)
					map.monsters.get(i).move(this.x, this.y, map, this, true);
				return true;
			}
			
			for(int i = 0; i < map.monsters.size(); i++)
				map.monsters.get(i).move(this.x, this.y, map, this, true);
			break;
			
		case SOUTH:
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

	public int getHealth() {
		return health;
	}

	public void setHealth(int health) {
		this.health = health;
	}
	
	public boolean isgameover(){
		if(health <= 0)
			return true;
		return false;
	}
	
	public int[] getLocation()
	{
		int[] out = {x, y};
		return out;
	}
}
