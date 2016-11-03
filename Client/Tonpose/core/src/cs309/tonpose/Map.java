package cs309.tonpose;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;

import org.w3c.dom.css.Rect;

import java.util.ArrayList;

/**
 * Created by Caleb on 10/24/2016.
 */

public class Map {
    private int height,width;
    private ArrayList<Entity> entities;
    private ArrayList<Item> items;



    public Map(int height, int width, int terrain, int difficulty){
        this.height=height;
        this.width=width;
        entities=new ArrayList<Entity>();
        for(int i=0; i<terrain; i++){
            entities.add(generateTerrain());
        }

    }

    private Entity generateTerrain(){                                                                 //TODO add chance to generate other objects besides trees
        int x=MathUtils.random(width);
        int y= MathUtils.random(height);
        int id=MathUtils.random(0,3);
        Rectangle playerRectangle= new Rectangle(400,240,45,64);
        if(id==0){
            Cabbage cabbage = new Cabbage(x,y);
            if(cabbage.body.overlaps(playerRectangle)){
                return generateTerrain(); //try again for a valid position
            }
            /*for(Entity enitity:entities){ //check for overlap
                if(cabbage.body.overlaps(enitity.getRectangle())){
                    return generateTerrain();
                }
            }*/
            return new Cabbage(x, y);
        }
        else if(id == 1){
            return new Mob(x, y);
        }
        else {
            Tree tree=new Tree(x,y);
            if(tree.body.overlaps(playerRectangle)){
                return generateTerrain();
            }
            /*for(Entity enitity:entities){ //check for overlap
                if(tree.body.overlaps(enitity.getRectangle())){
                    return generateTerrain();
                }
            }*/
            return tree;                                                             //TODO make an id list for all entities, tree is currently 1
        }
    }

    public ArrayList<Entity> getEntities() {                                                            //TODO move map generation function here?
        return entities;                                                                                //TODO fully add item support
    }

    //adds an item to appear on the map
    public void addToMap(Item item){
        items.add(item);
    }

    public void addToMap(Entity entity){
        entities.add(entity);
    }

    public int getWidth(){
        return width;
    }

    public int getHeight(){
        return height;
    }


    //removes an item so it no longer shows on the map
    public void removeFromMap(Item item){
        items.remove(item);
    }

    public void removeFromMap(Entity entity){
        entities.remove(entity);
    }
}
