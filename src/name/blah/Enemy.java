package name.blah;

import java.util.LinkedList;
import java.util.Random;

import android.app.Notification;

public class Enemy {
	int x, y, health, damagemin, damagemax, floor, speed, range, dotpower, dottime, regeneration;
	boolean alert;
	boolean hasdot;
	boolean playerseen;
	String id;
	String name;
	

	/**
	 * creates a standard enemy based on the current level
	 * @param x the enemies new x value
	 * @param y the enemies new y value
	 * @param level the level the enemy is generated on.
	 * @param map the map for the enemy to be generated into
	 */
	public Enemy(int x, int y, int level, RoguelikeMap map) {
		super();
		this.x = x;
		this.y = y;
		this.floor = level;
		this.range = 1;
		alert = false;
		hasdot = false;
		this.speed = 1;
		
		//decides the amount of points it has to spend on abilities for this enemy
		int power = (level * 2) + 10;
		
		//randomly choose an ability to give it.
		int ability = map.getRand(0, 7);
		switch(ability){
			case 0:
				//set it so that the alert function will 
				//be called when this creature sees the player
				alert = true;
				//decrease the number of points left to spend
				power -= 2;
				break;
			case 1:
				//make the creature faster
				speed = 2;
				power -= 5;
				break;
			case 2:
				//allow it to attack at range
				range = 4;
				power -= 5;
				break;
			case 3:
				//give the creature a damage over time ability 
				//the actual cost of the damage over time is decided by a seperate function
				power -= determineDot(level, map);
				break;
			case 4:
				//make the creature slower giving the creature more points to work with
				speed = 0;
				power += 3;
				break;
			case 5:
				//makes the creatures health decrease over time
				regeneration = -(level / 2);
				if(regeneration == 0)
					regeneration = -1;
				power += 5;
				break;
			case 6:
				//makes the creatures health increase over time
				regeneration = (level / 3);
				if(regeneration == 0)
					//if the regeneration would do nothing then it costs no points
					break;
				power -= 6;
				break;
		}
		// a second roung of abilities to make the creatures sometimes a bit weirder
		if(map.getRand(0, 3) == 0){
			ability = map.getRand(0,5);
			switch(ability){
				case 0:
					alert = true;
					power -= 2;
					break;
				case 1:
					speed = 2;
					power -= 4;
					break;
				case 2:
					range = 4;
					power -= 4;
					break;
				case 4:
					speed = 0;
					power += 3;
					break;	
			}
			
		}
		//choose a random template for the creature
		switch(map.getRand(0, 5)){
			case 0:
				//calls the makename function to add in the correct adjectives
				name = makeName("kobold");
				//sets the creatures identifier on the map
				id = "k";
				//all creatures health and damage are set as a 
				//proportion of the points they have remaining
				health = power/3;
				damagemin = power/5;
				damagemax = power/4+1;
				break;
			case 1:
				name = makeName("rat");
				health = power/5;
				damagemin = power/7;
				damagemax = power/6+1;
				id = "r";
				break;
			case 2:
				name = makeName("zombie");
				health = power;
				damagemin = power/8;
				damagemax = power/7+1;
				id = "z";
				break;
			case 3:
				name = makeName("orc");
				health = power/3;
				damagemin = power/5;
				damagemax = power/4+1;
				id = "o";
				break;
			case 4:
				name = makeName("snake");
				health = power/6;
				damagemin = power/5;
				damagemax = power/4+1;
				id = "s";
				break;
		}
		
		//make sure it doesn't automatically attack the player	
		this.playerseen = false;
		
	}
	
