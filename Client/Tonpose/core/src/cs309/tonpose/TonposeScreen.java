package cs309.tonpose;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;

import java.util.ArrayList;

import cs309.tonpose.Network.MovePlayer;
import cs309.tonpose.map.Entity;
import cs309.tonpose.map.Item;
import cs309.tonpose.map.Map;
import cs309.tonpose.map.Mob;
import cs309.tonpose.map.Player;
import cs309.tonpose.map.Projectile;
import cs309.tonpose.map.Terrain;
import cs309.tonpose.map.Wand;

import static java.lang.Math.abs;

public class TonposeScreen implements Screen {

	final Tonpose tonpose;
	public Map Map;
	private Music music;

	private Texture playerImage, buttonImage, playerMoving1, playerMoving2, Background;
	private Texture mob, tree, cabbage, woodBlock;
	private Texture	treeSeeds, cabbageSeeds, cabbageLeaves, log, sword, bones;
	private Texture moving1Mob;
	private Texture moving2Mob;
	private Texture attacking1Mob;
	private Texture attacking2Mob;
	private Texture hitMob;
	private Texture attacking1Player;
	private Texture attacking2Player;
	private Texture hitPlayer;
	private Texture fire;
	private Texture ranged;

	public Texture healthImage, healthImage0, healthImage1, healthImage2, healthImage3, healthImage4, healthImage5, healthImage6, healthImage7, healthImage8;
	private Stage stage;
	private TextureRegion buttonRegion;
	private TextureRegionDrawable buttonRegionDrawable;
	private ImageButton playersButton;
	private float renderBufferX;
	private float renderBufferY;
	private boolean moving;
	public enum state {
		standing, moving, action, hit
	}
	private int nextAnimation = 0;

	public static OrthographicCamera camera;
	private SpriteBatch batch;
	public static Player player;

	private Array<Rectangle> terrain;

	private long lastNpc = 0;
	private long lastTick = 0;
	private long lastMove = 0;
	private long lastUpdate = 0;
	private long lastSpawn = 0;
	private long lastAnimation = 0;
	private long lastFire = 0;
	private long lastTrap = 0;
	private final int TICKDELAY =       20000000;
	private final int NPCDELAY =        80000000;
	private final int MOVEDELAY =       20000000;
	private final int UPDATEDELAY =     50000000;
	private final long ANIMATIONDELAY = 160000000L;
	private final long SPAWNDELAY =     80000000000L;
	private final long TRAPDELAY  =     800000000L;
	private final long FIREDELAY = 		640000000L;
	private final long GROWTHDELAY =    8000000000L; //TODO implement growth of trees and cabbages after planting

	private int animationCount = 0;

	private boolean aiming = false;
	private float aimX;
	private float aimY;
	private boolean targeting =false;

	//private Stage stage;
	private TextButton inv;
	private TextButton actionButton;
	public Rectangle actionButtonDeadZone, inventoryDeadZone, playersOnlineDeadZone;
	private BitmapFont font = new BitmapFont();
	private Table table;
	private TextButton.TextButtonStyle textButtonStyle;
	public float playerHealthX=1;
	public float playerHealthY=416;
	Terrain terrainMap[][];
	private ArrayList<User> oldPlayers = new ArrayList<User>();

	public String Score="Score: ";
	public String Lvl="Level: ";

