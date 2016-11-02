package cs309.tonpose;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

/**
 * Created by Quade Spellman on 11/2/2016.
 */

public class TreeSeeds extends Item{
    public TreeSeeds(int number, float x, float y, int id) {
        super(number, x, y, id);
        texture = new Texture(Gdx.files.internal("treeStill.png"));
    }

    @Override
    public void action() {
        super.action();
    }
}
