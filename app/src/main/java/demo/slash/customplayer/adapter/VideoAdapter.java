package demo.slash.customplayer.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import demo.slash.customplayer.R;
import demo.slash.customplayer.bean.VideoItem;
import demo.slash.customplayer.utils.CommonUtils;
import demo.slash.customplayer.utils.Logger;
import demo.slash.customplayer.view.PlayerActivity;

/**
 * Created by Administrator on 2016/12/11 0011.
 */

public class VideoAdapter extends BaseAdapter implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {

    private static final String TAG = "VideoAdapter";
    public static final String EB_MSG_UPDATE_HEADER = "main_update_header";
    private final Context mCtx;
    private final List<VideoItem> mList;
    private final RequestManager mGlide;
    private boolean mShowCB = false;

    public VideoAdapter(final Context ctx, List<VideoItem> list){
        mCtx = ctx;
        mList = list;
        mGlide = Glide.with(ctx);
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
        final ViewHolder holder;
        if(null==convertView){
            holder = new ViewHolder();
            convertView = View.inflate(mCtx, R.layout.video_item_layout,null);

            holder.ivIcon = (ImageView) convertView.findViewById(R.id.iv_icon);
            holder.tvName = (TextView) convertView.findViewById(R.id.tv_name);
            holder.tvDuration = (TextView)convertView.findViewById(R.id.tv_duration);
            holder.cbSelect = (CheckBox)convertView.findViewById(R.id.cb_item);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final VideoItem item = getItem(position);

        holder.tvName.setText(item.getDisplayName());
        holder.tvDuration.setText(CommonUtils.convertTimeLong(item.getDuration()));
        holder.cbSelect.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                item.setSelected(holder.cbSelect.isChecked());
            }
        });

        holder.cbSelect.setVisibility(mShowCB ? View.VISIBLE : View.GONE);
        holder.cbSelect.setChecked(item.isSelected());

        mGlide.load(item.getPath())
                .centerCrop()
                .error(R.mipmap.player_ads_detail_failed)
                .into(holder.ivIcon);

        return convertView;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Log.d(TAG,"on item click");

        Intent intent = new Intent(mCtx, PlayerActivity.class);
        intent.setData(Uri.parse(mList.get(position).getPath()));
        mCtx.startActivity(intent);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        Logger.D(TAG,"item long click");
        updateCheckState(true);
        EventBus.getDefault().post(EB_MSG_UPDATE_HEADER);
        return true;
    }

    private static class ViewHolder{
        public ImageView ivIcon;
        public TextView tvName;
        public TextView tvDuration;
        public CheckBox cbSelect;
    }

    public void updateCheckState(boolean ck){
        mShowCB = ck;
        notifyDataSetChanged();
    }

}
