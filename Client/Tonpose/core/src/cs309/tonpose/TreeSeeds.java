package cs309.tonpose;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;

/**
 * Created by Quade Spellman on 11/2/2016.
 */

public class TreeSeeds extends Item{
    public TreeSeeds(int number, float x, float y, boolean map) {
        super(number, x, y, 10, map);
        texture = new Texture(Gdx.files.internal("adventureUnclicked.png"));
        hasAction = true;
        name = "Tree Seeds";
    }

    @Override
    public void action(Rectangle player) {
        if(count > 0){
            Tree tree = new Tree((int)player.getX(), (int) player.getY());
            TonposeScreen.Map.addToMap(tree);
            super.action(player);
        }
    }
}
