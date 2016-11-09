package cs309.tonpose;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;

import cs309.tonpose.Network.MovePlayer;

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

	public static OrthographicCamera camera;
	private SpriteBatch batch;
	public static Player player;

	private Array<Rectangle> terrain;

	private long lastNpc = 0;
	private long lastTick = 0;
	private long lastMove;
	private long lastUpdate;
	private final int TICKDELAY =    1000000;
	private final int NPCDELAY =    30000000;
	private final int MOVEDELAY =   10000000;
	private final int UPDATEDELAY = 20000000;

	//private Stage stage;
	private TextButton inv;
	private TextButton actionButton;
	private BitmapFont font = new BitmapFont();
	private Table table;
	private TextButton.TextButtonStyle textButtonStyle;
	public static float playerHealthX=1;
	public static float playerHealthY=416;
	Terrain terrainMap[][] = Map.getTerrains();

	public TonposeScreen(Tonpose t) {
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

		Map = new Map(1000, 1000, 20, 0);        //TODO retrieve map from server instead of making one here


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
			renderUpperY = Map.getHeight();
		}else if(renderLowerY < 0){
			renderLowerY = 0;
		}
		if(renderUpperX > Map.getWidth()){
			renderUpperX = Map.getWidth();
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

		//renders hp bar
		batch.draw(healthImage,playerHealthX,playerHealthY);			//FIXME hp doesnt display after dying and re-entering
		batch.end();  // submits all drawing requests between begin() and end() at once. Speeds up OpenGL rendering

		// make player move on touch
		if (Gdx.input.isTouched()) {
			Vector3 touchPos = new Vector3();
			touchPos.set(Gdx.input.getX(), Gdx.input.getY(),0);
			camera.unproject(touchPos); // transforms the coordinates of the vector to the coordinate system of the camera
			tonpose.lastX = touchPos.x;
			tonpose.lastY = touchPos.y;
		}else{
			tonpose.lastX = player.getX();
			tonpose.lastY = player.getY();
		}
		if (TimeUtils.nanoTime() > lastTick + TICKDELAY) {
			tick();
		}


		//render stage
		stage.act(delta);
		stage.draw();
	}

	private void tick() {

		if (TimeUtils.nanoTime() > lastMove + MOVEDELAY)

			movePlayer();

		if (TimeUtils.nanoTime() > lastNpc + NPCDELAY)
			moveEnemy();

		if (TimeUtils.nanoTime() > lastUpdate + UPDATEDELAY)
			updatePlayer();

		if(player.currentHp < 0&&player.killable)
			tonpose.setScreen(tonpose.deathScreen);

		Map.updateMap();

		lastTick = TimeUtils.nanoTime();
	}


	public void movePlayer() { //TODO change rectangles to better represent where they are on the screen. Also fix outlier case where player spawns in a terrain object
		player.move(tonpose.lastX, tonpose.lastY, 0, 0, 1);

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