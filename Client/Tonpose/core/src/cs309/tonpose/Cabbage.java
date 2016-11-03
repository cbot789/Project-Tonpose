package cs309.tonpose;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;


/**
 * Created by Caleb on 11/1/2016.
 */

public class Cabbage extends Terrain { //id is 0


    public Cabbage(int locationX, int locationY){
        super(locationX, locationY, 0, 10, 10, 10, 5, 0, true);
        texture=new Texture(Gdx.files.internal("cabbage.png"));
        sfx = Gdx.audio.newMusic(Gdx.files.internal("cabbageHit.mp3"));
    }
}
