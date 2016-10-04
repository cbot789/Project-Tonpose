package cs309.tonpose;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Quade Spellman on 9/15/2016.
 */
public class User {//TODO delete?
    private String userName;                                    //user's login name
    private String password = null;                                    //user's login password
    private String key;                                         //key used for encryption
    private List<Server> serverList = new ArrayList<Server>();       //stores recently played servers
    private int maxListSize = 3;                                    //max number of recently played servers stored

    public User(String name, String pass){                      //initialize user without key
            password = pass;
            userName = name;
            key = Encryption.createKey();
    }
    public User(String name, String pass, String oldKey){       //initialize user with key
        password = pass;
        userName = name;
        key = oldKey;
    }
    public void updateServerList(Server newServer){             //adds lastest server to list after checking for duplicates, deletes oldest if list is longer than maxSize
        for (Server server: serverList) {
            if(newServer.compare(server)){
                return;
            }
        }
        if(serverList.size() >= maxListSize){
            serverList.remove(0);
        }
        serverList.add(newServer);

    }
    public void setServerList(List<Server> list){
        serverList = list;
    }
    public String getUser(){                            //returns userName
        return userName;
    }
    public String getPassword(){                        //returns password
        return password;
    }
    public String getKey(){                             //returns key
        return key;
    }
    public List getLastServer(){                        //returns 'maxSize' number of recently played servers
        return serverList;
    }
}
