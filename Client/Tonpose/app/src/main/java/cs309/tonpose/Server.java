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
    private List<User> banList = new ArrayList<User>();
    private List<User> modList = new ArrayList<User>();
    private List<User> adminList = new ArrayList<User>();
    private int playerCount;
    private Map map;
    private String recieved;
    private boolean sending;

    public Server(String nameInput, String ipInput, int portInput){     //Server object initialize
        name = nameInput;
        ip = ipInput;
        port = portInput;
        sending = false;
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
    public List<User> getBanList(){                                     //returns the list of users banned on this server
        return banList;
    }
    public List<User> getModList(){                                     //returns the list of users with modderator permissions on the server
        return modList;
    }
    public List<User> getAdminList(){                                     //returns the list of users with admin permissions on the server
        return modList;
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
        updatePlayerCount();
        return playerCount;
    }

    private void updatePlayerCount(){
                                                                        //add delay so to update? (update only once every second?)
        sendData("updatePlayerCount");                                  //change string to speed up sending
        receiveData();
    }

    public Boolean receiveData(){
        recieved = null;

        //placeholder checking mysql database
        //recievedData = mysql database info
        if(recieved == null){
            return false;
        }
        sending = false;
        return true;
    }
    public void sendData(String toSend){
        //placeholder to send data to mysql
        sending = true;
    }
}
