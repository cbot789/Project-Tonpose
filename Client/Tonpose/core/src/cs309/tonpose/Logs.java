package cs309.tonpose;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

/**
 * Created by Caleb on 11/2/2016.
 */

public class Logs extends Item {
    public Logs(int number, float x, float y, boolean map) {
        super(number, x, y, 13, map);
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
                        return new Plank(1, locationX, locationY);
                    }
                }
                break;
            default:
                break;
        }
        return null;
    }
}
