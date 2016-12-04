package cs309.tonpose.map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
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
    private Tonpose tonpose;

    public Mob(int uid, int targetID, float x, float y, int number, Tonpose t){
        super(uid, x, y, 100, 64, 64, 1, 5, 1, 1, 100);
        this.targetID = targetID;
        this.tonpose = t;
        texture = new Texture(Gdx.files.internal("player2base.png"));
        flee = 0;
        sfx = Gdx.audio.newMusic(Gdx.files.internal("mobHit.wav"));
        sfx.setVolume((float) 0.3);
        npcNumber = number;
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
            for(Item item : TonposeScreen.Map.getItems()){
                if(!item.inInventory){
                    if(item.getBody().overlaps(body)){
                        addInventory(item);
                    }
                }
            }
            for (Entity entity : TonposeScreen.Map.getEntities()) { //checks if the player is going to collide with any entities
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
                } else if (locationX + xMove > TonposeScreen.Map.getWidth()) {
                    xMove = TonposeScreen.Map.getWidth() - locationX;
                }
            }
            if (!collidedY) {
                if (locationY + yMove < 0) {
                    yMove = -locationY;

                } else if (locationY + yMove > TonposeScreen.Map.getHeight()) {
                    yMove = TonposeScreen.Map.getHeight() - locationY;
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
                move.x = locationX;
                move.y = locationY;
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

    public void nextAnimation(int i){// 0 = standing, 1 = moving, 2 = attacking, 3 = hit
        if(state == TonposeScreen.state.hit || state == TonposeScreen.state.action){
            i = old + 1;
        }
        switch (i){
            case 1:
                switch (old) {
                    case 10:
                        texture = new Texture(Gdx.files.internal("player2base.png"));
                        i = 11;
                        break;
                    case 11:
                        texture = new Texture(Gdx.files.internal("player2WalkingRight3.png"));
                        i = 12;
                        break;
                    case 12:
                        texture = new Texture(Gdx.files.internal("player2base.png"));
                        break;
                    default:
                        texture = new Texture(Gdx.files.internal("player2WalkingRight1.png"));
                        i = 10;
                        break;
                }
                break;
            case 2:
                texture = new Texture(Gdx.files.internal("player2base.png"));
                state = TonposeScreen.state.action;
                i = 20;
                break;
            case 3:
                texture = new Texture(Gdx.files.internal("player2base.png"));
                state = TonposeScreen.state.hit;
                i = 30;
                break;
            case 21:
                texture = new Texture(Gdx.files.internal("player2Attack1.png"));
                break;
            case 22:
                texture = new Texture(Gdx.files.internal("player2Attack2.png"));
                state = TonposeScreen.state.standing;
                break;
            case 31:
                texture = new Texture(Gdx.files.internal("player2Scared.png"));
                break;
            case 32:
                texture = new Texture(Gdx.files.internal("player2Scared.png"));
                break;
            case 33:
                texture = new Texture(Gdx.files.internal("player2Scared.png"));
                break;
            case 34:
                texture = new Texture(Gdx.files.internal("player2Scared.png"));
                state = TonposeScreen.state.standing;
                break;
            default:
                texture = new Texture(Gdx.files.internal("player2base.png"));
        }
        old = i;
    }

    @Override
    public void attack(float x, float y) {
        Entity hit = TonposeScreen.Map.mobCheckMap(x, y, attackRange, attackRange);
        if(hit != null){
            hit.changeHp(-power);
            nextAnimation(2);
        }
    }
    @Override
    public void kill(){
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