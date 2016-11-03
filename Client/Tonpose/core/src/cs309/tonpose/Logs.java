package cs309.tonpose;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

/**
 * Created by Caleb on 11/2/2016.
 */

public class Logs extends Item {
    public Logs(int number, float x, float y, boolean map) {
        super(number, x, y, 13, map);
        texture = new Texture(Gdx.files.internal("adventureUnclicked.png"));
        hasAction = false;
        name = "Logs";
    }


}
