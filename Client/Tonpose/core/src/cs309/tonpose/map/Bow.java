package cs309.tonpose.map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;

import cs309.tonpose.Tonpose;

/**
 * Created by Quade Spellman on 12/6/2016.
 */

public class Bow extends Item {
    public Bow(int uid, int number, float x, float y, boolean map, Tonpose t){
        super(uid, number, x, y, 16, map, t);
        texture = new Texture(Gdx.files.internal("sword.png"));
        hasAction = true;
        name = "Sword";
        craftingBase=false;

    }

    @Override
    public void action(Rectangle player, Player user){
        
    }

    public void fire(float targetX, float targetY, Player user, Tonpose t){
        int speed = user.power/10;
        t.tonposeScreen.Map.addToMap(new Projectile(tonpose.tonposeScreen.Map.UIDmax++, user.getX(), user.getY(), targetX, targetY, user.power, speed,t));
    }
}
