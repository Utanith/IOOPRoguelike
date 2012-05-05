package name.blah;

import java.util.LinkedList;
import java.util.Random;

public class Enemy {
	int x, y, health, damagemin, damagemax, floor, speed, range, dotpower, dottime, regeneration;
	boolean alert;
	boolean hasdot;
	boolean playerseen;
	String id;
	String name;
	

	public Enemy(int x, int y, int level, RoguelikeMap map) {
		super();
		Random rand = new Random();
		this.x = x;
		this.y = y;
		this.floor = level;
		this.range = 1;
		alert = false;
		hasdot = false;
		this.speed = 1;
		int power = (level * 2) + 10;
		int ability = map.getRand(0, 7);
		switch(ability){
			case 0:
				alert = true;
				power -= 2;
				break;
			case 1:
				speed = 2;
				power -= 5;
				break;
			case 2:
				range = 4;
				power -= 5;
				break;
			case 3:
				power -= determineDot(level, map);
				break;
			case 4:
				speed = 0;
				power += 3;
				break;
			case 5:
				regeneration = -(level / 2);
				if(regeneration == 0)
					regeneration = -1;
				power += 5;
				break;
			case 6:
				regeneration = (level / 3);
				if(regeneration == 0)
					break;
				power -= 6;
				break;
		}
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
		switch(map.getRand(0, 5)){
			case 0:
				name = makeName("kobold");
				id = "k";
				health = power/3;
				damagemin = power/5;
				damagemax = power/5+1;
				break;
			case 1:
				name = makeName("rat");
				health = power/5;
				damagemin = power/6;
				damagemax = power/6+1;
				id = "r";
				break;
			case 2:
				name = makeName("zombie");
				health = power;
				damagemin = power/8;
				damagemax = power/8+1;
				id = "z";
				break;
			case 3:
				name = makeName("orc");
				health = power/3;
				damagemin = power/4;
				damagemax = power/4+1;
				id = "o";
				break;
			case 4:
				name = makeName("snake");
				health = power/6;
				damagemin = power/4;
				damagemax = power/4+1;
				id = "s";
				break;
		}
				
		this.playerseen = false;
		
	}
	
	public void move(int x, int y, RoguelikeMap map, Player player, boolean again){
		if(this.floor != map.level)
			return;
		if(speed == 0 && map.turn % 3 == 0)
			return;
		if(speed == 2 && map.turn % 3 == 0 && again == true)
			move(x, y, map, player, false);
		health += regeneration;
		if(playerseen == false){
			if(x > this.x){
				if(y > this.y){
					int i = this.x;
					int j = this.y;
					while(i < 24 && j < 24){
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
						j++;
					}
				}
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
		if(playerseen == false)
			return;
		for(int i = 0; i <= range; i++){
			if((this.x + i == x || this.x - i == x) && this.y == y){
				int damage = map.getRand(damagemin, damagemax);
				map.setMessage("the " + name + " hits you for " + damage + " damage\n" );
				player.setHealth(player.getHealth() - damage);
				return;
			}
		}
		for(int i = 0; i <= range; i++){
			if((this.y + i == y || this.y - i == y) && this.x == x){
				int damage = map.getRand(damagemin, damagemax);
				map.setMessage("the " + name + " hits you for " + damage + " damage\n" );
				player.setHealth(player.getHealth() - damage);
				return;
			}
		}
		if(this.x > x && map.location(this.x-1, this.y) == "."){
			this.x--;
			return;
		}
		if(this.x < x && map.location(this.x+1, this.y) == "."){
			this.x++;
			return;
		}
		if(this.y < y && map.location(this.x, this.y+1) == "."){
			this.y++;
			return;
		}
		if(this.y > y && map.location(this.x, this.y-1) == "."){
			this.y--;
			return;
		}
		
	}
	public int determineDot(int level, RoguelikeMap map){
		this.dotpower = level - map.getRand(0, 5) + 4;
		if(this.dotpower <= 0)
			this.dotpower = 1;
		this.dottime = level - map.getRand(0, 3) + 3;
		if(this.dottime <= 0)
			this.dottime = 2;
		this.hasdot = true;
		return (dotpower * 4) * (dottime / 2); 
	}
	public String makeName(String name){
		String answer = name;
		if(this.range > 1){
			answer = (answer + " Archer");
		}
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
	public void alert(int x, int y, RoguelikeMap map){
		for(int i = 0; i < map.monsters.size(); i++){
			if(Math.pow(map.monsters.get(i).x - x, 2) + Math.pow(map.monsters.get(i).y - y, 2) <= 25 && Math.pow(map.monsters.get(i).x - x, 2) + Math.pow(map.monsters.get(i).y - y, 2) >= -25 && map.monsters.get(i).floor == floor)
				map. monsters.get(i).playerseen = true;
		}
	}
}