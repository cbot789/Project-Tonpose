package com.example.caleb.gamescreen;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainMenu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

    Button buttonSettings = (Button) findViewById(R.id.settings); //this button goes to the settings screen
    buttonSettings.setOnClickListener(new View.OnClickListener() {

        @Override

        public void onClick(View v) {

            goToSecondActivity();

        }

    });
    Button buttonLogOut = (Button) findViewById(R.id.logout);
    buttonLogOut.setOnClickListener(new View.OnClickListener() {

        @Override

        public void onClick(View v) {
            goToMainMenu();


        }

    });
    Button buttonPlay = (Button) findViewById(R.id.play);
    buttonPlay.setOnClickListener(new View.OnClickListener() {

        @Override

        public void onClick(View v) {
            goToServerSelect();


        }

    });

    private void goToSecondActivity() {

        Intent intent = new Intent(this, Settings.class);

        startActivity(intent);

    }

    private void goToServerSelect() {

    Intent intent = new Intent(this, ServerSelect.class);

    startActivity(intent);

}
    private void goToMainMenu() {

        Intent intent = new Intent(this, MainActivity.class);

        startActivity(intent);

    }
}
