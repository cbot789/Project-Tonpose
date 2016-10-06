package cs309.tonpose;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
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
import java.util.List;
import java.util.Scanner;

import static android.R.attr.width;
import static cs309.tonpose.R.attr.height;

public class Game extends AppCompatActivity implements View.OnTouchListener {
    private int xDelta;
    private int yDelta;
    private FrameLayout screen;
    private ClientUpdateTask updateRequest = null;
    private int timeoutCounter = 0;
    List<User> userList = new ArrayList<>();
    Updater mUpdater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        Music.startSong(this, Music.Song.action, true);                                             //plays action song

        //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        //screen = (FrameLayout)findViewById(R.id.activity_game);
        //screen.setOnClickListener(this);

        RelativeLayout rl = (RelativeLayout) findViewById(R.id.activity_game);                              //TODO delete for testing purposes only
        ImageView p2 = new ImageView(this);
        p2.setImageResource(R.drawable.mainbase);
        p2.setLayoutParams(new RelativeLayout.LayoutParams(CoordinatorLayout.LayoutParams.WRAP_CONTENT, CoordinatorLayout.LayoutParams.WRAP_CONTENT));
        p2.setVisibility(View.VISIBLE);
        p2.setX(100);
        p2.setY(500);
        rl.addView(p2);                                                                                 //end delete

        final ImageView playerModel = (ImageView)findViewById(R.id.Player);
        playerModel.setOnTouchListener(this);

        mUpdater = new Updater(new Runnable() {
            @Override
            public void run() {
                if(playerModel.getVisibility() == View.VISIBLE){                                    //TODO delete for testing purposes only
                    playerModel.setVisibility(View.INVISIBLE);
                }else{
                    playerModel.setVisibility(View.VISIBLE);
                }                                                                                   //end delete
                /*timeoutCounter++;                                                              //TODO uncomment and test
                getUpdates();
                if(timeoutCounter > 10){
                    //timeout
                }
                */
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
    public boolean onTouch(View view, MotionEvent event) {//this function moves the image along with the touch event //TODO add constant rather than instantaneous motion
        final int X = (int) event.getRawX();
        final int Y = (int) event.getRawY();
        ImageView playerModel = (ImageView)findViewById(R.id.Player);
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                RelativeLayout.LayoutParams lParams = (RelativeLayout.LayoutParams) view.getLayoutParams();
                xDelta = (int) (X - playerModel.getTranslationX());
                yDelta = (int) (Y - playerModel.getTranslationY());
                break;
            case MotionEvent.ACTION_UP:

                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                break;
            case MotionEvent.ACTION_POINTER_UP:
                break;
            case MotionEvent.ACTION_MOVE:
                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) view.getLayoutParams();
                playerModel.setTranslationX(X - xDelta);
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
        updateRequest = new ClientUpdateTask("email", "X Y");                                       //TODO change to username and picture's x y location;
        updateRequest.execute((Void) null);
    }

    private void updateUserList(String updates){                                                    //given a string gets the name, and (x,y) coordinates and updates userList
        String name;
        int x;
        int y;
        Boolean found;
        Scanner scan = new Scanner(updates);
        while(scan.hasNext()){
            found = false;
            name = scan.next();
            x = scan.nextInt();
            y = scan.nextInt();
            for (User user:userList) {
                if(user.getName().equals(name)){
                    user.setLocationX(x);
                    user.setLocationY(y);
                    found = true;
                    break;
                }
            }
            if(!found){
                userList.add(new User(name, x, y, getApplicationContext()));
            }
        }
       updateMap();
    }

    private void updateMap() {                                                                      //updates the model and location of each player //TODO test me
        ImageView model;
        RelativeLayout rl = (RelativeLayout) findViewById(R.id.activity_game);
        for (User user:userList) {
            model = user.getModel();
            model.setImageResource(R.drawable.mainbase);
            model.setLayoutParams(new RelativeLayout.LayoutParams(CoordinatorLayout.LayoutParams.WRAP_CONTENT, CoordinatorLayout.LayoutParams.WRAP_CONTENT));
            model.setX(user.getLocationX());
            model.setY(user.getLocationY());
            model.setVisibility(View.VISIBLE);
            rl.addView(model);
        }
    }

    public class ClientUpdateTask extends AsyncTask<Void, Void, Boolean> {

        private final String mEmail;
        private final String location;
        private String ip = "10.25.70.122";
        private int port = 8080;
        private Socket socket = null;
        private ObjectOutputStream streamOut = null;
        private ObjectInputStream streamIn = null;
        private Message response;

        ClientUpdateTask(String email, String xy) {
            mEmail = email;
            location = xy;
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
            msg.setData2(location);
            try {
                streamOut.writeObject(msg);
            }
            catch(Exception e){
                e.printStackTrace();
                return false;
            }
            try {
                response = (Message)streamIn.readObject();                                          //get response
            }
            catch(Exception e) {
                e.printStackTrace();
                return false;
            }
            return response.getType().equals("game");
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            updateRequest = null;
            if(success && response.getData1().equals("true")){                                          //if there are map updates
                updateUserList(response.getData2());                                                  //FIXME what thread is this ran on?
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

        private Runnable mStatusChecker;
        private int interval = 500;                                                            //update interval in ms

        public Updater(final Runnable Updater) {
            mStatusChecker = new Runnable() {
                @Override
                public void run() {
                    Updater.run();                                                          // Run the passed runnable

                    mHandler.postDelayed(this, interval);                                       // Re-run it after the update interval
                }
            };
        }

        public synchronized void startUpdates(){                                                //starts updates
            mStatusChecker.run();
        }

        public synchronized void stopUpdates(){                                             //stops updates
            mHandler.removeCallbacks(mStatusChecker);
        }
    }
}

