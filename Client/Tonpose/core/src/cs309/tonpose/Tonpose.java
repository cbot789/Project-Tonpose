package cs309.tonpose;

import com.badlogic.gdx.Game;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

import java.io.IOException;
import java.util.HashMap;

/**
 * Created by Luke on 11/1/16.
 */
public class Tonpose extends Game{

    static AndroidMethods androidMethod;
    protected String Name;
    protected TonposeScreen tonposeScreen;
    protected PlayersScreen playersScreen;
    protected InventoryScreen inventoryScreen;
    protected DeathScreen deathScreen;
    protected Client client;
    protected int ID;
    protected float lastX = 400;
    protected float lastY = 240;
    protected HashMap<Integer, User> users = new HashMap();


    public Tonpose(AndroidMethods androidMethod, String name) {
        this.androidMethod = androidMethod;
        this.Name = name;

    }
    @Override
    public void create(){
        if(!Name.equals("offline")){
            connectToServer();
        }
        deathScreen = new DeathScreen(this);
        tonposeScreen = new TonposeScreen(this);
        playersScreen = new PlayersScreen(this);
        inventoryScreen = new InventoryScreen(this);
        setScreen(tonposeScreen);
    }


    public void Toast(String text) {
        androidMethod.Toast(text);
    }

    public void menu(){
        client.close();
        androidMethod.menu();
    }

    public void connectToServer() {
        client = new Client();
        client.start();

        Network.register(client);

        client.addListener(new Listener() {
            public void connected(Connection connection) {
                Network.ClientConnect player_connect = new Network.ClientConnect();
                player_connect.name = Name;
                player_connect.id = 0;
                player_connect.x = lastX;
                player_connect.y = lastY;
                client.sendTCP(player_connect);
            }

            public void received(Connection connection, Object object) {
                if (object instanceof Network.AddUser) {
                    Network.AddUser add = (Network.AddUser) object;
                    if (add.user.name.equals(Name)) {
                        //server will immediately send this back after clientConnect, this is how we get ID
                        ID = add.user.id;
                    } else {
                        //add the user to the hashmap
                        users.put(add.user.id, add.user);
                    }
                }
                if (object instanceof Network.UpdateUser) {
                    Network.UpdateUser update = (Network.UpdateUser) object;
                    if (update.id != ID) {    //do not update our own player
                        User user = users.get(update.id);
                        user.x = update.x;
                        user.y = update.y;
                        users.put(update.id, user);
                    }
                }
                if (object instanceof Network.RemoveUser) {
                    Network.RemoveUser remove = (Network.RemoveUser) object;
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
        client.close();
    }
}
