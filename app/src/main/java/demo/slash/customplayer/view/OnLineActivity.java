package demo.slash.customplayer.view;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import demo.slash.customplayer.R;
import demo.slash.customplayer.adapter.AbsBaseAdapter;
import demo.slash.customplayer.bean.VideoOnline;
import demo.slash.customplayer.utils.Logger;

public class OnLineActivity extends AppCompatActivity {

    private ListView mLvOnline;
    private ArrayList<VideoOnline> mVideoOnlines;
    private OnlineAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_line);

        mVideoOnlines = new ArrayList<>();
        mLvOnline = (ListView) findViewById(R.id.lv_online);
        mAdapter = new OnlineAdapter(mVideoOnlines);
        mLvOnline.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
        mLvOnline.setOnItemClickListener(mAdapter);

        loadOnline();
    }

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
    }

    class OnlineAdapter extends AbsBaseAdapter<VideoOnline> implements AdapterView.OnItemClickListener {

        protected OnlineAdapter(List list) {
            super(list);
        }

        @Override
        protected void onBindHolderData(IViewHolder holder, VideoOnline item) {
            ((ViewHolder) holder).title.setText(item.getTitle());
            ((ViewHolder) holder).url.setText(item.getUrl());
        }

        @Override
        protected void onGetHolderView(IViewHolder holder, View convertView) {
            holder = new ViewHolder();
            convertView = View.inflate(OnLineActivity.this,R.layout.online_item_layout,null);
            ((ViewHolder) holder).title = (TextView) convertView.findViewById(R.id.tv_title);
            ((ViewHolder) holder).url = (TextView) convertView.findViewById(R.id.tv_title);
        }

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Intent intent = new Intent(OnLineActivity.this, PlayerActivity.class);
            intent.setData(Uri.parse(getItem(position).getUrl()));
            startActivity(intent);
        }
    }

    class ViewHolder implements AbsBaseAdapter.IViewHolder{
        TextView title;
        TextView url;
    }
}
