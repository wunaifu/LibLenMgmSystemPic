package com.zhuolang.main.ui.activity;

import android.app.Activity;
import android.content.ContentValues;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhuolang.main.R;
import com.zhuolang.main.adapter.BookListAdapter;
import com.zhuolang.main.common.APPConfig;
import com.zhuolang.main.database.MyDatabaseHelper;
import com.zhuolang.main.model.Book;
import com.zhuolang.main.utils.SharedPrefsUtil;
import com.zhuolang.main.view.CustomWaitDialog;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by wnf on 2016/11/25.
 */
public class BookListActivity extends Activity implements AdapterView.OnItemClickListener {
    private ListView listView;
    private EditText et_info;

    private TextView tv_byname;
    private TextView tv_byid;
    private TextView tv_hint;
    private ImageView img_back;
    private String info;
    private int userType;

    private MyDatabaseHelper dbHelper;
    private SQLiteDatabase db;
    private BookListAdapter adapter;
    private List<Book> bookList = new ArrayList<>();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booklist);

        dbHelper = new MyDatabaseHelper(BookListActivity.this, "LibrarySystem.db", null, 1);
        db = dbHelper.getWritableDatabase();

        userType = SharedPrefsUtil.getValue(BookListActivity.this, APPConfig.USERTYPE, 0);

        listView = (ListView) findViewById(R.id.lv_booklist_list);
        et_info = (EditText) findViewById(R.id.et_booklist_info);
        tv_byname = (TextView) findViewById(R.id.tv_booklist_name);
        tv_byid = (TextView) findViewById(R.id.tv_booklist_id);
        tv_hint = (TextView) findViewById(R.id.tv_booklist_hint);
        img_back = (ImageView) findViewById(R.id.img_booklist_back);

        if (userType==1){
            tv_hint.setVisibility(View.GONE);
        }
        listView.setOnItemClickListener(this);

        initMotion();

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tv_byname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                info = et_info.getText().toString().trim();
                if (info.equals("")){
                    Toast.makeText(BookListActivity.this, "信息不能为空", Toast.LENGTH_SHORT).show();
                }else {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            String flag = "false";
                            boolean flag2 = false;
                            Cursor cursor = db.query("book_tab", null, null, null, null, null, null);
                            Message message = new Message();
                            message.what = 2;
                            if (!cursor.moveToFirst()) {
                                flag = "false";
                                message.obj = flag;
                                handler.sendMessage(message);
                            } else {
                                //遍历Cursor对象，取出数据
                                do {
                                    Book book = new Book();
                                    book.setBookName(cursor.getString(cursor.getColumnIndex("BookName")));
                                    book.setBookNumber(cursor.getInt(cursor.getColumnIndex("BookNumber")) + "");
                                    if (book.getBookName().equals(info)&&!book.getBookNumber().equals("0")) {
                                        flag = "true";
                                        book.setBookId(cursor.getString(cursor.getColumnIndex("BookId")));
                                        Log.d("testrun", "BookListActivity book.getBookId()" + book.getBookId());
                                        book.setBookType(cursor.getString(cursor.getColumnIndex("BookType")));
                                        book.setBookAuthor(cursor.getString(cursor.getColumnIndex("BookAuthor")));
                                        book.setBookPublisher(cursor.getString(cursor.getColumnIndex("BookPublisher")));
                                        book.setBookPublyear(cursor.getString(cursor.getColumnIndex("BookPublyear")));
                                        book.setBookPrice(cursor.getString(cursor.getColumnIndex("BookPrice")));
                                        book.setBookAddress(cursor.getString(cursor.getColumnIndex("BookAddress")));
                                        book.setBookLoanable(cursor.getInt(cursor.getColumnIndex("BookLoanable")) + "");
                                        book.setBookConten(cursor.getString(cursor.getColumnIndex("BookContent")));
                                        if (flag2==false){
                                            bookList.clear();
                                        }
                                        flag2 = true;
                                        bookList.add(book);
                                    }

                                } while (cursor.moveToNext());
                                cursor.close();
                                message.obj = flag;
                                handler.sendMessage(message);
                            }
                        }
                    }).start();
                }
            }
        });
        tv_byid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                info = et_info.getText().toString().trim();
                if (info.equals("")){
                    Toast.makeText(BookListActivity.this, "信息不能为空", Toast.LENGTH_SHORT).show();
                }else {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            String flag = "false";
                            Cursor cursor = db.query("book_tab", null, null, null, null, null, null);
                            Message message = new Message();
                            message.what = 1;
                            if (!cursor.moveToFirst()) {
                                flag = "false";
                                message.obj = flag;
                                handler.sendMessage(message);
                            } else {
                                //遍历Cursor对象，取出数据
                                do {
                                    Book book = new Book();
                                    book.setBookId(cursor.getString(cursor.getColumnIndex("BookId")));
                                    book.setBookNumber(cursor.getInt(cursor.getColumnIndex("BookNumber")) + "");
                                    if (book.getBookId().equals(info)&&!book.getBookNumber().equals("0")) {
                                        flag = "true";
                                        Log.d("testrun", "BookListActivity book.getBookId()" + book.getBookId());
                                        book.setBookName(cursor.getString(cursor.getColumnIndex("BookName")));
                                        book.setBookType(cursor.getString(cursor.getColumnIndex("BookType")));
                                        book.setBookAuthor(cursor.getString(cursor.getColumnIndex("BookAuthor")));
                                        book.setBookPublisher(cursor.getString(cursor.getColumnIndex("BookPublisher")));
                                        book.setBookPublyear(cursor.getString(cursor.getColumnIndex("BookPublyear")));
                                        book.setBookPrice(cursor.getString(cursor.getColumnIndex("BookPrice")));
                                        book.setBookAddress(cursor.getString(cursor.getColumnIndex("BookAddress")));
                                        book.setBookLoanable(cursor.getInt(cursor.getColumnIndex("BookLoanable")) + "");
                                        book.setBookConten(cursor.getString(cursor.getColumnIndex("BookContent")));
                                        bookList.clear();
                                        bookList.add(book);
                                        break;
                                    }

                                } while (cursor.moveToNext());
                                cursor.close();
                                message.obj = flag;
                                handler.sendMessage(message);
                            }
                        }
                    }).start();
                }

            }
        });
    }

    public void initMotion() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                String flag = "false";
                bookList.clear();
                Cursor cursor = db.query("book_tab", null, null, null, null, null, null);
                Message message = new Message();
                message.what = 0;
                if (!cursor.moveToFirst()) {
                    flag="false";
                    message.obj = flag;
                    handler.sendMessage(message);
                } else {
                    //遍历Cursor对象，取出数据
                    do {
                        Book book = new Book();
                        book.setBookNumber(cursor.getInt(cursor.getColumnIndex("BookNumber")) + "");
                        if (!book.getBookNumber().equals("0")) {
                            book.setBookId(cursor.getString(cursor.getColumnIndex("BookId")));
                            book.setBookName(cursor.getString(cursor.getColumnIndex("BookName")));
                            book.setBookType(cursor.getString(cursor.getColumnIndex("BookType")));
                            book.setBookAuthor(cursor.getString(cursor.getColumnIndex("BookAuthor")));
                            book.setBookPublisher(cursor.getString(cursor.getColumnIndex("BookPublisher")));
                            book.setBookPublyear(cursor.getString(cursor.getColumnIndex("BookPublyear")));
                            book.setBookPrice(cursor.getString(cursor.getColumnIndex("BookPrice")));
                            book.setBookAddress(cursor.getString(cursor.getColumnIndex("BookAddress")));
                            book.setBookLoanable(cursor.getInt(cursor.getColumnIndex("BookLoanable")) + "");
                            book.setBookConten(cursor.getString(cursor.getColumnIndex("BookContent")));
                            Log.d("testrun", "BookListActivity book.getBookId()" + book.getBookId());
                            bookList.add(book);
                            flag = "true";
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
    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
        Intent intent = new Intent();
        intent.setClass(BookListActivity.this, LendBookListDetailActivity.class);
        Book book = new Book();
        book = bookList.get(position);
        Gson gson = new Gson();
        String bookJS=gson.toJson(book);
        intent.putExtra("bookInfo",bookJS);
        intent.putExtra("returnActivityType", 0);
        startActivity(intent);
        finish();
    }

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
            info = et_info.getText().toString().trim();
            switch (msg.what){
                case 0:
                    if (msg.obj.equals("true")) {
                        adapter = new BookListAdapter(BookListActivity.this, bookList);
                        listView.setAdapter(adapter);
                    }else {
                        Toast.makeText(BookListActivity.this, "没有找到图书，请确认", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case 1:
                    if (msg.obj.equals("true")) {
                        Toast.makeText(BookListActivity.this, "已找到编号为\""+info+"\"的图书", Toast.LENGTH_LONG).show();
                        adapter = new BookListAdapter(BookListActivity.this, bookList);
                        listView.setAdapter(adapter);
                    }else {
                        initMotion();
                        Toast.makeText(BookListActivity.this, "没有找到编号为\""+info+"\"的图书,请确认输入是否正确", Toast.LENGTH_LONG).show();
                    }
                    break;
                case 2:
                    if (msg.obj.equals("true")) {
                        Toast.makeText(BookListActivity.this, "已找到书名为\""+info+"\"的图书", Toast.LENGTH_LONG).show();
                        adapter = new BookListAdapter(BookListActivity.this, bookList);
                        listView.setAdapter(adapter);
                    }else {
                        initMotion();
                        Toast.makeText(BookListActivity.this, "没有找到书名为\""+info+"\"的图书,请确认输入是否正确", Toast.LENGTH_LONG).show();
                    }
                    break;
                default:
                    break;
            }

        }
    };

}
