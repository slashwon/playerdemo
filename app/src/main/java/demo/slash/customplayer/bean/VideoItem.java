package demo.slash.customplayer.bean;

/**
 * Created by Administrator on 2016/12/10 0010.
 */

public class VideoItem {


    private String displayName;
    private String path;
    private long dateAdded;
    private long duration;
    private long size;

    @Override
    public String toString() {
        return "VideoItem{" +
                "displayName='" + displayName + '\'' +
                ", path='" + path + '\'' +
                ", dateAdded=" + dateAdded +
                ", duration=" + duration +
                ", size=" + size +
                '}';
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public VideoItem(String displayName, String path, long dateAdded, long duration, long size) {
        this.displayName = displayName;
        this.path = path;
        this.dateAdded = dateAdded;
        this.duration = duration;
        this.size = size ;

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

}
