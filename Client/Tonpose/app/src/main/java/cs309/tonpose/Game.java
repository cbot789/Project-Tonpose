package cs309.tonpose;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class Game extends AppCompatActivity implements View.OnTouchListener {
    private int _xDelta;
    private int _yDelta;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        Button Exit = (Button) findViewById(R.id.ExitGame);                                       //button to enter game
        ImageView j = (ImageView)findViewById(R.id.Player);

        j.setOnTouchListener(this);
        Exit.setOnClickListener(new View.OnClickListener() {                              //this button goes to the server select screen
            @Override
            public void onClick(View v) {
                goToServerSelect();
            }
        });
}
    public boolean onTouch(View view, MotionEvent event) {
        final int X = (int) event.getRawX();
        final int Y = (int) event.getRawY();
        ImageView j = (ImageView)findViewById(R.id.Player);
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                RelativeLayout.LayoutParams lParams = (RelativeLayout.LayoutParams) view.getLayoutParams();
                _xDelta = (int) (X - j.getTranslationX());
                _yDelta = (int) (Y - j.getTranslationY());
                break;
            case MotionEvent.ACTION_UP:
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                break;
            case MotionEvent.ACTION_POINTER_UP:
                break;
            case MotionEvent.ACTION_MOVE:
                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) view.getLayoutParams();

                j.setTranslationX(X - _xDelta);
                j.setTranslationY(Y - _yDelta);
                break;
        }

        return true;
    }
private void goToServerSelect(){
    Intent intent = new Intent(this, ServerSelect.class);
    startActivity(intent);
}

    }

