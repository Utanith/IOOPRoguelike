package name.blah;

import java.util.Random;

public class Enemy {
	int x;
	int y;
	int health;
	int damagemin;
	int damagemax;
	int floor;
	boolean playerseen;
	String id;
	public Enemy(int x, int y, int health, int damagemin, int damagemax, String id, int level) {
		super();
		this.x = x;
		this.y = y;
		this.health = health;
		this.damagemin = damagemin;
		this.damagemax = damagemax;
		this.id = id;
		this.floor = level;
		this.playerseen = false;
	}
	
	public void move(int x, int y, RoguelikeMap map, Player player){
		if(this.floor != map.level)
			return;
		Random rand = new Random();
		if(playerseen == false){
			if(x > this.x){
				if(y > this.y){
					int i = this.x;
					int j = this.y;
					while(i < 24 && j < 24){
						if(map.location(i, j) == " ")
							break;
						if(i == x && j == y)
							playerseen = true;
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
						if(i == x && j == y)
							playerseen = true;
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
						if(i == x)
							playerseen = true;
					}
				}
			}
			if(x < this.x){
				if(y > this.y){
					int i = this.x;
					int j = this.y;
					while(i < 24 && j < 24){
						if(map.location(i, j) == " ")
							break;
						if(i == x && j == y)
							playerseen = true;
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
						if(i == x && j == y)
							playerseen = true;
						i--;
						j--;
					}
				}
				if(y == this.y){
					int i = this.x;
					while(i >= 0){
						if(map.location(i, this.y) == " ")
							break;
						if(i == x)
							playerseen = true;
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
						if(j == y)
							playerseen = true;
						j++;
					}
				}
				if(y < this.y){
					int j = this.y;
					while(j >= 0){
						if(map.location(this.x, j) == " ")
							break;
						if(j == y)
							playerseen = true;
						j--;
					}
				}
				if(y == this.y)
					playerseen = true;
			}
		}
		if(playerseen == false)
			return;
		if((this.x + 1 == x || this.x - 1 == x) && this.y == y){
			player.setHealth(player.getHealth() - (rand.nextInt(this.damagemax - this.damagemin) + damagemax));
			return;
		}
		if((this.y + 1 == y || this.y - 1 == y) && this.x == x){
			player.setHealth(player.getHealth() - (rand.nextInt(this.damagemax - this.damagemin) + damagemax));
			return;
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
}
