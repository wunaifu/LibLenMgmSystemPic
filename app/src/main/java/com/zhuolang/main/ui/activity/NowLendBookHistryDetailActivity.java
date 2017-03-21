package com.zhuolang.main.ui.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.zhuolang.main.R;
import com.zhuolang.main.common.APPConfig;
import com.zhuolang.main.database.MyDatabaseHelper;
import com.zhuolang.main.model.Book;
import com.zhuolang.main.utils.SharedPrefsUtil;
import com.zhuolang.main.utils.TimeUtil;

import java.util.Date;

/**
 * Created by hzg on 2016/11/3.
 */
public class NowLendBookHistryDetailActivity extends Activity {

    private ImageView imageViewback;
    private TextView tv_return;
    private TextView tv_bookId;
    private TextView tv_bookName;
    private TextView tv_bookType;
    private TextView tv_bookAuthor;
    private TextView tv_bookPublisher;
    private TextView tv_bookPublyear;
    private TextView tv_bookPrice;
    private TextView tv_bookAddress;
    private TextView tv_bookNumber;
    private TextView tv_bookLoanable;
    private TextView tv_bookContent;

    private Book book = new Book();
    private String bookInfoStr="";
    private String userId="";
    private Gson gson = new Gson();

    private MyDatabaseHelper dbHelper;
    private SQLiteDatabase db;
    private String[] lendIds = new String[1];
    private String[] bookIds = new String[1];
    private boolean flag = false;
    private int flagNum = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lendbook_listdetail);
        dbHelper = new MyDatabaseHelper(this, "LibrarySystem.db", null, 1);
        db = dbHelper.getWritableDatabase();
        bookInfoStr=getIntent().getStringExtra("nowLendBookInfo");
        book=gson.fromJson(bookInfoStr, Book.class);
        userId = SharedPrefsUtil.getValue(this, APPConfig.ACCOUNT, "");

        lendIds[0] = book.getLendId();
        bookIds[0] = book.getBookId();
        Log.d("testrun", "1 NowLendBookHistryDetailActivity bookIds[0]" + bookIds[0] + " lendIds[0]" + lendIds[0]);
        initView();
        initModel();

    }

    private void initView(){
        imageViewback= (ImageView) findViewById(R.id.image_lendbook_listdetail_back);
        tv_return = (TextView) findViewById(R.id.tv_lendbook_listdetail_lend);
        tv_bookId = (TextView) findViewById(R.id.tv_lendbook_listdetail_id);
        tv_bookName = (TextView) findViewById(R.id.tv_lendbook_listdetail_name);
        tv_bookType = (TextView) findViewById(R.id.tv_lendbook_listdetail_type);
        tv_bookAuthor = (TextView) findViewById(R.id.tv_lendbook_listdetail_author);
        tv_bookPublisher = (TextView) findViewById(R.id.tv_lendbook_listdetail_publisher);
        tv_bookPublyear = (TextView) findViewById(R.id.tv_lendbook_listdetail_publyear);
        tv_bookPrice = (TextView) findViewById(R.id.tv_lendbook_listdetail_price);
        tv_bookAddress = (TextView) findViewById(R.id.tv_lendbook_listdetail_address);
        tv_bookNumber = (TextView) findViewById(R.id.tv_lendbook_listdetail_amount);
        tv_bookLoanable = (TextView) findViewById(R.id.tv_lendbook_listdetail_loadableamount);
        tv_bookContent = (TextView) findViewById(R.id.tv_lendbook_listdetail_content);

        tv_return.setText("还书");
        tv_bookId.setText("  编号:"+book.getBookId());
        tv_bookName.setText(book.getBookName());
        tv_bookType.setText(book.getBookType());
        tv_bookAuthor.setText(book.getBookAuthor());
        tv_bookPublisher.setText(book.getBookPublisher());
        tv_bookPublyear.setText(book.getBookPublyear());
        tv_bookPrice.setText(book.getBookPrice());
        tv_bookAddress.setText(book.getBookAddress());
        tv_bookNumber.setText(book.getBookNumber());
        tv_bookLoanable.setText(book.getBookLoanable());
        tv_bookContent.setText("\t\t内容简介："+book.getBookConten());
    }
    
    private void initModel(){
        
        tv_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (flagNum == 0) {
                    AlertDialog.Builder dialog1 = new AlertDialog.Builder(NowLendBookHistryDetailActivity.this);
                    dialog1.setTitle("温馨提示");
                    dialog1.setMessage("是否归还\"" + book.getBookName() + "\"图书！");
                    dialog1.setCancelable(false);
                    dialog1.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    //1.获取当前借阅项的ID  lendId LoadTime
                                    Cursor cursor = db.query("lendread_tab", null, null, null, null, null, null);
                                    Cursor cursorBook = db.query("book_tab", null, null, null, null, null, null);
                                    if (!cursor.moveToFirst()) {
                                        Log.d("testrun", "2 bookIds[0]" + bookIds[0] + " lendIds[0]" + lendIds[0]);
                                    } else {
                                        do {
                                            String lendid = cursor.getString(cursor.getColumnIndex("LendId"));
                                            //2.查找符合的lendId
                                            if (lendid.equals(lendIds[0])) {
                                                Log.d("testrun", "3 bookIds[0]" + bookIds[0] + " lendIds[0]" + lendIds[0]);
                                                String loadTime = cursor.getString(cursor.getColumnIndex("LoadTime"));
                                                //3.更新借阅表 returnTime number days
                                                Date date = new Date();
                                                String returnTime = TimeUtil.dateToStrNoTime(date);
                                                String days = TimeUtil.oleTimeTonowTime(loadTime, returnTime) + "";

                                                ContentValues values = new ContentValues();
                                                values.put("ReturnTime", returnTime);
                                                values.put("Days", days);
                                                db.update("lendread_tab", values, "LendId = ?", lendIds);
                                                //4.更新book_tab 表中的可借数量
                                                if (!cursorBook.moveToFirst()) {

                                                } else {
                                                    do {
                                                        Log.d("testrun", "4 bookIds[0]" + bookIds[0] + " lendIds[0]" + lendIds[0]);
                                                        String bokid = cursorBook.getString(cursorBook.getColumnIndex("BookId"));
                                                        if (bokid.equals(bookIds[0])) {
                                                            Log.d("testrun", "bookIds[0]" + bookIds[0] + " lendIds[0]" + lendIds[0]);
                                                            String loadable = cursorBook.getString(cursorBook.getColumnIndex("BookLoanable"));
                                                            //更新BookLoanable
                                                            ContentValues valuesBook = new ContentValues();
                                                            valuesBook.put("BookLoanable", (Integer.parseInt(loadable) + 1));
                                                            db.update("book_tab", valuesBook, "BookId = ?", bookIds);

                                                            tv_bookLoanable.setText((Integer.parseInt(loadable) + 1) + "");
                                                            flag = true;

                                                            break;
                                                        }
                                                    } while (cursorBook.moveToNext());

                                                }
                                                break;
                                            }
                                        } while (cursor.moveToNext());
                                        cursor.close();
                                    }
                                    if (flag == true) {
                                        Toast.makeText(NowLendBookHistryDetailActivity.this, "还书成功", Toast.LENGTH_SHORT).show();
                                        flagNum++;
                                    } else {
                                        Toast.makeText(NowLendBookHistryDetailActivity.this, "还书失败", Toast.LENGTH_SHORT).show();
                                    }

                                }
                            }

                    );

                    dialog1.setNegativeButton("取消", new DialogInterface.OnClickListener()

                            {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            }

                    );
                    dialog1.show();
                }else {
                    Toast.makeText(NowLendBookHistryDetailActivity.this, "已归还本书，不能再归还", Toast.LENGTH_SHORT).show();
                }

            }
        });
        imageViewback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(NowLendBookHistryDetailActivity.this, NowLendBookHistryActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            Intent intent = new Intent();
            intent.setClass(NowLendBookHistryDetailActivity.this, NowLendBookHistryActivity.class);
            startActivity(intent);
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
