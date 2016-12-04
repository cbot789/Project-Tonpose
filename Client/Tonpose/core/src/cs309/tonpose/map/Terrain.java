package cs309.tonpose.map;

/**
 * Created by Caleb on 10/26/2016.
 */

public class Terrain extends Entity {
//TODO change to not extend entity?

    protected int modX;
    protected int modY;
    protected float scale;

    public Terrain(int locationX, int locationY, int id, int modX, int modY, float scale){
        super(-1, locationX, locationY, id, -1, 80, 80, -1, 0, false, false);
        this.modX = modX;
        this.modY = modY;
        this.scale = scale;
    }

    public int getModX(){
        return modX;
    }

    public int getModY(){
        return modY;
    }

    public float getScale(){
        return scale;
    }
}
