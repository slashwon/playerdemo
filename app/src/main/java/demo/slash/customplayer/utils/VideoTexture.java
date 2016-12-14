package demo.slash.customplayer.utils;

import android.graphics.SurfaceTexture;
import android.opengl.GLES11Ext;
import android.opengl.GLES20;
import android.view.Surface;

import javax.microedition.khronos.opengles.GL10;

/**
 * Created by PICO-USER on 2016/12/14.
 */
public class VideoTexture {

    private SurfaceTexture mSurfaceTexture;
    private Surface mSurface;
    private IOnSurfaceCreated mCallback;

    public VideoTexture(IOnSurfaceCreated callback){
        mCallback = callback;
    }

    public int create(){
        int[] textures = new int[1];
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glGenTextures(1,textures,0);

        GLES20.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES,textures[0]);

        GLES20.glTexParameterf(GLES11Ext.GL_TEXTURE_EXTERNAL_OES,
                GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_LINEAR_MIPMAP_NEAREST);
        GLES20.glTexParameterf(GLES11Ext.GL_TEXTURE_EXTERNAL_OES,
                GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);
        GLES20.glTexParameteri(GLES11Ext.GL_TEXTURE_EXTERNAL_OES,
                GL10.GL_TEXTURE_WRAP_S, GL10.GL_CLAMP_TO_EDGE);
        GLES20.glTexParameteri(GLES11Ext.GL_TEXTURE_EXTERNAL_OES,
                GL10.GL_TEXTURE_WRAP_T, GL10.GL_CLAMP_TO_EDGE);

        return textures[0];
    }

    public void createSurface(int textureId){
        if(null==mSurfaceTexture) {
            mSurfaceTexture = new SurfaceTexture(textureId);
            mSurface = new Surface(mSurfaceTexture);

            if(mCallback!=null){
                mCallback.surfaceCreated(mSurface);
            }
        }
    }

    public void drawView(){
        mSurfaceTexture.updateTexImage();
    }

    public interface IOnSurfaceCreated{
        void surfaceCreated(Surface surface);
    }
}
