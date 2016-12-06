package cs309.tonpose.map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
//import com.sun.xml.internal.bind.annotation.OverrideAnnotationOf; //caused compile error, package doesnt exist

import cs309.tonpose.*;

import static java.lang.Math.abs;

/**
 * Created by Quade Spellman on 9/27/2016.
 */
public class Mob extends Living {

    public int old;
    public TonposeScreen.state state;
    protected int flee;
    protected int npcNumber;
    public int targetID;

    public Mob(int uid, int targetID, float x, float y, Tonpose t){
        super(uid, x, y, 100, 64, 64, 1, 5, 1, 1, 100, t);
        this.targetID = targetID;
        this.tonpose = t;

        standing = new Texture(Gdx.files.internal("player2base.png"));
        moving2 = new Texture(Gdx.files.internal("player2WalkingRight3.png"));
        moving1 = new Texture(Gdx.files.internal("player2WalkingRight1.png"));
        attacking1 = new Texture(Gdx.files.internal("player2Attack1.png"));
        attacking2 = new Texture(Gdx.files.internal("player2Attack2.png"));
        hit = new Texture(Gdx.files.internal("player2Scared.png"));
        
        texture = standing;
        old = 0;
        flee = 0;
        sfx = Gdx.audio.newMusic(Gdx.files.internal("mobHit.wav"));
        sfx.setVolume((float) 0.3);
        npcNumber = uid;
        nextAnimation(0);
    }

    public void setTarget(int x, int y){

    }

    // Sets the ID of the player the mob will follow
    public void setTargetID(int ID){
        targetID = ID;
    }

    public void setTarget(Entity prey){
        target = prey;
    }
    public void scare(int duration){
        flee += duration;
    }

    @Override
    public void move(Entity target, int modX, int modY, float scale) {//FIXME npc shake when colliding
        float x = target.getX() - locationX;
        float y = target.getY() - locationY;
        float sum = abs(x) + abs(y);
        boolean collidedX = false;
        boolean collidedY = false;
        if (sum > 4) { //stops if within 4 units of target location to prevent "the shakes"
            nextAnimation(1);
            float xMove = (moveSpeed * (x / sum)) * scale + modX;
            float yMove = (moveSpeed * (y / sum)) * scale + modY;
            if(flee > 0){
                xMove = -xMove;
                yMove = -yMove;
                flee--;
            }
            Rectangle newPositionX = new Rectangle(locationX + xMove, locationY, width, height);
            Rectangle newPositionY = new Rectangle(locationX, locationY + yMove, width, height);
            for(Item item : tonpose.tonposeScreen.Map.getItems()){
                if(!item.inInventory){
                    if(item.getBody().overlaps(body)){
                        addInventory(item);
                    }
                }
            }
            for (Entity entity : tonpose.tonposeScreen.Map.getEntities()) { //checks if the player is going to collide with any entities
                if (entity instanceof Mob) {
                    if (((Mob) entity).npcNumber != npcNumber) {
                        if (newPositionX.overlaps(entity.getRectangle())) {
                            xMove = - xMove;
                        }
                        if (newPositionY.overlaps(entity.getRectangle())) {
                            yMove = - yMove;
                        }
                     }
                } else {
                    if (entity.collision == true) {
                        if (newPositionX.overlaps(entity.getRectangle())) {
                            collidedX = true;
                        }
                        if (newPositionY.overlaps(entity.getRectangle())) {
                            collidedY = true;
                        }
                    }
                }

            }
            if (!collidedX) {
                if (locationX + xMove < 0) {  //this section assumes no collisions with objects after xmove and ymove are added
                    xMove = -locationX;
                } else if (locationX + xMove > tonpose.tonposeScreen.Map.getWidth()) {
                    xMove = tonpose.tonposeScreen.Map.getWidth() - locationX;
                }
            }
            if (!collidedY) {
                if (locationY + yMove < 0) {
                    yMove = -locationY;

                } else if (locationY + yMove > tonpose.tonposeScreen.Map.getHeight()) {
                    yMove = tonpose.tonposeScreen.Map.getHeight() - locationY;
                }
            }
            if(collidedX && collidedY){
                attack(locationX, locationY);
            }else{
                locationX += xMove;
                locationY += yMove;
                body.setX(locationX);
                body.setY(locationY);
                Network.MoveElement move = new Network.MoveElement();
                move.tid = 2;
                move.uid = uid;
                move.x = locationX;// + xMove;
                move.y = locationY;// + yMove;
                tonpose.client.sendTCP(move);
            }

            if (body.overlaps(target.getRectangle())) {
                if (flee < 1) {
                    scare(20);
                    target.changeHp(-1);
                    nextAnimation(2);
                }
            }
        }else{
            nextAnimation(0);
        }
    }

    @Override
    public void move(float x, float y){
        super.move(x, y);
        nextAnimation(0);
    }

    @Override
    public void nextAnimation(int i){// 0 = standing, 1 = moving, 2 = attacking, 3 = hit
        if(state == TonposeScreen.state.hit || state == TonposeScreen.state.action){
            i = old + 1;
        }
        switch (i){
            case 1:
                switch (old) {
                    case 10:
                        texture = standing;
                        i = 11;
                        break;
                    case 11:
                        texture = moving1;
                        i = 12;
                        break;
                    case 12:
                        texture = standing;
                        break;
                    default:
                        texture = moving2;
                        i = 10;
                        break;
                }
                break;
            case 2:
                texture = standing;
                state = TonposeScreen.state.action;
                i = 20;
                break;
            case 3:
                texture = standing;
                state = TonposeScreen.state.hit;
                i = 30;
                break;
            case 21:
                texture = attacking1;
                break;
            case 22:
                texture = attacking2;
                state = TonposeScreen.state.standing;
                break;
            case 31:
                texture = hit;
                break;
            case 32:
                texture = hit;;
                break;
            case 33:
                texture = hit;;
                break;
            case 34:
                texture = hit;;
                state = TonposeScreen.state.standing;
                break;
            default:
                texture = standing;
        }
        old = i;
    }

    @Override
    public void attack(float x, float y) {
        Entity hit = tonpose.tonposeScreen.Map.mobCheckMap(x, y, attackRange, attackRange);
        if(hit != null){
            hit.changeHp(-power);
            nextAnimation(2);
        }
    }
    @Override
    public void kill(){
        tonpose.tonposeScreen.Map.addToMap(new Bones(MathUtils.random(tonpose.tonposeScreen.Map.UIDmax), 1, locationX+32, locationY+32, true, tonpose)); //drop bones
        super.kill();
        TonposeScreen.player.updateScore(10);
    }

    @Override
    public void changeHp(int mod) {
        super.changeHp(mod);
        scare(10);
        nextAnimation(3);
    }
}