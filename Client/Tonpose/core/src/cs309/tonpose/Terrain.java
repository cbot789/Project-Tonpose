package cs309.tonpose;

import com.badlogic.gdx.math.Rectangle;

/**
 * Created by Caleb on 10/26/2016.
 */

public class Terrain extends Entity {





    public Terrain(int locationX, int locationY, int id, int maxHp, int height, int width, int mass, int invSize, boolean killable){
        this.locationX=locationX;
        this.locationY=locationY;
        this.id=id;
        this.currentHp=maxHp;
        this.maxHp=maxHp;
        this.width=width;
        this.height=height;
        this.mass=mass;
        this.invSize=invSize;
        this.killable=killable;
        this.body=new Rectangle(locationX,locationY,width,height);
    }

    public Terrain(){}

}
