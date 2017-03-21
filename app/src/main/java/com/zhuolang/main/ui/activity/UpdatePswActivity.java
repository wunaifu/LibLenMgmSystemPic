package com.zhuolang.main.ui.activity;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.zhuolang.main.R;
import com.zhuolang.main.common.APPConfig;
import com.zhuolang.main.database.MyDatabaseHelper;
import com.zhuolang.main.utils.SharedPrefsUtil;
import com.zhuolang.main.view.CustomWaitDialog;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wnf on 2016/11/1.
 */

public class UpdatePswActivity extends Activity {

    private Button btUpdatePsw;
    private EditText etOldPsw;
    private EditText etNewPsw;
    private ImageView ivBack;
    private String oldPsw;
    private String newPsw;
    private String userId;

    private MyDatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_updatepsw);
        initView();
        initDatas();

    }
    private void initView() {
        dbHelper = new MyDatabaseHelper(this,"LibrarySystem.db",null,1);
        userId = SharedPrefsUtil.getValue(this, APPConfig.ACCOUNT, "");
        etNewPsw = (EditText) findViewById(R.id.et_updatepsw_newpsd);
        etOldPsw = (EditText) findViewById(R.id.et_updatepsw_oldpsw);
        btUpdatePsw = (Button) findViewById(R.id.bt_updatepsw_pss);
        ivBack = (ImageView) findViewById(R.id.image_updatepsw_back);
    }

    private void initDatas() {
        btUpdatePsw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean flag = false;
                oldPsw = etOldPsw.getText().toString().trim();
                newPsw = etNewPsw.getText().toString().trim();
                if (oldPsw.equals("") || newPsw.equals("")) {
                    Toast.makeText(UpdatePswActivity.this, "密码不能为空！", Toast.LENGTH_SHORT).show();
                } else {
                    SQLiteDatabase db = dbHelper.getWritableDatabase();
                    Cursor cursor = db.query("user_tab", null, null, null, null, null, null);
                    if (cursor.moveToFirst()) {
                        do {
                            String userIdold = cursor.getString(cursor.getColumnIndex("UserId"));
                            String userPsw = cursor.getString(cursor.getColumnIndex("UserPassword"));
                            if (userIdold.equals(userId)) {
                                if (userPsw.equals(oldPsw))
                                    flag=true;
                                break;
                            }
                        } while (cursor.moveToNext());
                    }
                    cursor.close();
                    if (flag==true){
                        ContentValues values = new ContentValues();
                        values.put("UserPassword", newPsw);
                        String[] account = new String[1];
                        account[0] = userId;
                        db.update("user_tab", values, "UserId = ?", account);
                        Toast.makeText(UpdatePswActivity.this,"密码修改成功",Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(UpdatePswActivity.this,"密码错误，请重新确认旧密码",Toast.LENGTH_SHORT).show();

                    }

                }
            }
        });

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

}
