package cs309.tonpose.map;

import com.badlogic.gdx.math.Rectangle;

import cs309.tonpose.Network;
import cs309.tonpose.Tonpose;

import static java.lang.Math.abs;

/**
 * Created by Quade Spellman on 12/6/2016.
 */

public class Wizard extends Mob {
    public Wizard(int uid, int targetID, float x, float y, Tonpose t) {
        super(uid, targetID, x, y, t);
        id = 3;
    }

    @Override
    public void move(Entity target, int modX, int modY, float scale) {//FIXME npc shake when colliding
        float x = target.getX() - locationX;
        float y = target.getY() - locationY;
        float sum = abs(x) + abs(y);
        boolean collidedX = false;
        boolean collidedY = false;
        if (sum > 100) { //stops if within 4 units of target location to prevent "the shakes"
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
        }else{
            scare(1);
        }
    }
}
