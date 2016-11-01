package cs309.tonpose;

/**
 * Created by Quade Spellman on 9/27/2016.
 */
public class Living extends Entity {
    protected int power;
    protected boolean destory;
    protected int lvl;

    public void move(float x, float y){
        AI.direct(x, y, this.getRectangle());
    }
    public void move(Entity target){
        AI.direct(target.getRectangle(), this.getRectangle());
    }
    public void attack(int x, int y) {

    }
}