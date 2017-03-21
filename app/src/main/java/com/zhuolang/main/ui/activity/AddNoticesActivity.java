package com.zhuolang.main.ui.activity;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.zhuolang.main.R;
import com.zhuolang.main.database.MyDatabaseHelper;
import com.zhuolang.main.model.Notice;
import com.zhuolang.main.utils.TimeUtil;

import java.util.Date;

/**
 * Created by jat on 2016/11/1.
 */

public class AddNoticesActivity extends Activity {

    private EditText et_title;
    private EditText et_content;
    private ImageView iv_back;
    private TextView vt_add;

    private String noticeId;
    private String noticeTime;
    private String noticeContent;
    private String noticeTitle;
    private Notice notice = new Notice();

    private MyDatabaseHelper dbHelper;
    private SQLiteDatabase db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_addnotices);
        initView();
        initMotion();
    }
    private void initView() {
        //通过findViewById得到对应的控件对象
        et_title = (EditText) findViewById(R.id.et_addnotices_title);
        et_content = (EditText) findViewById(R.id.et_addnotices_content);
        iv_back = (ImageView) findViewById(R.id.iv_addnotices_back);
        vt_add = (TextView) findViewById(R.id.vt_addnotice_add);

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
                Intent intent = new Intent(AddNoticesActivity.this,MainActivity.class);
                intent.putExtra("Flagf", "true");
                startActivity(intent);
                finish();
            }
        });
        vt_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int flag=0;
                notice.setNoticeTitle(et_title.getText().toString().trim());
                notice.setNoticeContent(et_content.getText().toString().trim());
                Date date = new Date();
                notice.setNoticeTime(TimeUtil.dateToString(date));
                if (notice.getNoticeTitle().equals("") || notice.getNoticeContent().equals("")) {
                    Toast.makeText(AddNoticesActivity.this, "不能为空！", Toast.LENGTH_SHORT).show();
                }else{
                    ContentValues values = new ContentValues();
                    //开始组装数据
                    values.put("NoticeTitle",notice.getNoticeTitle());
                    values.put("NoticeTime",notice.getNoticeTime());
                    values.put("NoticeContent", notice.getNoticeContent());
                    db.insert("notice_tab", null, values);
                    Toast.makeText(AddNoticesActivity.this, "发布公告成功", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(AddNoticesActivity.this,MainActivity.class);
                    intent.putExtra("Flagf", "true");
                    startActivity(intent);
                    finish();
                }
            }
        });
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            Intent intent = new Intent(AddNoticesActivity.this,MainActivity.class);
            intent.putExtra("Flagf", "true");
            startActivity(intent);
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
