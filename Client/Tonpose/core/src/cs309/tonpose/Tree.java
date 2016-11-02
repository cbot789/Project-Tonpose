package cs309.tonpose;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

/**
 * Created by Caleb on 11/1/2016.
 */

public class Tree extends Entity { //id is 1


    public Tree(int locationX, int locationY, int id){
        super(locationX, locationY, id, 100, 100, 50, 1000, 0, true, true);
        texture = new Texture(Gdx.files.internal("treeStill.png"));
    }

    @Override
    public void setInventory() {

    }
}
