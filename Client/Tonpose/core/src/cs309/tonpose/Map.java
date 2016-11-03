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
    private int mobCount;


    public Map(int height, int width, int terrain, int difficulty){
        this.height=height;
        this.width=width;
        entities=new ArrayList<Entity>();
        items = new ArrayList<Item>();
        for(int i=0; i<terrain; i++){
            entities.add(generateTerrain());
        }
        mobCount = 0;
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
            mobCount++;
            if(mobCount > 10000)
                mobCount = 1;
            return new Mob(x, y, mobCount);
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

    public ArrayList<Item> getItems() {                                                            //TODO move map generation function here?
        return items;                                                                                //TODO fully add item support
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

    public Entity checkMap(float x, float y, float rangeX, float rangeY){ //TODO improve selection method
        Entity returnEntity=null;
        for (Entity entity:entities) {
            if(entity.getX() < rangeX + x){
                if(entity.getX() > x-rangeX) {
                    if (entity.getY() < rangeY + y) {
                        if(entity.getY() > y-rangeY) {
                            if(returnEntity!=null){
                                if(getDistance(x,y,entity.getX(),entity.getY())<getDistance(x,y,returnEntity.getX(),returnEntity.getY())){ //checks if object within range is closer than previous
                                    returnEntity=entity;
                                }
                            }else {
                                returnEntity = entity;
                            }
                        }
                    }
                }
            }
        }
        if(returnEntity!=null){
            return returnEntity;
        }
        return null;
    }

    public double getDistance(float x1, float y1, float x2, float y2){ // returns the distance between two points
      double distance=Math.sqrt(Math.abs(x1-x2)*Math.abs(x1-x2)+Math.abs(y1-y2)*Math.abs(y1-y2));
      return distance;

    }
}
