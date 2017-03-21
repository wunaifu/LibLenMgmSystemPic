package com.zhuolang.main.ui.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.zhuolang.main.R;
import com.zhuolang.main.adapter.SharesAdapter;
import com.zhuolang.main.common.APPConfig;
import com.zhuolang.main.database.MyDatabaseHelper;
import com.zhuolang.main.utils.SharedPrefsUtil;
import com.zhuolang.main.utils.TimeUtil;

import java.util.Date;

/**
 * Created by wnf on 2016/11/28.
 * 获取shareId shareUserId name shareTime shareLikes shareContent
 */

public class ShareSpecificInfoActivity extends Activity implements View.OnClickListener{


    private ImageView imageViewBack;
    private Button bt_updateInfo;

    private TextView tv_name;
    private TextView tv_time;
    private TextView tv_del;
    private TextView tv_likesAmount;
    private TextView tv_content;
    private ImageView image_likes;

    private String shareId;
    private String shareUserId;
    private boolean shareYoNlike;
    private String userName;
    private String likesAmount;
    private String time;
    private String content;

    private String userAccount = "";
    private MyDatabaseHelper dbHelper;
    private SQLiteDatabase db;

    private SharesAdapter sharesAdapter;
    private View viewBefore = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sharespecificinfo);
        dbHelper = new MyDatabaseHelper(this, "LibrarySystem.db", null, 1);
        db = dbHelper.getWritableDatabase();
        userAccount = SharedPrefsUtil.getValue(this, APPConfig.ACCOUNT, "");

        shareId = getIntent().getStringExtra("ShareId");
        shareUserId = getIntent().getStringExtra("ShareUserId");
        userName = getIntent().getStringExtra("ShareUserName");
        time = getIntent().getStringExtra("ShareTime");
        content = getIntent().getStringExtra("ShareContent");
        likesAmount = getIntent().getStringExtra("ShareLike");
        shareYoNlike = getIntent().getBooleanExtra("ShareIsImageShareLike", false);


        initView();
        initDatas();
    }
    private void initView() {
        tv_name=(TextView)findViewById(R.id.tv_share_specificinfo_username);
        tv_time=(TextView)findViewById(R.id.tv_share_specificinfo_time);
        tv_content=(TextView)findViewById(R.id.tv_share_specificinfo_content);
        tv_likesAmount=(TextView)findViewById(R.id.tv_share_specificinfo_likes);
        tv_del=(TextView)findViewById(R.id.tv_share_specificinfo_del);
        imageViewBack=(ImageView)findViewById(R.id.iv_share_specificinfo_back);
        image_likes=(ImageView)findViewById(R.id.iv_share_specificinfo_likeimage);

        imageViewBack.setOnClickListener(this);
        tv_del.setOnClickListener(this);
        image_likes.setOnClickListener(this);
        tv_name.setOnClickListener(this);

        if (!userAccount.equals(shareUserId)) {
            tv_del.setVisibility(View.INVISIBLE);
        }
    }

    private void initDatas() {

        tv_name.setText(userName);
        tv_time.setText(time);
        tv_content.setText(content);
        tv_likesAmount.setText(likesAmount);
        if (shareYoNlike==true) {
            image_likes.setImageResource(R.drawable.islike01);
        } else {
            image_likes.setImageResource(R.drawable.like01);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_share_specificinfo_username:
                Intent intent = new Intent();
                intent.setClass(ShareSpecificInfoActivity.this, NowLendUserinfoActivity.class);
                intent.putExtra("userId",shareUserId);
                startActivity(intent);

                break;
            case R.id.iv_share_specificinfo_back:
                ShareInfoListActivity.updateData();
                finish();
                break;
            case R.id.iv_share_specificinfo_likeimage:
            {
                if (shareYoNlike!=true) {//未点过赞
                    //点赞数加一
                    likesAmount = (Integer.parseInt(likesAmount) + 1) + "";
                    //点赞数加一更新到share表
                    ContentValues values = new ContentValues();
                    values.put("ShareLike", likesAmount);
                    String[] shareids = new String[1];
                    shareids[0] = shareId;
                    db.update("share_tab", values, "ShareId = ?", shareids);
                    values.clear();
                    //点赞表添加一条已点赞过的数据
                    ContentValues valuesLikes = new ContentValues();
                    valuesLikes.put("ShareId", shareId);
                    valuesLikes.put("LikesTime", TimeUtil.dateToString(new Date()));
                    valuesLikes.put("UserLikesId", userAccount);
                    db.insert("likes_tab", null, valuesLikes);
                    valuesLikes.clear();
                    //更新页面中该项的点赞数
                    tv_likesAmount.setText(likesAmount);
                    image_likes.setImageResource(R.drawable.islike01);
                    //更新判断变量
                    shareYoNlike=true;

                } else {//已点过赞
                    //点赞数减一
                    likesAmount = (Integer.parseInt(likesAmount) - 1) + "";
                    //点赞数减一更新到share表
                    ContentValues values = new ContentValues();
                    values.put("ShareLike", likesAmount);
                    String[] shareids = new String[1];
                    shareids[0] = shareId;
                    db.update("share_tab", values, "ShareId = ?", shareids);
                    values.clear();
                    //点赞表删除一条已点赞过的数据
                    String[] likesidStr = new String[2];
                    likesidStr[0] = shareId;
                    likesidStr[1] = userAccount;
                    db.delete("likes_tab", "ShareId = ? and UserLikesId = ?", likesidStr);

                    //更新页面中该项的点赞数
                    tv_likesAmount.setText(likesAmount);
                    image_likes.setImageResource(R.drawable.like01);

                    //更新判断变量
                    shareYoNlike = false;
                }
            }
                break;
            case R.id.tv_share_specificinfo_del:
                AlertDialog.Builder dialog = new AlertDialog.Builder(ShareSpecificInfoActivity.this);
                dialog.setTitle("温馨提示");
                dialog.setMessage("是否删除本条动态？");
                dialog.setCancelable(false);
                dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //删除likes表中该条点赞数据
                        String[] likesidStr = new String[2];
                        likesidStr[0] = shareId;
                        likesidStr[1] = userAccount;
                        db.delete("likes_tab", "ShareId = ? and UserLikesId = ?", likesidStr);
                        //删除share表中该条数据
                        String[] shareidStr = new String[1];
                        shareidStr[0] = shareId;
                        db.delete("share_tab", "ShareId = ? ", shareidStr);
                        Toast.makeText(ShareSpecificInfoActivity.this, "删除动态成功，返回中。。。", Toast.LENGTH_SHORT).show();
                        ShareInfoListActivity.updateData();
                        finish();
                    }
                });

                dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(ShareSpecificInfoActivity.this, "取消删除！", Toast.LENGTH_SHORT).show();
                    }
                });
                dialog.show();
                break;
            default:
                break;
        }
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            ShareInfoListActivity.updateData();
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
