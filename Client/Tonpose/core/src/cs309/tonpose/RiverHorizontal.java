package cs309.tonpose;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

/**
 * Created by Quade Spellman on 11/9/2016.
 */

public class RiverHorizontal extends Terrain {
    public RiverHorizontal(int locationX, int locationY,  boolean left) {
        super(locationX, locationY, 101, 0, 0, (float) 0.9);
        int flow = 10;
        if(left) {
            flow = -10;
        }
        modX = flow;
        texture=new Texture(Gdx.files.internal("water.png"));
    }
}
