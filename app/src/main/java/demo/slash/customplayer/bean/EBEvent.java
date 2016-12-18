package demo.slash.customplayer.bean;

/**
 * Created by Administrator on 2016/12/18 0018.
 */

public interface EBEvent {

    public class BackgroundEvent {
        public static final String EVENT_SYNC_DEL = "event_sync_del";
        public String event;

        public BackgroundEvent(String event) {
            this.event = event;
        }
    }

    public class MainEvent{
        public static final String EVENT_MAIN_DEL_DONE = "delete_done";
        public static final String EVENT_MAIN_DEL_START = "delete_start";
        public String event;

        public MainEvent(String event) {
            this.event = event;
        }
    }

    public class AsyncEvent{

    }

    public class PostingEvent{

    }
}
