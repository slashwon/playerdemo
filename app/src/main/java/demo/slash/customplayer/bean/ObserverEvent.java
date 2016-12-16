package demo.slash.customplayer.bean;

/**
 * Created by PICO-USER on 2016/12/16.
 */
public class ObserverEvent {

    public enum TYPE{
        CREATE,DELETE
    }

    private TYPE mTYPE;
    private String path;

    public ObserverEvent(TYPE TYPE, String path) {
        mTYPE = TYPE;
        this.path = path;
    }

    public TYPE getTYPE() {
        return mTYPE;
    }

    public void setTYPE(TYPE TYPE) {
        mTYPE = TYPE;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
