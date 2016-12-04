package cs309.tonpose.map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;

import cs309.tonpose.TonposeScreen;

/**
 * Created by Quade Spellman on 11/3/2016.
 */

public class Plank extends Item {
    public Plank(int uid, int number, float x, float y) {
        super(uid, number, x, y, 14, false);
        texture = new Texture(Gdx.files.internal("shlog.png"));
        hasAction = true;
        name = "Planks";
        //craftingBase = true;
    }

    @Override
    public void action(Rectangle player, Player user) {
        if(count > 0){
            WoodBlock WoodBlock = new WoodBlock(TonposeScreen.Map.UIDmax++, (int)player.getX() + 80, (int) player.getY());
            TonposeScreen.Map.addToMap(WoodBlock);
            super.action(player,user);
            user.updateScore(1); //add points for placing woodblock
        }
    }
}
