package cs309.tonpose.map;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;

import java.rmi.server.UID;
import java.util.ArrayList;

import cs309.tonpose.*;

/**
 * Created by Caleb on 10/24/2016.
 */

public class Map {
    private int height,width;
    private ArrayList<Entity> entities;
    private ArrayList<Item> items;
    private ArrayList<Entity> entitiesDelete;
    private ArrayList<Item> itemsDelete;
    private ArrayList<Entity> entitiesAdd;
    private ArrayList<Item> itemsAdd;
    private Terrain terrains[][];
    private int mobCount;
    public int UIDmax;
    private Tonpose tonpose;

    public Map(Tonpose t, int height, int width, int maxEntities, int difficulty){
        this.tonpose = t;
        this.height=height;
        this.width=width;
        entities=new ArrayList<Entity>();
        items = new ArrayList<Item>();
        entitiesAdd=new ArrayList<Entity>();
        itemsAdd = new ArrayList<Item>();
        entitiesDelete=new ArrayList<Entity>();
        itemsDelete = new ArrayList<Item>();
        terrains = new Terrain[height/20+1][width/20+1];
        mobCount = 0;

        for(int i=0; i<maxEntities; i++){
            entities.add(generateEntities());
        }
        generateTerrain();

    }

    public Map(Tonpose t, int height, int width, int[] terrain, int[][] entities){
        this.tonpose = t;
        UIDmax = 0;
        this.height = height;
        this.width = width;
        this.entities=new ArrayList<Entity>();
        items = new ArrayList<Item>();
        entitiesAdd=new ArrayList<Entity>();
        itemsAdd = new ArrayList<Item>();
        entitiesDelete=new ArrayList<Entity>();
        itemsDelete = new ArrayList<Item>();
        this.terrains = new Terrain[height/20+1][width/20+1];
        mobCount = 0;

        for(int i=0; i < entities.length; i++){
            this.entities.add(generateEntities(entities[i][0], entities[i][1], entities[i][2], entities[i][3]));
            UIDmax++;
        }
        generateTerrain(height, width, terrain);

    }

    public void spawn(){
        entities.add(generateEntities());
    }

    private Entity generateEntities(){
        int x=MathUtils.random(width);
        int y= MathUtils.random(height);
        int id=MathUtils.random(0,3);
        Rectangle playerRectangle= new Rectangle(400,240,45,64);
        if(id==0){
            Cabbage cabbage = new Cabbage(UIDmax++, x,y);
            if(cabbage.body.overlaps(playerRectangle)){
                return generateEntities(); //try again for a valid position
            }
            /*for(Entity enitity:entities){ //check for overlap
                if(cabbage.body.overlaps(enitity.getRectangle())){
                    return generateTerrain();
                }
            }*/
            return new Cabbage(UIDmax++, x, y);
        }
        else if(id == 1){
            mobCount++;
            if(mobCount > 10000)
                mobCount = 1;
            return new Mob(UIDmax++, x, y, mobCount);
        }
        else {
            Tree tree=new Tree(UIDmax++, x,y);
            if(tree.body.overlaps(playerRectangle)){
                return generateEntities();
            }
            /*for(Entity enitity:entities){ //check for overlap
                if(tree.body.overlaps(enitity.getRectangle())){
                    return generateTerrain();
                }
            }*/
            return tree;                                                             //TODO make an id list for all entities, tree is currently 1
        }
    }

    private Entity generateEntities(int uid, int id, float x, float y){
        switch(id){
            case 0:
                return new Cabbage(uid, x,y);
            case 2:
                mobCount++;
                return new Mob(uid, x,y, mobCount);
            case 9:
                return new Tree(uid, x, y);
            default:
                return new Tree(uid, x,y);
        }
    }

    private Item generateItems(int uid, int id, float x, float y){
        switch(id){
            case 10:
                return new TreeSeeds(uid, 1, x, y, true);
            case 11:
                return new CabbageSeeds(uid, 1, x, y, true);
            case 12:
                return new CabbageLeaves(uid, 1, x, y, true);
            case 13:
                return new Logs(uid, 1, x, y, true);
            default:
                return new Plank(uid, 1, x, y);
        }
    }

    private void generateTerrain(){
        for(int i=0; i < width/20 +1; i ++){
            for(int j = 0; j < height/20 +1; j ++){
                int id=MathUtils.random(0,10);
                switch(id){
                    default:
                        terrains[i][j] = new grass(i * 20, j* 20);
                }
            }
        }
        int x=MathUtils.random(width/20 - 20);
        int y= MathUtils.random(height/20 - 4);
        createRiverHorizontal(x, y, 4, 20, true);

    }

    private void generateTerrain(int height, int width, int[] terrain){
        int i = 0;
        for(int x = 0; x < width/20 + 1; x++){
            for(int y = 0; y < height/20 + 1; y++){
                switch(terrain[i]){
                    case 1:
                        this.terrains[x][y] = new RiverHorizontal(x * 20, y * 20, true);
                        break;
                    case 2:
                        this.terrains[x][y] = new RiverVertical(x*20, y*20, true);
                        break;
                    case 3:
                        this.terrains[x][y] = new RiverHorizontal(x * 20, y * 20, false);
                        break;
                    case 4:
                        this.terrains[x][y] = new RiverVertical(x*20, y*20, false);
                        break;
                    default:
                        this.terrains[x][y] = new grass(x * 20 ,y * 20);
                }
                i++;
            }
        }

    }

