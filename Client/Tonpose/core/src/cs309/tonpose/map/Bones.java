package cs309.tonpose.map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

import cs309.tonpose.Tonpose;

/**
 * Created by Caleb on 12/4/2016.
 */

public class Bones extends Item {


    public Bones(int uid, int number, float x, float y, boolean map, Tonpose t){
        super(uid, number, x, y, 15, map, t);
        texture = new Texture(Gdx.files.internal("bone.png"));
        hasAction = false;
        name = "Bones";
        craftingBase=true;

    }


}
