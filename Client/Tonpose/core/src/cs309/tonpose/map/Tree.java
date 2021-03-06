package cs309.tonpose.map;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;

import cs309.tonpose.*;

/**
 * Created by Caleb on 11/1/2016.
 */

public class Tree extends Entity { //id is 9

    public Tree(int uid, float locationX, float locationY, Tonpose t){
        super(uid, locationX, locationY, 9, 99, 50, 20, 1000,  7, true, true, t); //tree image is 64x128
        texture = new Texture(Gdx.files.internal("treeStill.png"));
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
        int i=32;
        for (Item item:inventory) {
            item.inInventory = false;
            item.setLocation(locationX+i, locationY+i);
            tonpose.tonposeScreen.Map.addToMap(item);
            i+=32;
        }
        tonpose.tonposeScreen.Map.addToMap(new Logs(MathUtils.random(tonpose.tonposeScreen.Map.UIDmax), 2, locationX+32, locationY+32, true, tonpose));
        tonpose.tonposeScreen.Map.addToMap(new TreeSeeds(MathUtils.random(tonpose.tonposeScreen.Map.UIDmax), 2, locationX+64, locationY+64, true, tonpose));
        tonpose.tonposeScreen.Map.removeFromMap(this);
    }
}
