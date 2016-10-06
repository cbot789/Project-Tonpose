package cs309.tonpose;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.SeekBar;
import android.widget.TextView;

import cs309.tonpose.R;

import static android.widget.SeekBar.*;

public class Settings extends AppCompatActivity {

    private int musicVolume = Music.getSfxVolume();
    private int sfxVolume;
    private boolean musicOn;
    private boolean sfxOn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        sfxVolume = Music.getSfxVolume();
        musicVolume = Music.getMusicVolume();
        sfxOn = Music.getSfxOn();
        musicOn = Music.getMusicOn();

        Button backButton = (Button) findViewById(R.id.Back);
        backButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Music.playSFX(Settings.this, Music.SFX.pop);
                goToMainMenu();
            }
        });

        Button saveButton = (Button) findViewById(R.id.Save);
        saveButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                saveSettings();
                Music.playSFX(Settings.this, Music.SFX.pop);
                goToMainMenu();
            }
        });

        SeekBar sfxBar = (SeekBar) findViewById(R.id.sfxBar);
        sfxBar.setProgress(sfxVolume);
        final TextView sfxView = (TextView) findViewById(R.id.currentSfxVolume);
        sfxView.setText(sfxVolume + "");
        sfxBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progresValue, boolean fromUser) {
                sfxVolume = progresValue;
                sfxView.setText(sfxVolume + "");
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        SeekBar musicBar = (SeekBar) findViewById(R.id.musicBar);
        musicBar.setProgress(musicVolume);
        final TextView musicView = (TextView) findViewById(R.id.currentMusicVolume);
        musicView.setText(musicVolume + "");
        musicBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progresValue, boolean fromUser) {
                musicVolume = progresValue;
                musicView.setText(musicVolume + "");
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        final CheckBox sfxCheck = (CheckBox) findViewById(R.id.sfxMute);
        if(sfxOn){
            sfxCheck.setChecked(true);
        }else{
            sfxCheck.setChecked(false);
        }
        sfxCheck.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(sfxCheck.isChecked()){
                    sfxOn = true;
                }else{
                    sfxOn = false;
                }
            }
        });

        final CheckBox musicCheck = (CheckBox) findViewById(R.id.musicMute);
        if(musicOn){
            musicCheck.setChecked(true);
        }else{
            musicCheck.setChecked(false);
        }
        musicCheck.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(musicCheck.isChecked()){
                    musicOn = true;
                }else{
                    musicOn = false;
                }
            }
        });
    }

    private void goToMainMenu() { //Returns to the main menu
        Intent intent = new Intent(this, MainMenu.class);
        startActivity(intent);
    }

    private void saveSettings(){
        Music.setMusicVolume(musicVolume);
        Music.setSfxVolume(sfxVolume);
        Music.muteMusic(!musicOn);
        Music.muteSFX(!sfxOn);
    }
}
