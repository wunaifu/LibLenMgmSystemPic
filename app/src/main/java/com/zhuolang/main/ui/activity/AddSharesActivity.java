package com.zhuolang.main.ui.activity;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.zhuolang.main.R;
import com.zhuolang.main.common.APPConfig;
import com.zhuolang.main.database.MyDatabaseHelper;
import com.zhuolang.main.model.Notice;
import com.zhuolang.main.model.Share;
import com.zhuolang.main.utils.SharedPrefsUtil;
import com.zhuolang.main.utils.TimeUtil;

import java.util.Date;

/**
 * Created by jat on 2016/11/1.
 */

public class AddSharesActivity extends Activity {

    private EditText et_title;
    private EditText et_content;
    private ImageView iv_back;
    private TextView vt_add;

    private Share share = new Share();

    private MyDatabaseHelper dbHelper;
    private SQLiteDatabase db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_addshares);
        initView();
        initMotion();
    }
    private void initView() {
        //通过findViewById得到对应的控件对象
        et_content = (EditText) findViewById(R.id.et_addshares_content);
        iv_back = (ImageView) findViewById(R.id.iv_addshares_back);
        vt_add = (TextView) findViewById(R.id.tv_addshares_add);
        share.setUserId(SharedPrefsUtil.getValue(this, APPConfig.ACCOUNT,""));
        dbHelper = new MyDatabaseHelper(this, "LibrarySystem.db", null, 1);
        db = dbHelper.getWritableDatabase();
    }

    /**
     * 初始化监听等
     */
    private void initMotion(){
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddSharesActivity.this,ShareInfoListActivity.class);
                startActivity(intent);
                finish();
            }
        });
        vt_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int flag=0;
                share.setShareContent(et_content.getText().toString().trim());
                Date date = new Date();
                share.setShareTime(TimeUtil.dateToString(date));

                if (et_content.equals("") ) {
                    Toast.makeText(AddSharesActivity.this, "发表内容不能为空！", Toast.LENGTH_SHORT).show();
                }else{
                    ContentValues values = new ContentValues();
                    //开始组装数据
                    values.put("ShareUserId",share.getUserId());
                    values.put("ShareTime",share.getShareTime());
                    values.put("ShareLike",0);
                    values.put("ShareContent", share.getShareContent());
                    db.insert("share_tab", null, values);
                    Toast.makeText(AddSharesActivity.this, "发表动态成功", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(AddSharesActivity.this,ShareInfoListActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            Intent intent = new Intent(AddSharesActivity.this,ShareInfoListActivity.class);
            startActivity(intent);
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
