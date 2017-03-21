package com.zhuolang.main.ui.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
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
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.zhuolang.main.R;
import com.zhuolang.main.adapter.BookListAdapter;
import com.zhuolang.main.common.APPConfig;
import com.zhuolang.main.database.MyDatabaseHelper;
import com.zhuolang.main.model.Book;
import com.zhuolang.main.utils.SharedPrefsUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wnf on 2016/11/25.
 */
public class DelBookListActivity extends Activity implements AdapterView.OnItemClickListener {
    private ListView listView;
    private ImageView img_back;
    private EditText et_info;
    private TextView tv_byname;
    private TextView tv_byid;
    private String info;
    private MyDatabaseHelper dbHelper;
    private SQLiteDatabase db;
    private BookListAdapter adapter;
    private List<Book> books = new ArrayList<>();
    private List<Book> bookList = new ArrayList<>();

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
            info = et_info.getText().toString().trim();
            switch (msg.what){
                case 0:
                    if (msg.obj.equals("true")) {
                        adapter = new BookListAdapter(DelBookListActivity.this, bookList);
                        listView.setAdapter(adapter);
                    }else {
                        Toast.makeText(DelBookListActivity.this, "没有找到图书，请确认", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case 1:
                    if (msg.obj.equals("true")) {
                        Toast.makeText(DelBookListActivity.this, "已找到编号为\""+info+"\"的图书", Toast.LENGTH_LONG).show();
                        adapter = new BookListAdapter(DelBookListActivity.this, bookList);
                        listView.setAdapter(adapter);
                    }else {
                        initMotion();
                        Toast.makeText(DelBookListActivity.this, "没有找到编号为\""+info+"\"的图书,请确认输入是否正确", Toast.LENGTH_LONG).show();
                    }
                    break;
                case 2:
                    if (msg.obj.equals("true")) {
                        Toast.makeText(DelBookListActivity.this, "已找到书名为\""+info+"\"的图书", Toast.LENGTH_LONG).show();
                        adapter = new BookListAdapter(DelBookListActivity.this, bookList);
                        listView.setAdapter(adapter);
                    }else {
                        initMotion();
                        Toast.makeText(DelBookListActivity.this, "没有找到书名为\""+info+"\"的图书,请确认输入是否正确", Toast.LENGTH_LONG).show();
                    }
                    break;
                case 5:
                    adapter = new BookListAdapter(DelBookListActivity.this, null);
                    listView.setAdapter(adapter);
                    break;
                default:
                    break;
            }

        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delbooklist);
        dbHelper = new MyDatabaseHelper(this, "LibrarySystem.db", null, 1);
        db = dbHelper.getWritableDatabase();
        listView = (ListView) findViewById(R.id.lv_delbooklist_list);
        img_back = (ImageView) findViewById(R.id.img_delbooklist_back);
        et_info = (EditText) findViewById(R.id.et_delbooklist_info);
        tv_byname = (TextView) findViewById(R.id.tv_delbooklist_name);
        tv_byid = (TextView) findViewById(R.id.tv_delbooklist_id);
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
                    Toast.makeText(DelBookListActivity.this, "信息不能为空", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(DelBookListActivity.this, "信息不能为空", Toast.LENGTH_SHORT).show();
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
                            Log.d("testrun", "BookLoanable" + book.getBookLoanable());
                            bookList.add(book);
                            flag = "true";
                        }
                    } while (cursor.moveToNext());
                    cursor.close();
//                    adapter = new BookListAdapter(BookListActivity.this, bookList);
//                    listView.setAdapter(adapter);

                    message.obj = flag;
                    handler.sendMessage(message);
                }
            }
        }).start();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
        if (bookList.size() <= 0) {
            Toast.makeText(DelBookListActivity.this,"没有可用数据，请刷新",Toast.LENGTH_SHORT).show();
        } else {
            int number = 0;
            int loadable = 0;

            final Cursor cursor = db.query("book_tab", null, null, null, null, null, null);
            final Cursor cursorLend = db.query("lendread_tab", null, null, null, null, null, null);
            if (!cursor.moveToFirst()) {
                Toast.makeText(DelBookListActivity.this, "数据库中没有图书数据", Toast.LENGTH_SHORT).show();
            } else {
                // 1. 获取 可供借阅数量 和 馆藏数量
                do {
                    String bookid = cursor.getString(cursor.getColumnIndex("BookId"));
                    if (bookid.equals(bookList.get(position).getBookId())) {
                        number = cursor.getInt(cursor.getColumnIndex("BookNumber"));
                        loadable = cursor.getInt(cursor.getColumnIndex("BookLoanable"));
                        break;
                    }
                } while (cursor.moveToNext());
                cursor.close();
                if (number <= loadable) {

                    AlertDialog.Builder dialog1 = new AlertDialog.Builder(DelBookListActivity.this);
                    dialog1.setTitle("温馨提示");
                    dialog1.setMessage("是否删除编号为\"" + bookList.get(position).getBookId() + "\"的\"" + bookList.get(position).getBookName() + "\"图书！");
                    dialog1.setCancelable(false);
                    dialog1.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            boolean flag = false;
                            if (!cursorLend.moveToFirst()) {

                            } else {
                                do {
                                    String bookid = cursorLend.getString(cursorLend.getColumnIndex("BookId"));
                                    if (bookid.equals(bookList.get(position).getBookId())) {
                                        flag = true;
                                        Toast.makeText(DelBookListActivity.this, "\"" + bookList.get(position).getBookName() +
                                                "\"图书曾被借阅过，不删除其在数据库中的数据，只重置其馆藏数量为0！", Toast.LENGTH_SHORT).show();
                                        break;
                                    }
                                } while (cursorLend.moveToNext());
                            }
                            String[] account = new String[1];
                            account[0] = bookList.get(position).getBookId();
                            if (flag == true) {
                                ContentValues values = new ContentValues();
                                values.put("BookNumber", 0);
                                values.put("BookLoanable", 0);
                                db.update("book_tab", values, "BookId = ?", account);
                            } else {
                                db.delete("book_tab", "BookId = ?", account);
                                Toast.makeText(DelBookListActivity.this, "成功删除编号为\"" + bookList.get(position).getBookId()
                                        + "\"的\"" + bookList.get(position).getBookName() + "\"图书！", Toast.LENGTH_SHORT).show();
                                if (bookList.size() <= 1) {
                                    Message message = new Message();
                                    message.what = 5;
                                    message.obj = "nothing";
                                    handler.sendMessage(message);
                                }
                            }
                            initMotion();

                        }
                    });
                    dialog1.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    dialog1.show();
                } else {
                    Toast.makeText(DelBookListActivity.this, "\"" + bookList.get(position).getBookName() + "\"图书已借出，不能删除", Toast.LENGTH_SHORT).show();
                }
            }
        }



    }
}
