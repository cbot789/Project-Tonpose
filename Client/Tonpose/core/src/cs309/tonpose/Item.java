package cs309.tonpose;

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
    protected boolean pickUpable;
    protected boolean interactable;
    protected boolean inInventory;

    public Item(int number, float x, float y, int id){
        count = number;
        locationX = x;
        locationY = y;
        itemID = id;
    }
    public void action(){
        if(!inInventory){
            if(pickUpable){

            }
            if(interactable){

            }
        }
        else{
            if(equippable){

            }
        }
    }
}