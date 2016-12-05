package cs309.tonpose;

import java.util.ArrayList;
import java.util.Random;

public class ServerMap {
	private int height,width;
	private int[][] terrains;
	private int[] terrain;
	private ArrayList<ServerEntity> mobs;
	private ArrayList<ServerEntity> entities;
	private ArrayList<ServerItem> items;
	private int entityCount;
	private int mobCount;
	public int UIDcount;

    public ServerMap(int height, int width, int entityCount){
        this.height=height;
        this.width=width;
        this.entityCount = entityCount;
        UIDcount = 0;
        terrains = new int[height/80+1][width/80+1];
        terrain = new int[(height/80+1)*(width/80+1)];
        mobCount = 0;
        mobs = new ArrayList<ServerEntity>();
        entities = new ArrayList<ServerEntity>();
        items = new ArrayList<ServerItem>();
        
        generateTerrain();
        // Fills the terrain 1d array (to send through network) with the 2d array
        for(int i = 0; i < height/80 + 1; i++){
            for(int j = 0; j < width/80 + 1; j++){
            	terrain[i*((width/80)+1) + j] = terrains[i][j];
            }
        }
        // Adds maxEntities
        for(int i = 0; i < entityCount; i++){
            entities.add(generateEntities());
        }
    }

    // Fills the terrain 2d array with grass, adds a single horizontal river
    private void generateTerrain(){
        for(int i = 0; i < height/80 +1; i++){
            for(int j = 0; j < width/80 +1; j++){
            	terrains[i][j] = 0;
            }
        }
        Random r = new Random();
        int x = r.nextInt(width/80 - 5);
        int y = r.nextInt(height/80 - 1);
        createRiverHorizontal(x, y, 1, 5, true);
        x = r.nextInt(width/80 - 5);
        y = r.nextInt(height/80 - 1);
        createRiverHorizontal(x, y, 1, 5, true);
    }

    // Adds a horizontal river to the terrain 2d array
    private void createRiverHorizontal(int x, int y, int width, int length, boolean left){
        for(int i = x; i < x + length; i++){
            for(int j = y; j < y + width; j++){
            	if(left){
            		terrains[i][j] = 1;
            	}
            	else{
            		terrains[i][j] = 3;
            	}
            }
        }
    }

    public int[] getTerrain(){
        return terrain;
    }
    
    public ServerEntity generateEntities(){
    	// Random location and type for new entity
    	Random r = new Random();
    	int x = r.nextInt(width);
        int y = r.nextInt(height);
        int id = r.nextInt(3);
        ServerEntity e = new ServerEntity();
        switch(id){
	        case 0:
	        	e.typeID = 0;
	        	break;
	        // For now, server does not generate mobs, they will be added from the client
	        //case 1:
	        //	e.typeID = 2;
	        //	break;
	        default:
	        	e.typeID = 9;
	        	break;
        }
        e.uniqueID = UIDcount++;
        e.x = (float)x;
        e.y = (float)y;
        return e;
    }

    public int[][] getEntityArray(){
    	// Fills 2d array (to be sent over network) with entity arraylist
    	int [][] arr = new int[entityCount][4];
    	for(int i = 0; i < entityCount; i++){
    		arr[i][0] = entities.get(i).uniqueID;
    		arr[i][1] = entities.get(i).typeID;
    		arr[i][2] = (int)entities.get(i).x;
    		arr[i][3] = (int)entities.get(i).y;
    	}
    	return arr;
    }
    
    public ArrayList<ServerEntity> getEntities() {
        return entities;
    }
    
    public ArrayList<ServerEntity> getMobs() {
        return mobs;
    }

    public ArrayList<ServerItem> getItems() {
        return items;
    }

    public int getWidth(){
        return width;
    }

    public int getHeight(){
        return height;
    }

    public void add(ServerItem item){
        items.add(item);
    }

    public void add(ServerEntity entity){
        if(entity.typeID == 2){
        	mobCount++;
        	mobs.add(entity);
        }
        else{
        	entities.add(entity);
        	entityCount++;
        }
    }
    
    public void remove(ServerItem item){
        items.remove(item);
    }

    public void remove(ServerEntity entity){
        if(entity.typeID == 2){
        	mobCount--;
        	mobs.remove(entity);
        }
        else{
        	entities.remove(entity);
        	entityCount--;
        }
    }
}