	public TonposeScreen(Tonpose t){
		this.tonpose = t;

		// load standing Entity textures
		healthImage=new Texture(Gdx.files.internal("pizza8.png"));
		playerImage=new Texture(Gdx.files.internal("mainbase.png"));
		mob=new Texture(Gdx.files.internal("player2base.png"));
		cabbage=new Texture(Gdx.files.internal("cabbage.png"));
		woodBlock =new Texture(Gdx.files.internal("woodBlock.png"));
		tree =new Texture(Gdx.files.internal("treeStill.png"));
		ranged=new Texture(Gdx.files.internal("rangedBase.png"));

		//load mob animation textures
		moving2Mob = new Texture(Gdx.files.internal("player2WalkingRight3.png"));
		moving1Mob = new Texture(Gdx.files.internal("player2WalkingRight1.png"));
		attacking1Mob = new Texture(Gdx.files.internal("player2Attack1.png"));
		attacking2Mob = new Texture(Gdx.files.internal("player2Attack2.png"));
		hitMob = new Texture(Gdx.files.internal("player2Scared.png"));

		//load item textures
		treeSeeds =new Texture(Gdx.files.internal("acorn.png"));
		cabbageSeeds =new Texture(Gdx.files.internal("cabbage seeds.png"));
		cabbageLeaves =new Texture(Gdx.files.internal("CabbageLeaves.png"));
		log =new Texture(Gdx.files.internal("shlog.png"));
		sword =new Texture(Gdx.files.internal("sword.png"));
		bones =new Texture(Gdx.files.internal("bone.png"));
		fire = new Texture(Gdx.files.internal("fire.png"));

		//load player textures
		playerMoving1=new Texture(Gdx.files.internal("mainWalkingRight1.png"));
		playerMoving2=new Texture(Gdx.files.internal("mainWalkingRight3.png"));
		Background= new Texture(Gdx.files.internal("waterbackground2.png"));
		attacking1Player=new Texture(Gdx.files.internal("mainAttack1.png"));
		attacking2Player=new Texture(Gdx.files.internal("mainAttack2.png"));
		hitPlayer=new Texture(Gdx.files.internal("mainScared.png"));

		healthImage0=new Texture(Gdx.files.internal("pizza0.png"));
		healthImage1=new Texture(Gdx.files.internal("pizza1.png"));
		healthImage2=new Texture(Gdx.files.internal("pizza2.png"));
		healthImage3=new Texture(Gdx.files.internal("pizza3.png"));
		healthImage4=new Texture(Gdx.files.internal("pizza4.png"));
		healthImage5=new Texture(Gdx.files.internal("pizza5.png"));
		healthImage6=new Texture(Gdx.files.internal("pizza6.png"));
		healthImage7=new Texture(Gdx.files.internal("pizza7.png"));
		healthImage8=new Texture(Gdx.files.internal("pizza8.png"));

		// load music
		music = Gdx.audio.newMusic(Gdx.files.internal("rain.mp3"));

		// start music
		music.setLooping(true);
		music.play();

		// new camera
		camera = new OrthographicCamera();
		camera.setToOrtho(false, 800, 480);
		renderBufferX = camera.viewportWidth/2 + 20;
		renderBufferY = camera.viewportHeight/2 + 20;


		batch = new SpriteBatch();

		Map = new Map(tonpose, 1000, 1000, tonpose.terrainArray, tonpose.entitiesArray, tonpose.itemsArray);
		terrainMap = Map.getTerrains();

		//initialize main character
		/*
		player = new Rectangle();
		player.width = 45;  //player image dimensions are 45X64 if measuring to the hat
		player.height = 64;
		player.x = tonpose.lastX;
		player.y = tonpose.lastY;
		*/

		player = new Player(tonpose.lastX, tonpose.lastY, tonpose.Name, tonpose);
		Map.spawnNPC();

		//add terrain to map	//TODO figure out what this is and rename it or delete it
		terrain = new Array<Rectangle>(); //the array for terrain
		for (Entity entity : Map.getEntities()) {
			terrain.add(new Rectangle(entity.locationX, entity.locationY, entity.width, entity.height));
		}
		actionButtonDeadZone=new Rectangle(675,0,125,50); //sets dead zone for the action button where player will not move if it is touched
		playersOnlineDeadZone=new Rectangle(0,0,75,75);
		inventoryDeadZone=new Rectangle(375,0,50,40);
	}

