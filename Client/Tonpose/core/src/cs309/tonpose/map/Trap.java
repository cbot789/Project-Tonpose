package cs309.tonpose.map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

import cs309.tonpose.Tonpose;

/**
 * Created by Caleb on 12/6/2016.
 */

public class Trap extends Entity {
    public Trap(int uid, float locationX, float locationY, Tonpose t){
        super(uid, locationX, locationY, 4, 150, 16, 16, 1000,  0, true, true, t); //64 by 64
        texture = new Texture(Gdx.files.internal("dot.png"));
        sfx = Gdx.audio.newMusic(Gdx.files.internal("logHit.wav"));

    }



}
