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

public class TonposeGame extends ApplicationAdapter {

	static AndroidMethods androidMethod; 															//used for android methods such as toasts or intent usage
	private Texture dropImage;
	private Texture bucketImage;
	private Sound dropSound;
	private Music rainMusic;
	private Texture playerImage, enemyImage;

	private OrthographicCamera camera;
	private SpriteBatch batch;
	private Rectangle bucket,player,enemy;

	private Array<Rectangle> raindrops;
	private long lastDropTime; //time in ns
	private int dropsLost;
	private boolean touchedEnemy;


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
		player.x=200;
		player.y=200;

		enemy=new Rectangle();
		enemy.width=64;
		enemy.height=64;
		enemy.x=500;
		enemy.y=400;

		// add raindrops
		raindrops = new Array<Rectangle>();
		spawnRaindrop();

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
		batch.end();  // submits all drawing requests between begin() and end() at once. Speeds up OpenGL rendering

		// make bucket move on touch
		if(Gdx.input.isTouched()){
			Vector3 touchPos = new Vector3();
			touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
			camera.unproject(touchPos); // transforms the coordinates of the vector to the coordinate system of the camera
			if(touchedEnemy==false) {
				bucket.x = touchPos.x - 64 / 2;
				bucket.y = touchPos.y - 64 / 2;
			}
			player.x= touchPos.x-64;
			player.y= touchPos.y-64/2;
		}

		// make sure bucket stays in screen
		if(bucket.x < 0)
			bucket.x = 0;
		if(bucket.y > 800 - 64)
			bucket.y = 800 - 64;

		if(player.x < 0)
			player.x = 0;
		if(player.y > 800 - 64)
			player.y = 800 - 64;

		if(player.overlaps(enemy)){ //if the player is touching the enemy
			touchedEnemy=true;
			Toast("ouch!");
		}

		// spawn raindrop if enough time has passed
		if(TimeUtils.nanoTime() - lastDropTime > 1000000000)
			spawnRaindrop();

		// move raindrops, remove if below screen or in bucket
		Iterator<Rectangle> iter = raindrops.iterator();
		while(iter.hasNext()){
			Rectangle raindrop = iter.next();
			raindrop.y -= 200 * Gdx.graphics.getDeltaTime();
			if(raindrop.overlaps(bucket)){
				dropSound.play();
				iter.remove();
			}
			if(raindrop.y + 64 < 0)
				iter.remove();
			 dropSound.play();
			dropsLost++;


		}

	}


	public void Toast(String text) {
		TonposeGame.androidMethod.Toast(text);
	}

	private void spawnRaindrop(){
		moveEnemy();
		Rectangle raindrop = new Rectangle();
		raindrop.x = MathUtils.random(0, 800 - 64);
		raindrop.y = 480;
		raindrop.width = 64;
		raindrop.height = 64;
		raindrops.add(raindrop);
		lastDropTime = TimeUtils.nanoTime();
	}

	private void moveEnemy(){ // called whenever a raindrop spawns
		enemy.x=MathUtils.random(0, 800 - 64);
		enemy.y=MathUtils.random(0, 480 - 64);
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

