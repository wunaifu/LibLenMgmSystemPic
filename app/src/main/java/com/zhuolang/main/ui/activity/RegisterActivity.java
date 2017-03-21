package com.zhuolang.main.ui.activity;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.zhuolang.main.R;
import com.zhuolang.main.database.MyDatabaseHelper;

/**
 * Created by jat on 2016/11/1.
 */

public class RegisterActivity extends Activity {

           //定义注册用户类型的复选框对象
    private RadioGroup radiogroup;
    private EditText et_id;
    private EditText et_psd;
    private EditText et_name;
    private EditText et_phone;
//    private EditText et_gender;
    private Button bt_register;

    private String id;
    private String psd;
    private String name;
    private String phone;
    private String type="";
    private MyDatabaseHelper dbHelper;
    private SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_register);
        initView();
        initMotion();
    }
    private void initView() {
        //通过findViewById得到对应的控件对象
        et_id = (EditText) findViewById(R.id.et_register_id);
        et_phone = (EditText) findViewById(R.id.et_register_phone);
        et_psd = (EditText) findViewById(R.id.et_register_psd);
        et_name = (EditText) findViewById(R.id.et_register_name);

        bt_register = (Button) findViewById(R.id.bt_register_reg);

        dbHelper = new MyDatabaseHelper(this, "LibrarySystem.db", null, 1);
        db = dbHelper.getWritableDatabase();
    }

    /**
     * 初始化监听等
     */
    private void initMotion(){
        bt_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int flag=0;
                id = et_id.getText().toString().trim();
                psd = et_psd.getText().toString().trim();
                name = et_name.getText().toString().trim();
                phone = et_phone.getText().toString().trim();
                if (id.equals("") || psd.equals("")||name.equals("")||phone.equals("")) {
                    Toast.makeText(RegisterActivity.this, "不能为空,请继续填写！", Toast.LENGTH_SHORT).show();
                } else{

                    Cursor cursor=db.query("user_tab",null, null, null, null,null,null);
                    if (!cursor.moveToFirst()){
                        ContentValues values = new ContentValues();
                        //开始组装数据
                        values.put("UserID", id);
                        values.put("UserPassword", psd);
                        values.put("UserName", name);
                        values.put("UserAge", 0);
                        values.put("UserClass", "");
                        values.put("UserSex", "");
                        values.put("UserType", 0);
                        values.put("UserPhone", phone);
                        values.put("UserAdress","");
                        db.insert("user_tab", null, values);
                        Toast.makeText(RegisterActivity.this, "读者注册成功,返回登陆", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent();
                        intent.setClass(RegisterActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }else {
                        do {
                            //遍历Cursor对象，取出数据
                            String userId=cursor.getString(cursor.getColumnIndex("UserId"));
                            if (userId.equals(id)) {
                                flag=1;
                                break;
                            }
                        }while (cursor.moveToNext());
                        cursor.close();
                        if (flag==1) {
                            Toast.makeText(RegisterActivity.this, "账号已经存在，请重新确认！", Toast.LENGTH_SHORT).show();
                        }else {
                            ContentValues values = new ContentValues();
                            //开始组装数据
                            values.put("UserID", id);
                            values.put("UserPassword", psd);
                            values.put("UserName", name);
                            values.put("UserAge", 0);
                            values.put("UserClass", "");
                            values.put("UserSex", "");
                            values.put("UserType", 0);
                            values.put("UserPhone", phone);
                            values.put("UserAdress","");
                            db.insert("user_tab", null, values);
                            Toast.makeText(RegisterActivity.this, "读者注册成功,返回登陆", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent();
                            intent.setClass(RegisterActivity.this, LoginActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }


                }
            }
        });
    }


}
