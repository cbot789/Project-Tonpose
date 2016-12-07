package cs309.tonpose.map;

import com.badlogic.gdx.math.Rectangle;

import java.util.ArrayList;

import cs309.tonpose.Network;
import cs309.tonpose.Tonpose;
import cs309.tonpose.TonposeScreen;
import cs309.tonpose.User;

import static java.lang.Math.abs;

/**
 * Created by Quade Spellman on 12/6/2016.
 */

public class Projectile {
    protected int uid;
    protected float currentX;
    protected float currentY;
    protected float xSpeed;
    protected float ySpeed;
    protected int damage;
    protected Tonpose tonpose;
    protected int itemID;
    protected Rectangle rectangle;
    public int ownerID;

    public Projectile(int uid, float startX, float startY, float targetX, float targetY, int speed, int damage, Tonpose t){
        this.uid = uid;
        currentX = startX;
        currentY = startY;
        this.damage = damage;
        rectangle = new Rectangle(startX, startY, 32, 32);
        ownerID = 1;
        float x = targetX - startX;
        float y = targetY - startY;
        float sum = abs(x) + abs(y);

        xSpeed = (speed * (x / sum));
        ySpeed = (speed * (y / sum));

        tonpose = t;
    }


    public Projectile(int uid, float startX, float startY, int id, Tonpose t){
        this.uid = uid;
        currentX = startX;
        currentY = startY;
        rectangle = new Rectangle(startX, startY, 32, 32);
        ownerID = -1;
        switch (id){
            case 20:
                damage = 20;
            default:
                damage = id;
        }
        tonpose = t;
    }

    public void move(ArrayList<Entity> entityList, Player user){
        currentY += ySpeed;
        currentX += xSpeed;
        rectangle.setX(currentX);
        rectangle.setY(currentY);
        if(currentX < 0 || currentX > tonpose.tonposeScreen.Map.getWidth()){
            tonpose.tonposeScreen.Map.removeFromMap(this);
        }else if(currentY < 0 || currentY > tonpose.tonposeScreen.Map.getHeight()){
            tonpose.tonposeScreen.Map.removeFromMap(this);
        }else{
            for(Entity entity : entityList){
                if(entity.getRectangle().overlaps(rectangle)){
                    System.out.print(damage);
                    entity.changeHp(-damage);
                    tonpose.tonposeScreen.Map.removeFromMap(this);
                    return;
                }
            }
            if(ownerID != 1){
                if(user.getRectangle().overlaps(rectangle)){
                    user.changeHp(-damage/10);
                    tonpose.tonposeScreen.Map.removeFromMap(this);
                }
            }
        }
        Network.MoveElement move = new Network.MoveElement();
        move.tid = itemID;
        move.uid = uid;
        move.x = currentX;
        move.y = currentY;
        tonpose.client.sendTCP(move);
    }

    public void move(float newX, float newY, Player user){
        currentX = newX;
        currentY = newY;
        if(user.getRectangle().overlaps(rectangle)){
            user.changeHp(-damage/10);
            tonpose.tonposeScreen.Map.removeFromMap(this);
        }
    }

    public float getX(){
        return currentX;
    }

    public float getY(){
        return currentY;
    }
}
