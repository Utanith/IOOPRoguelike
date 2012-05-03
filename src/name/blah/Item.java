package name.blah;

public class Item {
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	int x;
	int y;
	String id;
	public Item(int x, int y, String id) {
		super();
		this.x = x;
		this.y = y;
		this.id = id;
	}
}
