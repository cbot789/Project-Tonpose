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
    Music death = Gdx.audio.newMusic(Gdx.files.internal("playerDeath.mp3"));

    public Player(float x, float y, String name){
        super(x, y, 8, 64, 45, 10, 5, 50, 1, 100);
        texture = new Texture(Gdx.files.internal("mainbase.png"));
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
    }
    public void equipItem(Item toEquip){
            equiped = toEquip;
    }
    public void gainXp(int gain){
        xp += gain;
        if(xp >= lvl*100){
            lvl++;
            xp -= lvl*100;
        }
    }
    public void login(){
        logged  = true;
    }
    public void logout(){
        logged = false;
    }

    public void action(){
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

    public void craft(){
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
            for(Item item : TonposeScreen.Map.getItems()){
                if(!item.inInventory){
                    if(item.getBody().overlaps(body)){
                        addInventory(item);
                    }
                }
            }
            for (Entity entity : TonposeScreen.Map.getEntities()) { //checks if the player is going to collide with any entities
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

                } else if (locationX + xMove > TonposeScreen.Map.getWidth()) {
                    xMove = TonposeScreen.Map.getWidth() -locationX;
                    locationX = TonposeScreen.Map.getWidth();

                } else {
                    locationX += xMove;
                }
                body.setX(locationX);
                TonposeScreen.camera.translate(xMove, 0);
                TonposeScreen.playerHealthX+=xMove;
                TonposeScreen.actionButtonDeadZone.setPosition(TonposeScreen.actionButtonDeadZone.x+xMove,TonposeScreen.actionButtonDeadZone.y);
            }
            if(!collidedY){
                if (locationY + yMove < 0) {
                    yMove = -locationY;
                    locationY = 0;

                } else if (locationY + yMove > TonposeScreen.Map.getHeight()) {
                    yMove = TonposeScreen.Map.getHeight() - locationY;
                    locationY = TonposeScreen.Map.getHeight();

                } else {
                    locationY += yMove;
                }
                body.setY(locationY);
                TonposeScreen.camera.translate(0, yMove);//keeps camera within the map's bounds
                TonposeScreen.playerHealthY+=yMove;
                TonposeScreen.actionButtonDeadZone.setPosition(TonposeScreen.actionButtonDeadZone.x,TonposeScreen.actionButtonDeadZone.y+yMove);
            }
        }
    }

    public ArrayList<Item> getInventory(){
        return inventory;
    }

    @Override
    public void changeHp(int mod) {
        if(userName != "God"){
            if(lastHit + 50000000 < TimeUtils.nanoTime()){
                super.changeHp(mod);
            }
        }
        switch (currentHp){
            case 0:
                TonposeScreen.healthImage=new Texture(Gdx.files.internal("pizza0.png"));
                break;
            case 1:
                TonposeScreen.healthImage=new Texture(Gdx.files.internal("pizza1.png"));
                break;
            case 2:
                TonposeScreen.healthImage=new Texture(Gdx.files.internal("pizza2.png"));
                break;
            case 3:
                TonposeScreen.healthImage=new Texture(Gdx.files.internal("pizza3.png"));
                break;
            case 4:
                TonposeScreen.healthImage=new Texture(Gdx.files.internal("pizza4.png"));
                break;
            case 5:
                TonposeScreen.healthImage=new Texture(Gdx.files.internal("pizza5.png"));
                break;
            case 6:
                TonposeScreen.healthImage=new Texture(Gdx.files.internal("pizza6.png"));
                break;
            case 7:
                TonposeScreen.healthImage=new Texture(Gdx.files.internal("pizza7.png"));
                break;
            case 8:
                TonposeScreen.healthImage=new Texture(Gdx.files.internal("pizza8.png"));
                break;
            default:
               if(killable){
                kill();
               }
        }

    }

    @Override
    public void kill() {
        death.play();
        locationX = -1000;
        locationY = -1000;
        body.setCenter(-1000, -1000);
    }
}