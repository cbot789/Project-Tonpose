package cs309.tonpose;

public class User {

	private String name;
	private int locationX;
	private int locationY;
	
	public User(String name, int X, int Y){
        this.name = name;
        locationX = X;
        locationY = Y;
    }
	public String getName(){
        return name;
    }

    public float getLocationX(){
        return locationX;
    }

    public float getLocationY() {
        return locationY;
    }

    public void setLocationX(int x){
        locationX = x;
    }

    public void setLocationY(int y){
        locationY = y;
    }
}
