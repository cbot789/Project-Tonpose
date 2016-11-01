package cs309.tonpose;

/**
 * Created by Quade Spellman on 9/27/2016.
 */
public class Living extends Entity {
    protected int power;
    protected boolean destory;
    protected int lvl;

    //Entity(int locationX, int locationY, int id, int maxHp, int height, int width, int mass, int invSize, boolean killable, boolean collision){
    public Living(int x, int y, int hp, int height, int width, int invSize) {
        super(x, y, -1, hp, height, width, 1, invSize, true, true);
    }

    public void move(float x, float y){
        AI.direct(x, y, this.getRectangle());
    }
    public void move(Entity target){
        AI.direct(target.getRectangle(), this.getRectangle());
    }
    public void attack(int x, int y) {

    }
}