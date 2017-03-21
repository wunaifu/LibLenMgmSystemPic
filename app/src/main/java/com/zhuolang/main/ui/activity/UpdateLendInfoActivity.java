package com.zhuolang.main.ui.activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.zhuolang.main.R;
import com.zhuolang.main.common.APPConfig;
import com.zhuolang.main.database.MyDatabaseHelper;
import com.zhuolang.main.model.UserNowLend;
import com.zhuolang.main.utils.SharedPrefsUtil;
import com.zhuolang.main.utils.TimeUtil;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by wnf on 2016/11/6.
 */

public class UpdateLendInfoActivity extends Activity{

    private ImageView imageViewBack;
    private TextView tv_userName;
    private TextView tv_bookName;
    private TextView tv_loadTime;
    private TextView tv_returnTime;
    private TextView tv_days;
    private TextView tv_bookAmount;
    private RadioGroup radioGroup;
    private DatePicker dp_returnTime;
    private String returnTime;

    private TextView tv_update;
    private MyDatabaseHelper dbHelper;
    private UserNowLend userNowLend = new UserNowLend();
    private Gson gson = new Gson();
    private String userNowLendStr = "";
    private String daysStr="false";
    private int days=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_updatelendinfo);

        userNowLendStr = getIntent().getStringExtra("userNowLendStr");
        userNowLend = gson.fromJson(userNowLendStr, UserNowLend.class);

        initView();
        initDatas();
    }


    private void initView() {
        dbHelper=new MyDatabaseHelper(this,"LibrarySystem.db",null,1);
        tv_userName = (TextView) findViewById(R.id.tv_updatelendinfo_username);
        tv_bookName = (TextView) findViewById(R.id.tv_updatelendinfo_bookname);
        tv_loadTime = (TextView) findViewById(R.id.tv_updatelendinfo_lendtime);
        tv_returnTime = (TextView) findViewById(R.id.tv_updatelendinfo_returntime);
        tv_days = (TextView) findViewById(R.id.tv_updatelendinfo_days);
        tv_bookAmount = (TextView) findViewById(R.id.tv_updatelendinfo_bookamount);
        imageViewBack = (ImageView) findViewById(R.id.iv_updatelendinfo_back);
        tv_update = (TextView) findViewById(R.id.tv_updatelendinfo_update);
        dp_returnTime = (DatePicker) findViewById(R.id.dp_updatelendinfo_datepicker);
        radioGroup = (RadioGroup) findViewById(R.id.rg_updatelendinfo_gender);

        dp_returnTime.setVisibility(View.GONE);

        String lendTime=userNowLend.getLendRead().getLoadTime();
        Date date1 = new Date();
        days = TimeUtil.oleTimeTonowTime(lendTime, TimeUtil.dateToStrNoTime(date1));
        returnTime = TimeUtil.dateToStrNoTime(date1);

        tv_userName.setText("借阅人："+userNowLend.getUserName());
        tv_bookName.setText("所借书籍："+userNowLend.getBookName());
        tv_bookAmount.setText("借阅本数："+userNowLend.getLendRead().getNumber());
        tv_loadTime.setText("借出时间："+lendTime);
        tv_days.setText("借阅天数："+days);
        tv_returnTime.setText(returnTime);



        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup Group, int Checkid) {
                String str;
                RadioButton radioButton = (RadioButton) findViewById(radioGroup.getCheckedRadioButtonId());
                str = radioButton.getText().toString();
                //如果修改为归还，归还时间为当前时间
                //如果不归还，则可修改归还日期
                if (str.equals("是")) {
                    daysStr = days + "";
                    returnTime = TimeUtil.dateToStrNoTime(new Date());
                } else {
                    daysStr = "false";

                }

            }
        });

//        dp_returnTime.init(dp_returnTime.getYear(), dp_returnTime.getMonth(), dp_returnTime.getDayOfMonth(),
//                new DatePicker.OnDateChangedListener() {
//                    @Override
//                    public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
//                        // 获取一个日历对象，并初始化为当前选中的时间
//                        long mindate = System.currentTimeMillis() - 1000L;
//                        view.setMinDate(mindate);
//                        Calendar calendar = Calendar.getInstance();
//                        calendar.set(year, monthOfYear, dayOfMonth);
//                        returnTime = TimeUtil.dateToStrNoTime(calendar.getTime());
//                        tv_returnTime.setText(returnTime);
//                    }
//                });

    }

    private void initDatas() {

        imageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(UpdateLendInfoActivity.this, UpdateNowLendHistryActivity.class);
                startActivity(intent);
                finish();
            }
        });
        tv_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (daysStr.equals("false")) {
                    Toast.makeText(UpdateLendInfoActivity.this, "此次不修改借阅信息，可自行返回", Toast.LENGTH_SHORT).show();
                }else {
                    SQLiteDatabase db = dbHelper.getWritableDatabase();
                    ContentValues values = new ContentValues();
                    ContentValues valuesbook = new ContentValues();
                    Cursor cursor = db.query("book_tab", null, null, null, null, null, null);
                    if (!cursor.moveToFirst()) {
                        Toast.makeText(UpdateLendInfoActivity.this, "没有找到该书籍", Toast.LENGTH_SHORT).show();
                    } else {
                        do {
                            String bookid = cursor.getString(cursor.getColumnIndex("BookId"));
                            if (bookid.equals(userNowLend.getLendRead().getBookId())) {
                                int loadableNum = cursor.getInt(cursor.getColumnIndex("BookLoanable"));
                                int bookNum = cursor.getInt(cursor.getColumnIndex("BookNumber"));

                                //修改Book表中可借阅数量  +1
                                String[] account = new String[1];
                                account[0] = bookid;
                                if (loadableNum >= bookNum) {
                                    valuesbook.put("BookLoanable", bookNum);

                                } else {
                                    valuesbook.put("BookLoanable", (loadableNum + 1));
                                }
                                db.update("book_tab", valuesbook, "BookId = ?", account);

                                //修改lendread表中 returnTime 和 days
                                values.put("ReturnTime", returnTime);
                                values.put("Days", daysStr);
                                account[0] = userNowLend.getLendRead().getLendId();
                                db.update("lendread_tab", values, "LendId = ?", account);

                                Toast.makeText(UpdateLendInfoActivity.this, "修改借阅信息成功，返回", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent();
                                intent.setClass(UpdateLendInfoActivity.this, UpdateNowLendHistryActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        } while (cursor.moveToNext());
                        cursor.close();
                    }
                }


            }
        });
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            Intent intent = new Intent();
            intent.setClass(UpdateLendInfoActivity.this, UpdateNowLendHistryActivity.class);
            startActivity(intent);
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
