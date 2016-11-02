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
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;

import cs309.tonpose.Network.MovePlayer;

import static java.lang.Math.abs;

public class TonposeScreen implements Screen {

	final Tonpose tonpose;
	private Map Map;
	private Music music;
	private Texture playerImage, buttonImage,healthImage;
	private Stage stage;
	private TextureRegion buttonRegion;
	private TextureRegionDrawable buttonRegionDrawable;
	private ImageButton playersButton;

	private OrthographicCamera camera;
	private SpriteBatch batch;
	private Rectangle player;

	private Array<Rectangle> terrain;

	private long lastNpc = 0;
	private long lastHit = 0;
	private boolean touchedEnemy;
	private long lastTick = 0;
	private long lastMove;
	private long lastUpdate;
	private final int TICKDELAY = 1000000;
	private final int NPCDELAY =  30000000;
	private final int MOVEDELAY = 10000000;
	private final int UPDATEDELAY = 20000000;

	//private Stage stage;
	private TextureAtlas atlas;
	private Skin skin;
	private TextButton inv;
	private BitmapFont font = new BitmapFont();
	private Table table;
	private TextButton.TextButtonStyle textButtonStyle;
	private float playerHealthX=1;
	private float playerHealthY=416;

	public TonposeScreen(Tonpose t) {
		this.tonpose = t;

		// load textures TODO remove
		playerImage = new Texture(Gdx.files.internal("mainbase.png"));
		healthImage=new Texture(Gdx.files.internal("pizza8.png"));
		touchedEnemy = false;

		// load music
		music = Gdx.audio.newMusic(Gdx.files.internal("rain.mp3"));

		// start music
		music.setLooping(true);
		music.play();

		/*//add stage and players button
		buttonImage = new Texture(Gdx.files.internal("playersButton.png"));
		buttonRegion = new TextureRegion(buttonImage, 0, 0, 200, 400);
		buttonRegionDrawable = new TextureRegionDrawable(buttonRegion);
		playersButton = new ImageButton(buttonRegionDrawable);
		playersButton.addListener(new EventListener()
		{
			@Override
			public boolean handle(Event event)
			{
				tonpose.setScreen(tonpose.playersScreen);
				return true;
			}
		});
		stage = new Stage();
		stage.addActor(playersButton);
		Gdx.input.setInputProcessor(stage);*/

		// new camera
		camera = new OrthographicCamera();
		camera.setToOrtho(false, 800, 480);

		batch = new SpriteBatch();

		Map = new Map(1000, 1000, 20, 0);        //TODO retrieve map from server instead of making one here


		//initialize main character
		player = new Rectangle();
		player.width = 45;  //player image dimensions are 45X64 if measuring to the hat
		player.height = 64;
		player.x = tonpose.lastX;
		player.y = tonpose.lastY;

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

		//render stage
		stage.act(delta);
		stage.draw();

		// render player and enemy
		batch.setProjectionMatrix(camera.combined); // tells spriteBatch to use camera coordinate system
		batch.begin();
		batch.draw(playerImage, player.x, player.y);
		batch.draw(healthImage,playerHealthX,playerHealthY);
		//batch.draw(enemyImage, enemy.x, enemy.y);			//TODO replace with mob
		for (Entity entity : Map.getEntities()) { //draws terrain
				batch.draw(entity.getTexture(), entity.locationX, entity.locationY);
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
			//player.x= touchPos.x-64;
			//player.y= touchPos.y-64/2;
		}else{
			tonpose.lastX = player.getX();
			tonpose.lastY = player.getY();
		}
		if (TimeUtils.nanoTime() > lastTick + TICKDELAY) {
			tick();
		}
	}

	private void tick() {

		if (TimeUtils.nanoTime() > lastMove + MOVEDELAY)
			movePlayer();

		if (TimeUtils.nanoTime() > lastNpc + NPCDELAY)
			moveEnemy();

		if (TimeUtils.nanoTime() > lastUpdate + UPDATEDELAY)
			updatePlayer();

		lastTick = TimeUtils.nanoTime();
	}


