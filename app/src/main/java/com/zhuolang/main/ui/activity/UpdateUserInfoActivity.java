package com.zhuolang.main.ui.activity;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.zhuolang.main.R;
import com.zhuolang.main.common.APPConfig;
import com.zhuolang.main.database.MyDatabaseHelper;
import com.zhuolang.main.utils.SharedPrefsUtil;
import com.zhuolang.main.view.CustomWaitDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wnf on 2016/11/6.
 */

public class UpdateUserInfoActivity extends Activity{

    private ImageView imageViewBack;
    private TextView tv_account;
    private EditText et_name;
    private EditText et_class;
    private EditText et_age;
    private EditText et_sex;
    private EditText et_phone;
    private EditText et_address;

    private String userAccount;
    private String userName;
    private String userPhone;
    private String userAge;
    private String userSex;
    private String userAddress;
    private String userClass;

    private TextView tv_update;
    private MyDatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //获取当前用户信息
        userAccount = SharedPrefsUtil.getValue(this, APPConfig.ACCOUNT, "");
        setContentView(R.layout.activity_updateuserinfo);
        initView();
        initDatas();
    }


    private void initView() {
        dbHelper=new MyDatabaseHelper(this,"LibrarySystem.db",null,1);
        tv_account = (TextView) findViewById(R.id.tv_updateinfo_account);
        tv_account.setText("当前账号："+userAccount);
        et_name = (EditText) findViewById(R.id.et_update_updateinfo_name);
        et_age = (EditText) findViewById(R.id.et_update_updateinfo_age);
        et_phone = (EditText) findViewById(R.id.et_updateinfo_phone);
        et_class = (EditText) findViewById(R.id.et_update_updateinfo_class);
        et_sex = (EditText) findViewById(R.id.et_update_updateinfo_sex);
        et_address = (EditText) findViewById(R.id.et_update_updateinfo_address);
        imageViewBack = (ImageView) findViewById(R.id.image_updateinfo_back);
        tv_update = (TextView) findViewById(R.id.tv_updateinfo_update);
    }

    private void initDatas() {
        Intent intent=getIntent();
        et_name.setText(intent.getStringExtra("userName"));
        et_age.setText(intent.getStringExtra("userAge"));
        et_sex.setText(intent.getStringExtra("userSex"));
        et_class.setText(intent.getStringExtra("userClass"));
        et_phone.setText(intent.getStringExtra("userPhone"));
        et_address.setText(intent.getStringExtra("userAdress"));

        imageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(UpdateUserInfoActivity.this, UserinfoActivity.class);
                startActivity(intent);
                finish();
            }
        });
        tv_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userName = et_name.getText().toString().trim();
                userAge = et_age.getText().toString().trim();
                userSex = et_sex.getText().toString().trim();
                userClass = et_class.getText().toString().trim();
                userPhone = et_phone.getText().toString().trim();
                userAddress = et_address.getText().toString().trim();
                SQLiteDatabase db=dbHelper.getWritableDatabase();
                ContentValues values = new ContentValues();
                values.put("UserName", userName);
                values.put("UserAge", userAge);
                values.put("UserSex", userSex);
                values.put("UserClass", userClass);
                values.put("UserPhone", userPhone);
                values.put("UserAdress", userAddress);
                String[] account=new String[1];
                account[0]=userAccount;
                db.update("user_tab", values, "UserId = ?", account);
                Toast.makeText(UpdateUserInfoActivity.this,"修改信息成功",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent();
                intent.setClass(UpdateUserInfoActivity.this, UserinfoActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

}
