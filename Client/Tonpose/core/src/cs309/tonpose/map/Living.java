package cs309.tonpose.map;


import com.badlogic.gdx.graphics.Texture;

import cs309.tonpose.*;

/**
 * Created by Quade Spellman on 9/27/2016.
 */
public class Living extends cs309.tonpose.map.Entity {
    protected int power;
    protected int moveSpeed;
    protected int lvl;
    protected cs309.tonpose.map.Entity target;
    protected int attackRange;
    protected Texture moving1;
    protected Texture moving2;
    protected Texture attacking1;
    protected Texture attacking2;
    protected Texture hit;
    protected Texture standing;

    //Entity(int locationX, int locationY, int id, int maxHp, int height, int width, int mass, int invSize, boolean killable, boolean collision){
    public Living(int uid, float x, float y, int hp, int height, int width, int invSize, int moveSpeed, int power, int lvl, int attackRange, Tonpose t) {
        super(uid, x, y, 2, hp, height, width, 1, invSize, true, false, t);
        this.power = power;
        this.moveSpeed = moveSpeed;
        this.lvl = lvl;
        this.attackRange = attackRange;
    }

    public void move(float targetX, float targetY, int modX, int modY, float scale){
        AI.direct(targetX, targetY, this.getRectangle());
    }
    public void move(cs309.tonpose.map.Entity target, int modX, int modY, float scale){
        AI.direct(target.getRectangle(), this.getRectangle());
    }

    public void attack(float x, float y) {
        Entity hit = tonpose.tonposeScreen.Map.checkMap(x, y, attackRange, attackRange);
        if(hit != null){
            hit.changeHp(-power);
        }
    }
}