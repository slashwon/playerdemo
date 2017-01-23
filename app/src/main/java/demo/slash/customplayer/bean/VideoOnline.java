package demo.slash.customplayer.bean;

/**
 * Created by PICO-USER on 2017/1/23.
 */
public class VideoOnline {
    private String title;
    private String url;
    private String protocal;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getProtocal() {
        return protocal;
    }

    public void setProtocal(String protocal) {
        this.protocal = protocal;
    }

    public VideoOnline() {

    }

    public VideoOnline(String title, String url, String protocal) {

        this.title = title;
        this.url = url;
        this.protocal = protocal;
    }
}
