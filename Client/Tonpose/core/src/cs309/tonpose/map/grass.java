package cs309.tonpose.map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import cs309.tonpose.*;

/**
 * Created by Quade Spellman on 11/9/2016.
 */

public class grass extends Terrain {
    public grass(int locationX, int locationY, Tonpose t) {
        super(locationX, locationY, 100, 0, 0, 1, t);
        texture=new Texture(Gdx.files.internal("grass80x80hd.png"));
    }
}
