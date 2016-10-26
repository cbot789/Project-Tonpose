package cs309.tonpose;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;

import org.w3c.dom.css.Rect;

import java.util.Iterator;

import static java.lang.Math.abs;

public class TonposeGame extends ApplicationAdapter {

	static AndroidMethods androidMethod; 															//used for android methods such as toasts or intent usage
	private Texture dropImage;
	private Texture tree;
	private Map Map;
	private Texture bucketImage;
	private Sound dropSound;
	private Music rainMusic;
	private Texture playerImage, enemyImage;

	private OrthographicCamera camera;
	private SpriteBatch batch;
	private Rectangle bucket,player,enemy;

	private Array<Rectangle> raindrops, terrain;
	private long lastDrop = 0; //time in ns
	private long lastMove = 0;
	private long lastNpc  =	0;
	private long lastHit = 0;
	private int dropsLost;
	private boolean touchedEnemy;
	private float lastX = 400;
	private float lastY = 240;
	private long lastTick = 0;
	private final int TICKDELAY = 1000000;
	private final int DROPDELAY = 1000000000;
	private final int NPCDELAY =    40000000;
	private final int MOVEDELAY =   10000000;

	public TonposeGame(AndroidMethods androidMethod) {
		this.androidMethod=androidMethod;
	}


	@Override
	public void create () {
		// load images for droplet and bucket
		dropImage = new Texture(Gdx.files.internal("droplet.png"));
		bucketImage = new Texture(Gdx.files.internal("bucket.png"));
		playerImage= new Texture(Gdx.files.internal("mainbase.png"));
		enemyImage= new Texture(Gdx.files.internal("player2base.png"));
		tree= new Texture(Gdx.files.internal("treeStill.png"));

		dropsLost=0;
		touchedEnemy=false;

		// load drop sound and rain music
		dropSound = Gdx.audio.newSound(Gdx.files.internal("drop.wav"));
		rainMusic = Gdx.audio.newMusic(Gdx.files.internal("rain.mp3"));

		// start music
		rainMusic.setLooping(true);
		rainMusic.play();

		// new camera
		camera = new OrthographicCamera();
		camera.setToOrtho(false, 800, 480);

		batch = new SpriteBatch();

		Map=new Map(1000,1000, 10, 0);																		//TODO retrieve map from server instead of making one here

		// initialize the bucket
		bucket = new Rectangle();
		bucket.x = 800 / 2 - 64 / 2;
		bucket.y = 20;
		bucket.width = 64;
		bucket.height = 64;

		//initialize main character
		player = new Rectangle();
		player.width=64;
		player.height=64;
		player.x=lastX;
		player.y=lastY;

		enemy=new Rectangle();
		enemy.width=64;
		enemy.height=64;
		enemy.x=500;
		enemy.y=400;

		// add raindrops
		raindrops = new Array<Rectangle>();
		spawnRaindrop();

		terrain=new Array<Rectangle>(); //the array for terrain
		for(Entity entity:Map.getEntities()){
			terrain.add(new Rectangle(entity.locationX,entity.locationY,entity.width,entity.height));
		}

	}

