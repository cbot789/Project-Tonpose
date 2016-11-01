package cs309.tonpose;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import java.util.List;
import com.badlogic.gdx.Gdx;

/**
 * Created by Quade Spellman on 9/27/2016.
 */
public class Entity {
    protected int locationX;
    protected int locationY;
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

    protected int invSize;
    protected List<Item> inventory;

    public Entity(){

    }

    public Entity(int locationX, int locationY, int id, int maxHp, int height, int width, int mass, int invSize, boolean killable, boolean collision){
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
        body = new Rectangle();
        body.set(locationX,locationY,width,height);
        setInventory();
    }

    //initilizes what items the entity carries and will drop on death
    public void setInventory(){

    }

    //removes item from inventory and spawns it on the map
    public void dropItem(Item toDrop){
        inventory.remove(toDrop);
        toDrop.toggleInventory();
        toDrop.setLocation(locationX, locationY);
        //create world object
    }

    //adds an item to the inventory and deletes it from the map if there is room in inventory
    public void addInventory(Item toAdd){
        if(inventory.size() < invSize){
            inventory.add(toAdd);
            toAdd.toggleInventory();
            //remove world object
        }
        else{
            //display "inv full"
        }
    }

    //heals or damages the entity by "mod" amount
    public void changeHp(int mod){
        currentHp += mod;
        if (currentHp > maxHp){
            currentHp = maxHp;
        }
        else if(currentHp < 0){
            currentHp = 0;
            kill();
        }
    }

    //pushes entity
    public void addForce(int x, int y){
        forceX += x;
        forceY += y;
    }

    //deletes entity from the map
    public void kill(){
        for (Item item:inventory) {
            dropItem(item);
        }
        //TODO remove from map
    }

    public Texture getTexture(){
        return texture;
    }

    //returns the body of the entity
    public Rectangle getRectangle(){
        return body;
    }
}