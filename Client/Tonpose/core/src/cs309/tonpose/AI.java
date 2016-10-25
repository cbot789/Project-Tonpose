package cs309.tonpose;

import com.badlogic.gdx.math.Rectangle;

import static java.lang.Math.abs;
import static java.lang.Math.acos;
import static java.lang.Math.sqrt;
import static java.lang.Math.tan;

/**
 * Created by Quade Spellman on 10/23/2016.
 */

public class AI {
    public static void basic(Rectangle target, Rectangle npc){
        if(target.getX() > npc.getX()){
            npc.x += 5;
        }
        else if(target.getX() < npc.getX() - 10){
            npc.x -= 5;
        }
        if(target.getY() > npc.getY()){
            npc.y += 5;
        }
        else if(target.getY() < npc.getY() - 10){
            npc.y -= 5;
        }
    }
    public static void direct(Rectangle target, Rectangle npc){
        float x = target.getX() - npc.getX();
        float y = target.getY() - npc.getY();
        float sum = abs(x) + abs(y);
        if(sum != 0){
            npc.x += 5 * (x/sum);
            npc.y += 5 * (y/sum);
        }

    }
    public static void circle(Rectangle target, Rectangle npc, Boolean clockwise){
        float y = abs(target.getX() - npc.getX());
        float x = abs(target.getY() - npc.getY());
        float radius = x*x + y * y;
        if(radius > 40){
            direct(target, npc);
        }else{
            int moveX = (int) (0.5 * (sqrt(radius * radius + 5 * 10) - radius));
            int moveY = (int) (moveX * tan(acos(moveX / 5)));
            if(clockwise){
                npc.x += moveX;
                npc.y += moveY;
            }else{
                npc.x -= moveX;
                npc.y -= moveY;
            }
        }
    }
}
