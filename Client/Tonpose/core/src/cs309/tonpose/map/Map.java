package cs309.tonpose.map;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;

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

    //creates map in single player
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
        terrains = new Terrain[height/40+1][width/40+1];
        mobCount = 0;

        for(int i=0; i<maxEntities; i++){
            entities.add(generateEntities(MathUtils.random(3)));
        }
        generateTerrain();

    }

    //create map based on server data
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
        this.terrains = new Terrain[height/80+1][width/80+1];
        mobCount = 0;

        for(int i=0; i < entities.length; i++){
            this.entities.add(generateEntities(entities[i][0], entities[i][1], entities[i][2], entities[i][3]));
            UIDmax = entities[i][0] + 1;
        }
        generateTerrain(height, width, terrain);

    }

    //randomly spawns an npc onto the map
    public void spawnNPC(){
        addToMap(generateEntities(1));
    }

    //used for single player
    private Entity generateEntities(int id){
        int x=MathUtils.random(width);
        int y= MathUtils.random(height);
        Rectangle playerRectangle= new Rectangle(400,240,45,64);
        if(id==0){
            Cabbage cabbage = new Cabbage(UIDmax++, x,y);
            if(cabbage.body.overlaps(playerRectangle)){
                return generateEntities(0); //try again for a valid position
            }
            /*for(Entity enitity:entities){ //check for overlap
                if(cabbage.body.overlaps(enitity.getRectangle())){
                    return generateTerrain();
                }
            }*/
            return new Cabbage(UIDmax++ + tonpose.ID, x, y);
        }
        else if(id == 1){
            mobCount++;
            if(mobCount > 10000)
                mobCount = 1;
            return new Mob(UIDmax++, tonpose.ID, x, y, mobCount, tonpose);
        }
        else {
            Tree tree=new Tree(UIDmax++, x,y);
            if(tree.body.overlaps(playerRectangle)){
                return generateEntities(2);
            }
            /*for(Entity enitity:entities){ //check for overlap
                if(tree.body.overlaps(enitity.getRectangle())){
                    return generateTerrain();
                }
            }*/
            return tree;
        }
    }

    //adds entities (mobs, trees ect) based on server side
    private Entity generateEntities(int uid, int id, float x, float y){
        switch(id){
            case 0:
                return new Cabbage(uid, x,y);
            case 2:
                mobCount++;
                return new Mob(uid, -1, x,y, mobCount, tonpose);
            case 9:
                return new Tree(uid, x, y);
            default:
                return new Tree(uid, x,y);
        }
    }

    //adds the item based on the server side info
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

    //used in single player
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

    //gen terrain based on server side info
    private void generateTerrain(int height, int width, int[] terrain){
        int i = 0;
        for(int x = 0; x < width/80 + 1; x++){
            for(int y = 0; y < height/80 + 1; y++){
                switch(terrain[i]){
                    case 1:
                        this.terrains[x][y] = new RiverHorizontal(x * 80, y * 80, true);
                        break;
                    case 2:
                        this.terrains[x][y] = new RiverVertical(x*80, y*80, true);
                        break;
                    case 3:
                        this.terrains[x][y] = new RiverHorizontal(x * 80, y * 80, false);
                        break;
                    case 4:
                        this.terrains[x][y] = new RiverVertical(x*80, y*80, false);
                        break;
                    default:
                        this.terrains[x][y] = new grass(x * 80 ,y * 80);
                }
                i++;
            }
        }

    }

    //creates a river at x,y location with given width and lenght
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

    //adds entitys to map
    public void addToMap(Entity entity){
        entitiesAdd.add(entity);
        Network.AddElement add = new Network.AddElement();
        add.id = entity.id;
        add.uid = entity.uid;
        add.x = entity.locationX;
        add.y = entity.locationY;
        tonpose.client.sendTCP(add);
    }

    //wrapper for entity and item addToMap
    public void addToMap(Network.AddElement add){
        if(add.id < 10){
            entitiesAdd.add(generateEntities(add.uid, add.id, add.x, add.y));
        }
        else if(add.id <= 14){
            itemsAdd.add(generateItems(add.uid, add.id, add.x, add.y));
        }
        UIDmax = add.uid + 1;
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

    //removes an entity so it no longer shows on the map
    public void removeFromMap(Entity entity){
        entitiesDelete.add(entity);
        Network.RemoveElement remove = new Network.RemoveElement();
        remove.tid = entity.id;
        remove.uid = entity.uid;
        tonpose.client.sendTCP(remove);
    }

    //wrapper
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

    //moves mobs from other clients
    public void moveElement(Network.MoveElement move){
        // Only move mob elements
        boolean exists = false;
        if(move.tid == 2){
            for(Entity e: entities){
                if(e.uid == move.uid){
                    e.move(move.x, move.y);
                    exists = true;
                }
            }
            if(!exists){
                entitiesAdd.add(generateEntities(move.uid, 2, move.x, move.y));
                UIDmax = move.uid++;
            }
        }
    }

    //checks for nearby targets for the player
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

    //checks for nearby targets for mobs
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

    //returns the distance between two points
    public double getDistance(float x1, float y1, float x2, float y2){ // returns the distance between two points
      double distance=Math.sqrt(Math.abs(x1-x2)*Math.abs(x1-x2)+Math.abs(y1-y2)*Math.abs(y1-y2));
      return distance;

    }

    //adds and removes all objects from buffer list to the map
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
