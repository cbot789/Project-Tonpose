package cs309.tonpose;


import com.badlogic.gdx.math.Rectangle;

/**
 * Created by Quade Spellman on 9/27/2016.
 */
public class Living extends Entity {
    protected int power;
    protected boolean destory;
    protected int lvl;
    protected Entity target;

    //Entity(int locationX, int locationY, int id, int maxHp, int height, int width, int mass, int invSize, boolean killable, boolean collision){
    public Living(float x, float y, int hp, int height, int width, int invSize) {
        super(x, y, 2, hp, height, width, 1, invSize, true, false);
    }

    public void move(float targetX, float targetY){
        AI.direct(targetX, targetY, this.getRectangle());
    }
    public void move(Entity target){
        AI.direct(target.getRectangle(), this.getRectangle());
    }
    public void attack(float x, float y) {

    }
}