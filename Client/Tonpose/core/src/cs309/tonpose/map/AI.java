package cs309.tonpose.map;

import com.badlogic.gdx.math.Rectangle;

import static java.lang.Math.abs;
import static java.lang.Math.acos;
import static java.lang.Math.sqrt;
import static java.lang.Math.tan;

/**
 * Created by Quade Spellman on 10/23/2016.
 */

public class AI {           //TODO delete? unused
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

    //moves npc towards target
    public static void direct(Rectangle target, Rectangle npc){
        float x = target.getX() - npc.getX();
        float y = target.getY() - npc.getY();
        float sum = abs(x) + abs(y);
        if(sum > 1){
            npc.x += 5 * (x/sum);
            npc.y += 5 * (y/sum);
        }

    }

    //moves npc to point x,y
    public static void direct(float targetX, float targetY, Rectangle npc){
        float x = targetX - npc.getX();
        float y = targetY - npc.getY();
        float sum = abs(x) + abs(y);
        if(sum > 1){
            npc.x += 5 * (x/sum);
            npc.y += 5 * (y/sum);
        }

    }

    //npc moves around target   //FIXME
    public static void circle(Rectangle target, Rectangle npc, Boolean clockwise){
        float x = target.getX() - npc.getX();
        float y = target.getY() - npc.getY();
        float sum = abs(x) + abs(y);
        if(sum > 1){
            npc.x += 5 * (x/sum);
            npc.y += 5 * (y/sum);
        }
    }
}
