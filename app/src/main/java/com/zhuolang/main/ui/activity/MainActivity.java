package com.zhuolang.main.ui.activity;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

import com.zhuolang.main.database.MyDatabaseHelper;
import com.zhuolang.main.ui.fragment.MeTabFragment;
import com.zhuolang.main.R;
import com.zhuolang.main.ui.fragment.ShareTabFragment;
import com.zhuolang.main.utils.SharedPrefsUtil;
import com.zhuolang.main.view.ChangeColorIconWithText;
import com.zhuolang.main.ui.fragment.HomepageTabFragment;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends FragmentActivity implements ViewPager.OnPageChangeListener {

    private ViewPager mViewPager;
    private List<Fragment> mTabs = new ArrayList<Fragment>();
    private long exitTime = 0;
    private FragmentPagerAdapter mAdapter;
    private ChangeColorIconWithText one;
    private ChangeColorIconWithText two;
    private ChangeColorIconWithText three;

    HomepageTabFragment homepageTabFragment = new HomepageTabFragment();
    ShareTabFragment shareTabFragment = new ShareTabFragment();
    MeTabFragment meTabFragment = new MeTabFragment();

    private List<ChangeColorIconWithText> mTabIndicators = new ArrayList<ChangeColorIconWithText>();
    private MyDatabaseHelper dbHelper;
    private String flag="false";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        flag=getIntent().getStringExtra("Flagf");
        Log.d("activityID","这个是----------:"+this.toString());

        dbHelper=new MyDatabaseHelper(this,"LibrarySystem.db",null,1);
//        dbHelper.getWritableDatabase();//创建或打开数据库

        initView();//获取viewpager
        initDatas();//初始化数据
        mViewPager.setAdapter(mAdapter);//用adapter为viewpager赋值
        initEvent();
        one.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetOtherTabs();
                mTabIndicators.get(0).setIconAlpha(1.0f);
                mViewPager.setCurrentItem(0, false);
            }
        });
        two.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetOtherTabs();
                mTabIndicators.get(1).setIconAlpha(1.0f);
                mViewPager.setCurrentItem(1, false);
            }
        });
        three.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetOtherTabs();
                mTabIndicators.get(2).setIconAlpha(1.0f);
                mViewPager.setCurrentItem(2, false);
            }
        });
        //自动点击
        if (flag.equals("true")){
            two.performClick();
        }
    }

    /**
     * 初始化所有事件
     */
    private void initEvent() {
        mViewPager.setOnPageChangeListener(this);

    }
    //为fragment传输数据
    private void initDatas() {

        mTabs.add(homepageTabFragment);
        mTabs.add(shareTabFragment);
        mTabs.add(meTabFragment);


        mAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {

            @Override
            public int getCount() {
                return mTabs.size();
            }

            @Override
            public Fragment getItem(int position) {
                return mTabs.get(position);
            }
        };
    }

    /*
     *初始化数据和点击事件
     */
    private void initView(){
        mViewPager = (ViewPager) findViewById(R.id.id_viewpager);

        one = (ChangeColorIconWithText) findViewById(R.id.id_indicator_one);
        mTabIndicators.add(one);
        two = (ChangeColorIconWithText) findViewById(R.id.id_indicator_two);
        mTabIndicators.add(two);
        three = (ChangeColorIconWithText) findViewById(R.id.id_indicator_three);
        mTabIndicators.add(three);

//        one.setOnClickListener(this);
//        two.setOnClickListener(this);
//        three.setOnClickListener(this);

        one.setIconAlpha(1.0f);

    }

//    @Override
//    public void onClick(View v) {
//        clickTab(v);
//
//    }

    /**
     * 点击Tab按钮，重置其他页面tab的透明度，并设置该页面透明度为1
     *
     * @param v
     */
//    private void clickTab(View v){
//        resetOtherTabs();
//
//        switch (v.getId()){
//            case R.id.id_indicator_one:
//                mTabIndicators.get(0).setIconAlpha(1.0f);
//                mViewPager.setCurrentItem(0, false);
//                break;
//            case R.id.id_indicator_two:
//                mTabIndicators.get(1).setIconAlpha(1.0f);
//                mViewPager.setCurrentItem(1, false);
//                break;
//            case R.id.id_indicator_three:
//                mTabIndicators.get(2).setIconAlpha(1.0f);
//                mViewPager.setCurrentItem(2, false);
//                break;
//        }
//    }

    /**
     * 重置其他的TabIndicator的颜色
     */
    private void resetOtherTabs(){
        for (int i = 0; i < mTabIndicators.size(); i++){
            mTabIndicators.get(i).setIconAlpha(0);
        }
    }

    /*
     *从左到右和从右到左滑动，透明度变化
     */
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        // Log.e("TAG", "position = " + position + " ,positionOffset =  "
        // + positionOffset);
        if (positionOffset > 0) {
            ChangeColorIconWithText left = mTabIndicators.get(position);
            ChangeColorIconWithText right = mTabIndicators.get(position + 1);
            left.setIconAlpha(1 - positionOffset);
            right.setIconAlpha(positionOffset);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                Toast.makeText(getApplicationContext(), "再按一次退出", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}

