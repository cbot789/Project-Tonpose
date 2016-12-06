package cs309.tonpose;


import android.content.Context;
import android.content.Intent;
import android.media.ToneGenerator;
import android.os.Handler;
import android.widget.Toast;

/**
 * Created by Caleb on 10/19/2016.
 */

public class AndroidMethod implements AndroidMethods {
    Handler handler;
    Context context;
    ToneGenerator player = new ToneGenerator(0,100);

    public AndroidMethod(Context context){
        handler=new Handler();                                                                         //allows enqueuing of actions to be performed in a different thread
        this.context=context;

    }
    public void Toast(final String text) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void Tone(final int pitch){
        handler.post(new Runnable() {
            @Override
            public void run() {
                player.startTone(pitch);
            }
        });

    }

    public void menu(){
        handler.post(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(context, MainMenu.class);
                context.startActivity(intent);
            }
        });

    }

}
