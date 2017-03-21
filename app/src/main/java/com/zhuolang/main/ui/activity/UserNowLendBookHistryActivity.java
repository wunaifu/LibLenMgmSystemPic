package com.zhuolang.main.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.zhuolang.main.R;
import com.zhuolang.main.adapter.LendReadListAdapter;
import com.zhuolang.main.adapter.UserLendListAdapter;
import com.zhuolang.main.adapter.UserLendListHistryAdapter;
import com.zhuolang.main.common.APPConfig;
import com.zhuolang.main.database.MyDatabaseHelper;
import com.zhuolang.main.model.Book;
import com.zhuolang.main.model.LendRead;
import com.zhuolang.main.model.NowLend;
import com.zhuolang.main.model.UserNowLend;
import com.zhuolang.main.utils.SharedPrefsUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wnf on 2016/11/25.
 */
public class UserNowLendBookHistryActivity extends Activity implements AdapterView.OnItemClickListener {
    private ListView listView;
    private ImageView img_back;
    private TextView tv_hint;
    private TextView tv_top;

    private MyDatabaseHelper dbHelper;
    private UserLendListHistryAdapter adapter;
    private SQLiteDatabase db;
    private List<UserNowLend> userLendList = new ArrayList<>();
    private String userId;


    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
            if (msg.obj.equals("true")) {
                adapter = new UserLendListHistryAdapter(UserNowLendBookHistryActivity.this, userLendList);
                listView.setAdapter(adapter);
            }else {
                Toast.makeText(UserNowLendBookHistryActivity.this, "对不起，当前没有正在借阅的图书", Toast.LENGTH_SHORT).show();
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nowlendhistry);

        dbHelper = new MyDatabaseHelper(this, "LibrarySystem.db", null, 1);
        db = dbHelper.getWritableDatabase();

        tv_hint = (TextView) findViewById(R.id.tv_nowlendhis_hint);
        tv_top = (TextView) findViewById(R.id.tv_nowlendhis_listtop);
        tv_hint.setText("选择借阅项可查看借阅书籍信息");
        tv_top.setText("历史借阅情况");
        listView = (ListView) findViewById(R.id.lv_nowlendhistry_list);
        listView.setOnItemClickListener(this);

        img_back = (ImageView) findViewById(R.id.img_nowlendhistry_back);
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        initMotion();
    }

    public void initMotion() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                String flag = "false";
                Message message = new Message();
                message.what = 0;

                Cursor cursor = db.query("lendread_tab", null, null, null, null, null, null);
                if (!cursor.moveToFirst()) {
                    flag="false";
                    message.obj = flag;
                    handler.sendMessage(message);
                } else {
                    //遍历Cursor对象，取出数据
                    Log.d("testrun", "Cursor cursorbook = db.query(\"book_tab\", null, null, null, null, null, null);");
                    do {
                        LendRead lendRead = new LendRead();
                        lendRead.setDays(cursor.getString(cursor.getColumnIndex("Days")));
                        if (!lendRead.getDays().equals("false")) {
                            lendRead.setLendId(cursor.getString(cursor.getColumnIndex("LendId")));
                            lendRead.setUserId(cursor.getString(cursor.getColumnIndex("UserId")));
                            lendRead.setBookId(cursor.getString(cursor.getColumnIndex("BookId")));
                            lendRead.setNumber(cursor.getString(cursor.getColumnIndex("Number")));
                            lendRead.setLoadTime(cursor.getString(cursor.getColumnIndex("LoadTime")));
                            lendRead.setReturnTime(cursor.getString(cursor.getColumnIndex("ReturnTime")));

                            Cursor cursorbook = db.query("book_tab", null, null, null, null, null, null);
                            if (!cursorbook.moveToFirst()) {
                                flag="false";
                                message.obj = flag;
                                handler.sendMessage(message);
                            } else {
                                do {
                                    UserNowLend userNowLend = new UserNowLend();
                                    String bookid = cursorbook.getString(cursorbook.getColumnIndex("BookId"));
                                    if (bookid.equals(lendRead.getBookId())) {
                                        userNowLend.setBookName(cursorbook.getString(cursorbook.getColumnIndex("BookName")));

                                        Cursor cursoruser = db.query("user_tab", null, null, null, null, null, null);
                                        if (!cursoruser.moveToFirst()) {
                                            flag="false";
                                            message.obj = flag;
                                            handler.sendMessage(message);
                                        } else {
                                            do {
                                                String userid = cursoruser.getString(cursoruser.getColumnIndex("UserId"));
                                                if (userid.equals(lendRead.getUserId())) {
                                                    userNowLend.setUserName(cursoruser.getString(cursoruser.getColumnIndex("UserName")));

                                                    userNowLend.setLendRead(lendRead);
                                                    userLendList.add(userNowLend);
                                                    flag = "true";
                                                    break;
                                                }
                                            } while (cursoruser.moveToNext());
                                            cursoruser.close();
                                        }
                                        break;
                                    }
                                } while (cursorbook.moveToNext());
                                cursorbook.close();
                            }
                        }
                    } while (cursor.moveToNext());
                    cursor.close();

                    message.obj = flag;
                    handler.sendMessage(message);
                }
            }
        }).start();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//        Intent intent = new Intent();
//        intent.setClass(UserNowLendBookHistryActivity.this, NowLendUserinfoActivity.class);
//        intent.putExtra("userId",userLendList.get(position).getLendRead().getUserId());
//        startActivity(intent);
        Book book = new Book();
        book.setBookId(userLendList.get(position).getLendRead().getBookId());
        Cursor cursorbook = db.query("book_tab", null, null, null, null, null, null);
        if (!cursorbook.moveToFirst()) {

        } else {
            do {
                String bookid = cursorbook.getString(cursorbook.getColumnIndex("BookId"));
                if (bookid.equals(book.getBookId())) {
                    book.setBookName(cursorbook.getString(cursorbook.getColumnIndex("BookName")));
                    book.setBookType(cursorbook.getString(cursorbook.getColumnIndex("BookType")));
                    book.setBookAuthor(cursorbook.getString(cursorbook.getColumnIndex("BookAuthor")));
                    book.setBookPublisher(cursorbook.getString(cursorbook.getColumnIndex("BookPublisher")));
                    book.setBookPublyear(cursorbook.getString(cursorbook.getColumnIndex("BookPublyear")));
                    book.setBookPrice(cursorbook.getString(cursorbook.getColumnIndex("BookPrice")));
                    book.setBookAddress(cursorbook.getString(cursorbook.getColumnIndex("BookAddress")));
                    book.setBookNumber(cursorbook.getString(cursorbook.getColumnIndex("BookNumber")));
                    book.setBookLoanable(cursorbook.getString(cursorbook.getColumnIndex("BookLoanable")));
                    book.setBookConten(cursorbook.getString(cursorbook.getColumnIndex("BookContent")));
                }
            }while (cursorbook.moveToNext()) ;
            cursorbook.close();
        }
        Intent intent = new Intent();
        intent.setClass(UserNowLendBookHistryActivity.this, LendBookListDetailActivity.class);
        Gson gson = new Gson();
        String bookJS=gson.toJson(book);
        intent.putExtra("bookInfo",bookJS);
        intent.putExtra("returnActivityType", 1);
        startActivity(intent);
//        finish();
    }
}
