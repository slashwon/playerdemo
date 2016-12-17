package demo.slash.customplayer.bean;

import android.text.TextUtils;

/**
 * Created by Administrator on 2016/12/10 0010.
 */

public class VideoItem {


    private String displayName;
    private String path;
    private long dateAdded;
    private long duration;
    private long size;
    private long lastPos;
    private boolean isSelected;

    public VideoItem(String displayName, String path, long dateAdded, long duration, long size, long lastPos, boolean isSelected) {
        this.displayName = displayName;
        this.path = path;
        this.dateAdded = dateAdded;
        this.duration = duration;
        this.size = size;
        this.lastPos = lastPos;
        this.isSelected = isSelected;
    }

    public long getLastPos() {
        return lastPos;
    }

    public void setLastPos(long lastPos) {
        this.lastPos = lastPos;
    }

    @Override
    public boolean equals(Object o) {
        if(o instanceof VideoItem){
            VideoItem item = (VideoItem)o;
            return TextUtils.equals(path,item.getPath());
        }
        return false;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    @Override
    public String toString() {
        return "VideoItem{" +
                "displayName='" + displayName + '\'' +
                ", path='" + path + '\'' +
                ", dateAdded=" + dateAdded +
                ", duration=" + duration +
                ", size=" + size +
                ", lastPos=" + lastPos +
                ", isSelected=" + isSelected +
                '}';
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
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
