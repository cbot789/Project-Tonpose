package cs309.tonpose.map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;

import cs309.tonpose.*;

/**
 * Created by Quade Spellman on 11/2/2016.
 */

public class TreeSeeds extends Item {
    public TreeSeeds(int uid, int number, float x, float y, boolean map, Tonpose t) {
        super(uid, number, x, y, 10, map, t);
        texture = new Texture(Gdx.files.internal("acorn.png"));
        hasAction = true;
        name = "Tree Seeds";
    }

    @Override
    public void action(Rectangle player, Player user) {
        if(count > 0){
            Tree tree = new Tree(tonpose.tonposeScreen.Map.UIDmax++, (int)player.getX()+ 40, (int) player.getY(), tonpose);
            tonpose.tonposeScreen.Map.addToMap(tree, true);
            super.action(player,user);
            user.updateScore(5); //add points for planting tree
        }
    }
}
