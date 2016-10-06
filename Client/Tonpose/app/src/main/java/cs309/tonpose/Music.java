package cs309.tonpose;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;

import cs309.tonpose.R;
/*
    main music by Dasgoat freesound.org
    pop by PearceL
    action by haydensayshi123
    club by zagi2
    brush by anagar
 */

/**
 * Created by Quade Spellman on 10/5/2016.
 */

public class Music {                                                                //TODO read from file
    public enum Song {
        main, action, brush, nothing, boss
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

    public static void startSong(Context context, Song newSong, Boolean loop){              //starts song based on Song enum, loops if true
        if(musicOn){
            switch (newSong) {
                case main:
                    play(context, R.raw.mainmusic, Song.main);
                    break;
                case action:
                    play(context, R.raw.actionmusic, Song.action);
                    break;
                case boss:
                    play(context, R.raw.bossmusic, Song.boss);

                default:
                    play(context, R.raw.brushmusic, Song.brush);
            }
            musicPlayer.setLooping(loop);
        }
    }
    public static void endSong(){                                                       //stops current song
        musicPlayer.pause();
        currentSong = Song.nothing;
    }
    public static Song getCurrentSong(){                                                //returns current song
        return currentSong;
    }
    public static void playSFX(Context context, SFX sfx){                               //plays a sfx from SFX enum
        if(sfxOn) {
            switch (sfx) {
                case pop:
                    sfxPlayer = MediaPlayer.create(context, R.raw.pop);
                    sfxPlayer.setVolume(sfxVolume, sfxVolume);
                    sfxPlayer.start();
                    break;
                default:
                    sfxPlayer = MediaPlayer.create(context, R.raw.pop);
                    sfxPlayer.setVolume(sfxVolume, sfxVolume);
                    sfxPlayer.start();
            }
        }
    }
    public static int getSfxVolume(){                                                           //returns current sfx volume
        return (int)(sfxVolume * 100);
    }
    public static int getMusicVolume(){                                                         //returns current music volume
        return (int)(musicVolume * 100);
    }
    public static void setSfxVolume(int volume){
        sfxVolume = (float)(volume)/100;
        sfxPlayer.setVolume(sfxVolume, sfxVolume);
    }
    public static void setMusicVolume(int volume){                                              //sets music volume
        musicVolume = (float)(volume)/100;
        musicPlayer.setVolume(musicVolume, musicVolume);
    }
    public static void muteMusic(Boolean mute){                                                     //mutes music
        musicOn = !mute;
        if(!musicOn){
            musicPlayer.stop();
            currentSong = Song.nothing;
        }else{
            musicPlayer.start();;
        }
    }
    public static void muteSFX(Boolean mute){                                                   //mutes sfx
        sfxOn = !mute;
    }
    public static boolean getSfxOn(){                                                           //returns whether sfx are on
        return sfxOn;
    }
    public static boolean getMusicOn(){                                                         //returns whether music is on
        return musicOn;
    }
    private static void play(Context context, int file, Song song){                               //helper method to play or restart chosen song
        if(currentSong == song){
            if(!musicPlayer.isPlaying()){
                musicPlayer.reset();

            }
        }
        else{
            musicPlayer.stop();
            musicPlayer.release();
            musicPlayer = MediaPlayer.create(context, file);
            currentSong = song;
        }
        musicPlayer.setVolume(musicVolume, musicVolume);
        musicPlayer.start();
    }
    public static boolean isMusicPlaying(){
        return musicPlayer.isPlaying();
    }
}
