package name.blah;

import java.util.Random;

public class Enemy {
	int x;
	int y;
	int health;
	int damagemin;
	int damagemax;
	String id;
	public Enemy(int x, int y, int health, int damagemin, int damagemax, String id) {
		super();
		this.x = x;
		this.y = y;
		this.health = health;
		this.damagemin = damagemin;
		this.damagemax = damagemax;
		this.id = id;
	}
	
	public void move(int x, int y, RoguelikeMap map, Player player){
		Random rand = new Random();
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
