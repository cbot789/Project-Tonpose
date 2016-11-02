package cs309.tonpose;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;

/**
 * Created by Quade Spellman on 9/27/2016.
 */
public class Mob extends Living {

    public Mob(int x, int y){
        super(x, y, 100, 64, 64, 1);
        texture = new Texture(Gdx.files.internal("player2base.png"));
    }

    private void setTarget(int x, int y){

    }
    private void setTarget(Entity prey){
        target = prey;
    }
}