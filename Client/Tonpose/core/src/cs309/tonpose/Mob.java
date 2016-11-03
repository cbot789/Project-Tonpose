package cs309.tonpose;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;

import static java.lang.Math.abs;

/**
 * Created by Quade Spellman on 9/27/2016.
 */
public class Mob extends Living {

    protected int flee;
    private static Music sfx = Gdx.audio.newMusic(Gdx.files.internal("mobHit.wav"));

    public Mob(int x, int y){
        super(x, y, 100, 64, 64, 1);
        texture = new Texture(Gdx.files.internal("player2base.png"));
        flee = 0;
        sfx.setVolume((float) 0.3);
    }

    public void setTarget(int x, int y){

    }
    public void setTarget(Entity prey){
        target = prey;
    }
    public void scare(int duration){
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
            if(flee < 1){
                scare(10);
                target.changeHp(-1);
            }
        }
    }

    @Override
    public void changeHp(int mod) {
        super.changeHp(mod);
        sfx.setPosition(0);
        sfx.play();
    }
}