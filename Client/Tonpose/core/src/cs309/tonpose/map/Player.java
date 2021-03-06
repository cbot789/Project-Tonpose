package cs309.tonpose.map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.TimeUtils;

import java.util.ArrayList;

import cs309.tonpose.*;

import static java.lang.Math.abs;

/**
 * Created by Quade Spellman on 9/27/2016.
 */
public class Player extends Living {
    public int score;
    public int xp;
    public int lvl;
    public boolean logged;
    public int userId;
    public String  userName;
    public Item equiped;
    public Item base;
    public Item mod;
    public int old;
    public TonposeScreen.state state;
    Music death = Gdx.audio.newMusic(Gdx.files.internal("playerDeath.mp3"));

    public Player(float x, float y, String name, Tonpose t){
        super(-1, x, y, 8, 64, 45, 10, 5, 25, 1, 80, t); //move speed 5 power 25
        
        standing = new Texture(Gdx.files.internal("mainbase.png"));
        moving2 = new Texture(Gdx.files.internal("mainWalkingRight3.png"));
        moving1 = new Texture(Gdx.files.internal("mainWalkingRight1.png"));
        attacking1 = new Texture(Gdx.files.internal("mainAttack1.png"));
        attacking2 = new Texture(Gdx.files.internal("mainAttack2.png"));
        hit = new Texture(Gdx.files.internal("mainScared.png"));
        
        texture = standing;
        userName = name;
        score = 0;
        xp = 0;
        lvl = 1;
        logged = true;
        userId = 0;
        killable=true;
        sfx = Gdx.audio.newMusic(Gdx.files.internal("playerHit.wav"));
    }

    public void updateScore(int points){
        score += points;
        gainXp(points); //points are directly related to experience
    }
    public void equipItem(Item toEquip){
        equiped = toEquip;
    }
    public void gainXp(int gain){ //levels up player if enough points have been gained
        xp += gain;
        if(xp >= lvl*50){
            lvl++;
            xp -= lvl*50;
            if(lvl%2==0){ //adds move speed or attack damage upon leveling up
                this.moveSpeed+=2;
            }
            else this.power+=5;
        }
    }
    public void login(){
        logged  = true;
    }
    public void logout(){
        logged = false;
    }

    public void action(){//uses equipped item, if nothing is equipped, attack
        nextAnimation(2);
        if(equiped != null){
            if(equiped.hasAction){
                equiped.action(body, this);
                if(equiped.getCount() < 1){
                    equiped = null;
                }
            }
        } else{
            attack(locationX, locationY);
        }
    }

    public void craft(){//delete 1 mod and base item to create a new item
        if(base != null){
            if(mod != null){
                Item result = base.craft(mod);
                if (result != null)
                    addInventory(result);
            }
        }
    }

    public int getScore(){
        return score;
    }

