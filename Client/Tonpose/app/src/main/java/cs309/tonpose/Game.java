package cs309.tonpose;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class Game extends AppCompatActivity implements View.OnTouchListener {
    private int xDelta;
    private int yDelta;
    private FrameLayout screen;
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

}

