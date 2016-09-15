package com.example.caleb.mytest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;
import android.widget.Button;
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {

            @Override

            public void onClick(View v) {

                goToSecondActivity();

            }

        });
    }
    private void goToSecondActivity() {

        Intent intent = new Intent(this, secondactivity.class);

        startActivity(intent);

    }


}
