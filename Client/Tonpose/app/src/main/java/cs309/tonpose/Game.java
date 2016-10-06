package cs309.tonpose;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Game extends AppCompatActivity implements View.OnTouchListener {
    private int xDelta;
    private int yDelta;
    private FrameLayout screen;
    private ClientUpdateTask updateRequest = null;
    private int timeoutCounter = 0;
    List<User> userList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        Music.startSong(this, Music.Song.action, true);
        //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        Button Exit = (Button) findViewById(R.id.ExitGame);                                       //button to exit game
        ImageView j = (ImageView)findViewById(R.id.Player);
        //screen = (FrameLayout)findViewById(R.id.activity_game);
        //screen.setOnClickListener(this);
        j.setOnTouchListener(this);
        Exit.setOnClickListener(new View.OnClickListener() {                              //this button goes to the server select screen
            @Override
            public void onClick(View v) {
                goToMainMenu();
            }
        });
    }
    public boolean onTouch(View view, MotionEvent event) {//this function moves the image along with the touch event //TODO add constant rather than instantaneous motion
        final int X = (int) event.getRawX();
        final int Y = (int) event.getRawY();
        ImageView j = (ImageView)findViewById(R.id.Player);
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                RelativeLayout.LayoutParams lParams = (RelativeLayout.LayoutParams) view.getLayoutParams();
                xDelta = (int) (X - j.getTranslationX());
                yDelta = (int) (Y - j.getTranslationY());
                break;
            case MotionEvent.ACTION_UP:

                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                break;
            case MotionEvent.ACTION_POINTER_UP:
                break;
            case MotionEvent.ACTION_MOVE:
                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) view.getLayoutParams();

                j.setTranslationX(X - xDelta);
                j.setTranslationY(Y - yDelta);
                break;
        }

        return true;
    }
    private void goToMainMenu() { //Returns to the main menu
        Music.playSFX(this, Music.SFX.pop);
        Intent intent = new Intent(this, MainMenu.class);
        startActivity(intent);
    }

    private void getUpdates(){                                                                      //starts async thread TODO implement on delay to game
        if(updateRequest != null){
            return;
        }
        updateRequest = new ClientUpdateTask("email", "X Y");
        updateRequest.execute((Void) null);
    }

    private void updateMap(String updates){                                                         //TODO move to new thread
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
                if(name.equals(user.getName())){
                    user.setLocationX(x);
                    user.setLocationY(y);
                    found = true;
                    break;
                }
            }
            if(!found){
                userList.add(new User(name, x, y));
            }
        }
                                                                                                  //TODO display other chars on screen
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
            if(success && response.getData1().equals("true")){
                updateMap(response.getData2());                                                     //if there are map updates
                timeoutCounter = 0;
            }else if(!success){
                timeoutCounter++;                                                                   //TODO see if connection timedout
            }else{
                timeoutCounter = 0;
            }
        }

        @Override
        protected void onCancelled() {
            updateRequest = null;
        }
    }

}

