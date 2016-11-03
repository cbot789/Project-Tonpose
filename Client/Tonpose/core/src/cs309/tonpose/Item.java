package cs309.tonpose;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;

/**
 * Created by Quade Spellman on 9/27/2016.
 */
public class Item {
    protected String name;
    protected int itemID;
    protected int count;
    protected float locationX;
    protected float locationY;
    protected boolean hasAction;
    protected boolean inInventory;
    protected Rectangle body;
    protected Texture texture;

    public Item(int number, float x, float y, int id, boolean map){
        count = number;
        locationX = x;
        locationY = y;
        itemID = id;
        body = new Rectangle();
        body.set(locationX, locationY, 32, 32);
        inInventory = !map;
    }
    public void action(Rectangle player,Player user){
            if(!hasAction){
                return;
            }
            else{
                count--;
            }
    }
    public float getX(){
        return locationX;
    }
    public float getY(){
        return locationY;
    }
    public void setLocation(float x, float y){
        locationX = x;
        locationY = y;
        body.set(locationX, locationY, 64, 64);
    }
    public int getID(){
        return itemID;
    }
    public int getCount(){
        return count;
    }
    public Texture getTexture(){
        return texture;
    }
    public Rectangle getBody(){
        return body;
    }
}