	@Override
	public void render () {
		// clear screen to dark blue color
		Gdx.gl.glClearColor(0, 0, 0.2f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		camera.update();

		// render bucket and raindrops
		batch.setProjectionMatrix(camera.combined); // tells spriteBatch to use camera coordinate system
		batch.begin();
		if(touchedEnemy==false)
			batch.draw(bucketImage, bucket.x, bucket.y);
		batch.draw(playerImage, player.x, player.y);
		batch.draw(enemyImage,enemy.x,enemy.y);
		for(Rectangle raindrop: raindrops){
			batch.draw(dropImage, raindrop.x, raindrop.y);
		}
		for(Entity entity:Map.getEntities()){ //draws terrain
			if(entity.id==1) //checks if it is a standard tree
			batch.draw(tree,entity.locationX,entity.locationY);
		}

		batch.end();  // submits all drawing requests between begin() and end() at once. Speeds up OpenGL rendering

		// make bucket move on touch
		if(Gdx.input.isTouched()){
			Vector3 touchPos = new Vector3();
			touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
			camera.unproject(touchPos); // transforms the coordinates of the vector to the coordinate system of the camera
			lastX = touchPos.x;
			lastY = touchPos.y;
			//player.x= touchPos.x-64;
			//player.y= touchPos.y-64/2;
		}
		if(TimeUtils.nanoTime() > lastTick + TICKDELAY){
			tick();
		}
		// move raindrops, remove if below screen or in bucket
		Iterator<Rectangle> iter = raindrops.iterator();
		while(iter.hasNext()){
			Rectangle raindrop = iter.next();
			raindrop.y -= 200 * Gdx.graphics.getDeltaTime();
			if(raindrop.overlaps(bucket)){
				dropSound.play();
				iter.remove();
			}
			if(raindrop.y + 20 < 0) {
				iter.remove();
				dropSound.play();
				dropsLost++;
			}

		}

	}

	private void tick(){
		lastTick = TimeUtils.nanoTime();

		if(TimeUtils.nanoTime() > lastMove + MOVEDELAY)
			movePlayer();

		if(TimeUtils.nanoTime() > lastNpc + NPCDELAY)
			moveEnemy();

		if(player.overlaps(enemy)){ //if the player is touching the enemy
			touchedEnemy=true;
			Toast("ouch!");
		}
		// spawn raindrop if enough time has passed
		if(TimeUtils.nanoTime() > lastDrop + DROPDELAY)
			spawnRaindrop();
	}



	public void movePlayer(){
		float x = lastX - player.getX();
		float y = lastY - player.getY();
		float sum = abs(x) + abs(y);

		if(sum != 0){
			float xMove = 5 * (x/sum);
			float yMove = 5 * (y/sum);
			player.x += xMove;
			player.y += yMove;
			camera.translate(xMove, yMove);
		}
	/*
		if(player.getX() < lastX - 65){
			player.setX(player.getX() + 5);
			camera.translate(5,0);
		}
		else if(player.getX() > lastX - 60){
			player.setX(player.getX() - 5);
			camera.translate(-5,0);
		}
		if(player.getY() < lastY - 65){
			player.setY(player.getY() + 5);
			camera.translate(0,5);
		}
		else if(player.getY() > lastY - 60){
			player.setY(player.getY() - 5);
			camera.translate(0,-5);
		}*/
		if(touchedEnemy==false) {
			bucket.x = player.getX() + 32;
			bucket.y = player.getY();
		}

		// make sure bucket and player stays in screen					//TODO change to edit lastX and lastY
	/*	if(bucket.x < 0)
			bucket.x = 0;
		if(bucket.y > 800 - 64)
			bucket.y = 800 - 64;

		if(player.x < 0)
			player.x = 0;
		if(player.y > 800 - 64)
			player.y = 800 - 64;*/

		lastMove =  TimeUtils.nanoTime();
	}

	public void Toast(String text) {
		TonposeGame.androidMethod.Toast(text);
	}

	private void spawnRaindrop(){
		Rectangle raindrop = new Rectangle();
		raindrop.x = MathUtils.random(0, 800 - 64);
		raindrop.y = 480;
		raindrop.width = 64;
		raindrop.height = 64;
		raindrops.add(raindrop);
		lastDrop =  TimeUtils.nanoTime();
	}

	private void moveEnemy(){ // called whenever a raindrop spawns
		AI.direct(player, enemy);
		lastNpc =  TimeUtils.nanoTime();
		//enemy.x=MathUtils.random(0, 800 - 64);
		//enemy.y=MathUtils.random(0, 480 - 64);
	}

	/*public void goToMenu(){
		Intent intent = new Intent(this, MainMenu.class);	//TODO implement interface to allow activity switching
		context.startActivity(intent);
	}*/

	
	@Override
	public void dispose () {
		//dispose assets
		dropImage.dispose();
		bucketImage.dispose();
		dropSound.dispose();
		rainMusic.dispose();
		batch.dispose();
	}
}

