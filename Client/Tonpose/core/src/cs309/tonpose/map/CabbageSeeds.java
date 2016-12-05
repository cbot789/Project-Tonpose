package cs309.tonpose.map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;

import cs309.tonpose.*;

/**
 * Created by Caleb on 11/2/2016.
 */

public class CabbageSeeds extends Item {
    public CabbageSeeds(int uid, int number, float x, float y, boolean map, Tonpose t) {
        super(uid, number, x, y, 11, map, t);
        texture = new Texture(Gdx.files.internal("cabbage seeds.png"));
        hasAction = true;
        name = "Cabbage Seeds";
    }
    @Override
    public void action(Rectangle player, Player user) {
        if(count > 0){
            Cabbage cabbage = new Cabbage(tonpose.tonposeScreen.Map.UIDmax++, (int)player.getX()+ 40, (int) player.getY(), tonpose);
            tonpose.tonposeScreen.Map.addToMap(cabbage);
            super.action(player, user);
            user.updateScore(5); //adds points for planting cabbage
        }
    }
}
