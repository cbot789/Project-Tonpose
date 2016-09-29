package quadespellman.tonposeclasses;

/**
 * Created by Quade Spellman on 9/27/2016.
 */
public class Item {
    protected String name;
    protected int count;
    protected int locationX;
    protected int locationY;
    protected boolean equippable;
    protected boolean pickUpable;
    protected boolean interactable;
    protected boolean inInventory;

    public Item(){

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
