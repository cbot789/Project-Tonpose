package cs309.tonpose.map;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import cs309.tonpose.*;

/**
 * Created by Quade Spellman on 9/27/2016.
 */
public class Item {
    public String name;
    public int itemID;
    public int uid;
    public int count;
    public float locationX;
    public float locationY;
    public boolean hasAction;
    public boolean inInventory;
    public Rectangle body;
    public Texture texture;
    public boolean craftingBase;
    protected Tonpose tonpose;

    public Item(int uid, int number, float x, float y, int id, boolean map, Tonpose t){
        this.uid = uid;
        count = number;
        locationX = x;
        locationY = y;
        itemID = id;
        body = new Rectangle();
        body.set(locationX, locationY, 32, 32);
        inInventory = !map;
        craftingBase = false;
        this.tonpose = t;
    }

    //when item is used, decrease count by 1
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

    public Item craft(Item mod){
        return null;
    }
}