package cs309.tonpose.map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;

import cs309.tonpose.Tonpose;

/**
 * Created by Quade Spellman on 12/6/2016.
 */

public class Wand extends Item {
    public Music shoot;

    public Wand(int uid, int number, float x, float y, boolean map, Tonpose t){
        super(uid, number, x, y, 17, map, t);
        texture = new Texture(Gdx.files.internal("fire.png"));
        hasAction = true;
        name = "Wand";
        craftingBase=false;

        shoot = Gdx.audio.newMusic(Gdx.files.internal("fire.wav"));
        shoot.setVolume((float) 0.3);

    }

    @Override
    public void action(Rectangle player, Player user){

    }

    public void fire(float targetX, float targetY, Player user, Tonpose t){
        shoot.setPosition(0);
        shoot.play();

        int speed = user.power;
        t.tonposeScreen.Map.addToMap(new Projectile(tonpose.tonposeScreen.Map.UIDmax++, user.getX(), user.getY(), targetX, targetY, 20, speed,t));
    }
}
