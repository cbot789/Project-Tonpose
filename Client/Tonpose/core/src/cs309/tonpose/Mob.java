package cs309.tonpose;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;

import static java.lang.Math.abs;

/**
 * Created by Quade Spellman on 9/27/2016.
 */
public class Mob extends Living {

    protected int flee;

    public Mob(int x, int y){
        super(x, y, 100, 64, 64, 1);
        texture = new Texture(Gdx.files.internal("player2base.png"));
        flee = 0;
    }

    private void setTarget(int x, int y){

    }
    private void setTarget(Entity prey){
        target = prey;
    }
    private void scare(int duration){
        flee += duration;
    }

    @Override
    public void move(Entity target) {
        if(flee < 1){
            float x = target.getX() - locationX;
            float y = target.getY() - locationY;
            float sum = abs(x) + abs(y);
            if(sum > 4){
                locationX += 5 * (x/sum);
                locationY += 5 * (y/sum);
                setBody(locationX,locationY);
            }
        }else{
            float x = target.getX() - locationX;
            float y = target.getY() - locationY;
            float sum = abs(x) + abs(y);
            if(sum > 4){
                locationX -= 5 * (x/sum);
                locationY -= 5 * (y/sum);
                setBody(locationX,locationY);
            }
            flee--;
        }
        if(body.overlaps(target.getRectangle())){
            scare(10);
            target.changeHp(-1);
        }
    }
}