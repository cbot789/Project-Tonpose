package cs309.tonpose;

import android.content.Context;
import android.widget.ImageView;

/**
 * Created by Quade Spellman on 9/15/2016.
 */
public class User {//TODO delete?
    private String userName;                                    //user's login name
    private float locationX;
    private float locationY;
    ImageView model;

    public User(String name, int X, int Y, Context context){                      //initialize user without key
        userName = name;
        locationX = X;
        locationY = Y;
        model = new ImageView(context);
        model.setImageResource(R.drawable.mainbase);
    }

    public String getName(){                            //returns userName
        return userName;
    }

    public float getLocationX(){
        return locationX;
    }

    public float getLocationY() {
        return locationY;
    }

    public void setLocationX(float x){
        locationX = x;
    }

    public void setLocationY(float y){
        locationY = y;
    }

    public void setModel(int resource){                              //TODO test
        model.setImageResource(resource);
    }

    public ImageView getModel(){                                                    //TODO test
        return model;
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
