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
    protected int npcNumber;

    public Mob(int x, int y, int number){
        super(x, y, 100, 64, 64, 1);
        texture = new Texture(Gdx.files.internal("player2base.png"));
        flee = 0;
        sfx = Gdx.audio.newMusic(Gdx.files.internal("mobHit.wav"));
        sfx.setVolume((float) 0.3);
        npcNumber = number;
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
        float x = target.getX() - locationX;
        float y = target.getY() - locationY;
        float sum = abs(x) + abs(y);
        boolean collidedX=false;
        boolean collidedY=false;
        if (sum > 3) { //stops if within 3 units of clicked location to prevent never stopping
            float xMove = 5 * (x / sum);
            float yMove = 5 * (y / sum);
            Rectangle newPositionX = new Rectangle(locationX + xMove, locationY, width, height);
            Rectangle newPositionY = new Rectangle(locationX, locationY + yMove, width, height);
            /*for(Item item : TonposeScreen.Map.getItems()){
                if(!item.inInventory){
                    if(item.getBody().overlaps(body)){
                        addInventory(item);
                    }
                }
            }*/
            for (Entity entity : TonposeScreen.Map.getEntities()) { //checks if the player is going to collide with any entities
                if(entity instanceof Mob){
                    if(((Mob) entity).npcNumber != npcNumber){
                        if (newPositionX.overlaps(entity.getRectangle())) {
                            collidedX = true;
                        }
                        if(newPositionY.overlaps(entity.getRectangle())){
                            collidedY=true;
                        }
                    }
                }else{
                    if(entity.collision == true){
                        if (newPositionX.overlaps(entity.getRectangle())) {
                            collidedX = true;
                        }
                        if(newPositionY.overlaps(entity.getRectangle())){
                            collidedY=true;
                        }
                    }
                }

            }
            if (!collidedX) {
                if (locationX + xMove < 0) {  //this section assumes no collisions with objects after xmove and ymove are added
                    locationX = 0;

                } else if (locationX + xMove > TonposeScreen.Map.getWidth()) {
                    locationX = TonposeScreen.Map.getWidth();

                } else {
                    locationX += xMove;
                }
                body.setX(locationX);
            }
            if(!collidedY){
                if (locationY + yMove < 0) {
                    locationY = 0;

                } else if (locationY + yMove > TonposeScreen.Map.getHeight()) {
                    locationY = TonposeScreen.Map.getHeight();

                } else {
                    locationY += yMove;
                }
                body.setY(locationY);
            }
        }
        /*
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
                locationX -= 2 * (x/sum);
                locationY -= 2 * (y/sum);
                setBody(locationX,locationY);
            }
            flee--;
        }*/
        if(body.overlaps(target.getRectangle())){
            if(flee < 1){
                scare(20);
                target.changeHp(-1);
            }
        }
    }
}