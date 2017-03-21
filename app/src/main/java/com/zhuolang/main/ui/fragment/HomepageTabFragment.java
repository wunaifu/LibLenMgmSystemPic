package com.zhuolang.main.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhuolang.main.R;
import com.zhuolang.main.common.APPConfig;
import com.zhuolang.main.ui.activity.BookListActivity;
import com.zhuolang.main.ui.activity.LendBookHistryActivity;
import com.zhuolang.main.ui.activity.NowLendBookHistryActivity;
import com.zhuolang.main.ui.activity.UpdateBookActivity;
import com.zhuolang.main.ui.activity.UpdateNowLendHistryActivity;
import com.zhuolang.main.ui.activity.UserNowLendBookHistryActivity;
import com.zhuolang.main.ui.activity.UserinfoActivity;
import com.zhuolang.main.utils.SharedPrefsUtil;

/**
 * Created by wnf on 2016/10/29.
 * 首页界面fragment
 */


public class HomepageTabFragment extends Fragment implements View.OnClickListener {

    private View view = null;
    private int userType;

    private ImageView iv_bookList;
    private ImageView iv_updateBook;
    private ImageView iv_nowlend;
    private ImageView iv_lendhistry;
    private TextView tv_updateBook;
    private TextView tv_histry;
    private TextView tv_nowlend;

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {

        view = new View(getActivity());
        view = inflater.inflate(R.layout.homepage, container, false);
        Log.d("activityID", "这个是HomepageTabFragment----------:" + this.toString());
        userType = SharedPrefsUtil.getValue(getActivity(), APPConfig.USERTYPE, 0);
        initView(view);
        return view;
    }

    /*
     *初始化数据
     */
    private void initView(View view) {
        iv_bookList = (ImageView) view.findViewById(R.id.image_homepage_allbook);
        iv_updateBook = (ImageView) view.findViewById(R.id.image_homepage_updatebook);
        iv_nowlend = (ImageView) view.findViewById(R.id.iv_homepage_nowlend);
        iv_lendhistry = (ImageView) view.findViewById(R.id.image_homepage_lendhistry);
        tv_updateBook = (TextView) view.findViewById(R.id.tv_homepage_updatebook);
        tv_histry = (TextView) view.findViewById(R.id.tv_homep_history);
        tv_nowlend = (TextView) view.findViewById(R.id.tv_homep_nowlend);
        if (userType==1){
            iv_updateBook.setImageResource(R.drawable.bookinfo);
            tv_updateBook.setText("更新图书信息");

            iv_lendhistry.setImageResource(R.drawable.updatelend);
            tv_histry.setText("当前借阅情况");

            iv_nowlend.setImageResource(R.drawable.rmyappointment);
            tv_nowlend.setText("历史借阅情况");
        }
        iv_updateBook.setOnClickListener(this);
        iv_bookList.setOnClickListener(this);
        iv_lendhistry.setOnClickListener(this);
        iv_nowlend.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        clickImage(v);
    }

    private void clickImage(View v){
        switch (v.getId()){
            case R.id.image_homepage_updatebook:
                Intent intent = new Intent();
                if (userType==1){
                    intent.setClass(getActivity(), UpdateBookActivity.class);
                }else {
                    intent.setClass(getActivity(), UserinfoActivity.class);
                }
                getActivity().startActivity(intent);
//                getActivity().overridePendingTransition(R.anim.enter_anim, R.anim.exit_anim);
                break;
            case R.id.image_homepage_allbook:
                Intent intent3 = new Intent();
                intent3.setClass(getActivity(), BookListActivity.class);
                getActivity().startActivity(intent3);
//                getActivity().overridePendingTransition(R.anim.enter_anim, R.anim.exit_anim);
                break;
            case R.id.image_homepage_lendhistry:
                Intent intent1 = new Intent();
                if (userType==1){
                    intent1.setClass(getActivity(), UpdateNowLendHistryActivity.class);
                }else {
                    intent1.setClass(getActivity(), LendBookHistryActivity.class);
                }
                getActivity().startActivity(intent1);
//                getActivity().overridePendingTransition(R.anim.enter_anim, R.anim.exit_anim);
                break;
            case R.id.iv_homepage_nowlend:
                Intent intent2 = new Intent();
                if (userType==1) {
                    intent2.setClass(getActivity(), UserNowLendBookHistryActivity.class);
                }else {
                    intent2.setClass(getActivity(), NowLendBookHistryActivity.class);
                }
                getActivity().startActivity(intent2);
//                getActivity().overridePendingTransition(R.anim.enter_anim, R.anim.exit_anim);
                break;
            default:
                break;
        }
    }

}
