package cs309.tonpose.map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

import cs309.tonpose.Tonpose;

/**
 * Created by Caleb on 12/6/2016.
 */

public class Trap extends Entity {
    private boolean active;
    public int old;

    public Trap(int uid, float locationX, float locationY, Tonpose t){
        super(uid, locationX, locationY, 4, 150, 16, 16, 1000,  0, true, false, t); //64 by 64
        texture = new Texture(Gdx.files.internal("dot.png"));
        sfx = Gdx.audio.newMusic(Gdx.files.internal("logHit.wav"));
        active=false;
        old = 0;
    }

    public boolean getActive(){
        return active;
    }

    public int nextAnimationTrap(){
        if(old == 0){
            old = 1;
            return old;
        }
        old = 0;
        return old;
    }
}
