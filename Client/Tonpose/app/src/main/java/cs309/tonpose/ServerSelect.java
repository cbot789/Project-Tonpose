package cs309.tonpose;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import cs309.tonpose.user.R;

public class ServerSelect extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_server_select);
        Button buttonServerConnect = (Button) findViewById(R.id.Connect); //
        buttonServerConnect.setOnClickListener(new View.OnClickListener() {

            @Override

            public void onClick(View v) {



            }

        });
        Button buttonQuickPlay = (Button) findViewById(R.id.QuickPlay); //
        buttonQuickPlay.setOnClickListener(new View.OnClickListener() {

            @Override

            public void onClick(View v) {



            }

        });
        Button buttonBack = (Button) findViewById(R.id.Back); //this button goes to the Main Menu screen
        buttonBack.setOnClickListener(new View.OnClickListener() {

            @Override

            public void onClick(View v) {
                goToMain();


            }

        });

    }
    private void goToMain() {

        Intent intent = new Intent(this, MainMenu.class);

        startActivity(intent);

    }


}
