package cs309.tonpose;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;

/**
 * Created by Caleb on 11/1/2016.
 */

public class Tree extends Entity { //id is 1
    private static Music sfx = Gdx.audio.newMusic(Gdx.files.internal("logHit.wav"));

    public Tree(int locationX, int locationY){
        super(locationX, locationY, 9, 100, 50, 20, 1000,  7, true, true); //tree image is 64x128
        texture = new Texture(Gdx.files.internal("treeStill.png"));
    }

    @Override
    public void setInventory() {
        super.setInventory();
        int number= MathUtils.random(0,3);
        addInventory(new TreeSeeds(number, locationX, locationY, false));
        addInventory(new Logs(3,locationX,locationY,false));
    }

    @Override
    public void changeHp(int mod) {
        super.changeHp(mod);

        sfx.setPosition(0);
        sfx.play();
    }
}
