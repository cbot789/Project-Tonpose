package cs309.tonpose;

/**
 * Created by Caleb on 10/26/2016.
 */

public class Terrain extends Entity {





    public Terrain(int locationX, int locationY, int id, int maxHp, int size, int mass, int invSize, boolean killable){
        this.locationX=locationX;
        this.locationY=locationY;
        this.id=id;
        this.currentHp=maxHp;
        this.maxHp=maxHp;
        this.size=size;
        this.mass=mass;
        this.invSize=invSize;
        this.killable=killable;
    }

}
