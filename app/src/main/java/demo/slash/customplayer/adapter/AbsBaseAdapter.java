package demo.slash.customplayer.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by PICO-USER on 2017/1/23.
 */
public abstract class AbsBaseAdapter<T> extends BaseAdapter {

    protected List<T> mList;
    protected int mLayoutId;
    protected Context mContext;

   /* protected AbsBaseAdapter(List<T> list){
        mList = (null == list) ? new ArrayList<T>() : list;
    }*/

    protected AbsBaseAdapter(Context c,int layoutId) {
        mLayoutId =  layoutId;
        mContext = c;
        mList = new ArrayList<>();
    }

    // TODO 更新数据

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public T getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        GlobalHolder holder = null ;
        if (convertView == null) {
            convertView = View.inflate(mContext,mLayoutId,null);
            holder = new GlobalHolder(convertView);
            onBindView(holder);
            convertView.setTag(holder);
        } else {
            holder = (GlobalHolder) convertView.getTag();
        }

        onBindData(holder,getItem(position));

        return convertView;
    }

    protected abstract void onBindData(GlobalHolder holder, T item);

    protected abstract void onBindView(GlobalHolder holder);
}
