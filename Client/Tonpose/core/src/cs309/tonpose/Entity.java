package cs309.tonpose;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;

import java.util.ArrayList;
import java.util.List;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.TimeUtils;

/**
 * Created by Quade Spellman on 9/27/2016.
 */
public class Entity {
    protected float locationX;
    protected float locationY;
    protected int id;                                                                               //TODO determine if id is needed for all entities, or just for items
    protected int currentHp;
    protected int maxHp;
    protected boolean killable;
    protected boolean collision;
    protected int width,height;

    protected Rectangle body;
    protected Texture texture;
    //sprite

    protected int currentSpeed;
    protected int maxSpeed;
    protected int mass;
    protected int forceX;
    protected int forceY;
    protected int acceleration;
    protected long lastHit = 0;

    protected int invSize;
    protected ArrayList<Item> inventory;
    protected Music sfx;

    public Entity(){

    }

    public Entity(float locationX, float locationY, int id, int maxHp, int height, int width, int mass, int invSize, boolean killable, boolean collision){
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

    //initilizes what items the entity carries and will drop on death
    public void setInventory(){
        inventory = new ArrayList<Item>();
    }

    //removes item from inventory and spawns it on the map
    public void dropItem(Item toDrop){
        inventory.remove(toDrop);
        toDrop.inInventory = false;
        toDrop.setLocation(locationX, locationY);
        TonposeScreen.Map.addToMap(toDrop);
        //create world object
    }

    //adds an item to the inventory and deletes it from the map if there is room in inventory
    public void addInventory(Item toAdd){//FIXME doesnt add counts correctly or entities dont spawn/drop right ammount of items
        if(toAdd.count > 0){
            if(inventory.size() < invSize){
                for (Item item:inventory) {
                    if(item.name.equals(toAdd.name)){
                        item.count += toAdd.count;
                        if(toAdd.inInventory == false){
                            TonposeScreen.Map.removeFromMap(toAdd);
                        }
                        return;
                    }
                }
                inventory.add(toAdd);
                if(toAdd.inInventory == false){
                    toAdd.inInventory = true;
                    TonposeScreen.Map.removeFromMap(toAdd);
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
            sfx.setPosition(0);
            sfx.play();
    }

    //pushes entity
    public void addForce(int x, int y){
        forceX += x;
        forceY += y;
    }
    public void setBody(float x,float y){
        body.set(x,y,width,height);
    }

    //deletes entity from the map
    public void kill(){
        for (Item item:inventory) {
            dropItem(item);
        }
        TonposeScreen.Map.removeFromMap(this);
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