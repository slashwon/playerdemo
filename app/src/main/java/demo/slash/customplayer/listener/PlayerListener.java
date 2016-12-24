package demo.slash.customplayer.listener;

import tv.danmaku.ijk.media.player.IMediaPlayer;

/**
 * Created by whs on 16-12-24.
 */

public class PlayerListener {

    public IMediaPlayer.OnVideoSizeChangedListener mVideoSizeChangedListener;
    public IMediaPlayer.OnPreparedListener mOnPreparedListener;
    public IMediaPlayer.OnCompletionListener mOnCompletionListener;

    public PlayerListener(){}
}
