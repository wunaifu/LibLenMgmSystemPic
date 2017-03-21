package com.zhuolang.main.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import com.zhuolang.main.utils.TimeUtil;

import java.util.Date;

/**
 * Created by wnf on 2016/11/17.
 */
public class MyDatabaseHelper extends SQLiteOpenHelper{

    public static final String CREATE_USER="create table user_tab("
            +"UserId varchar(20) primary key,"
            +"UserName varchar(20),"
            +"UserAge int,"
            +"UserPhone varchar(15),"
            +"UserClass varchar(20),"
            +"UserType int,"
            +"UserSex varchar(5),"
            +"UserAdress varchar(28),"
            +"UserPassword varchar(30))";

    public static final String CREATE_BOOK="create table book_tab("
            +"BookId varchar(20) primary key,"
            +"BookName varchar(30),"
            +"BookType varchar(20),"
            +"BookAuthor varchar(20),"
            +"BookPublisher varchar(30),"
            +"BookPublyear varchar(15),"
            +"BookPrice varchar(10),"
            +"BookAddress varchar(30),"
            +"BookNumber int,"
            +"BookLoanable int,"
            +"BookContent varchar(65))";

    public static final String CREATE_LENDREAD="create table lendread_tab("
            +"LendId integer primary key autoincrement,"
            +"BookId varchar(20),"
            +"UserId varchar(20),"
            +"LoadTime Date,"
            +"ReturnTime Date,"
            +"Number int,"
            +"Days varchar(6))";

    public static final String CREATE_NOTICE="create table notice_tab("
            +"NoticeId integer primary key autoincrement,"
            +"NoticeTitle varchar(15),"
            +"NoticeTime Date,"
            +"NoticeContent varchar(150))";

    public static final String CREATE_SHARE="create table share_tab("
            +"ShareId integer primary key autoincrement,"
            +"ShareUserId varchar(20),"
            +"ShareTime Date,"
            +"ShareLike int,"
            +"ShareContent varchar(150))";

    public static final String CREATE_SHARELIKES="create table likes_tab("
            +"LikesId integer primary key autoincrement,"
            +"ShareId varchar(20),"
            +"LikesTime Date,"//
            +"UserLikesId varchar(20))";

    private Context mContext;

    public MyDatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        mContext=context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {//创建数据库
        db.execSQL(CREATE_USER);//执行建表语句
        db.execSQL(CREATE_BOOK);
        db.execSQL(CREATE_LENDREAD);
        db.execSQL(CREATE_NOTICE);
        db.execSQL(CREATE_SHARE);
        db.execSQL(CREATE_SHARELIKES);
        //默认添加测试数据
        initDatas(db);
        Toast.makeText(mContext,"创建数据库成功，已添加测试数据",Toast.LENGTH_SHORT).show();
    }

