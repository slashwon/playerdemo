package demo.slash.customplayer;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.util.TypedValue;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import java.lang.reflect.Field;
import java.util.ArrayList;

import demo.slash.customplayer.adapter.TopPagerAdapter;
import demo.slash.customplayer.view.LocalVideos;
import demo.slash.customplayer.view.OnlineVideos;

public class Launcher extends FragmentActivity {

    private ViewPager mPager;
    private PagerTabStrip mTabs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_launcher);

        initViews();
    }

    private void initViews() {
        mPager = (ViewPager) findViewById(R.id.launcher_pager);
        mTabs = (PagerTabStrip) findViewById(R.id.pager_tab);
        initTabs();
        ArrayList<Fragment> fragments = initFragments();
        TopPagerAdapter adapter = new TopPagerAdapter(this,getSupportFragmentManager(),fragments);
        mPager.setAdapter(adapter);
    }

    private void initTabs() {
        try {
            // modify indicator height
            Class<?> pts = Class.forName("android.support.v4.view.PagerTabStrip");
            Field mIndicatorHeight = pts.getDeclaredField("mIndicatorHeight");
            Field indicator_height = pts.getDeclaredField("INDICATOR_HEIGHT");
            indicator_height.setAccessible(true);
            mIndicatorHeight.setAccessible(true);
            final  float density = this.getResources().getDisplayMetrics().density;
            mIndicatorHeight.setInt(mTabs, (int) (indicator_height.getInt(mTabs)*density*1.5f+0.5f));

            // set the tab's text size and the spacing between tabs
            mTabs.setTextSize(TypedValue.COMPLEX_UNIT_SP,20);
            mTabs.setTextSpacing((int) (80*density+0.5f));

            mTabs.invalidate();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private ArrayList<Fragment> initFragments() {
        OnlineVideos online = new OnlineVideos();
        LocalVideos local = new LocalVideos();
        ArrayList<Fragment> fragments = new ArrayList<>();
        fragments.add(online);
        fragments.add(local);
        return fragments;
    }
}
