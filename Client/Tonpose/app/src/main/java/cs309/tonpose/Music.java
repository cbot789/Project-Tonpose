package cs309.tonpose;

import android.content.Context;
import android.media.MediaPlayer;

import cs309.tonpose.R;
/*
    main music by Dasgoat freesound.org
    pop by PearceL
 */

/**
 * Created by Quade Spellman on 10/5/2016.
 */

public class Music {
    public enum Song {
        main, nothing
    }
    public enum SFX {
        pop
    }

    private static float sfxVolume = 1;
    private static float musicVolume = 1;
    private static boolean sfxOn = true;
    private static boolean musicOn = true;
    private static MediaPlayer sfxPlayer = new MediaPlayer();
    private static Song currentSong = Song.nothing;
    private static MediaPlayer musicPlayer = new MediaPlayer();

    public static void startSong(Context context, Song newSong, Boolean loop){
        if(musicOn){
            musicPlayer.setVolume(musicVolume, musicVolume);
            switch (newSong) {
                case main:
                    if(currentSong == Song.main){
                        if(!musicPlayer.isPlaying()){
                            musicPlayer.reset();
                            musicPlayer.start();
                        }
                    }
                    else{
                        musicPlayer = MediaPlayer.create(context, R.raw.mainmusic);
                        currentSong = Song.main;
                        musicPlayer.start();
                    }
                    break;
                default:
                    musicPlayer = MediaPlayer.create(context, R.raw.mainmusic);
                    musicPlayer.start();
            }
            musicPlayer.setLooping(loop);
        }
    }
    public static void endSong(){
        musicPlayer.pause();
        currentSong = Song.nothing;
    }
    public static Song getCurrentSong(){
        return currentSong;
    }
    public static void playSFX(Context context, SFX sfx){
        if(sfxOn) {
            sfxPlayer.setVolume(sfxVolume, sfxVolume);
            switch (sfx) {
                case pop:
                    sfxPlayer = MediaPlayer.create(context, R.raw.pop);
                    sfxPlayer.start();
                    break;
                default:
                    sfxPlayer = MediaPlayer.create(context, R.raw.pop);
                    sfxPlayer.start();
            }
        }
    }
    public static int getSfxVolume(){
        return (int)(sfxVolume * 100);
    }
    public static int getMusicVolume(){
        return (int)(musicVolume * 100);
    }
    public static void setSfxVolume(int volume){
        sfxVolume = (float)(volume)/100;
        sfxPlayer.setVolume(sfxVolume, sfxVolume);
    }
    public static void setMusicVolume(int volume){
        musicVolume = (float)(volume)/100;
        musicPlayer.setVolume(musicVolume, musicVolume);
    }
    public static void muteMusic(Boolean mute){
        musicOn = !mute;
        if(!musicOn){
            musicPlayer.stop();
            currentSong = Song.nothing;
        }else{
            musicPlayer.start();;
        }
    }
    public static void muteSFX(Boolean mute){
        sfxOn = !mute;
    }
    public static boolean getSfxOn(){
        return sfxOn;
    }
    public static boolean getMusicOn(){
        return musicOn;
    }
}