    @Override
    public void move(float targetX, float targetY, int modX, int modY, float scale) {
        float moveScale = moveSpeed * scale;
        float x = targetX - locationX;
        float y = targetY - locationY;
        float sum = abs(x) + abs(y);
        boolean collidedX=false;
        boolean collidedY=false;
        if (sum > 3 || sum == 0) { //stops if within 3 units of clicked location to prevent never stopping
            float xMove = (moveScale * (x / sum)) + modX;
            float yMove = (moveScale * (y / sum)) + modY;
            if(sum == 0){
                xMove = modX;
                yMove = modY;
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
                if(entity.collision == true){
                    if (newPositionX.overlaps(entity.getRectangle())) {
                        collidedX = true;
                    }
                    if(newPositionY.overlaps(entity.getRectangle())){
                        collidedY=true;
                    }
                }
            }
            if (!collidedX) {
                if (locationX + xMove < 0) {  //this section assumes no collisions with objects after xmove and ymove are added
                    xMove = -locationX;
                    locationX = 0;

                } else if (locationX + xMove > tonpose.tonposeScreen.Map.getWidth()) {
                    xMove = tonpose.tonposeScreen.Map.getWidth() -locationX;
                    locationX = tonpose.tonposeScreen.Map.getWidth();

                } else {
                    locationX += xMove;
                }
                body.setX(locationX);
                tonpose.tonposeScreen.camera.translate(xMove, 0);
                tonpose.tonposeScreen.playerHealthX+=xMove;
                tonpose.tonposeScreen.actionButtonDeadZone.setPosition(tonpose.tonposeScreen.actionButtonDeadZone.x+xMove,tonpose.tonposeScreen.actionButtonDeadZone.y); //moves dead zones with the camera
                tonpose.tonposeScreen.inventoryDeadZone.setPosition(tonpose.tonposeScreen.inventoryDeadZone.x+xMove,tonpose.tonposeScreen.inventoryDeadZone.y);
                tonpose.tonposeScreen.playersOnlineDeadZone.setPosition(tonpose.tonposeScreen.playersOnlineDeadZone.x+xMove,tonpose.tonposeScreen.playersOnlineDeadZone.y);
            }
            if(!collidedY){
                if (locationY + yMove < 0) {
                    yMove = -locationY;
                    locationY = 0;

                } else if (locationY + yMove > tonpose.tonposeScreen.Map.getHeight()) {
                    yMove = tonpose.tonposeScreen.Map.getHeight() - locationY;
                    locationY = tonpose.tonposeScreen.Map.getHeight();

                } else {
                    locationY += yMove;
                }
                body.setY(locationY);
                tonpose.tonposeScreen.camera.translate(0, yMove);//keeps camera within the map's bounds
                tonpose.tonposeScreen.playerHealthY+=yMove;
                tonpose.tonposeScreen.actionButtonDeadZone.setPosition(tonpose.tonposeScreen.actionButtonDeadZone.x,tonpose.tonposeScreen.actionButtonDeadZone.y+yMove); //moves dead zones with the camera
                tonpose.tonposeScreen.inventoryDeadZone.setPosition(tonpose.tonposeScreen.inventoryDeadZone.x,tonpose.tonposeScreen.inventoryDeadZone.y+yMove);
                tonpose.tonposeScreen.playersOnlineDeadZone.setPosition(tonpose.tonposeScreen.playersOnlineDeadZone.x,tonpose.tonposeScreen.playersOnlineDeadZone.y+yMove);
            }
        }
    }

    public ArrayList<Item> getInventory(){
        return inventory;
    }

    @Override
    public void changeHp(int mod) { //changes image for health based on current players health
        if(userName != "God"){
            if(lastHit + 50000000 < TimeUtils.nanoTime()){
                super.changeHp(mod);
                nextAnimation(3);
            }
        }
        switch (currentHp){
            case 0:
                tonpose.tonposeScreen.healthImage=new Texture(Gdx.files.internal("pizza0.png"));
                break;
            case 1:
                tonpose.tonposeScreen.healthImage=new Texture(Gdx.files.internal("pizza1.png"));
                break;
            case 2:
                tonpose.tonposeScreen.healthImage=new Texture(Gdx.files.internal("pizza2.png"));
                break;
            case 3:
                tonpose.tonposeScreen.healthImage=new Texture(Gdx.files.internal("pizza3.png"));
                break;
            case 4:
                tonpose.tonposeScreen.healthImage=new Texture(Gdx.files.internal("pizza4.png"));
                break;
            case 5:
                tonpose.tonposeScreen.healthImage=new Texture(Gdx.files.internal("pizza5.png"));
                break;
            case 6:
                tonpose.tonposeScreen.healthImage=new Texture(Gdx.files.internal("pizza6.png"));
                break;
            case 7:
                tonpose.tonposeScreen.healthImage=new Texture(Gdx.files.internal("pizza7.png"));
                break;
            case 8:
                tonpose.tonposeScreen.healthImage=new Texture(Gdx.files.internal("pizza8.png"));
                break;
            default:
               if(killable){
                kill();
               }
        }
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
    public void kill() {
        death.play();
        //locationX = -1000;
        //locationY = -1000;
        //body.setCenter(-1000, -1000);
        for (Entity entity : tonpose.tonposeScreen.Map.getEntities()) {
            if(entity instanceof Mob){
                if(((Mob)entity).targetID == tonpose.ID){
                    tonpose.tonposeScreen.Map.removeFromMap(entity);
                }
            }
        }
        for( Projectile p : tonpose.tonposeScreen.Map.getProjectiles()){
            if(p.ownerID != -1){
                tonpose.tonposeScreen.Map.removeFromMap(p);
            }
        }
    }
}