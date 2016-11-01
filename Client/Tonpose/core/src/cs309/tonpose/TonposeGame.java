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

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import cs309.tonpose.Network.ClientConnect;
import cs309.tonpose.Network.AddUser;
import cs309.tonpose.Network.UpdateUser;
import cs309.tonpose.Network.RemoveUser;
import cs309.tonpose.Network.MovePlayer;

import org.w3c.dom.css.Rect;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;

import static java.lang.Math.abs;

public class TonposeGame extends ApplicationAdapter {

	static AndroidMethods androidMethod;                                                            //used for android methods such as toasts or intent usage
	private Texture treeImage;
	private Map Map;
	private Music music;
	private Texture playerImage, enemyImage;

	private OrthographicCamera camera;
	private SpriteBatch batch;
	private Rectangle player, enemy;

	private Array<Rectangle> raindrops, terrain;

	private long lastNpc = 0;
	private long lastHit = 0;
	private boolean touchedEnemy;
	private float lastX = 400;
	private float lastY = 240;
	private long lastTick = 0;
	private long lastMove;
	private long lastUpdate;
	private final int TICKDELAY = 1000000;
	private final int NPCDELAY =  30000000;
	private final int MOVEDELAY = 10000000;
	private final int UPDATEDELAY = 10000000;
	private String Name;
	private int ID;
	private String[] players;
	private Client client;

	HashMap<Integer, User> users = new HashMap();

	public TonposeGame(AndroidMethods androidMethod, String name) {
		this.androidMethod = androidMethod;
		this.Name = name;
	}


	@Override
	public void create() {

		if(!Name.equals("offline")){
			connectToServer();
		}

		// load textures
		playerImage = new Texture(Gdx.files.internal("mainbase.png"));
		enemyImage = new Texture(Gdx.files.internal("player2base.png"));
		treeImage = new Texture(Gdx.files.internal("treeStill.png"));

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

		Map = new Map(1000, 1000, 10, 0);        //TODO retrieve map from server instead of making one here


		//initialize main character
		player = new Rectangle();
		player.width = 64;
		player.height = 64;
		player.x = lastX;
		player.y = lastY;

		enemy = new Rectangle();
		enemy.width = 64;
		enemy.height = 64;
		enemy.x = 500;
		enemy.y = 400;

		//add terrain to map
		terrain = new Array<Rectangle>(); //the array for terrain
		for (Entity entity : Map.getEntities()) {
			terrain.add(new Rectangle(entity.locationX, entity.locationY, entity.width, entity.height));
		}

	}

	@Override
	public void render() {
		// clear screen to dark blue color
		Gdx.gl.glClearColor(0, 0, 0.2f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		camera.update();

		// render player and enemy
		batch.setProjectionMatrix(camera.combined); // tells spriteBatch to use camera coordinate system
		batch.begin();
		batch.draw(playerImage, player.x, player.y);
		batch.draw(enemyImage, enemy.x, enemy.y);
		for (Entity entity : Map.getEntities()) { //draws terrain
			if (entity.id == 1) //checks if it is a standard tree
				batch.draw(treeImage, entity.locationX, entity.locationY);
		}
		if(!Name.equals("offline")) {
			for (User value : users.values()) {
				batch.draw(playerImage, value.x, value.y);
			}
		}

		batch.end();  // submits all drawing requests between begin() and end() at once. Speeds up OpenGL rendering

		// make player move on touch
		if (Gdx.input.isTouched()) {
			Vector3 touchPos = new Vector3();
			touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
			camera.unproject(touchPos); // transforms the coordinates of the vector to the coordinate system of the camera
			lastX = touchPos.x;
			lastY = touchPos.y;
			//player.x= touchPos.x-64;
			//player.y= touchPos.y-64/2;
		}else{
			lastX = player.getX();
			lastY = player.getY();
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

		if (player.overlaps(enemy)) { //if the player is touching the enemy
			touchedEnemy = true;
			Toast("ouch!");
		}
		lastTick = TimeUtils.nanoTime();
	}


	public void movePlayer() {
		float x = lastX - player.getX();
		float y = lastY - player.getY();
		float sum = abs(x) + abs(y);

		if (sum > 3) {
			float xMove = 5 * (x/sum);
			float yMove = 5 * (y/sum);

			if(player.x+xMove<0){
				xMove=-player.getX();
				player.x=0;

			}
			else if(player.x+xMove>Map.getWidth()){
				xMove=Map.getWidth()-player.x;
				player.x=Map.getWidth();

			}else {
				player.x += xMove;
			}
			camera.translate(xMove, 0);
			//cameraX+=xMove;

			if(player.y+yMove<0){
				yMove=-player.getY();
				player.y=0;

			}
			else if(player.y+xMove>Map.getHeight()){
				yMove=Map.getHeight()-player.y;
				player.x=Map.getHeight();

			}else {
				player.y += yMove;
			}
			//cameraY+=yMove;
			camera.translate(0, yMove);//keeps camera within the map's bounds
		}

		lastMove = TimeUtils.nanoTime();
	}

	public void Toast(String text) {
		TonposeGame.androidMethod.Toast(text);
	}

	private void moveEnemy() { // called whenever a raindrop spawns
		AI.direct(player, enemy);
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
		if(!Name.equals("offline")) {
			client.sendTCP(move);
		}
		lastUpdate = TimeUtils.nanoTime();
	}


	public void connectToServer() {
		System.out.println("testestewstresdf");
		client = new Client();
		client.start();

		Network.register(client);

		client.addListener(new Listener() {
			public void connected(Connection connection) {
				ClientConnect player_connect = new ClientConnect();
				player_connect.name = Name;
				player_connect.id = 0;
				player_connect.x = lastX;
				player_connect.y = lastY;
				client.sendTCP(player_connect);
			}

			public void received(Connection connection, Object object) {
				if (object instanceof AddUser) {
					AddUser add = (AddUser) object;
					if (add.user.name.equals(Name)) {
						//server will immediately send this back after clientConnect, this is how we get ID
						ID = add.user.id;
					} else {
						//add the user to the hashmap
						users.put(add.user.id, add.user);
					}
				}
				if (object instanceof UpdateUser) {
					UpdateUser update = (UpdateUser) object;
					if (update.id != ID) {    //do not update our own player
						User user = users.get(update.id);
						user.x = update.x;
						user.y = update.y;
						users.put(update.id, user);
					}
				}
				if (object instanceof RemoveUser) {
					RemoveUser remove = (RemoveUser) object;
					//remove the user from the hashmap
					users.remove(remove.id);
				}
			}

			public void disconnected(Connection connection) {
				Toast("Lost Connection to Server");
				client.close();
			}
		});
		new Thread("Connect") {
			public void run () {
				try {
					client.connect(5000, "10.25.70.122", Network.port); //10.25.70.122
				} catch (IOException ex) {
					ex.printStackTrace();
					System.exit(1);
				}
			}
		}.start();
	}

	@Override
	public void dispose() {
		//dispose assets
		playerImage.dispose();
		enemyImage.dispose();
		treeImage.dispose();
		batch.dispose();
	}
}