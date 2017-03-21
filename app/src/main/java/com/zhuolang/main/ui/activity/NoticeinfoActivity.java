package com.zhuolang.main.ui.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.zhuolang.main.R;
import com.zhuolang.main.common.APPConfig;
import com.zhuolang.main.database.MyDatabaseHelper;
import com.zhuolang.main.utils.SharedPrefsUtil;

/**
 * Created by wnf on 2016/11/1.
 */

public class NoticeinfoActivity extends Activity implements View.OnClickListener{


    private ImageView imageViewBack;
    private Button bt_updateInfo;

    private TextView tv_title;
    private TextView tv_time;
    private TextView tv_content;
    private TextView tv_del;
    private String noticeId;
    private String title;
    private String time;
    private String content;
    private int userType = 0;
    private MyDatabaseHelper dbHelper;
    private SQLiteDatabase db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_noticeinfo);
        dbHelper = new MyDatabaseHelper(this, "LibrarySystem.db", null, 1);
        db = dbHelper.getWritableDatabase();
        userType = SharedPrefsUtil.getValue(this, APPConfig.USERTYPE, 0);
        noticeId = getIntent().getStringExtra("NoticeId");
        title = getIntent().getStringExtra("noticeTitle");
        time = getIntent().getStringExtra("noticeTime");
        content = getIntent().getStringExtra("noticeTontent");
        initView();
        initDatas();
    }
    private void initView() {
        tv_content=(TextView)findViewById(R.id.tv_noticeinfo_content);
        tv_time=(TextView)findViewById(R.id.tv_noticeinfo_time);
        tv_del=(TextView)findViewById(R.id.tv_noticeinfo_del);
        tv_title=(TextView)findViewById(R.id.tv_noticeinfo_title);
        imageViewBack=(ImageView)findViewById(R.id.iv_noticeinfo_back);

        imageViewBack.setOnClickListener(this);
        tv_del.setOnClickListener(this);
        if (userType != 1) {
            tv_del.setVisibility(View.INVISIBLE);
        }
        tv_title.setText(title);
        tv_time.setText(time);
        tv_content.setText("\t\t" + content);
    }

    private void initDatas() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_noticeinfo_back:
                if (userType == 1){
                    Intent intent = new Intent(NoticeinfoActivity.this, MainActivity.class);
                    intent.putExtra("Flagf", "true");
                    startActivity(intent);
                    finish();
                }else {
                    finish();
                }
                break;
            case R.id.tv_noticeinfo_del:
                AlertDialog.Builder dialog = new AlertDialog.Builder(NoticeinfoActivity.this);
                dialog.setTitle("温馨提示");
                dialog.setMessage("是否删除本条公告？");
                dialog.setCancelable(false);
                dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String[] noticeids = new String[1];
                        noticeids[0] = noticeId;
                        db.delete("notice_tab", "NoticeId = ?", noticeids);
                        Toast.makeText(NoticeinfoActivity.this, "删除成功，返回中。。。", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(NoticeinfoActivity.this, MainActivity.class);
                        intent.putExtra("Flagf", "true");
                        startActivity(intent);
                        finish();
                    }
                });

                dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(NoticeinfoActivity.this, "取消删除！", Toast.LENGTH_SHORT).show();
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
            if (userType == 1){
                Intent intent = new Intent(NoticeinfoActivity.this, MainActivity.class);
                intent.putExtra("Flagf", "true");
                startActivity(intent);
                finish();
            }else {
                finish();
            }

            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
