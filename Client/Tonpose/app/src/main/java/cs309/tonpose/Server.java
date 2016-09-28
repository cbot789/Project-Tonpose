package cs309.tonpose;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Quade Spellman on 9/19/2016.
 */
public class Server {
    private String name;
    private String ip;
    private int port;
    private int playerCount;
    private Map map;

    public Server(String nameInput, String ipInput, int portInput){     //Server object initialize
        name = nameInput;
        ip = ipInput;
        port = portInput;
    }
    public String getIp(){                                              //return server's ip address
        return ip;
    }
    public String getName(){                                              //returns server's name
        return name;
    }
    public int getPort(){                                              //returns server's port number
        return port;
    }
    public boolean compare(Server other){                               //compares the ip and port number of two servers. if they are the same, returns true
        if(other.getIp().equals(ip)){
            if(other.getPort() == port){
                return true;
            }
        }
        return false;
    }
    public int getPlayerCount(){                                        //returns players currently on the server
        updatePlayerCount();                                            //add delay so to update? (update only once every second?)
        return playerCount;
    }

    private void updatePlayerCount(){
        sendData("updatePlayerCount");                                  //change string to speed up sending
        String data = receiveData();
        playerCount = Integer.parseInt(data);
    }

    public String receiveData(){
        //placeholder to recive data from server
        return null;
    }
    public void sendData(String toSend){
        //placeholder to send data to mysql
    }
}
