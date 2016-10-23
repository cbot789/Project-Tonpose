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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        Button buttonSettings = (Button) findViewById(R.id.settings);                               //this button goes to the settings screen
        Button buttonLogOut = (Button) findViewById(R.id.logout);                                   //button to go back to login screen
        Button buttonPlay = (Button) findViewById(R.id.play);                                       //button to enter game
        Music.startSong(MainMenu.this, Music.Song.main, true);

        buttonSettings.setOnClickListener(new View.OnClickListener() {                              //this button goes to the settings screen
            @Override
            public void onClick(View v) {
                Music.playSFX(MainMenu.this, Music.SFX.pop);
                goToSecondActivity();
            }
        });

        buttonLogOut.setOnClickListener(new View.OnClickListener() {                                //button to go back to login screen
            @Override
            public void onClick(View v) {
                Music.endSong();
                goToLogin();
            }
        });

        buttonPlay.setOnClickListener(new View.OnClickListener() {                                  //button to enter game
            @Override
            public void onClick(View v) {
                Music.playSFX(MainMenu.this, Music.SFX.pop);
                goToGame();
            }
        });
    }

    private void goToSecondActivity() {
        Intent intent = new Intent(this, Settings.class);
        startActivity(intent);
    }

    private void goToGame(){
        Music.playSFX(this, Music.SFX.pop);
        Intent intent = new Intent(this, AndroidLauncher.class);                                    //starts the libGDX game
        startActivity(intent);
    }

    private void goToLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

}