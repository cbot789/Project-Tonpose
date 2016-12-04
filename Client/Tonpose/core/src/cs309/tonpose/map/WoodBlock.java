package cs309.tonpose.map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

import cs309.tonpose.TonposeScreen;

/**
 * Created by Quade Spellman on 12/3/2016.
 */

public class WoodBlock extends Entity {
    public WoodBlock(int uid, float locationX, float locationY){
        super(uid, locationX, locationY, 8, 400, 32, 32, 1000,  0, true, true); //tree image is 64x128
        texture = new Texture(Gdx.files.internal("woodBlock.png"));
        sfx = Gdx.audio.newMusic(Gdx.files.internal("logHit.wav"));
    }

    @Override
    public void setInventory() {
        super.setInventory();
        //int number= MathUtils.random(0,3);
        int number = 1;
        //addInventory(new TreeSeeds(number, locationX, locationY, false));
        //addInventory(new Logs(3,locationX,locationY,false));
    }
    @Override
    public void kill(){
        TonposeScreen.Map.removeFromMap(this);
    }
}
