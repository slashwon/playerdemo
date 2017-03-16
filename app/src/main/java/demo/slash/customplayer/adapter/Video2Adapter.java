package demo.slash.customplayer.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import demo.slash.customplayer.R;
import demo.slash.customplayer.bean.VideoItem;
import demo.slash.customplayer.data.MediaQueryer;
import demo.slash.customplayer.utils.CommonUtils;
import demo.slash.customplayer.utils.Logger;
import demo.slash.customplayer.view.LocalVideos;
import demo.slash.customplayer.view.PlayerActivity;

/**
 * Created by PICO-USER on 2017/3/15.
 */
public class Video2Adapter extends AbsBaseAdapter<VideoItem> implements AdapterView.OnItemClickListener
    ,AdapterView.OnItemLongClickListener{

    public static final String EB_MSG_UPDATE_HEADER = "main_update_header";
    private static final String TAG = "Video2Adapter";
    private boolean mShowCB = false;

    public Video2Adapter(Context c, int layoutId) {
        super(c, layoutId);
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onBindData(final GlobalHolder holder, final VideoItem item) {
        ((TextView)holder.get(R.id.tv_name)).setText(item.getDisplayName());
        ((TextView)holder.get(R.id.tv_duration)).setText(CommonUtils.convertSize(item.getSize()));
        final CheckBox cb = (CheckBox) holder.get(R.id.cb_item);
        (cb).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                item.setSelected((cb).isChecked());
            }
        });

        cb.setVisibility(mShowCB ? View.VISIBLE : View.GONE);
        cb.setChecked(item.isSelected());

        Glide.with(mContext).load(item.getPath())
                .centerCrop()
                .error(R.mipmap.player_ads_detail_failed)
                .into((ImageView)holder.get(R.id.iv_icon));
    }

    @Override
    protected void onBindView(GlobalHolder holder) {
        holder.save(R.id.iv_icon);
        holder.save(R.id.tv_name);
        holder.save(R.id.tv_duration);
        holder.save(R.id.cb_item);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Log.d(TAG,"on item click");

        Intent intent = new Intent(mContext, PlayerActivity.class);
        intent.setData(Uri.parse(mList.get(position).getPath()));
        mContext.startActivity(intent);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        Logger.D(TAG,"item long click");
        updateCheckState(true);
        EventBus.getDefault().post(EB_MSG_UPDATE_HEADER);
        return true;
    }

    public void updateCheckState(boolean ck){
        mShowCB = ck;
        notifyDataSetChanged();
    }

    public void refresh(final Handler handler) {
        MediaQueryer.instance().syncLoadVideos(mContext, false, new MediaQueryer.IOnLoadingDoneListener() {
            @Override
            public void onLoadingDone(List<VideoItem> l) {
                EventBus.getDefault().post(l);
                handler.sendEmptyMessage(LocalVideos.MSG_QUERY_END);
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void dataChanged(Object event){
        if (event instanceof List) {
            List<VideoItem> newL = (List<VideoItem>) event;
            mList.clear();
            mList.addAll(newL);
            notifyDataSetChanged();
        }
    }
}