	@Override
	public void render(float delta) {
		//sets area around screen so client only renders what the user will see
		float renderUpperX = camera.position.x + renderBufferX;
		float renderLowerX = camera.position.x - renderBufferX;
		float renderUpperY = camera.position.y + renderBufferY;
		float renderLowerY = camera.position.y - renderBufferY;

		//dont render anything off the map
		if(renderUpperY > Map.getHeight()){
			renderUpperY = Map.getHeight() + 20;
		}else if(renderLowerY < 0){
			renderLowerY = 0;
		}
		if(renderUpperX > Map.getWidth()){
			renderUpperX = Map.getWidth() + 20;
		}else if(renderLowerX < 0){
			renderLowerX = 0;
		}

		camera.update();
		batch.setProjectionMatrix(camera.combined); // tells spriteBatch to use camera coordinate system
		batch.begin();

		// clear screen to dark blue color
		Gdx.gl.glClearColor(0, 0, 0.7f, 0.5f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.draw(Background,-400,-400);
		//render terrain on screen
		for(int i = (int)(renderLowerX / 80); i < renderUpperX / 80; i++){
			for(int j = (int)(renderLowerY / 80); j < renderUpperY / 80; j ++){
				batch.draw(terrainMap[i][j].getTexture(), terrainMap[i][j].locationX, terrainMap[i][j].locationY);
			}
		}

		//renders other players and plays walking or standing animation
		for (User value	: tonpose.users.values()) {
			boolean found = false;
			for (User old: oldPlayers) {
				if(old.id == value.id){
					if(old.x != value.x || old.y != value.y){
						switch(animationCount){
							case 0:
								batch.draw(playerMoving1, value.x, value.y);
								break;
							case 1:
								batch.draw(playerImage, value.x, value.y);
								break;
							case 2:
								batch.draw(playerMoving2, value.x, value.y);
								break;
							case 3:
								batch.draw(playerImage, value.x, value.y);
								break;
							default:
								batch.draw(playerMoving1, value.x, value.y);
								animationCount = 0;
						}
					}else{
						batch.draw(playerImage, value.x, value.y);
					}
					old.x = value.x;
					old.y = value.y;
					found = true;
				}
			}
			if(found == false){
				oldPlayers.add(value);
			}
		}

		/*//render other players old
		for (User value : tonpose.users.values()) {
			batch.draw(playerImage, value.x, value.y);
		}*/

		//renders user's player
		batch.draw(player.texture, player.getX(), player.getY());

		//renders other entities
		for (Entity entity : Map.getEntities()) { //draws Entities
			if(renderUpperX > entity.locationX) {
				if (renderLowerX  - 40 < entity.locationX) {
					if (renderUpperY > entity.locationY) {
						if (renderLowerY - 100 < entity.locationY) {
							//batch.draw(entity.getTexture(), entity.locationX, entity.locationY);
							switch (entity.id){
								case 0:
									batch.draw(cabbage, entity.locationX, entity.locationY);
									break;
								case 2:
									batch.draw(getTextureMob(((Mob) entity).old, 2), entity.locationX, entity.locationY);
									break;
								case 8:
									batch.draw(woodBlock, entity.locationX, entity.locationY);
									break;
								case 9:
									batch.draw(tree, entity.locationX, entity.locationY);
									break;
								default:
									batch.draw(ranged, entity.locationX, entity.locationY);
							}
						}
					}
				}
			}
		}

		//renders items dropped on the map
		for (Item item : Map.getItems()){
			if(!item.inInventory){
				if(renderUpperX > item.locationX) {
					if (renderLowerX < item.locationX) {
						if (renderUpperY > item.locationY) {
							if (renderLowerY < item.locationY) {
								switch (item.getID()){
									case 10:
										batch.draw(treeSeeds, item.locationX, item.locationY);
										break;
									case 11:
										batch.draw(cabbageSeeds, item.locationX, item.locationY);
										break;
									case 12:
										batch.draw(cabbageLeaves, item.locationX, item.locationY);
										break;
									case 13:
										batch.draw(log, item.locationX, item.locationY);
										break;
									case 14:
										batch.draw(log, item.locationX, item.locationY);
										break;
									case 15:
										batch.draw(bones, item.locationX, item.locationY);
										break;
									case 16:
										batch.draw(sword, item.locationX, item.locationY);
										break;
									default:
										batch.draw(playerImage, item.locationX, item.locationY);
								}
								//batch.draw(item.getTexture(), item.locationX, item.locationY);
							}
						}
					}
				}
			}
		}

		//renders projectiles
		for (Projectile p: Map.getProjectiles()){
			batch.draw(fire, p.getX(), p.getY());
		}

		font.draw(batch, Score+player.getScore(),playerHealthX+65,playerHealthY+60);					//Render Score
		font.draw(batch, Lvl+player.lvl, playerHealthX+65, playerHealthY+30); // draws current player level
		//renders hp bar
		batch.draw(updateHP(), playerHealthX, playerHealthY);			//FIXME hp doesnt display after dying and re-entering
		batch.end();  // submits all drawing requests between begin() and end() at once. Speeds up OpenGL rendering
		// make player move on touch
		int i=0;
		moving=false;
		aiming=false;
		targeting=false;
		for(i=0; i<3; i++){ //iterates through all possible touch events (Maximum of 3), and uses the first one found
			if (Gdx.input.isTouched(i)) { //checks if touch event i is active
				Vector3 touchPos = new Vector3(); //obtains coordinates of touch event i
				touchPos.set(Gdx.input.getX(i), Gdx.input.getY(i),0);
				camera.unproject(touchPos); // transforms the coordinates of the vector to the coordinate system of the camera
				if(actionButtonDeadZone.contains(touchPos.x,touchPos.y)||playersOnlineDeadZone.contains(touchPos.x,touchPos.y)||inventoryDeadZone.contains(touchPos.x,touchPos.y)){ //checks if touch is in the dead zone, if so the player will not move
					tonpose.lastX = player.getX();
					tonpose.lastY = player.getY();
					if(player.equiped instanceof Wand){
						aiming = true;
					}
				}
				else if (aiming == false){
					tonpose.lastX = touchPos.x;
					tonpose.lastY = touchPos.y;
					moving = true;
					nextAnimation = 1;
				}else{
					aimX = touchPos.x;
					aimY = touchPos.y;
					targeting = true;
				}
			}
		}
		if(!moving){
			tonpose.lastX = player.getX();
			tonpose.lastY = player.getY();
			nextAnimation = 0;
		}

		//render stage
		stage.act(delta);
		stage.draw();

		if (TimeUtils.nanoTime() > lastTick + TICKDELAY) {
			tick(TimeUtils.nanoTime());
		}
	}

	private void tick(long time) {
		if(time > lastFire + FIREDELAY){
			if(targeting == true){
				if(aiming == true){
					((Wand)player.equiped).fire(aimX, aimY, player);
					targeting = false;
					lastFire = time;
				}
			}
		}

		if (time > lastMove + MOVEDELAY)
			//if(moving)
			movePlayer();

		if (time > lastNpc + NPCDELAY)
			moveEnemy();

		if (time > lastUpdate + UPDATEDELAY) //updates player position on other clients
			updatePlayer();

		if (time > lastSpawn + SPAWNDELAY){
			Map.spawnNPC();
			lastSpawn = time;
		}

		if(time > lastAnimation + ANIMATIONDELAY) {
			player.nextAnimation(nextAnimation);
			lastAnimation = time;
			animationCount++;
		}
		if(time>lastTrap+TRAPDELAY){
			for(Entity entity:Map.getEntities()){
				if(entity.id==4){
					if(Map.getDistance(entity.getX(),entity.getY(),player.getX(),player.getY())<75){
						player.changeHp(-1);
					}
				}
			}
			lastTrap=time;
		}

		if(player.currentHp < 0 && player.killable) //checks if player is dead and changes screen accordingly
			tonpose.setScreen(tonpose.deathScreen);

		Map.updateMap();

		lastTick = time;
	}

	public void movePlayer() { //TODO change rectangles to better represent where they are on the screen. Also fix outlier case where player spawns in a terrain object
		int x = (int)player.getX()/80;
		int y = (int)player.getY()/80;
		int modX = terrainMap[x][y].getModX();
		int modY = terrainMap[x][y].getModY();
		float scale = terrainMap[x][y].getScale();
		player.move(tonpose.lastX, tonpose.lastY, modX, modY, scale);

		lastMove = TimeUtils.nanoTime();
	}

	//moves enemys and projectiles
	private void moveEnemy() {
		for (Entity entity : Map.getEntities()) {
			if(entity instanceof Mob){
				if(((Mob)entity).targetID == tonpose.ID){ //checks if the mob is after the player
					int x = (int)entity.getX()/80;
					int y = (int)entity.getY()/80;
					int modX = terrainMap[x][y].getModX();
					int modY = terrainMap[x][y].getModY();
					float scale = terrainMap[x][y].getScale();
					((Mob) entity).move(player, modX ,modY, scale); //moves mob towards player
				}
			}
		}

		for(Projectile projectile : Map.getProjectiles()){
			if(projectile.ownerID != -1){
				projectile.move(Map.getEntities(), player);
			}
		}

		lastNpc = TimeUtils.nanoTime();
	}

	//updates the player location through the network
	public void updatePlayer(){
		MovePlayer move = new MovePlayer();
		move.x = player.getX();
		move.y = player.getY();
		tonpose.client.sendTCP(move);
		lastUpdate = TimeUtils.nanoTime();
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void show() {
		//add stage and players button
		buttonImage = new Texture(Gdx.files.internal("playersButton.png"));
		buttonRegion = new TextureRegion(buttonImage);
		buttonRegionDrawable = new TextureRegionDrawable(buttonRegion);
		playersButton = new ImageButton(buttonRegionDrawable);
		playersButton.addListener(new ClickListener()
		{
			@Override
			public void clicked(InputEvent event, float x, float y)
			{
				tonpose.setScreen(tonpose.playersScreen);
			}
		});
		stage = new Stage();
		Gdx.input.setInputProcessor(stage);

		//new below
		stage.addActor(playersButton);
		table = new Table();
		table.setBounds(0,0, Gdx.graphics.getWidth(), 120);
		table.center().bottom();
		stage.addActor(table);
		font = new BitmapFont();
		textButtonStyle = new TextButton.TextButtonStyle();
		textButtonStyle.font = font;
		inv = new TextButton("inv", textButtonStyle);
		inv.getLabel().setFontScale(5,5);
		inv.addListener(new ClickListener()
		{
			@Override
			public void clicked(InputEvent event, float x, float y)
			{
				tonpose.setScreen(tonpose.inventoryScreen);
			}
		});
		table.add(inv);
		Table tableRight = new Table();
		tableRight.setBounds(0,0, Gdx.graphics.getWidth(), 120);
		tableRight.right();

		actionButton = new TextButton("Action", textButtonStyle);
		actionButton.getLabel().setFontScale(5,5);
		actionButton.addListener(new ClickListener()
		{
			@Override
			public void clicked(InputEvent event, float x, float y)
			{
				player.action();
			}
		});
		tableRight.add(actionButton);
		stage.addActor(tableRight);
		//actionButtonDeadZone=new Rectangle(actionButton.getX(),actionButton.getY(),actionButton.getWidth(),actionButton.getHeight());
	}
	@Override
	public void hide() {
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	@Override
	public void dispose() {
		//dispose assets
		playerImage.dispose();
		buttonImage.dispose();
		batch.dispose();
	}

	//sets texture for players
	private Texture getTexturePlayer(int old){
		switch (old) {
			case 10:
				return playerImage;
			case 11:
				return playerMoving1;
			case 12:
				return playerImage;
			case 13:
				return playerMoving2;
			case 2:
				return playerImage;
			case 3:
				return playerImage;
			case 21:
				return attacking1Player;
			case 22:
				return attacking2Player;
			case 31:
				return hitPlayer;
			case 32:
				return hitPlayer;
			case 33:
				return hitPlayer;
			case 34:
				return hitPlayer;
			default:
				return playerImage;
		}
	}

	//sets the texture for mob
	private Texture getTextureMob(int old, int type){
		switch (type){
			case 2:
				switch (old) {
					case 10:
						return moving1Mob;
					case 11:
						return mob;
					case 12:
						return moving2Mob;
					case 13:
						return mob;
					case 2:
						return mob;
					case 3:
						return mob;
					case 21:
						return attacking1Mob;
					case 22:
						return attacking2Mob;
					case 31:
						return hitMob;
					case 32:
						return hitMob;
					case 33:
						return hitMob;
					case 34:
						return hitMob;
					default:
						return mob;
				}
			default:
				switch (old) {
					case 10:
						return moving1Mob;
					case 11:
						return mob;
					case 12:
						return moving2Mob;
					case 13:
						return mob;
					case 2:
						return mob;
					case 3:
						return mob;
					case 21:
						return attacking1Mob;
					case 22:
						return attacking2Mob;
					case 31:
						return hitMob;
					case 32:
						return hitMob;
					case 33:
						return hitMob;
					case 34:
						return hitMob;
					default:
						return mob;
				}
		}

	}

	private Texture updateHP(){
		switch(player.currentHp){
			case 0:
				return healthImage0;
			case 1:
				return healthImage1;
			case 2:
				return healthImage2;
			case 3:
				return healthImage3;
			case 4:
				return healthImage4;
			case 5:
				return healthImage5;
			case 6:
				return healthImage6;
			case 7:
				return healthImage7;
			case 8:
				return healthImage8;
			default:
				return healthImage0;
		}
	}
}