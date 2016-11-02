package cs309.tonpose;

import com.badlogic.gdx.math.Rectangle;

/**
 * Created by Caleb on 10/26/2016.
 */

public class Terrain extends Entity {
//TODO no purpose? Delete?

    public Terrain(int locationX, int locationY, int id, int maxHp, int height, int width, int mass, int invSize, boolean killable){

        super(locationX, locationY, id, maxHp, height, width, mass, invSize, killable, false);
    }

    public Terrain(){}

}
