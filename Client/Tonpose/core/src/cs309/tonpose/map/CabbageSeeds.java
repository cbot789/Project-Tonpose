package cs309.tonpose.map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;

import cs309.tonpose.*;

/**
 * Created by Caleb on 11/2/2016.
 */

public class CabbageSeeds extends Item {
    public CabbageSeeds(int uid, int number, float x, float y, boolean map) {
        super(uid, number, x, y, 11, map);
        texture = new Texture(Gdx.files.internal("cabbage seeds.png"));
        hasAction = true;
        name = "Cabbage Seeds";
    }
    @Override
    public void action(Rectangle player, Player user) {
        if(count > 0){
            Cabbage cabbage = new Cabbage(TonposeScreen.Map.UIDmax++, (int)player.getX()+ 40, (int) player.getY());
            TonposeScreen.Map.addToMap(cabbage);
            super.action(player, user);
            user.updateScore(5); //adds points for planting cabbage
        }
    }
}
