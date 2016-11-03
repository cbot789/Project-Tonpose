package cs309.tonpose;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;

/**
 * Created by Caleb on 11/2/2016.
 */

public class CabbageSeeds extends Item {
    public CabbageSeeds(int number, float x, float y, boolean map) {
        super(number, x, y, 11, map);
        texture = new Texture(Gdx.files.internal("adventureUnclicked.png"));
        hasAction = true;
        name = "Cabbage Seeds";
    }
    @Override
    public void action(Rectangle player, Player user) {
        if(count > 0){
            Cabbage cabbage = new Cabbage((int)player.getX()+ 40, (int) player.getY());
            TonposeScreen.Map.addToMap(cabbage);
            super.action(player, user);
        }
    }
}
