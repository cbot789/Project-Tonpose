package cs309.tonpose;

import java.util.List;

/**
 * Created by Quade Spellman on 9/27/2016.
 */
public class Entity {
    protected int locationX;
    protected int locationY;

    protected int currentHp;
    protected int maxHp;
    protected boolean killable;
    protected boolean collision;
    protected int size;
    //sprite

    protected int currentSpeed;
    protected int maxSpeed;
    protected int mass;
    protected int forceX;
    protected int forceY;
    protected int acceleration;

    protected int invSize;
    protected List<Item> inventory;

    public void setInventory(){

    }
    public void dropItem(Item toDrop){
        inventory.remove(toDrop);
        //create world object
    }
    public void addInventory(Item toAdd){
        if(inventory.size() < invSize){
            inventory.add(toAdd);
            //remove world object
        }
        else{
            //display "inv full"
        }
    }
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
    public void addForce(int x, int y){
        forceX += x;
        forceY += y;
    }
    public void kill(){

    }
}