package cs309.tonpose.map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;


/**
 * Created by Caleb on 11/1/2016.
 */

public class Cabbage extends Entity { //id is 0


    public Cabbage(int locationX, int locationY){
        super(locationX, locationY, 0, 10, 10, 10, 5, 7, true, false); //x,y,id,hp,height,width,mass,invsize,killable, collision
        texture=new Texture(Gdx.files.internal("cabbage.png"));
        sfx = Gdx.audio.newMusic(Gdx.files.internal("cabbageHit.mp3"));
    }

    @Override
    public void setInventory() {
        super.setInventory();
        int number= MathUtils.random(0,3);
        int number2=MathUtils.random(0,3);
        addInventory(new CabbageLeaves(number, locationX, locationY, false));
        addInventory((new CabbageSeeds(number2,locationX,locationY,false)));
    }
}
