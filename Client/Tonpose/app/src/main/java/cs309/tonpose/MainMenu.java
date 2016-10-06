package cs309.tonpose;
/*
    music by user Erokia on wwww.freesound.org
 */

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import cs309.tonpose.R;

public class MainMenu extends AppCompatActivity {

    private MediaPlayer mp = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        if(mp == null){                                                                             //starts playing music
            mp = new MediaPlayer();                                                                 //FIXME music keeps playing after switching pages every other time
            mp = MediaPlayer.create(MainMenu.this, R.raw.mainmusic);                                //TODO make so it continues while in settings
            mp.setLooping(true);
            mp.setVolume(100, 100);
        }
        mp.start();

        Button buttonSettings = (Button) findViewById(R.id.settings);                               //this button goes to the settings screen
        Button buttonLogOut = (Button) findViewById(R.id.logout);                                   //button to go back to login screen
        Button buttonPlay = (Button) findViewById(R.id.play);                                       //button to enter game

        buttonSettings.setOnClickListener(new View.OnClickListener() {                              //this button goes to the settings screen
            @Override
            public void onClick(View v) {
                stopMusic();
                goToSecondActivity();
            }
        });

        buttonLogOut.setOnClickListener(new View.OnClickListener() {                                //button to go back to login screen
            @Override
            public void onClick(View v) {
                stopMusic();
                goToLogin();
            }
        });

        buttonPlay.setOnClickListener(new View.OnClickListener() {                                  //button to enter game
            @Override
            public void onClick(View v) {
                stopMusic();
                goToServerSelect();
            }
        });
    }

    private void goToSecondActivity() {
        Intent intent = new Intent(this, Settings.class);
        startActivity(intent);
    }

    private void goToServerSelect() {
        Intent intent = new Intent(this, ServerSelect.class);
        startActivity(intent);
    }

    private void goToLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private void stopMusic(){                                                                       //stops all music being played
        if(mp != null){                                                                             //FIXME works every other time
            mp.setLooping(false);
            mp.pause();
            mp.stop();
            mp.release();
            mp = null;
        }
    }
}
