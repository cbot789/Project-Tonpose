package cs309.tonpose;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Scanner;

import static android.R.attr.width;
import static cs309.tonpose.R.attr.height;

public class Game extends AppCompatActivity implements View.OnTouchListener {
    private int xDelta, xVelocity,xPos;
    private int yDelta, yVelocity,yPos;
    //private FrameLayout screen;
    private ClientUpdateTask updateRequest = null;
    private int timeoutCounter = 0;
    List<User> userList = new ArrayList<>();
    Updater mUpdater;
    private int interval = 500;                                                            //update interval in ms for pinging server
    private int TIMEOUTVARB = 10;                                                           //TIMEOUTVARB * interval = ms before timeout
    Context gameContext;
    private View fullScreen;
    private RelativeLayout rl;
    private ImageView playerModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        Music.startSong(this, Music.Song.action, true);                                             //plays action song

        fullScreen=(View)findViewById(R.id.activity_game);                                          //This view is the entire screen
        //fullScreen= (RelativeLayout) findViewById(R.id.activity_game);
        //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        //screen = (FrameLayout)findViewById(R.id.activity_game);
        //screen.setOnClickListener(this);

        gameContext = this;

        rl = (RelativeLayout) findViewById(R.id.activity_game);                              //TODO delete for testing purposes only
        final ImageView p2 = new ImageView(this);
        p2.setImageResource(R.drawable.mainbase);
        p2.setLayoutParams(new RelativeLayout.LayoutParams(CoordinatorLayout.LayoutParams.WRAP_CONTENT, CoordinatorLayout.LayoutParams.WRAP_CONTENT));
        p2.setVisibility(View.VISIBLE);
        p2.setX(100);
        p2.setY(500);
        rl.addView(p2);                                                                                 //end delete

        playerModel = (ImageView)findViewById(R.id.Player);
        // playerModel.setOnTouchListener(this);
        fullScreen.setOnTouchListener(this);

        mUpdater = new Updater(new Runnable() {
            @Override
            public void run() {
                if(p2.getVisibility() == View.VISIBLE){                                    //TODO delete for testing purposes only
                    p2.setVisibility(View.INVISIBLE);
                }else{
                    p2.setVisibility(View.VISIBLE);
                }                                                                                   //end delete
                timeoutCounter++;
                getUpdates();
                if(timeoutCounter == TIMEOUTVARB){                                                   //when client times out from server
                    new AlertDialog.Builder(gameContext)
                            .setTitle("ERROR: Server Timeout")
                            .setMessage("Lost connection to server. Return to Menu?")
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    goToMainMenu();
                                }
                            })
                            .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                }
            }
        });
        mUpdater.startUpdates();

        Button Exit = (Button) findViewById(R.id.ExitGame);                                          //this button goes to the main menu
        Exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToMainMenu();
            }
        });
    }

    @Override
    public void onPause(){
        super.onPause();
        Music.pause();
    }
    @Override
    public void onResume(){
        super.onPause();
        Music.resume();
    }
    public boolean onTouch(View view, MotionEvent event) {//this function moves the image according to the touch event //TODO add constant rather than instantaneous motion
        final int X = (int) event.getRawX();
        final int Y = (int) event.getRawY();
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                //RelativeLayout.LayoutParams lParams = (RelativeLayout.LayoutParams) view.getLayoutParams(); //gave classcast exception
                xDelta = (int) (X - playerModel.getTranslationX());
                yDelta = (int) (Y - playerModel.getTranslationY());
                break;
            case MotionEvent.ACTION_UP:
                final int endX=(int)event.getRawX();
                final int endY=(int)event.getRawY();
                //playerModel.setTranslationX(endX-xDelta); //using these 2 lines causes movement upon release of the touch
                //playerModel.setTranslationY(endY-yDelta);
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                break;
            case MotionEvent.ACTION_POINTER_UP:
                break;
            case MotionEvent.ACTION_MOVE:
                // RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) view.getLayoutParams(); //also gave class cast exception
                playerModel.setTranslationX(X - xDelta); //using these 2 lines causes continuos movement during the touch action
                playerModel.setTranslationY(Y - yDelta);
                break;
        }

        return true;
    }
    private void goToMainMenu() {                                                           //Returns to the main menu
        mUpdater.stopUpdates();
        Music.playSFX(this, Music.SFX.pop);
        Intent intent = new Intent(this, MainMenu.class);
        startActivity(intent);
    }

    private void getUpdates(){                                                                      //starts async thread to tell client updated info
        if(updateRequest != null){
            return;
        }
        int x = (int)playerModel.getTranslationX();
        int y = (int)playerModel.getTranslationY();
        updateRequest = new ClientUpdateTask("a@a.aa", x, y);                                       //TODO change to username and picture's x y location;
        updateRequest.execute((Void) null);
    }

    private void updateUserList(String name, int newX, int newY){                                                    //given a string gets the name, and (x,y) coordinates and updates userList
        Boolean found = false;
        for (User user:userList) {
            if(user.getName().equals(name)){
                user.setLocationX(newX);
                user.setLocationY(newY);
                user.display(rl);
                found = true;
                break;
            }
        }
        if(!found){
            userList.add(new User(name, newX, newY, getApplicationContext()));
            userList.get(userList.size()-1).display(rl);
        }
    }

    public class ClientUpdateTask extends AsyncTask<Void, Void, Boolean> {                          //connects to server on new thread, sends player location and looks for other player locations

        private final String mEmail;
        private final int x;
        private final int y;
        private String ip = "10.25.70.122";
        private int port = 8080;
        private Socket socket = null;
        private ObjectOutputStream streamOut = null;
        private ObjectInputStream streamIn = null;
        private Message response;

        ClientUpdateTask(String email, int locationX, int locationY) {
            mEmail = email;
            x = locationX;
            y = locationY;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                socket = new Socket(ip, port);                                                      //try to connect to server
                streamIn = new ObjectInputStream(socket.getInputStream());
                streamOut = new ObjectOutputStream(socket.getOutputStream());
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }

            Message msg = new Message("game");                                                      //send user and current location
            msg.setData1(mEmail);
            msg.setData3(x);
            msg.setData4(y);
            if(msg!=null) {
                try {
                    streamOut.writeObject(msg); //null pointer exception encountered here
                } catch (Exception e) {
                    e.printStackTrace();
                    return false;
                }

                try {
                    response = (Message) streamIn.readObject();                                          //get response
                } catch (Exception e) {
                    e.printStackTrace();
                    return false;
                }
            }
            return response.getType().equals("game");
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            updateRequest = null;
            if(success && response.getData1().equals("true")){                                          //if there are map updates
                updateUserList(response.getData2(), response.getData3(), response.getData4());        //FIXME what thread is this ran on?
                timeoutCounter = 0;
            }
        }

        @Override
        protected void onCancelled() {
            updateRequest = null;
        }
    }

    public class Updater {        // Create a Handler that uses the Main Looper to run in   //TODO test and improve
        private Handler mHandler = new Handler(Looper.getMainLooper());

        private Runnable pingServer;

        public Updater(final Runnable Updater) {
            pingServer = new Runnable() {
                @Override
                public void run() {
                    Updater.run();                                                          // Run the passed runnable

                    mHandler.postDelayed(this, interval);                                       // Re-run it after the update interval
                }
            };
        }

        public synchronized void startUpdates(){                                                //starts updates
            pingServer.run();
        }

        public synchronized void stopUpdates(){                                             //stops updates
            mHandler.removeCallbacks(pingServer);
        }
    }
}