	/**
	 * a function to move a monster around the map
	 * @param x the players x value
	 * @param y the players y value
	 * @param map the map it will be moving around
	 * @param player the target
	 * @param again a boolean to check if this is the first or second time a 
	 * creatures moved this time and prevent it from moving more than twice
	 */
	public void move(int x, int y, RoguelikeMap map, Player player, boolean again){
		//if the moster's on the wrong floor then it doesn't move
		if(this.floor != map.level)
			return;
		//if it's a slow creature then every third turn it won't move
		if(speed == 0 && map.turn % 3 == 0)
			return;
		//if it's a fast creature then every third turn it moves twice
		if(speed == 2 && map.turn % 3 == 0 && again == true)
			move(x, y, map, player, false);
		//change the health by the creatures regeneration value
		health += regeneration;
		
		//if it hasn't seen the player check if it can now
		if(playerseen == false){
			//figure out which quadrant he'll be in then check that way
			if(x > this.x){
				if(y > this.y){
					int i = this.x;
					int j = this.y;
					//progress though the array until we hit a wall or find the player
					while(i < 24 && j < 24){
						if(map.location(i, j) == " ")
							break;
						//if the coordinates are right then the monster has seen 
						//the player and can move freely now
						if(i == x && j == y){
							playerseen = true;
							//if the monster has the alert trait then it will try to warn it's friends
							if(alert == true){
								alert(this.x, this.y, map);
								//tell the player what's happening.
								map.setMessage("The " + name + " shouts a warning\n");
							}
						}
						i++;
						j++;
					}
				}
				//all of the rest of these are implementing the same formula for differnt quadrants
				if(y < this.y){
					int i = this.x;
					int j = this.y;
					while(i < 24 && j >= 0){
						if(map.location(i, j) == " ")
							break;
						if(i == x && j == y){
							playerseen = true;
							if(alert == true){
								alert(this.x, this.y, map);
								map.setMessage("The " + name + " shouts a warning\n");
							}
						}
						i++;
						j--;
					}
				}
				if(y == this.y){
					int i = this.x;
					while(i < 24){
						if(map.location(i, this.x) == " ")
							break;
						i++;
						if(i == x){
							playerseen = true;
							if(alert == true){
								alert(this.x, this.y, map);
								map.setMessage("The " + name + " shouts a warning\n");
							}
						}
					}
				}
			}
			if(x < this.x){
				if(y > this.y){
					int i = this.x;
					int j = this.y;
					while(i < 23 && j < 23){
						if(map.location(i, j) == " ")
							break;
						if(i == x && j == y){
							playerseen = true;
							if(alert == true){
								alert(this.x, this.y, map);
								map.setMessage("The " + name + " shouts a warning\n");
							}
						}
						i--;
						j++;
					}
				}
				if(y < this.y){
					int i = this.x;
					int j = this.y;
					while(i >= 0 && j >= 0){
						if(map.location(i, j) == " ")
							break;
						if(i == x && j == y){
							playerseen = true;
							if(alert == true){
								alert(this.x, this.y, map);
								map.setMessage("The " + name + " shouts a warning\n");
							}
						}
						i--;
						j--;
					}
				}
				if(y == this.y){
					int i = this.x;
					while(i >= 0){
						if(map.location(i, this.y) == " ")
							break;
						if(i == x){
							playerseen = true;
							if(alert == true){
								alert(this.x, this.y, map);
								map.setMessage("The " + name + " shouts a warning\n");
							}
						}
						i--;
					}
				}
			}
			if(x == this.x){
				if(y > this.y){
					int j = this.y;
					while(j < 24){
						if(map.location(this.x, j) == " ")
							break;
						if(j == y){
							playerseen = true;
							if(alert == true){
								alert(this.x, this.y, map);
								map.setMessage("The " + name + " shouts a warning\n");
							}
						}
						j++;
					}
				}
				if(y < this.y){
					int j = this.y;
					while(j >= 0){
						if(map.location(this.x, j) == " ")
							break;
						if(j == y){
							playerseen = true;
							if(alert == true){
								alert(this.x, this.y, map);
								map.setMessage("The " + name + " shouts a warning\n");
							}
						}
						j--;
					}
				}
				if(y == this.y){
					playerseen = true;
					if(alert == true){
						alert(this.x, this.y, map);
						map.setMessage("The " + name + " shouts a warning\n");
					}
				}
			}
		}
		
		//if the monster hasn't seen the player then it won't move
		if(playerseen == false)
			return;
		
		//check to see if the player is within range of the monster on the x-axis
		for(int i = 0; i <= range; i++){
			if((this.x + i == x || this.x - i == x) && this.y == y){
				// if the player's within range we decrease his health and tell him he was hit
				int damage = map.getRand(damagemin, damagemax);
				map.setMessage("the " + name + " hits you for " + damage + " damage\n" );
				player.setHealth(player.getHealth() - damage);
				//if the creature has a damage over time ability then we make sure the player knows about it
				if(hasdot){
					player.setDotdamage(this.dotpower);
					player.setDotlength(this.dottime);
				}
				return;
			}
		}
		//check to see if the player is within range of the monster on the y-axis
		for(int i = 0; i <= range; i++){
			if((this.y + i == y || this.y - i == y) && this.x == x){
				//deal the player his damage
				int damage = map.getRand(damagemin, damagemax);
				map.setMessage("the " + name + " hits you for " + damage + " damage\n" );
				player.setHealth(player.getHealth() - damage);
				//implement dots if we can
				if(hasdot){
					player.setDotdamage(this.dotpower);
					player.setDotlength(this.dottime);
				}
				return;
			}
		}
		
		//if the players x is less than the monsters and we can move 
		//to the left then do so 
		if(this.x > x && map.location(this.x-1, this.y) == "."){
			this.x--;
			return;
		}
		//if the players x is greater than the monsters and we can move 
		//to the right then do so 
		if(this.x < x && map.location(this.x+1, this.y) == "."){
			this.x++;
			return;
		}
		//if the players y is less than the monsters and we can move 
		//up then do so 
		if(this.y < y && map.location(this.x, this.y+1) == "."){
			this.y++;
			return;
		}
		//if the players y is greater than the monsters and we can move 
		//down then do so 
		if(this.y > y && map.location(this.x, this.y-1) == "."){
			this.y--;
			return;
		}
		
	}
	
	
	/**
	 * a function to determine a good damage over time effect for the current level
	 * @param level the current level
	 * @param map required for the getrand function
	 * @return returns the cost for the ability
	 */
	public int determineDot(int level, RoguelikeMap map){
		//set the amount of damage to be dealt per turn
		this.dotpower = (level - map.getRand(0, 5) + 4)/ 3;
		//if this would do nothing then have it do something but just a little
		if(this.dotpower <= 0)
			this.dotpower = 1;
		//determine the amount of turns it will last
		this.dottime = level - map.getRand(0, 3) + 3;
		//if it would end instantaneously then give it a little time
		if(this.dottime <= 0)
			this.dottime = 2;
		//set  a value so we know to set a dot on hit
		this.hasdot = true;
		return (dotpower * 4) * (dottime / 2); 
	}
	/**
	 * this function adds on the correct modifiers so the player knows what they're fighting
	 * @param name the base name
	 * @return returns the name with the appropriate adjectives
	 */
	public String makeName(String name){
		String answer = name;
		//if it has range then we add that tag onto the end
		if(this.range > 1){
			answer = (answer + " Archer");
		}
		// all other modifiers we add to the front
		if(this.hasdot){
			answer = ("Venomous " + answer);
		}
		if(this.alert == true){
			answer = ("Clear-eyed " + answer);
		}
		if(this.regeneration < 0){
			answer = ("Rotting " + answer);
		}
		if(this.regeneration > 0){
			answer = ("Immortal " + answer);
		}
		if(this.speed == 2){
			answer = ("Quick " + answer);
		}
		if(this.speed == 0){
			answer = ("Slow " + answer);
		}
		return answer;
	}
	
	/**
	 * this function will alert other monsters to the player's presence whenever 
	 * a monster with alert finds the player
	 * @param x the x value of the monster issuing the alert
	 * @param y the y value of the monster issuing the alert
	 * @param map the map so we can find other monsters to alert
	 */
	public void alert(int x, int y, RoguelikeMap map){
		for(int i = 0; i < map.monsters.size(); i++){
			//finds all monsters wiithin a five block radius and tells them to search for the player
			if(Math.pow(map.monsters.get(i).x - x, 2) + Math.pow(map.monsters.get(i).y - y, 2) <= 25 && Math.pow(map.monsters.get(i).x - x, 2) + Math.pow(map.monsters.get(i).y - y, 2) >= -25 && map.monsters.get(i).floor == floor)
				map. monsters.get(i).playerseen = true;
		}
	}
}
