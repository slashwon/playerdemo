package demo.slash.customplayer.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import demo.slash.customplayer.PlayerActivity;
import demo.slash.customplayer.R;
import demo.slash.customplayer.bean.VideoItem;
import demo.slash.customplayer.player.ThumbnailGener;
import demo.slash.customplayer.utils.StringUtils;

/**
 * Created by Administrator on 2016/12/11 0011.
 */

public class VideoAdapter extends BaseAdapter implements AdapterView.OnItemClickListener {

    private static final String TAG = "VideoAdapter";
    private final Context mCtx;
    private final List<VideoItem> mList;
    private final ThumbnailGener mThumbnailGener;

    public VideoAdapter(final Context ctx, List<VideoItem> list){
        mCtx = ctx;
        mList = list;
        mThumbnailGener = new ThumbnailGener(null);
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public VideoItem getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(null==convertView){
            holder = new ViewHolder();
            convertView = View.inflate(mCtx, R.layout.video_item_layout,null);

            holder.ivIcon = (ImageView) convertView.findViewById(R.id.iv_icon);
            holder.tvName = (TextView) convertView.findViewById(R.id.tv_name);
            holder.tvDuration = (TextView)convertView.findViewById(R.id.tv_duration);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.tvName.setText((getItem(position)).getDisplayName());
        holder.tvDuration.setText(StringUtils.convertTimeLong(getItem(position).getDuration()));
        mThumbnailGener.showThumbnail(holder.ivIcon,getItem(position).getPath());

        return convertView;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Log.d(TAG,"on item click");

        Intent intent = new Intent(mCtx, PlayerActivity.class);
        intent.setData(Uri.parse(mList.get(position).getPath()));
        mCtx.startActivity(intent);
    }

    private static class ViewHolder{
        public ImageView ivIcon;
        public TextView tvName;
        public TextView tvDuration;
    }
}
