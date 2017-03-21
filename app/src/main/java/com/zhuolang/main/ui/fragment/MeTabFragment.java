package com.zhuolang.main.ui.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.zhuolang.main.R;
import com.zhuolang.main.common.APPConfig;
import com.zhuolang.main.ui.activity.BookListActivity;
import com.zhuolang.main.ui.activity.LoginActivity;
import com.zhuolang.main.ui.activity.NowLendBookHistryActivity;
import com.zhuolang.main.ui.activity.SettingActivity;
import com.zhuolang.main.ui.activity.UpdateBookActivity;
import com.zhuolang.main.ui.activity.UpdateNowLendHistryActivity;
import com.zhuolang.main.ui.activity.UserNowLendBookHistryActivity;
import com.zhuolang.main.ui.activity.UserinfoActivity;
import com.zhuolang.main.utils.SharedPrefsUtil;

/**
 * Created by wnf on 2016/10/29.
 * “我”界面的fragment
 */


public class MeTabFragment extends Fragment implements View.OnClickListener{

    private ImageView imageView=null;
    private LinearLayout ll_finish;
    private LinearLayout ll_setting;
    private LinearLayout ll_lendbook;
    private LinearLayout ll_nowlend;
    private LinearLayout ll_returnbook;
    private LinearLayout ll_logout;
    private TextView tv_me;
    private TextView tv_returnbook;
    private TextView tv_lendbook;
    private View view = null;
    private int userType=0;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState){

        view=new View(getActivity());

        view = inflater.inflate(R.layout.me, container, false);
        Log.d("activityID", "这个是meTabFragment----------:" + this.toString());
        userType = SharedPrefsUtil.getValue(getContext(), APPConfig.USERTYPE, 0);
        initView(view);
        return view;

    }

    private void initView(View view) {
        imageView=(ImageView)view.findViewById(R.id.image_me_mineinfo);
        ll_finish= (LinearLayout) view.findViewById(R.id.me_ll_finish);
        ll_setting= (LinearLayout) view.findViewById(R.id.ll_me_setting);
        ll_lendbook= (LinearLayout) view.findViewById(R.id.ll_me_lendbook);
        ll_nowlend= (LinearLayout) view.findViewById(R.id.ll_me_nowlend);
        ll_returnbook= (LinearLayout) view.findViewById(R.id.ll_me_returnbook);
        ll_logout= (LinearLayout) view.findViewById(R.id.me_ll_logout);
        tv_me=(TextView)view.findViewById(R.id.me_tv_me);
        tv_returnbook=(TextView)view.findViewById(R.id.tv_me_returnbook);
        tv_lendbook=(TextView)view.findViewById(R.id.tv_me_lendbook);

        ll_logout.setOnClickListener(this);
        ll_lendbook.setOnClickListener(this);
        ll_nowlend.setOnClickListener(this);
        ll_returnbook.setOnClickListener(this);
        ll_finish.setOnClickListener(this);
        ll_setting.setOnClickListener(this);
        imageView.setOnClickListener(this);
        if (userType == 1) {
            tv_lendbook.setText(" 更新图书信息");
            tv_returnbook.setText(" 修改借阅情况");
        }else {
            ll_nowlend.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        clickImage(v);
    }
    private void clickImage(View v) {
        switch (v.getId()) {
            case R.id.ll_me_lendbook:
                Intent intent1 = new Intent();
                if (userType == 1) {
                    intent1.setClass(getActivity(), UpdateBookActivity.class);
                } else {
                    intent1.setClass(getActivity(), BookListActivity.class);
                }
                getActivity().startActivity(intent1);
//                getActivity().overridePendingTransition(R.anim.enter_anim, R.anim.exit_anim);
                break;
            case R.id.ll_me_nowlend:
                Intent intent3 = new Intent();
                intent3.setClass(getActivity(), UserNowLendBookHistryActivity.class);
                getActivity().startActivity(intent3);
                break;
            case R.id.ll_me_returnbook:
                Intent intent2 = new Intent();
                if (userType == 1) {
                    intent2.setClass(getActivity(), UpdateNowLendHistryActivity.class);
                } else {
                    intent2.setClass(getActivity(), NowLendBookHistryActivity.class);
                }
                getActivity().startActivity(intent2);
//                getActivity().overridePendingTransition(R.anim.enter_anim, R.anim.exit_anim);
                break;
            case R.id.image_me_mineinfo:
                Intent intent = new Intent();
                intent.setClass(getActivity(), UserinfoActivity.class);
                getActivity().startActivity(intent);
//                getActivity().overridePendingTransition(R.anim.enter_anim, R.anim.exit_anim);
                break;
            case R.id.me_ll_finish:
                AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
                dialog.setTitle("温馨提示");
                dialog.setMessage("是否结束体验，退出程序？");
                dialog.setCancelable(false);
                dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        getActivity().finish();
                    }
                });
                dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                dialog.show();
                break;
            case R.id.ll_me_setting:
                Intent intentSet = new Intent();
                intentSet.setClass(getActivity(), SettingActivity.class);
                startActivity(intentSet);
//                getActivity().overridePendingTransition(R.anim.enter_anim, R.anim.exit_anim);
                break;
            case R.id.me_ll_logout:
                AlertDialog.Builder dialog1 = new AlertDialog.Builder(getActivity());
                dialog1.setTitle("温馨提示");
                dialog1.setMessage("是否注销切换账号？");
                dialog1.setCancelable(false);
                dialog1.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SharedPrefsUtil.putValue(getActivity(), APPConfig.IS_LOGIN, false);
                        Intent intent2 = new Intent();
                        intent2.setClass(getActivity(), LoginActivity.class);
                        startActivity(intent2);
//                        getActivity().overridePendingTransition(R.anim.enter_anim, R.anim.exit_anim);
                        getActivity().finish();
                    }
                });
                dialog1.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                dialog1.show();
                break;

            default:
                break;

        }
    }
}
