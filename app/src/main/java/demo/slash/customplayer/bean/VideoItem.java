package demo.slash.customplayer.bean;

/**
 * Created by Administrator on 2016/12/10 0010.
 */

public class VideoItem {

    private String displayName;
    private String path;
    private long dateAdded;
    private long duration;

    public VideoItem(String displayName, String path, long dateAdded, long duration) {
        this.displayName = displayName;
        this.path = path;
        this.dateAdded = dateAdded;
        this.duration = duration;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public long getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(long dateAdded) {
        this.dateAdded = dateAdded;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    @Override
    public String toString() {
        return "VideoItem{" +
                "displayName='" + displayName + '\'' +
                ", path='" + path + '\'' +
                ", dateAdded=" + dateAdded +
                ", duration=" + duration +
                '}';
    }
}
