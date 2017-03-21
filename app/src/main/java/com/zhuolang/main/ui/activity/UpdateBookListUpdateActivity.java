package com.zhuolang.main.ui.activity;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.zhuolang.main.R;
import com.zhuolang.main.common.APPConfig;
import com.zhuolang.main.database.MyDatabaseHelper;
import com.zhuolang.main.model.Book;
import com.zhuolang.main.utils.SharedPrefsUtil;

/**
 * Created by wnf on 2016/11/6.
 */

public class UpdateBookListUpdateActivity extends Activity{

    private TextView tv_bookId;
    private EditText et_bookName;
    private EditText et_bookType;
    private EditText et_bookAuther;
    private EditText et_bookPublisher;
    private EditText et_bookPublyear;
    private EditText et_bookPrice;
    private EditText et_bookAddress;
    private EditText et_bookNumber;
    private EditText et_bookContent;
    private ImageView iv_back;
    private TextView tv_update;

    private String bookId;
    private String bookName;
    private String bookType;
    private String bookAuther;
    private String bookPublisher;
    private String bookPublyear;
    private String bookPrice;
    private String bookAddress;
    private String bookNumber;
    private String bookContent;

    private MyDatabaseHelper dbHelper;
    private Book book;
    private Gson gson = new Gson();
    private String bookInfoStr="";
    private int number;
    private int loadable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //获取当前用户信息
        setContentView(R.layout.activity_updatebooklist_update);
        bookInfoStr=getIntent().getStringExtra("bookInfo");
        book=gson.fromJson(bookInfoStr, Book.class);
        initView();
        initDatas();
    }


    private void initView() {
        //通过findViewById得到对应的控件对象
        tv_bookId = (TextView) findViewById(R.id.tv_updatebooklist_update_id);
        et_bookName = (EditText) findViewById(R.id.et_updatebooklist_update_name);
        et_bookType = (EditText) findViewById(R.id.et_updatebooklist_update_type);
        et_bookAuther = (EditText) findViewById(R.id.et_updatebooklist_update_author);
        et_bookPublisher = (EditText) findViewById(R.id.et_updatebooklist_update_publisher);
        et_bookPublyear = (EditText) findViewById(R.id.et_updatebooklist_update_publyear);
        et_bookPrice = (EditText) findViewById(R.id.et_updatebooklist_update_price);
        et_bookAddress = (EditText) findViewById(R.id.et_updatebooklist_update_address);
        et_bookNumber = (EditText) findViewById(R.id.et_updatebooklist_update_number);
        et_bookContent = (EditText) findViewById(R.id.et_updatebooklist_update_content);
        iv_back = (ImageView) findViewById(R.id.image_updatebooklist_update_back);
        tv_update = (TextView) findViewById(R.id.tv_updatebooklist_update_update);

        dbHelper = new MyDatabaseHelper(this, "LibrarySystem.db", null, 1);
    }

    private void initDatas() {
        tv_bookId.setText("  编号:"+book.getBookId());
        et_bookName.setText(book.getBookName());
        et_bookType.setText(book.getBookType());
        et_bookAuther.setText(book.getBookAuthor());
        et_bookPublisher.setText(book.getBookPublisher());
        et_bookPublyear.setText(book.getBookPublyear());
        et_bookPrice.setText(book.getBookPrice());
        et_bookAddress.setText(book.getBookAddress());
        et_bookNumber.setText(book.getBookNumber());
        et_bookContent.setText(book.getBookConten());

        tv_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bookName = et_bookName.getText().toString().trim();
                bookType = et_bookType.getText().toString().trim();
                bookAuther = et_bookAuther.getText().toString().trim();
                bookPublisher = et_bookPublisher.getText().toString().trim();
                bookPublyear = et_bookPublyear.getText().toString().trim();
                bookPrice = et_bookPrice.getText().toString().trim();
                bookAddress = et_bookAddress.getText().toString().trim();
                bookNumber = et_bookNumber.getText().toString().trim();
                bookContent = et_bookContent.getText().toString().trim();
                if (bookNumber.equals("") || bookName.equals("")) {
                    Toast.makeText(UpdateBookListUpdateActivity.this, "书名和馆藏数量不能为空", Toast.LENGTH_SHORT).show();
                }else if (bookNumber.equals("0")) {
                    Toast.makeText(UpdateBookListUpdateActivity.this, "馆藏数量不能为0", Toast.LENGTH_SHORT).show();
                } else {
                    SQLiteDatabase db = dbHelper.getWritableDatabase();
                    Cursor cursor = db.query("book_tab", null, null, null, null, null, null);
                    if (!cursor.moveToFirst()) {
                        Toast.makeText(UpdateBookListUpdateActivity.this, "数据库中没有图书数据", Toast.LENGTH_SHORT).show();
                    } else {
                        // 1. 获取 可供借阅数量 和 馆藏数量
                        do {
                            String bookid = cursor.getString(cursor.getColumnIndex("BookId"));
                            if (bookid.equals(book.getBookId())) {
                                number = cursor.getInt(cursor.getColumnIndex("BookNumber"));
                                loadable = cursor.getInt(cursor.getColumnIndex("BookLoanable"));
                                break;
                            }
                        } while (cursor.moveToNext());
                        cursor.close();
                        // 2. 输入的数量是否 < number - loadable
                        if ((number - loadable) <= Integer.parseInt(bookNumber)) {
                            ContentValues values = new ContentValues();
                            values.put("BookName", bookName);
                            values.put("BookType", bookType);
                            values.put("BookAuthor", bookAuther);
                            values.put("BookPublisher", bookPublisher);
                            values.put("BookPublyear", bookPublyear);
                            values.put("BookPrice", bookPrice);
                            values.put("BookAddress", bookAddress);
                            values.put("BookNumber", Integer.parseInt(bookNumber));
                            values.put("BookLoanable", (loadable - (number - Integer.parseInt(bookNumber))));
                            values.put("BookContent", bookContent);
                            String[] account = new String[1];
                            account[0] = book.getBookId();
                            db.update("book_tab", values, "BookId = ?", account);
                            Toast.makeText(UpdateBookListUpdateActivity.this, "修改信息成功", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent();
                            intent.setClass(UpdateBookListUpdateActivity.this, UpdateBookListActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(UpdateBookListUpdateActivity.this, "修改后馆藏数量小于已借出数量，请重试", Toast.LENGTH_SHORT).show();
                        }


                    }
                }


            }
        });
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(UpdateBookListUpdateActivity.this, UpdateBookListActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            Intent intent = new Intent();
            intent.setClass(UpdateBookListUpdateActivity.this, UpdateBookListActivity.class);
            startActivity(intent);
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
