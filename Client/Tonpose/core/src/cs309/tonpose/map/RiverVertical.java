package cs309.tonpose.map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import cs309.tonpose.*;

/**
 * Created by Quade Spellman on 11/8/2016.
 */

public class RiverVertical extends Terrain {
    public RiverVertical(int locationX, int locationY, boolean down, Tonpose t) {
        super(locationX, locationY, 102, 0, 0, (float) 0.9, t);
        int flow = 3;
        if(down) {
            flow = -3;
        }
        modY = flow;
        texture=new Texture(Gdx.files.internal("water80x80hd.png"));
    }
}
