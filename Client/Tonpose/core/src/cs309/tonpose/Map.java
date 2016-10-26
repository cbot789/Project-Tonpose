package cs309.tonpose;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;

import java.util.ArrayList;

/**
 * Created by Caleb on 10/24/2016.
 */

public class Map {
    private int height,width;
    private ArrayList<Entity> entities;



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
        return new Terrain(x,y,1,100,100,50,1000,0,true); //a tree                                                             //TODO make an id list for all entities, tree is currently 1

    }

    public ArrayList<Entity> getEntities() {
        return entities;
    }
}
