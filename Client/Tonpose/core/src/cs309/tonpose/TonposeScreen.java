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

import cs309.tonpose.Network.MovePlayer;
import cs309.tonpose.map.Entity;
import cs309.tonpose.map.Item;
import cs309.tonpose.map.Map;
import cs309.tonpose.map.Mob;
import cs309.tonpose.map.Player;
import cs309.tonpose.map.Terrain;

import static java.lang.Math.abs;

public class TonposeScreen implements Screen {

	final Tonpose tonpose;
	public static Map Map;
	private Music music;
	private Texture playerImage, buttonImage;
	public static Texture	healthImage;
	private Stage stage;
	private TextureRegion buttonRegion;
	private TextureRegionDrawable buttonRegionDrawable;
	private ImageButton playersButton;
	private float renderBufferX;
	private float renderBufferY;
	private boolean moving;

	public static OrthographicCamera camera;
	private SpriteBatch batch;
	public static Player player;

	private Array<Rectangle> terrain;

	private long lastNpc = 0;
	private long lastTick = 0;
	private long lastMove = 0;
	private long lastUpdate = 0;
	private long lastSpawn = 0;
	private final int TICKDELAY =     1000000;
	private final int NPCDELAY =     60000000;
	private final int MOVEDELAY =    10000000;
	private final int UPDATEDELAY =  20000000;
	private final long SPAWNDELAY =  8000000000L;
	private final long GROWTHDELAY = 8000000000L; //TODO implement growth of trees and cabbages after planting

	//private Stage stage;
	private TextButton inv;
	private TextButton actionButton;
	public static Rectangle actionButtonDeadZone, inventoryDeadZone, playersOnlineDeadZone;
	private BitmapFont font = new BitmapFont();
	private Table table;
	private TextButton.TextButtonStyle textButtonStyle;
	public static float playerHealthX=1;
	public static float playerHealthY=416;
	Terrain terrainMap[][];

	public String Score="Score: ";

	public TonposeScreen(Tonpose t, int[] terrainArray, int[][] entitiesArray){
		this.tonpose = t;

		// load textures
		healthImage=new Texture(Gdx.files.internal("pizza8.png"));
		playerImage=new Texture(Gdx.files.internal("mainbase.png"));
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

		Map = new Map(1000, 1000, terrainArray, entitiesArray);
		terrainMap = Map.getTerrains();

		//initialize main character
		/*
		player = new Rectangle();
		player.width = 45;  //player image dimensions are 45X64 if measuring to the hat
		player.height = 64;
		player.x = tonpose.lastX;
		player.y = tonpose.lastY;
		*/

		player = new Player(tonpose.lastX, tonpose.lastY, tonpose.Name);



		//add terrain to map	//TODO figure out what this is and rename it or delete it
		terrain = new Array<Rectangle>(); //the array for terrain
		for (Entity entity : Map.getEntities()) {
			terrain.add(new Rectangle(entity.locationX, entity.locationY, entity.width, entity.height));
		}
		actionButtonDeadZone=new Rectangle(725,0,75,40); //sets dead zone for the action button where player will not move if it is touched
		playersOnlineDeadZone=new Rectangle(0,0,75,75);
		inventoryDeadZone=new Rectangle(375,0,50,40);
	}

