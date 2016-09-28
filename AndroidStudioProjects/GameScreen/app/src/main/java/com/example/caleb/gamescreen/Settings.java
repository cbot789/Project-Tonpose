package com.example.caleb.gamescreen;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Settings extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);


        Button button = (Button) findViewById(R.id.Back);
        button.setOnClickListener(new View.OnClickListener() {

            @Override

            public void onClick(View v) {

                goToMainMenu();

            }

        });
        Button button2 = (Button) findViewById(R.id.Save);
        button.setOnClickListener(new View.OnClickListener() {

            @Override

            public void onClick(View v) {

                goToMainMenu();

            }

        });
    }


    private void goToMainMenu() { //Returns to the main menu

        Intent intent = new Intent(this, MainActivity.class);

        startActivity(intent);

    }

}
