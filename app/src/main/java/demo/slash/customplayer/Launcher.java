package demo.slash.customplayer;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import demo.slash.customplayer.adapter.TopPagerAdapter;

public class Launcher extends FragmentActivity {

    private ViewPager mPager;
    private View mTabs;

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
        mTabs = findViewById(R.id.pager_tab);

        initFragments();
        TopPagerAdapter adapter = new TopPagerAdapter(this,getSupportFragmentManager(),null);
        mPager.setAdapter(adapter);
    }

    private void initFragments() {

    }
}
