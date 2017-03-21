package com.zhuolang.main.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.zhuolang.main.R;
import com.zhuolang.main.common.APPConfig;
import com.zhuolang.main.database.MyDatabaseHelper;
import com.zhuolang.main.utils.SharedPrefsUtil;
import com.zhuolang.main.view.CustomWaitDialog;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jat on 2016/11/1.
 */

public class LoginActivity extends Activity {

    private EditText et_login_account;
    private EditText et_login_psd;
    private Button bt_login_login;
    private TextView tv_login_find;
    private TextView tv_login_register;

    private String account;
    private String psd;
    private int type;

    private MyDatabaseHelper dbHelper;
    private SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);
        dbHelper = new MyDatabaseHelper(this, "LibrarySystem.db", null, 1);
        db=dbHelper.getWritableDatabase();
        boolean is_login = SharedPrefsUtil.getValue(this, APPConfig.IS_LOGIN, false);
        if (is_login){
            Intent intent = new Intent(LoginActivity.this,MainActivity.class);
            intent.putExtra("Flagf", "false");
            this.startActivity(intent);
            finish();
        }{
            init();
            initMotion();
        }

    }

    /**
     * 初始化控件
     */
    private void init(){
        et_login_account = (EditText)findViewById(R.id.et_login_account);//账号，对应学号
        et_login_psd = (EditText)findViewById(R.id.et_login_psd);
        bt_login_login = (Button)findViewById(R.id.bt_login_login);
        tv_login_find = (TextView)findViewById(R.id.tv_login_find);
        tv_login_register = (TextView)findViewById(R.id.tv_login_register);
    }

    /**
     * 初始化监听等
     */
    private void initMotion(){
        bt_login_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean flag=false;
                account=et_login_account.getText().toString().trim();
                psd=et_login_psd.getText().toString().trim();

                if (account.equals("") || psd.equals("")) {
                    Toast.makeText(LoginActivity.this, "账号密码不能为空！", Toast.LENGTH_SHORT).show();
                }else{
                    Cursor cursor=db.query("user_tab",null,null,null,null,null,null);
                    if (!cursor.moveToFirst()){
                        Toast.makeText(LoginActivity.this,"还没有用户注册，请先注册再登陆",Toast.LENGTH_SHORT).show();
                    }else {
                        do {
                            //遍历Cursor对象，取出数据
                            String userId=cursor.getString(cursor.getColumnIndex("UserId"));
                            String userPsw=cursor.getString(cursor.getColumnIndex("UserPassword"));
                            int userType=cursor.getInt(cursor.getColumnIndex("UserType"));
                            if (userId.equals(account)&&userPsw.equals(psd)) {
                                //保存用户类型到本地
                                SharedPrefsUtil.putValue(LoginActivity.this,APPConfig.USERTYPE,userType);
                                type = userType;
                                flag=true;
                                break;
                            }
                        }while (cursor.moveToNext());
                        cursor.close();
                    }
                    if (flag==true) {
                        if (type == 1) {
                            Toast.makeText(LoginActivity.this, "管理员登陆成功", Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(LoginActivity.this, "读者登陆成功", Toast.LENGTH_SHORT).show();
                        }
                        //保存登录状态
                        SharedPrefsUtil.putValue(LoginActivity.this, APPConfig.IS_LOGIN, true);
                        SharedPrefsUtil.putValue(LoginActivity.this, APPConfig.ACCOUNT, account);
                        Intent intent = new Intent();
                        intent.setClass(LoginActivity.this, MainActivity.class);
                        intent.putExtra("Flagf", "false");
                        startActivity(intent);
                        finish();
                    }else {
                        Toast.makeText(LoginActivity.this,"账号或密码错误，请确认后输入！",Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });
        tv_login_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);
            }
        });
    }
}
