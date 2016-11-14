package cs309.tonpose.map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;

/**
 * Created by Caleb on 11/2/2016.
 */

public class CabbageLeaves extends  Item {
    public CabbageLeaves(int number, float x, float y, boolean map) {
        super(number, x, y, 12, map);
        texture = new Texture(Gdx.files.internal("CabbageLeaves.png"));
        hasAction = true;
        name = "Cabbage Leaves";
    }
    @Override
    public void action(Rectangle player, Player user) {
        if(count > 0){
            user.changeHp(1); // heals player by one
            super.action(player, user);
        }
    }
}