    private void createRiverHorizontal(int x, int y, int width, int length, boolean left){
        for(int i = x; i < x + length; i++){
            for(int j = y; j < y + width; j++){
                terrains[i][j] = new RiverHorizontal(i * 20, j * 20, left);
            }
        }
    }

    public Terrain[][] getTerrains(){
        return terrains;
    }

    public ArrayList<Entity> getEntities() {
        return entities;
    }

    public ArrayList<Item> getItems() {
        return items;
    }
    //adds an item to appear on the map
    public void addToMap(Item item){
        itemsAdd.add(item);
        Network.AddElement add = new Network.AddElement();
        add.id = item.itemID;
        add.uid = item.uid;
        add.x = item.locationX;
        add.y = item.locationY;
        tonpose.client.sendTCP(add);
    }

    public void addToMap(Entity entity){
        entitiesAdd.add(entity);
        Network.AddElement add = new Network.AddElement();
        add.id = entity.id;
        add.uid = entity.uid;
        add.x = entity.locationX;
        add.y = entity.locationY;
        tonpose.client.sendTCP(add);
    }
    public void addToMap(Network.AddElement add){
        if(add.id < 10){
            entitiesAdd.add(generateEntities(add.uid, add.id, add.x, add.y));
        }
        else if(add.id <= 14){
            itemsAdd.add(generateItems(add.uid, add.id, add.x, add.y));
        }
    }

    public int getWidth(){
        return width;
    }

    public int getHeight(){
        return height;
    }


    //removes an item so it no longer shows on the map
    public void removeFromMap(Item item){
        itemsDelete.add(item);
        Network.RemoveElement remove = new Network.RemoveElement();
        remove.tid = item.itemID;
        remove.uid = item.uid;
        tonpose.client.sendTCP(remove);

    }

    public void removeFromMap(Entity entity){
        entitiesDelete.add(entity);
        Network.RemoveElement remove = new Network.RemoveElement();
        remove.tid = entity.id;
        remove.uid = entity.uid;
        tonpose.client.sendTCP(remove);
    }
    public void removeFromMap(Network.RemoveElement remove){
        if(remove.tid < 10){
            for(Entity e: entities){
                if(e.uid == remove.uid){
                    entitiesDelete.add(e);
                }
            }
        }
        else if(remove.tid <= 14){
            for(Item i: items){
                if(i.uid == remove.uid){
                    itemsDelete.add(i);
                }
            }
        }
    }


    public Entity checkMap(float x, float y, float rangeX, float rangeY){ //TODO improve selection method
        Entity returnEntity=null;
        for (Entity entity:entities) {
            if(entity.getX() < rangeX + x){
                if(entity.getX() > x-rangeX) {
                    if (entity.getY() < rangeY + y) {
                        if(entity.getY() > y-rangeY) {
                            if(returnEntity!=null){
                                if(getDistance(x,y,entity.getX(),entity.getY())<getDistance(x,y,returnEntity.getX(),returnEntity.getY())){ //checks if object within range is closer than previous
                                    returnEntity=entity;
                                }
                            }else {
                                returnEntity = entity;
                            }
                        }
                    }
                }
            }
        }
        if(returnEntity!=null){
            return returnEntity;
        }
        return null;
    }

    public Entity mobCheckMap(float x, float y, float rangeX, float rangeY){ //TODO improve selection method
        Entity returnEntity=null;
        for (Entity entity:entities) {
            if(entity instanceof Mob){

            }else if(entity.getX() < rangeX + x){
                if(entity.getX() > x-rangeX) {
                    if (entity.getY() < rangeY + y) {
                        if(entity.getY() > y-rangeY) {
                            if(returnEntity!=null){
                                if(getDistance(x,y,entity.getX(),entity.getY())<getDistance(x,y,returnEntity.getX(),returnEntity.getY())){ //checks if object within range is closer than previous
                                    returnEntity=entity;
                                }
                            }else {
                                returnEntity = entity;
                            }
                        }
                    }
                }
            }
        }
        if(TonposeScreen.player.getX() < rangeX + x){
            if(TonposeScreen.player.getX() > x-rangeX) {
                if (TonposeScreen.player.getY() < rangeY + y) {
                    if(TonposeScreen.player.getY() > y-rangeY) {
                        if(returnEntity!=null){
                            if(getDistance(x,y,TonposeScreen.player.getX(),TonposeScreen.player.getY())<getDistance(x,y,returnEntity.getX(),returnEntity.getY())){ //checks if object within range is closer than previous
                                returnEntity=TonposeScreen.player;
                            }
                        }else {
                            returnEntity = TonposeScreen.player;
                        }
                    }
                }
            }
        }
        if(returnEntity!=null){
            return returnEntity;
        }
        return null;
    }

    public double getDistance(float x1, float y1, float x2, float y2){ // returns the distance between two points
      double distance=Math.sqrt(Math.abs(x1-x2)*Math.abs(x1-x2)+Math.abs(y1-y2)*Math.abs(y1-y2));
      return distance;

    }

    public void updateMap(){
        for (Entity entity: entitiesAdd) {
            entities.add(entity);
        }
        entitiesAdd.clear();
        for (Entity entity: entitiesDelete) {
            entities.remove(entity);
        }
        entitiesDelete.clear();
        for (Item item: itemsAdd) {
            items.add(item);
        }
        itemsAdd.clear();
        for (Item item: itemsDelete) {
            items.remove(item);
        }
        itemsDelete.clear();
    }
}
