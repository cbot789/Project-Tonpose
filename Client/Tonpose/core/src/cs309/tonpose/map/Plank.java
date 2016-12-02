package cs309.tonpose.map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

/**
 * Created by Quade Spellman on 11/3/2016.
 */

public class Plank extends Item {
    public Plank(int uid, int number, float x, float y) {
        super(uid, number, x, y, 14, false);
        texture = new Texture(Gdx.files.internal("shlog.png"));
        hasAction = false;
        name = "Planks";
        //craftingBase = true;
    }
}
