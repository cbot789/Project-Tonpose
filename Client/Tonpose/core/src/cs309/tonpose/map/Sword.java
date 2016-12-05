package cs309.tonpose.map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;


import cs309.tonpose.Tonpose;

/**
 * Created by Caleb on 12/4/2016.
 */

public class Sword extends Item {

    public Sword(int uid, int number, float x, float y, boolean map, Tonpose t){
        super(uid, number, x, y, 16, map, t);
        texture = new Texture(Gdx.files.internal("sword.png"));
        hasAction = true;
        name = "Sword";
        craftingBase=false;

    }

    @Override
    public void action(Rectangle player, Player user){
        user.power+=75; //temporarily raises attack power for this attack
        user.attack(user.locationX,user.locationY);
        user.power-=75;
    }

}
