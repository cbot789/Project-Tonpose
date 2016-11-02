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
    protected boolean equippable;
    protected boolean hasAction;
    protected boolean inInventory;
    protected Rectangle body;
    protected Texture texture;

    public Item(int number, float x, float y, int id){
        count = number;
        locationX = x;
        locationY = y;
        itemID = id;
    }
    public void action(){
            if(!hasAction){
                return;
            }
    }
    public void toggleInventory(){
        inInventory = !inInventory;
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
    }
    public boolean getEquippable(){
        return equippable;
    }
    public int getID(){
        return itemID;
    }
    public int getCount(){
        return count;
    }
}