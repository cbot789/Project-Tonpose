package cs309.tonpose.map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;

import cs309.tonpose.Tonpose;

/**
 * Created by Caleb on 12/6/2016.
 */

public class TrapParts extends Item {

    public TrapParts(int uid, int number, float x, float y, boolean map, Tonpose t){
        super(uid, number, x, y, 18, map, t);
        texture = new Texture(Gdx.files.internal("dot.png"));
        hasAction = true;
        name = "Trap";
        craftingBase=false;

    }

    @Override
    public void action(Rectangle player, Player user) {
        if(count > 0){
            Trap Trap = new Trap(MathUtils.random(tonpose.tonposeScreen.Map.UIDmax), (int)player.getX() + 100, (int) player.getY(), tonpose); //places trap away from player
            tonpose.tonposeScreen.Map.addToMap(Trap);
            super.action(player,user);
            user.updateScore(25); //add points for placing trap
        }
    }
}