	@Override
	public void render(float delta) { //TODO change to only render inside of the camera
		camera.update();
		batch.setProjectionMatrix(camera.combined); // tells spriteBatch to use camera coordinate system
		batch.begin();

		// clear screen to dark blue color
		Gdx.gl.glClearColor(0, 0, 0.2f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		float renderUpperX = camera.position.x + renderBufferX;
		float renderLowerX = camera.position.x - renderBufferX;
		float renderUpperY = camera.position.y + renderBufferY;
		float renderLowerY = camera.position.y - renderBufferY;

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

		for(int i = (int)(renderLowerX / 20); i < renderUpperX / 20; i++){
			for(int j = (int)(renderLowerY / 20); j < renderUpperY /20; j++){
				batch.draw(terrainMap[i][j].getTexture(), terrainMap[i][j].locationX, terrainMap[i][j].locationY);
			}
		}

		//render other players
		if(!tonpose.Name.equals("offline")) {
			for (User value : tonpose.users.values()) {
				batch.draw(playerImage, value.x, value.y);
			}
		}

		//renders user's player
		batch.draw(player.texture, player.getX(), player.getY());

		//renders other entities
		for (Entity entity : Map.getEntities()) { //draws Entities
			if(renderUpperX > entity.locationX) {
				if (renderLowerX  - 40 < entity.locationX) {
					if (renderUpperY > entity.locationY) {
						if (renderLowerY - 100 < entity.locationY) {
							batch.draw(entity.getTexture(), entity.locationX, entity.locationY);
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
								batch.draw(item.getTexture(), item.locationX, item.locationY);
							}
						}
					}
				}
			}
		}

		font.draw(batch, Score+player.getScore(),playerHealthX+65,playerHealthY+60);					//Render Score
		//renders hp bar
		batch.draw(healthImage,playerHealthX,playerHealthY);			//FIXME hp doesnt display after dying and re-entering
		batch.end();  // submits all drawing requests between begin() and end() at once. Speeds up OpenGL rendering
		// make player move on touch
		int i=0;
		 moving=false;
		for(i=0; i<3; i++){ //iterates through all possible touch events (Maximum of 3), and uses the first one found
			if (Gdx.input.isTouched(i)) { //checks if touch event i is active
				Vector3 touchPos = new Vector3(); //obtains coordinates of touch event i
				touchPos.set(Gdx.input.getX(i), Gdx.input.getY(i),0);
				camera.unproject(touchPos); // transforms the coordinates of the vector to the coordinate system of the camera
				if(actionButtonDeadZone.contains(touchPos.x,touchPos.y)||playersOnlineDeadZone.contains(touchPos.x,touchPos.y)||inventoryDeadZone.contains(touchPos.x,touchPos.y)){ //checks if touch is in the dead zone, if so the player will not move
					tonpose.lastX = player.getX();
					tonpose.lastY = player.getY();
				}
				else {
					tonpose.lastX = touchPos.x;
					tonpose.lastY = touchPos.y;
					moving = true;
				}
			}
		}
		if(!moving){
			tonpose.lastX = player.getX();
			tonpose.lastY = player.getY();
		}

		if (TimeUtils.nanoTime() > lastTick + TICKDELAY) {
			tick(TimeUtils.nanoTime());
		}


		//render stage
		stage.act(delta);
		stage.draw();
	}

	private void tick(long time) {

		if (time > lastMove + MOVEDELAY)
			if(moving)
			movePlayer();

		if (time > lastNpc + NPCDELAY)
			moveEnemy();

		if (time > lastUpdate + UPDATEDELAY)
			updatePlayer();

		/* spawning temporarily disabled
		if (time > lastSpawn + SPAWNDELAY){
			Map.spawn();
			lastSpawn = time;
		}
		*/

		if(player.currentHp < 0 && player.killable)
			tonpose.setScreen(tonpose.deathScreen);

		Map.updateMap();

		lastTick = time;
	}


	public void movePlayer() { //TODO change rectangles to better represent where they are on the screen. Also fix outlier case where player spawns in a terrain object
		int x = (int)player.getX()/20;
		int y = (int)player.getY()/20;
		int modX = terrainMap[x][y].getModX();
		int modY = terrainMap[x][y].getModY();
		float scale = terrainMap[x][y].getScale();
		player.move(tonpose.lastX, tonpose.lastY, modX, modY, scale);

		lastMove = TimeUtils.nanoTime();
	}

	private void moveEnemy() {
		for (Entity entity : Map.getEntities()) {
			if(entity instanceof Mob){
				/*
				float x = player.getX() - entity.locationX;
				float y = player.getY() - entity.locationY;
				float sum = abs(x) + abs(y);
				if(sum > 4){
					entity.locationX += 5 * (x/sum);
					entity.locationY += 5 * (y/sum);
					entity.setBody(entity.locationX,entity.locationY);
				}
				*/
				((Mob) entity).move(player, 0 ,0, 1);
			}
		}

		lastNpc = TimeUtils.nanoTime();
	}

	//TODO add menu button

	//updates the player location through the network
	public void updatePlayer(){
		MovePlayer move = new MovePlayer();
		move.x = player.getX();
		move.y = player.getY();
		if(!tonpose.Name.equals("offline")) {
			tonpose.client.sendTCP(move);
		}
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
}