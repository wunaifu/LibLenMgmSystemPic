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

import org.json.JSONObject;
import org.json.JSONStringer;

import java.util.Date;

/**
 * Created by hzg on 2016/11/3.
 */
public class BookListDetailActivity extends Activity {

    private ImageView imageViewback;
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

    private TextView tv_lend;
    private long exitTime = 0;
    private Book book = new Book();
    private String bookInfoStr="";
    private String userId="";
    private int userType=0;
    private Gson gson = new Gson();
    private MyDatabaseHelper dbHelper;
    private SQLiteDatabase db;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lendbook_listdetail);

        dbHelper = new MyDatabaseHelper(this, "LibrarySystem.db", null, 1);
        db=dbHelper.getWritableDatabase();

        userId = SharedPrefsUtil.getValue(this, APPConfig.ACCOUNT, "");
        userType = SharedPrefsUtil.getValue(this, APPConfig.USERTYPE, 0);

        bookInfoStr=getIntent().getStringExtra("bookInfo");
        book=gson.fromJson(bookInfoStr, Book.class);

        initView();
        initModel();

    }

    private void initView(){
        imageViewback= (ImageView) findViewById(R.id.image_lendbook_listdetail_back);
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
        tv_lend = (TextView) findViewById(R.id.tv_lendbook_listdetail_lend);
        if (userType==1){
            tv_lend.setVisibility(View.INVISIBLE);
        }
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
        tv_bookContent.setText("\t\t内容简介：" + book.getBookConten());
    }
    private void initModel(){
        tv_lend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialog1 = new AlertDialog.Builder(BookListDetailActivity.this);
                dialog1.setTitle("温馨提示");
                dialog1.setMessage("是否借阅\"" + book.getBookName() + "\"图书！");
                dialog1.setCancelable(false);
                dialog1.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                int loadable = 0;
                                int numberLend = 0;
                                int havenumber = 0;
                                //1.用户当前正在借了多少本书，上限为5本书
                                Cursor cursorCheck = db.query("lendread_tab", null, null, null, null, null, null);
                                if (!cursorCheck.moveToFirst()) {

                                } else {
                                    do {
                                        String dayss = cursorCheck.getString(cursorCheck.getColumnIndex("Days"));
                                        String useridsss = cursorCheck.getString(cursorCheck.getColumnIndex("UserId"));
                                        if (dayss.equals("false") && useridsss.equals(userId)) {
                                            havenumber++;
                                        }
                                    } while (cursorCheck.moveToNext());
                                    cursorCheck.close();
                                }
                                if (havenumber < 5) {

                                    Cursor cursor = db.query("book_tab", null, null, null, null, null, null);
                                    if (!cursor.moveToFirst()) {
                                        Toast.makeText(BookListDetailActivity.this, "数据库中没有该图书数据", Toast.LENGTH_SHORT).show();
                                    } else {
                                        // 2. 获取可供借阅数量
                                        do {
                                            String bookid = cursor.getString(cursor.getColumnIndex("BookId"));
                                            if (bookid.equals(book.getBookId())) {
                                                loadable = cursor.getInt(cursor.getColumnIndex("BookLoanable"));
                                                break;
                                            }
                                        } while (cursor.moveToNext());
                                        cursor.close();
                                        // 3. 可供借阅数量是否大于0
                                        if (loadable > 0) {
//                                        cursor = db.query("lendread_tab", null, null, null, null, null, null);
//                                        if (cursor.moveToFirst()) {
//                                            // 3. 是否已经借阅过本书，获取已借阅数量
//                                            do {
//                                                String bookidLend = cursor.getString(cursor.getColumnIndex("BookId"));
//                                                String useridLend = cursor.getString(cursor.getColumnIndex("UserId"));
//                                                if (bookidLend.equals(book.getBookId()) && useridLend.equals(userId)) {
//                                                    numberLend = cursor.getInt(cursor.getColumnIndex("Number"));
//                                                    break;
//                                                }
//                                            } while (cursor.moveToNext());
//                                            cursor.close();
//                                        }
                                            // 4. 添加借阅表信息
                                            ContentValues valuesLend = new ContentValues();
                                            Date date = new Date();
                                            //开始组装数据
                                            valuesLend.put("BookId", book.getBookId());
                                            Log.d("testrun", "LendBookListdeActivity book.getBookId()" + book.getBookId());
                                            valuesLend.put("UserId", userId);
                                            valuesLend.put("LoadTime", TimeUtil.dateToStrNoTime(date));
//                                        valuesLend.put("LoadTime", "2016-11-22");
                                            valuesLend.put("ReturnTime", TimeUtil.dateToStrNoTime(date));
                                            valuesLend.put("Number", (numberLend + 1));
                                            valuesLend.put("Days", "false");
                                            db.insert("lendread_tab", null, valuesLend);
                                            Toast.makeText(BookListDetailActivity.this, "借阅成功", Toast.LENGTH_SHORT).show();

                                            String[] bookidStr = new String[1];
                                            bookidStr[0] = book.getBookId();
                                            // 5. 更新book_tab表  BookLoanable-1
                                            ContentValues valuesBook = new ContentValues();
                                            //开始组装数据
                                            valuesBook.put("BookLoanable", (loadable - 1));
                                            db.update("book_tab", valuesBook, "BookId = ?", bookidStr);
                                            // 6. 更细当前页面可供借阅数量  -1
                                            book.setBookLoanable((Integer.parseInt(book.getBookLoanable()) - 1) + "");
                                            tv_bookLoanable.setText(book.getBookLoanable());
                                        } else {
                                            Toast.makeText(BookListDetailActivity.this, "暂时没有该图书可供借阅的", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                } else {
                                    Toast.makeText(BookListDetailActivity.this, "您目前所借书籍已经达到上限5本，请先还书再借阅", Toast.LENGTH_SHORT).show();
                                }

                            }
                        }

                );
                dialog1.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                dialog1.show();
            }
        });
        imageViewback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(BookListDetailActivity.this, LendBookHistryActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            Intent intent = new Intent();
            intent.setClass(BookListDetailActivity.this, LendBookHistryActivity.class);
            startActivity(intent);
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
