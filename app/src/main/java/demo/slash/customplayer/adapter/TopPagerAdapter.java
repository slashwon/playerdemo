package demo.slash.customplayer.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

import demo.slash.customplayer.R;

/**
 * 首页
 * Created by PICO-USER on 2017/1/23.
 */
public class TopPagerAdapter extends FragmentPagerAdapter {

    private List<Fragment> mFrags;
    private Context mContext;

    public TopPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    public TopPagerAdapter(Context context,FragmentManager fm, List<Fragment> frags){
        this(fm);
        mFrags = (null==frags)? new ArrayList<Fragment>() : frags;
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        return mFrags.get(position);
    }


    @Override
    public int getCount() {
        return mFrags.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        String titles[] = {
                mContext.getString(R.string.pager_tab_1),
                mContext.getString(R.string.pager_tab_2)
        };
        return titles[position];
    }
}
