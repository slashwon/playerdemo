package demo.slash.customplayer.view;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.view.Surface;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import demo.slash.customplayer.player.MediaPlayerWrapper;
import demo.slash.customplayer.utils.Logger;
import demo.slash.customplayer.utils.VideoTexture;
import tv.danmaku.ijk.media.player.IMediaPlayer;

/**
 * Created by PICO-USER on 2016/12/14.
 */
public class VideoGlView extends GLSurfaceView implements GLSurfaceView.Renderer {

    private String mPath;
    private MediaPlayerWrapper mPlayer;
    private VideoTexture mVideoTexture;

    public VideoGlView(Context context) {
        this(context,null);
    }

    public VideoGlView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        setEGLContextClientVersion(2);
        setRenderer(this);
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        GLES20.glClearColor(0,0,0,0);
        GLES20.glEnable(GLES20.GL_CULL_FACE);
        GLES20.glEnable(GLES20.GL_DEPTH_TEST);

        initProgram();
        initTexture();

    }

    private void initProgram() {

    }

    private void initTexture() {
        mVideoTexture = new VideoTexture(new VideoTexture.IOnSurfaceCreated() {
            @Override
            public void surfaceCreated(Surface surface) {
                mPlayer.setSurface(surface);
            }
        });

        int textureId = mVideoTexture.create();
        mVideoTexture.createSurface(textureId);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        GLES20.glViewport(0,0,width,height);

    }

    @Override
    public void onDrawFrame(GL10 gl) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
        GLES20.glEnable(GLES20.GL_CULL_FACE);
        GLES20.glEnable(GLES20.GL_DEPTH_TEST);

        mVideoTexture.drawView();
    }

    public void initPlayer(){
        if(null==mPlayer) {
            mPlayer = new MediaPlayerWrapper(null);
        }
    }

    public void setPath(String path){
        mPath = path;
    }

    public void playVideo(String path){
        mPlayer.openFile(path);
    }

    public void startVideo(){
    }

    public void stopVideo(){
        mPlayer.stop();
    }

    public void pauseVideo(){
        mPlayer.pause();
    }

    public void resumeVideo(){
        mPlayer.start();
    }

    public void fastMove(float d){
        if(mPlayer!=null){
            mPlayer.fastMove(d);
        }
    }

    public void fastRateMove(float rate){
        mPlayer.fastRateMove(rate);
    }

    public void onPrepared(IMediaPlayer mp) {
        mp.start();
        Logger.D(MainActivity.TAG,"player is prepared");
    }
}
