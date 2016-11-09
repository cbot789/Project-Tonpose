package cs309.tonpose;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

/**
 * Created by Quade Spellman on 11/8/2016.
 */

public class RiverVertical extends Terrain {
    public RiverVertical(int locationX, int locationY, boolean down) {
        super(locationX, locationY, 102, 0, 0, (float) 0.9);
        int flow = 10;
        if(down) {
            flow = -10;
        }
        modY = flow;
        texture=new Texture(Gdx.files.internal("water.png"));
    }
}