    @Override//更新数据库
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists user_tab");//将存在的表删除，再调用更新函数
        db.execSQL("drop table if exists book_tab");
        db.execSQL("drop table if exists lendread_tab");
        db.execSQL("drop table if exists notice_tab");
        db.execSQL("drop table if exists share_tab");
        db.execSQL("drop table if exists likes_tab");
        onCreate(db);
    }

    private void initDatas(SQLiteDatabase db){
        //添加测试用户
        ContentValues values = new ContentValues();
        //开始组装数据
        values.put("UserID", "123456");
        values.put("UserPassword", "123456");
        values.put("UserName", "管理员");
        values.put("UserAge", 30);
        values.put("UserClass", "无");
        values.put("UserSex", "男");
        values.put("UserType", 1);
        values.put("UserPhone", "18219111000");
        values.put("UserAdress","B栋教师楼309");
        db.insert("user_tab", null, values);

        ContentValues values1 = new ContentValues();
        //开始组装数据
        values1.put("UserID", "3114002566");
        values1.put("UserPassword", "123456");
        values1.put("UserName", "吴乃福");
        values1.put("UserAge", 18);
        values1.put("UserClass", "140803");
        values1.put("UserSex", "男");
        values1.put("UserType", 0);
        values1.put("UserPhone", "18219111626");
        values1.put("UserAdress","玫瑰园宿舍1309");
        db.insert("user_tab", null, values1);

        ContentValues values2 = new ContentValues();
        //开始组装数据
        values2.put("UserID", "3114002581");
        values2.put("UserPassword", "123456");
        values2.put("UserName", "黎志荣");
        values2.put("UserAge", 18);
        values2.put("UserClass", "140803");
        values2.put("UserSex", "男");
        values2.put("UserType", 0);
        values2.put("UserPhone", "18219112581");
        values2.put("UserAdress","玫瑰园宿舍1309");
        db.insert("user_tab", null, values2);

        //第一次进入注册页面默认添加几本书
        ContentValues valuesBook = new ContentValues();
        valuesBook.put("BookId","AK0001");
        valuesBook.put("BookName","数据库系统概论(第4版)");
        valuesBook.put("BookType","计算机");
        valuesBook.put("BookAuthor","王珊");
        valuesBook.put("BookPublisher","高等教育出版社");
        valuesBook.put("BookPublyear","2012");
        valuesBook.put("BookPrice","25");
        valuesBook.put("BookAddress","103");
        valuesBook.put("BookNumber",3);
        valuesBook.put("BookLoanable",3);
        valuesBook.put("BookContent","数据库系统概论，数据库系统概论(第4版)");
        db.insert("book_tab", null, valuesBook);

        ContentValues valuesBook1 = new ContentValues();
        valuesBook1.put("BookId","AK0002");
        valuesBook1.put("BookName","编译原理");
        valuesBook1.put("BookType", "教材");
        valuesBook1.put("BookAuthor","王百珊");
        valuesBook1.put("BookPublisher","高等教育出版社");
        valuesBook1.put("BookPublyear","2013");
        valuesBook1.put("BookPrice","25");
        valuesBook1.put("BookAddress","103");
        valuesBook1.put("BookNumber",3);
        valuesBook1.put("BookLoanable",3);
        valuesBook1.put("BookContent","编译原理，数据库系统概论(第5版)");
        db.insert("book_tab", null, valuesBook1);

        ContentValues valuesBook2 = new ContentValues();
        valuesBook2.put("BookId","AK0003");
        valuesBook2.put("BookName","数据库");
        valuesBook2.put("BookType","教材");
        valuesBook2.put("BookAuthor","王珊");
        valuesBook2.put("BookPublisher","高等教育出版社");
        valuesBook2.put("BookPublyear","2014");
        valuesBook2.put("BookPrice","25");
        valuesBook2.put("BookAddress","103");
        valuesBook2.put("BookNumber",3);
        valuesBook2.put("BookLoanable",3);
        valuesBook2.put("BookContent","数据库系统概论，数据库系统概论(第6版)");
        db.insert("book_tab", null, valuesBook2);

        ContentValues valuesBook3 = new ContentValues();
        valuesBook3.put("BookId","AK0004");
        valuesBook3.put("BookName","操作系统");
        valuesBook3.put("BookType","教材");
        valuesBook3.put("BookAuthor","韩其睿");
        valuesBook3.put("BookPublisher","清华大学出版社");
        valuesBook3.put("BookPublyear","2014");
        valuesBook3.put("BookPrice","25");
        valuesBook3.put("BookAddress","103");
        valuesBook3.put("BookNumber",3);
        valuesBook3.put("BookLoanable",1);
        valuesBook3.put("BookContent","操作系统，测试");
        db.insert("book_tab", null, valuesBook3);
        //借阅表
        ContentValues valuesLend = new ContentValues();
        //开始组装数据
        valuesLend.put("BookId", "AK0004");
        valuesLend.put("UserId", "3114002581");
        valuesLend.put("LoadTime", "2016-10-25");
        valuesLend.put("ReturnTime", "");
        valuesLend.put("Number", "1");
        valuesLend.put("Days", "false");
        db.insert("lendread_tab", null, valuesLend);

        ContentValues valuesLend1 = new ContentValues();
        //开始组装数据
        valuesLend1.put("BookId", "AK0004");
        valuesLend1.put("UserId", "3114002581");
        valuesLend1.put("LoadTime", "2016-10-16");
        valuesLend1.put("ReturnTime", "2016-11-28");
        valuesLend1.put("Number", "1");
        valuesLend1.put("Days", "17");
        db.insert("lendread_tab", null, valuesLend1);

        ContentValues valuesLend2 = new ContentValues();
        //开始组装数据
        valuesLend2.put("BookId", "AK0004");
        valuesLend2.put("UserId", "3114002566");
        valuesLend2.put("LoadTime", "2016-10-27");
        valuesLend2.put("ReturnTime", "");
        valuesLend2.put("Number", "1");
        valuesLend2.put("Days", "false");
        db.insert("lendread_tab", null, valuesLend2);

        ContentValues valuesLend3 = new ContentValues();
        //开始组装数据
        valuesLend3.put("BookId", "AK0004");
        valuesLend3.put("UserId", "3114002566");
        valuesLend3.put("LoadTime", "2016-11-10");
        valuesLend3.put("ReturnTime", "2016-11-25");
        valuesLend3.put("Number", "1");
        valuesLend3.put("Days", "15");
        db.insert("lendread_tab", null, valuesLend3);

        ContentValues valuesNotice = new ContentValues();
        //开始组装数据
        valuesNotice.put("NoticeTitle","测试标题");
        valuesNotice.put("NoticeTime","2016-11-27 10:10:10");
        valuesNotice.put("NoticeContent", "测试内容，今天天气特别好");
        db.insert("notice_tab", null, valuesNotice);

        ContentValues valuesNotice1 = new ContentValues();
        //开始组装数据
        valuesNotice1.put("NoticeTitle","闭馆通知");
        valuesNotice1.put("NoticeTime","2016-11-28 20:52:10");
        valuesNotice1.put("NoticeContent", "测试内容，今天天气特别好。这周星期五下午图书馆闭馆半天，请同学们做好借阅情况，谢谢大家");
        db.insert("notice_tab", null, valuesNotice1);

        ContentValues valuesShare = new ContentValues();
        //开始组装数据
        valuesShare.put("ShareUserId","3114002566");
        valuesShare.put("ShareTime", "2016-11-27 10:10:10");
        valuesShare.put("ShareLike","0");
        valuesShare.put("ShareContent", "吴乃福测试内容，今天天气特别好,我又借了一本Java书");
        db.insert("share_tab", null, valuesShare);

        ContentValues valuesShare1 = new ContentValues();
        //开始组装数据
        valuesShare1.put("ShareUserId","3114002566");
        valuesShare1.put("ShareTime", "2016-11-27 12:39:10");
        valuesShare1.put("ShareLike","0");
        valuesShare1.put("ShareContent", "今天在图书馆捡到两块钱，我把他交给了管理员");
        db.insert("share_tab", null, valuesShare1);

        ContentValues valuesShare2 = new ContentValues();
        //开始组装数据
        valuesShare2.put("ShareUserId","3114002581");
        valuesShare2.put("ShareTime", "2016-11-28 16:19:10");
        valuesShare2.put("ShareLike","0");
        valuesShare2.put("ShareContent", "荣测试内容，今天天气特别好,我又借了一本Java书");
        db.insert("share_tab", null, valuesShare2);

        ContentValues valuesShare3 = new ContentValues();
        //开始组装数据
        valuesShare3.put("ShareUserId","3114002581");
        valuesShare3.put("ShareTime", "2016-11-29 14:19:10");
        valuesShare3.put("ShareLike","0");
        valuesShare3.put("ShareContent", "今天适合去图书馆看书，哈哈哈");
        db.insert("share_tab", null, valuesShare3);

        ContentValues valuesShare4 = new ContentValues();
        //开始组装数据
        valuesShare4.put("ShareUserId","3114002566");
        valuesShare4.put("ShareTime", "2016-11-29 15:39:10");
        valuesShare4.put("ShareLike","0");
        valuesShare4.put("ShareContent", "今天在图书馆看到有人在装逼，吓得我去看了会书，一脸懵逼，德玛西亚万岁");
        db.insert("share_tab", null, valuesShare4);

    }
}




