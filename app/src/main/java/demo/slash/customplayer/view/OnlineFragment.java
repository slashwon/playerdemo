package demo.slash.customplayer.view;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import demo.slash.customplayer.R;
import demo.slash.customplayer.adapter.AbsBaseAdapter;
import demo.slash.customplayer.adapter.GlobalHolder;
import demo.slash.customplayer.bean.VideoOnline;

public class OnlineFragment extends Fragment {
    private ListView mLvOnline;
    private OnlineAdapter mAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_on_line,null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mLvOnline = (ListView) getView().findViewById(R.id.lv_online);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mAdapter = new OnlineAdapter(getContext());
        mLvOnline.setAdapter(mAdapter);
        mLvOnline.setOnItemClickListener(mAdapter);
    }

    /*
//    code for testing online
    private void loadOnline() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String url = "http://172.31.83.6:8080/videos";
                ByteArrayOutputStream baos = null;
                try {
                    HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
                    conn.setRequestMethod("GET");
                    conn.setConnectTimeout(8000);
                    conn.setReadTimeout(8000);
                    conn.connect();
                    int code = conn.getResponseCode();
                    if(code>=200 && code<300){
                        InputStream ins = conn.getInputStream();
                        baos = new ByteArrayOutputStream();
                        byte buf[] = new byte[2014];
                        int len;
                        while ((len=ins.read(buf))!=-1){
                            baos.write(buf,0,len);
                        }

                        String json = new String(baos.toByteArray());
                        Logger.D("Online : ","json = \n"+json);

                    } else {
                        Logger.E("Online : " ,"connection failed");
                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }*/

    class OnlineAdapter extends AbsBaseAdapter<VideoOnline> implements AdapterView.OnItemClickListener {
        public OnlineAdapter(Context c){
            super(c,R.layout.online_item_layout);
            loadOnlineAddresses();
        }

        private void loadOnlineAddresses() {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    // fake dates
                    mList = new ArrayList<>();
                    mList.add(new VideoOnline("本地服务器测试","http://172.31.83.5/video/outman.mp4"));
                    mList.add(new VideoOnline("香港卫视","rtmp://live.hkstv.hk.lxdns.com/live/hks"));
                    mList.add(new VideoOnline("珠海","rtsp://218.204.223.237:554/live/1/66251FC11353191F/e7ooqwcfbqjoo80j.sdp"));
                    mList.add(new VideoOnline("大熊兔（点播）  ","rtsp://184.72.239.149/vod/mp4://BigBuckBunny_175k.mov"));
                    mList.add(new VideoOnline("香港卫视2","http://live.hkstv.hk.lxdns.com/live/hks/playlist.m3u8"));
                    mList.add(new VideoOnline("CCTV1高清","http://ivi.bupt.edu.cn/hls/cctv1hd.m3u8"));
                    mList.add(new VideoOnline("CCTV3高清","http://ivi.bupt.edu.cn/hls/cctv3hd.m3u8"));
                    mList.add(new VideoOnline("CCTV5高清","http://ivi.bupt.edu.cn/hls/cctv5hd.m3u8"));
                    mList.add(new VideoOnline("CCTV5+高清","http://ivi.bupt.edu.cn/hls/cctv5phd.m3u8"));
                    mList.add(new VideoOnline("CCTV6高清","http://ivi.bupt.edu.cn/hls/cctv6hd.m3u8"));

                    notifyDataSetChanged();
                }
            }).start();
        }

        @Override
        protected void onBindData(GlobalHolder holder, VideoOnline item) {
            ((TextView)holder.get(R.id.tv_title)).setText(item.title);
            ((TextView)holder.get(R.id.tv_url)).setText(item.url);
        }

        @Override
        protected void onBindView(GlobalHolder holder) {
            holder.save(R.id.tv_title);
            holder.save(R.id.tv_url);
        }

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Intent intent = new Intent(getContext(), PlayerActivity.class);
            intent.setData(Uri.parse(getItem(position).url));
            startActivity(intent);
        }
    }
}
