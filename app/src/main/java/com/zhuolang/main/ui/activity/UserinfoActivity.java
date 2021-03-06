package com.zhuolang.main.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.zhuolang.main.R;
import com.zhuolang.main.common.APPConfig;
import com.zhuolang.main.database.MyDatabaseHelper;
import com.zhuolang.main.utils.SharedPrefsUtil;

/**
 * Created by wnf on 2016/11/1.
 */

public class UserinfoActivity extends Activity implements View.OnClickListener{

    private LinearLayout ll_myicon;
    private ImageView imageViewBack;
    private TextView tv_updateInfo;

    private TextView tv_acount;
    private TextView tv_class;
    private TextView tv_name;
    private TextView tv_phone;
    private TextView tv_age;
    private TextView tv_sex;
    private TextView tv_address;

    private MyDatabaseHelper dpHelper;
    private String userAccount;
    private String userName;
    private String userAge;
    private String userPhone;
    private String usrClass;
    private String userSex;
    private String userAdress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userinfo);
        userAccount = SharedPrefsUtil.getValue(this, APPConfig.ACCOUNT, "");
        initView();
        initDatas();
    }


    private void initView() {
        dpHelper=new MyDatabaseHelper(this,"LibrarySystem.db",null,1);
        ll_myicon = (LinearLayout) findViewById(R.id.ll_userinfo_usericons);
        tv_acount=(TextView)findViewById(R.id.tv_userinfo_acount);
        tv_class=(TextView)findViewById(R.id.tv_userinfo_class);
        tv_name=(TextView)findViewById(R.id.tv_userinfo_name);
        tv_phone=(TextView)findViewById(R.id.tv_userinfo_phone);
        tv_age=(TextView)findViewById(R.id.tv_userinfo_age);
        tv_sex=(TextView)findViewById(R.id.tv_userinfo_sex);
        tv_address=(TextView)findViewById(R.id.tv_userinfo_address);
        imageViewBack=(ImageView)findViewById(R.id.image_shdotinfo_back);
        tv_updateInfo= (TextView) findViewById(R.id.tv_userinfo_update);
        tv_updateInfo.setOnClickListener(this);
        imageViewBack.setOnClickListener(this);
        ll_myicon.setOnClickListener(this);
    }

    private void initDatas() {
        SQLiteDatabase db=dpHelper.getWritableDatabase();
        Cursor cursor= db.query("user_tab",null,null,null,null,null,null);
        if (cursor.moveToFirst()){
            do {
                String userId = cursor.getString(cursor.getColumnIndex("UserId"));
                if (userId.equals(userAccount)){
                    userName = cursor.getString(cursor.getColumnIndex("UserName"));
                    userAge = cursor.getInt(cursor.getColumnIndex("UserAge"))+"";
                    userPhone = cursor.getString(cursor.getColumnIndex("UserPhone"));
                    usrClass = cursor.getString(cursor.getColumnIndex("UserClass"));
                    userSex = cursor.getString(cursor.getColumnIndex("UserSex"));
                    userAdress=cursor.getString(cursor.getColumnIndex("UserAdress"));
                    break;
                }

            }while (cursor.moveToNext());
        }
        tv_acount.setText(userAccount);
        tv_name.setText(userName);
        tv_age.setText(userAge);
        tv_phone.setText(userPhone);
        tv_class.setText(usrClass);
        tv_sex.setText(userSex);
        tv_address.setText(userAdress);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.image_shdotinfo_back:
                finish();
                break;
            case R.id.tv_userinfo_update:
                Intent intent = new Intent();
                intent.setClass(UserinfoActivity.this, UpdateUserInfoActivity.class);
                intent.putExtra("userName", userName);
                intent.putExtra("userAge", userAge);
                intent.putExtra("userPhone", userPhone);
                intent.putExtra("userClass", usrClass);
                intent.putExtra("userSex", userSex);
                intent.putExtra("userAdress", userAdress);
                startActivity(intent);
                finish();
                break;
            case R.id.ll_userinfo_usericons:
                Intent intent1 = new Intent();
                intent1.setClass(UserinfoActivity.this, TokePhotoActivity.class);
                startActivity(intent1);
                finish();
                break;
            default:
                break;
        }
    }
}
