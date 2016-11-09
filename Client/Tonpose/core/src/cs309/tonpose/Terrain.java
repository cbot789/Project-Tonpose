package cs309.tonpose;

import com.badlogic.gdx.math.Rectangle;

/**
 * Created by Caleb on 10/26/2016.
 */

public class Terrain extends Entity {
//TODO change to not extend entity?

    protected int modX;
    protected int modY;
    protected float scale;

    public Terrain(int locationX, int locationY, int id, int modX, int modY, float scale){
        super(locationX, locationY, id, -1, 20, 20, -1, 0, false, false);
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

    public double getScale(){
        return scale;
    }
}
