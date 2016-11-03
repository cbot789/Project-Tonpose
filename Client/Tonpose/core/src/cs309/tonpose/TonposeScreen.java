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

	public static OrthographicCamera camera;
	private SpriteBatch batch;
	public static Player player;

	private Array<Rectangle> terrain;

	private long lastNpc = 0;
	private long lastHit = 0;
	private boolean touchedEnemy;
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

	public TonposeScreen(Tonpose t) {
		this.tonpose = t;

		// load textures
		healthImage=new Texture(Gdx.files.internal("pizza8.png"));
		touchedEnemy = false;

		// load music
		music = Gdx.audio.newMusic(Gdx.files.internal("rain.mp3"));

		// start music
		music.setLooping(true);
		music.play();

		// new camera
		camera = new OrthographicCamera();
		camera.setToOrtho(false, 800, 480);

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



		//add terrain to map
		terrain = new Array<Rectangle>(); //the array for terrain
		for (Entity entity : Map.getEntities()) {
			terrain.add(new Rectangle(entity.locationX, entity.locationY, entity.width, entity.height));
		}
	}

	@Override
	public void render(float delta) {
		// clear screen to dark blue color
		Gdx.gl.glClearColor(0, 0, 0.2f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		camera.update();

		// render player and enemy
		batch.setProjectionMatrix(camera.combined); // tells spriteBatch to use camera coordinate system
		batch.begin();
		batch.draw(player.texture, player.getX(), player.getY());
		batch.draw(healthImage,playerHealthX,playerHealthY);			//FIXME hp doesnt display after dying and re-entering
		//batch.draw(enemyImage, enemy.x, enemy.y);			//TODO replace with mob
		for (Entity entity : Map.getEntities()) { //draws terrain
			batch.draw(entity.getTexture(), entity.locationX, entity.locationY);
		}
		for(Item item : TonposeScreen.Map.getItems()){
			if(!item.inInventory){
				if(item.getBody().overlaps(player.body)){
					player.addInventory(item);
				}
			}
		}
		for (Item item : Map.getItems()){
			if(!item.inInventory){
				batch.draw(item.getTexture(), item.locationX, item.locationY);
			}
		}
		if(!tonpose.Name.equals("offline")) {
			for (User value : tonpose.users.values()) {
				batch.draw(playerImage, value.x, value.y);
			}
		}

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

		if(player.currentHp < 0)
			tonpose.setScreen(tonpose.deathScreen);

		Map.updateMap();

		lastTick = TimeUtils.nanoTime();
	}


	public void movePlayer() { //TODO change rectangles to better represent where they are on the screen. Also fix outlier case where player spawns in a terrain object
		player.move(tonpose.lastX, tonpose.lastY);

		lastMove = TimeUtils.nanoTime();
	}



	private void moveEnemy() { // called whenever a raindrop spawns
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
				((Mob) entity).move(player);
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