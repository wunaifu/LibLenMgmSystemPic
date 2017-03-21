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
import android.widget.Toast;

import com.google.gson.Gson;
import com.zhuolang.main.R;
import com.zhuolang.main.adapter.LendHistryAdapter;
import com.zhuolang.main.adapter.LendReadListAdapter;
import com.zhuolang.main.common.APPConfig;
import com.zhuolang.main.database.MyDatabaseHelper;
import com.zhuolang.main.model.Book;
import com.zhuolang.main.model.LendRead;
import com.zhuolang.main.model.NowLend;
import com.zhuolang.main.utils.SharedPrefsUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wnf on 2016/11/25.
 */
public class LendBookHistryActivity extends Activity implements AdapterView.OnItemClickListener {
    private ListView listView;
    private ImageView img_back;

    private MyDatabaseHelper dbHelper;
    private SQLiteDatabase db;
    private LendHistryAdapter adapter;
    private List<NowLend> nowLendList = new ArrayList<>();
    private List<Book> bookList = new ArrayList<>();
    private String userId;


    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
            if (msg.obj.equals("true")) {
                adapter = new LendHistryAdapter(LendBookHistryActivity.this, nowLendList);
                listView.setAdapter(adapter);
            }else {
                Toast.makeText(LendBookHistryActivity.this, "对不起，还没有已归还的图书", Toast.LENGTH_SHORT).show();
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lendbookhistry);
        userId = SharedPrefsUtil.getValue(this, APPConfig.ACCOUNT, "");
        dbHelper = new MyDatabaseHelper(this, "LibrarySystem.db", null, 1);
        db = dbHelper.getWritableDatabase();

        listView = (ListView) findViewById(R.id.lv_lendbookhistry_list);
        listView.setOnItemClickListener(this);

        img_back = (ImageView) findViewById(R.id.img_lendbookhistry_back);
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

                Cursor cursor = db.query("lendread_tab", null, null, null, null, null, null);
                Message message = new Message();
                message.what = 0;
                Log.d("testrun", "message.what = 0;");
                if (!cursor.moveToFirst()) {
                    flag="false";
                    message.obj = flag;
                    Log.d("testrun", "message.obj = flag;");
                    handler.sendMessage(message);
                } else {
                    //遍历Cursor对象，取出数据
                    Cursor cursorbook = db.query("book_tab", null, null, null, null, null, null);
                    Log.d("testrun", "Cursor cursorbook = db.query(\"book_tab\", null, null, null, null, null, null);");
                    do {
                        NowLend nowLend = new NowLend();
                        LendRead lendRead = new LendRead();
                        lendRead.setUserId(cursor.getString(cursor.getColumnIndex("UserId")));
                        lendRead.setDays(cursor.getString(cursor.getColumnIndex("Days")));
                        Log.d("aaaaaaaaaaaaa", "ddddddddddddd" + lendRead.getDays());
                        if (lendRead.getUserId().equals(userId) && !lendRead.getDays().equals("false")) {
//                        if (lendRead.getUserId().equals(userId)){
                            lendRead.setLendId(cursor.getString(cursor.getColumnIndex("LendId")));
                            lendRead.setBookId(cursor.getString(cursor.getColumnIndex("BookId")));
                            lendRead.setNumber(cursor.getString(cursor.getColumnIndex("Number")));
                            lendRead.setLoadTime(cursor.getString(cursor.getColumnIndex("LoadTime")));
                            lendRead.setReturnTime(cursor.getString(cursor.getColumnIndex("ReturnTime")));
                            Log.d("aaaaaaaaaaaaa", "ddddddddddddd" + lendRead.getDays());
                            Log.d("aaaaaaaaaaaaa", "getUserId" + lendRead.getUserId());
                            Log.d("aaaaaaaaaaaaa", "getBookId" + lendRead.getBookId());
                            Log.d("aaaaaaaaaaaaa", "getNumber" + lendRead.getNumber());
                            Log.d("aaaaaaaaaaaaa", "getLoadTime" + lendRead.getLoadTime());
                            Log.d("aaaaaaaaaaaaa", "getReturnTime" + lendRead.getReturnTime());
                            if (!cursorbook.moveToFirst()) {
                                flag = "false";
                                message.obj = flag;
                                handler.sendMessage(message);
                            } else {
                                do {
                                    Book book = new Book();
                                    String bookid = cursorbook.getString(cursorbook.getColumnIndex("BookId"));
                                    if (bookid.equals(lendRead.getBookId())) {
                                        book.setLendId(lendRead.getLendId());
                                        book.setBookId(cursorbook.getString(cursorbook.getColumnIndex("BookId")));
                                        book.setBookName(cursorbook.getString(cursorbook.getColumnIndex("BookName")));
                                        book.setBookType(cursorbook.getString(cursorbook.getColumnIndex("BookType")));
                                        book.setBookAuthor(cursorbook.getString(cursorbook.getColumnIndex("BookAuthor")));
                                        book.setBookPublisher(cursorbook.getString(cursorbook.getColumnIndex("BookPublisher")));
                                        book.setBookPublyear(cursorbook.getString(cursorbook.getColumnIndex("BookPublyear")));
                                        book.setBookPrice(cursorbook.getString(cursorbook.getColumnIndex("BookPrice")));
                                        book.setBookAddress(cursorbook.getString(cursorbook.getColumnIndex("BookAddress")));
                                        book.setBookNumber(cursorbook.getString(cursorbook.getColumnIndex("BookNumber")) + "");
                                        book.setBookLoanable(cursorbook.getString(cursorbook.getColumnIndex("BookLoanable")) + "");
                                        book.setBookConten(cursorbook.getString(cursorbook.getColumnIndex("BookContent")));
                                        nowLend.setBook(book);
                                        nowLend.setLendRead(lendRead);
                                        nowLendList.add(nowLend);
                                        flag = "true";
                                    }
                                } while (cursorbook.moveToNext());
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
        Intent intent = new Intent();
        intent.setClass(LendBookHistryActivity.this, BookListDetailActivity.class);
        Book book = new Book();
        book = nowLendList.get(position).getBook();
        Gson gson = new Gson();
        String bookJS=gson.toJson(book);
        intent.putExtra("bookInfo",bookJS);
        startActivity(intent);
        finish();
    }
}
