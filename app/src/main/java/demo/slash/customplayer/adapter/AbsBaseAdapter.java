package demo.slash.customplayer.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by PICO-USER on 2017/1/23.
 */
public abstract class AbsBaseAdapter<T> extends BaseAdapter {

    private final List<T> mList;

    protected AbsBaseAdapter(List<T> list){
        mList = (null == list) ? new ArrayList<T>() : list;
    }

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
        IViewHolder holder = null;
        if(convertView == null){
            onGetHolderView(holder,convertView);
            convertView.setTag(holder);
        } else {
            holder = (IViewHolder) convertView.getTag();
        }

        T item = getItem(position);
        onBindHolderData(holder,item);
        return convertView;
    }

    protected abstract void onBindHolderData(IViewHolder holder, T item);

    protected abstract void onGetHolderView(IViewHolder holder, View convertView);

    public interface IViewHolder{

    }
}
