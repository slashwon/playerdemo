package demo.slash.customplayer.player;

import android.media.MediaPlayer;

/**
 * Created by PICO-USER on 2016/12/12.
 */
public class MediaPlayerWrapper {

    private MediaPlayer mPlayer;

    public MediaPlayerWrapper(){
        if(mPlayer ==null){
            mPlayer = new MediaPlayer();
        }
    }

}
