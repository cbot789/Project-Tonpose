package cs309.tonpose.map;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;

import java.util.ArrayList;

import com.badlogic.gdx.utils.TimeUtils;

import cs309.tonpose.*;

/**
 * Created by Quade Spellman on 9/27/2016.
 */
public class Entity {
    public float locationX;
    public float locationY;
    public int id;
    public int uid;
    public int currentHp;
    public int maxHp;
    public boolean killable;
    public boolean collision;
    public int width,height;

    public Rectangle body;
    public Texture texture;

    public int mass;
    public int forceX;
    public int forceY;
    public long lastHit = 0;

    public int invSize;
    public ArrayList<Item> inventory;
    public Music sfx;
    protected Tonpose tonpose;
    public Entity(){

    }

    public Entity(int uid, float locationX, float locationY, int id, int maxHp, int height, int width, int mass, int invSize, boolean killable, boolean collision, Tonpose t){
        this.uid = uid;
        this.locationX=locationX;
        this.locationY=locationY;
        this.id=id;
        this.currentHp=maxHp;
        this.maxHp=maxHp;
        this.height=height;
        this.width=width;
        this.mass=mass;
        this.invSize=invSize;
        this.killable=killable;
        this.tonpose = t;
        this.collision = collision;
        if(id==9){ //different rectangle for tree
            body=new Rectangle();
            body.set(locationX+20,locationY,width,height);
        }
        else{
            body = new Rectangle();
            body.set(locationX,locationY,width,height);
        }

        setInventory();
    }

    //initializes what items the entity carries and will drop on death
    public void setInventory(){
        inventory = new ArrayList<Item>();
    }

    //removes item from inventory and spawns it on the map
    public void dropItem(Item toDrop){
        inventory.remove(toDrop);
        toDrop.inInventory = false;
        toDrop.setLocation(locationX, locationY);
        tonpose.tonposeScreen.Map.addToMap(toDrop, true);
        //create world object
    }

    //adds an item to the inventory and deletes it from the map if there is room in inventory
    public void addInventory(Item toAdd){
        if(toAdd.count > 0){
            if(inventory.size() < invSize){
                for (Item item:inventory) {
                    if(item.name.equals(toAdd.name)){
                        item.count += toAdd.count;
                        if(toAdd.inInventory == false){
                            tonpose.tonposeScreen.Map.removeFromMap(toAdd);
                        }
                        return;
                    }
                }
                inventory.add(toAdd);
                if(toAdd.inInventory == false){
                    toAdd.inInventory = true;
                    tonpose.tonposeScreen.Map.removeFromMap(toAdd);
                }
            }
            else{
                //TODO display "inv full"
            }
        }

    }

    //heals or damages the entity by "mod" amount
    public void changeHp(int mod){
            currentHp += mod;
            if (currentHp > maxHp){
                currentHp = maxHp;
            }
            else if(currentHp < 0){
                currentHp = -1;
                kill();
            }
            lastHit = TimeUtils.nanoTime();
        if(mod < 0){
            sfx.setPosition(0);
            sfx.play();
        }
    }

    //pushes entity
    public void addForce(int x, int y){
        forceX += x;
        forceY += y;
    }
    public void setBody(float x,float y){
        body.set(x,y,width,height);
    }

    public void move(float x, float y){
        locationX = x;
        locationY = y;
        body.setX(locationX);
        body.setY(locationY);
    }

    //deletes entity from the map
    public void kill(){
        int i=32;
        for (Item item:inventory) {
            item.inInventory = false;
            item.setLocation(locationX+i, locationY+i);
            tonpose.tonposeScreen.Map.addToMap(item, true);
            i+=32;
        }
        tonpose.tonposeScreen.Map.removeFromMap(this);
    }

    public Texture getTexture(){
        return texture;
    }

    //returns the body of the entity
    public Rectangle getRectangle(){
        return body;
    }

    public float getX(){
        return locationX;
    }
    public float getY(){
        return locationY;
    }
    public float getWidth(){
        return width;
    }
    public float getHeight(){
        return height;
    }
}