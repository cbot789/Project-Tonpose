package cs309.tonpose;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Quade Spellman on 9/15/2016.
 */
public class User {//TODO delete?
    private String userName;                                    //user's login name
    private String password = null;                                    //user's login password
    private List<Server> serverList = new ArrayList<Server>();       //stores recently played servers
    private int maxListSize = 3;                                    //max number of recently played servers stored

    public User(String name, String pass){                      //initialize user without key
            password = pass;
            userName = name;
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
    public List getLastServer(){                        //returns 'maxSize' number of recently played servers
        return serverList;
    }
}
