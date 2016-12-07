package cs309.tonpose.map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;

import cs309.tonpose.*;

/**
 * Created by Quade Spellman on 11/3/2016.
 */

public class Plank extends Item {
    public Plank(int uid, int number, float x, float y, Tonpose t) {
        super(uid, number, x, y, 14, false, t);
        texture = new Texture(Gdx.files.internal("shlog.png"));
        hasAction = true;
        name = "Planks";
        craftingBase = true;
    }

    @Override
    public void action(Rectangle player, Player user) {
        if(count > 0){
            WoodBlock WoodBlock = new WoodBlock(MathUtils.random(tonpose.tonposeScreen.Map.UIDmax), (int)player.getX() + 80, (int) player.getY(), tonpose);
            tonpose.tonposeScreen.Map.addToMap(WoodBlock);
            super.action(player,user);
            user.updateScore(5); //add points for placing woodblock
        }
    }



}
