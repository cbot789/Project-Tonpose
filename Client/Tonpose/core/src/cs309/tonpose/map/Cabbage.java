package cs309.tonpose.map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;

import cs309.tonpose.*;


/**
 * Created by Caleb on 11/1/2016.
 */

public class Cabbage extends Entity { //id is 0


    public Cabbage(int uid, float locationX, float locationY, Tonpose t){
        super(uid, locationX, locationY, 0, 10, 10, 10, 5, 7, true, false, t); //uid,x,y,id,hp,height,width,mass,invsize,killable, collision
        texture=new Texture(Gdx.files.internal("cabbage.png"));
        sfx = Gdx.audio.newMusic(Gdx.files.internal("cabbageHit.mp3"));
    }

    @Override
    public void setInventory() {
        super.setInventory();
        //int number= MathUtils.random(0,3);
        //int number2=MathUtils.random(0,3);
        //addInventory(new CabbageLeaves(1, locationX, locationY, false));
        //addInventory((new CabbageSeeds(1,locationX,locationY,false)));
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
        tonpose.tonposeScreen.Map.addToMap(new CabbageLeaves(MathUtils.random(tonpose.tonposeScreen.Map.UIDmax), 2, locationX+32, locationY+32, true, tonpose));
        tonpose.tonposeScreen.Map.addToMap(new CabbageSeeds(MathUtils.random(tonpose.tonposeScreen.Map.UIDmax), 2, locationX+64, locationY+64, true, tonpose));
        tonpose.tonposeScreen.Map.removeFromMap(this);
    }
}
