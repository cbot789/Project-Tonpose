package cs309.tonpose;

import android.support.design.widget.CoordinatorLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Quade Spellman on 9/15/2016.
 */
public class User {//TODO delete?
    private String userName;                                    //user's login name
    private double locationX;
    private double locationY;

    public User(String name, int X, int Y){                      //initialize user without key
        userName = name;
        locationX = X;
        locationY = Y;
    }

    public String getName(){                            //returns userName
        return userName;
    }

    public double getLocationX(){
        return locationX;
    }

    public double getLocationY() {
        return locationY;
    }

    public void setLocationX(double x){
        locationX = x;
    }

    public void setLocationY(double y){
        locationY = y;
    }
    /*                                                                                              //TODO delete
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
    public List getLastServer(){                        //returns 'maxSize' number of recently played servers
        return serverList;
    }*/
}
