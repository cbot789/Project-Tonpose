package cs309.tonpose.map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

import cs309.tonpose.*;

/**
 * Created by Caleb on 11/2/2016.
 */

public class Logs extends Item {
    public Logs(int uid, int number, float x, float y, boolean map, Tonpose t) {
        super(uid, number, x, y, 13, map, t);
        texture = new Texture(Gdx.files.internal("shlog.png"));
        hasAction = false;
        name = "Logs";
        craftingBase = true;
    }

    @Override
    public Item craft(Item mod) {
        switch (mod.getID()){
            case 13:
                if(count > 1){
                    if(mod.count > 1){
                        count--;
                        mod.count--;
                        return new Plank(tonpose.tonposeScreen.Map.UIDmax++, 1, locationX, locationY, tonpose);
                    }
                }
                break;
            default:
                break;
        }
        return null;
    }
}
