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
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.zhuolang.main.R;
import com.zhuolang.main.database.MyDatabaseHelper;

/**
 * Created by jat on 2016/11/1.
 */

public class AddBookActivity extends Activity {

    private EditText et_bookId;
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
    private TextView tv_add;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_addbookinfo);
        initView();
        initMotion();
    }
    private void initView() {
        //通过findViewById得到对应的控件对象
        et_bookId = (EditText) findViewById(R.id.et_update_addbook_id);
        et_bookName = (EditText) findViewById(R.id.et_update_addbook_name);
        et_bookType = (EditText) findViewById(R.id.et_update_addbook_type);
        et_bookAuther = (EditText) findViewById(R.id.et_update_addbook_author);
        et_bookPublisher = (EditText) findViewById(R.id.et_update_addbook_publisher);
        et_bookPublyear = (EditText) findViewById(R.id.et_update_addbook_publyear);
        et_bookPrice = (EditText) findViewById(R.id.et_update_addbook_price);
        et_bookAddress = (EditText) findViewById(R.id.et_update_addbook_address);
        et_bookNumber = (EditText) findViewById(R.id.et_update_addbook_number);
        et_bookContent = (EditText) findViewById(R.id.et_update_addbook_content);
        iv_back = (ImageView) findViewById(R.id.image_addbook_back);
        tv_add = (TextView) findViewById(R.id.tv_addbookinfo_add);

        dbHelper = new MyDatabaseHelper(this, "LibrarySystem.db", null, 1);
    }

    /**
     * 初始化监听等
     */
    private void initMotion(){
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tv_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int flag=0;
                bookId = et_bookId.getText().toString().trim();
                bookName = et_bookName.getText().toString().trim();
                bookType = et_bookType.getText().toString().trim();
                bookAuther = et_bookAuther.getText().toString().trim();
                bookPublisher = et_bookPublisher.getText().toString().trim();
                bookPublyear = et_bookPublyear.getText().toString().trim();
                bookPrice = et_bookPrice.getText().toString().trim();
                bookAddress = et_bookAddress.getText().toString().trim();
                bookNumber = et_bookNumber.getText().toString().trim();
                bookContent = et_bookContent.getText().toString().trim();
                Log.d("testrun", "bookNumber" + bookNumber);
                if (bookName.equals("") || bookId.equals("")||bookNumber.equals("")) {
                    Toast.makeText(AddBookActivity.this, "书名、编号和数量不能为空！", Toast.LENGTH_SHORT).show();
                }else{
                    SQLiteDatabase db = dbHelper.getWritableDatabase();
                    Cursor cursor=db.query("book_tab",null, null, null, null,null,null);
                    if (!cursor.moveToFirst()){
                        ContentValues values = new ContentValues();
                        //开始组装数据
                        values.put("BookId",bookId);
                        values.put("BookName",bookName);
                        values.put("BookType",bookType);
                        values.put("BookAuthor",bookAuther);
                        values.put("BookPublisher",bookPublisher);
                        values.put("BookPublyear",bookPublyear);
                        values.put("BookPrice",bookPrice);
                        values.put("BookAddress",bookAddress);
                        values.put("BookNumber",Integer.parseInt(bookNumber));
                        values.put("BookLoanable",Integer.parseInt(bookNumber));
                        values.put("BookContent", bookContent);
                        Log.d("testrun", "Integer.parseInt(bookNumber)" + Integer.parseInt(bookNumber));
                        db.insert("book_tab", null, values);
                        Toast.makeText(AddBookActivity.this, "添加书籍成功", Toast.LENGTH_SHORT).show();

                        finish();
                    }else {
                        do {
                            //遍历Cursor对象，取出数据
                            String userId = cursor.getString(cursor.getColumnIndex("BookId"));
                            if (userId.equals(bookId)) {
                                flag = 1;
                                break;
                            }
                        } while (cursor.moveToNext());
                        cursor.close();
                        if (flag==1) {
                            Toast.makeText(AddBookActivity.this, "已经存在该编号的书籍，请重新确认！", Toast.LENGTH_SHORT).show();
                        }else {
                            ContentValues values = new ContentValues();
                            //开始组装数据
                            values.put("BookId", bookId);
                            values.put("BookName", bookName);
                            values.put("BookType", bookType);
                            values.put("BookAuthor", bookAuther);
                            values.put("BookPublisher", bookPublisher);
                            values.put("BookPublyear",bookPublyear);
                            values.put("BookPrice", bookPrice);
                            values.put("BookAddress", bookAddress);
                            values.put("BookNumber", Integer.parseInt(bookNumber));
                            values.put("BookLoanable",Integer.parseInt(bookNumber));
                            values.put("BookContent", bookContent);
                            Log.d("testrun", "Integer.parseInt(bookNumber)" + Integer.parseInt(bookNumber));
                            db.insert("book_tab", null, values);
                            Toast.makeText(AddBookActivity.this, "添加书籍成功", Toast.LENGTH_SHORT).show();

                            finish();
                        }
                    }
                }
            }
        });
    }


}