	public void movePlayer() { //TODO change rectangles to better represent where they are on the screen. Also fix outlier case where player spawns in a terrain object
		float prevX=player.getX();
		float prevY=player.getY();
		float x = tonpose.lastX - player.getX();
		float y = tonpose.lastY - player.getY();
		float sum = abs(x) + abs(y);
		boolean collidedX=false;
		boolean collidedY=false;
		if (sum > 3) { //stops if within 3 units of clicked location to prevent never stopping
			float xMove = 5 * (x / sum);
			float yMove = 5 * (y / sum);
			Rectangle newPositionX = new Rectangle(player.getX() + xMove, player.getY(), player.getWidth(), player.getHeight());
			Rectangle newPositionY = new Rectangle(player.getX(), player.getY() + yMove, player.getWidth(), player.getHeight());
			for (Entity entity : Map.getEntities()) { //checks if the player is going to collide with any entities
				if(entity.collision == true){
					if (newPositionX.overlaps(entity.getRectangle())) {
						collidedX = true;
					}
					if(newPositionY.overlaps(entity.getRectangle())){

						collidedY=true;
					}
				}
			}
			if (!collidedX) {
				if (player.x + xMove < 0) {  //this section assumes no collisions with objects after xmove and ymove are added
					xMove = -player.getX();
					player.x = 0;

				} else if (player.x + xMove > Map.getWidth()) {
					xMove = Map.getWidth() - player.x;
					player.x = Map.getWidth();

				} else {
					player.x += xMove;
				}
				camera.translate(xMove, 0);
				playerHealthX+=xMove;
				//cameraX+=xMove;
			}
			if(!collidedY){
				if (player.y + yMove < 0) {
					yMove = -player.getY();
					player.y = 0;

				} else if (player.y + yMove > Map.getHeight()) {
					yMove = Map.getHeight() - player.y;
					player.y = Map.getHeight();

				} else {
					player.y += yMove;
				}
				//cameraY+=yMove;
				camera.translate(0, yMove);//keeps camera within the map's bounds
				playerHealthY+=yMove;
			}

		}

		lastMove = TimeUtils.nanoTime();
	}



	private void moveEnemy() { // called whenever a raindrop spawns
		for (Entity entity : Map.getEntities()) {
			if(entity instanceof Mob){
				float x = player.getX() - entity.locationX;
				float y = player.getY() - entity.locationY;
				float sum = abs(x) + abs(y);
				if(sum > 4){
					entity.locationX += 5 * (x/sum);
					entity.locationY += 5 * (y/sum);
					entity.setBody(entity.locationX+5*(x/sum),entity.locationY+5*(y/sum),entity.width,entity.height);
				}
				//AI.direct(player.getX(), player.getY(), entity.getRectangle());
				//((Mob) entity).move(player.getX(), player.getY());
			}
		}

		lastNpc = TimeUtils.nanoTime();
	}

	/*public void goToMenu(){
		Intent intent = new Intent(this, MainMenu.class);	//TODO implement interface to allow activity switching
		context.startActivity(intent);
	}*/

	//updates the player location through the network
	public void updatePlayer(){
		MovePlayer move = new MovePlayer();
		move.x = player.x;
		move.y = player.y;
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
		playersButton.addListener(new EventListener()
		{
			@Override
			public boolean handle(Event event)
			{
				tonpose.setScreen(tonpose.playersScreen);
				return true;
			}
		});
		stage = new Stage();
		stage.addActor(playersButton);
		Gdx.input.setInputProcessor(stage);

		//new below
		Table table = new Table();
		table.setBounds(0,0, Gdx.graphics.getWidth(), 30);
		table.right();
		stage.addActor(table);

		font = new BitmapFont();
		textButtonStyle = new TextButton.TextButtonStyle();
		textButtonStyle.font = font;
		inv = new TextButton("inv", textButtonStyle);
		inv.getLabel().setFontScale(5,5);
		inv.addListener(new EventListener()
		{
			@Override
			public boolean handle(Event event)
			{
				tonpose.setScreen(tonpose.inventoryScreen);
				return true;
			}
		});
		table.add(inv);

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