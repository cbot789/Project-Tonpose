package cs309.tonpose;

/**
 * Created by Caleb on 11/1/2016.
 */

public class Tree extends Terrain { //id is 1


    public Tree(int locationX, int locationY, int id, int maxHp, int height, int width, int mass, int invSize, boolean killable){
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
    }

}
