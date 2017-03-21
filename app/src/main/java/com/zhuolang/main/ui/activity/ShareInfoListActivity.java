package com.zhuolang.main.ui.activity;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.zhuolang.main.R;
import com.zhuolang.main.adapter.NoticeListAdapter;
import com.zhuolang.main.adapter.SharesAdapter;
import com.zhuolang.main.common.APPConfig;
import com.zhuolang.main.database.MyDatabaseHelper;
import com.zhuolang.main.model.Notice;
import com.zhuolang.main.model.Share;
import com.zhuolang.main.utils.SharedPrefsUtil;
import com.zhuolang.main.view.CustomWaitDialog;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by wnf on 2016/11/1.
 */

public class ShareInfoListActivity extends Activity implements View.OnClickListener,AdapterView.OnItemClickListener{

    private ImageView iv_addshare;
    private ImageView imageViewBack;
    private TextView tv_likes;
    private ListView listView;
    private LinearLayout ll_top;
    private static LinearLayout statice_top;
    private long updateTime = 0;
    private MyDatabaseHelper dpHelper;
    private SQLiteDatabase db;
    private SharesAdapter sharesAdapter;
    private List<Share> shareList1 = new ArrayList<>();
    private List<Share> shareList2 = new ArrayList<>();
    private String userAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sharelist);
        userAccount = getIntent().getStringExtra("userId");
        initView();
        initModel();
    }

    private void initView() {
        dpHelper = new MyDatabaseHelper(this, "LibrarySystem.db", null, 1);
        db = dpHelper.getWritableDatabase();
        userAccount = SharedPrefsUtil.getValue(this, APPConfig.ACCOUNT, "");

        imageViewBack = (ImageView) findViewById(R.id.iv_shareinfolist_back);
        iv_addshare = (ImageView) findViewById(R.id.iv_shareinfolist_add);
        listView = (ListView) findViewById(R.id.lv_shareinfolist_list);
        tv_likes = (TextView) findViewById(R.id.tv_item_sharelist_likes);
//        ll_top = (LinearLayout) findViewById(R.id.ll_sharelist_top);
        statice_top=(LinearLayout) findViewById(R.id.ll_sharelist_top);
        listView.setOnItemClickListener(this);

        imageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        iv_addshare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ShareInfoListActivity.this, AddSharesActivity.class);
                startActivity(intent);
                finish();
            }
        });
        statice_top.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((System.currentTimeMillis() - updateTime) > 1000) {
//                    Toast.makeText(getApplicationContext(), "单击", Toast.LENGTH_SHORT).show();
                    updateTime = System.currentTimeMillis();
                    initModel();
                } else {
//                    CustomWaitDialog.show(ShareInfoListActivity.this,"刷新中...");
//                    Intent intent = new Intent(ShareInfoListActivity.this, ShareInfoListActivity.class);
//                    startActivity(intent);
//                    finish();
//                    Toast.makeText(ShareInfoListActivity.this, "双击刷新", Toast.LENGTH_SHORT).show();
                }
            }
        });
//        ll_top.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if ((System.currentTimeMillis() - updateTime) > 1000) {
////                    Toast.makeText(getApplicationContext(), "再按一次退出", Toast.LENGTH_SHORT).show();
//                    updateTime = System.currentTimeMillis();
//                } else {
//                    Toast.makeText(ShareInfoListActivity.this, "双击刷新", Toast.LENGTH_SHORT).show();
//                    initModel();
//                }
//            }
//        });
    }

    private void initModel() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                shareList1.clear();
                shareList2.clear();
                Message message = new Message();
                message.what = 0;
                message.obj = "false";
                Cursor cursor = db.query("share_tab", null, null, null, null, null, null, null);
                if (!cursor.moveToFirst()) {
                    Log.d("sharelist", "  if (!cursor.moveToFirst()) {");
                } else {
                    do {
                        Share share = new Share();
                        share.setImageShareLike(false);
                        share.setUserId(cursor.getString(cursor.getColumnIndex("ShareUserId")));
                        Cursor cursoruser = db.query("user_tab", null, null, null, null, null, null, null);
                        if (!cursoruser.moveToFirst()) {
                            Log.d("sharelist", " if (!cursoruser.moveToFirst()) {");
                        } else {
                            do {
                                Log.d("sharelist", " userid = cursoruser.getString(cursoruser.getColumnIndex(\"UserId\"));");
                                String userid = cursoruser.getString(cursoruser.getColumnIndex("UserId"));
                                if (userid.equals(share.getUserId())) {
                                    share.setUserName(cursoruser.getString(cursoruser.getColumnIndex("UserName")));
                                    share.setShareId(cursor.getString(cursor.getColumnIndex("ShareId")));
                                    share.setShareTime(cursor.getString(cursor.getColumnIndex("ShareTime")));
                                    share.setShareLikes(cursor.getString(cursor.getColumnIndex("ShareLike")));
                                    share.setShareContent(cursor.getString(cursor.getColumnIndex("ShareContent")));
                                    //当前用户是否已点过赞
                                    Cursor cursorlikes= db.query("likes_tab", null, null, null, null, null, null, null);
                                    if (!cursorlikes.moveToFirst()) {

                                    } else {
                                        do {
                                            String shareId = cursorlikes.getString(cursorlikes.getColumnIndex("ShareId"));
                                            String userlikeid = cursorlikes.getString(cursorlikes.getColumnIndex("UserLikesId"));
                                            if (shareId.equals(share.getShareId()) && userlikeid.equals(userAccount)) {
                                                share.setImageShareLike(true);
                                                break;
                                            }
                                        } while (cursorlikes.moveToNext());
                                        cursorlikes.close();
                                    }
                                    shareList1.add(share);
                                    message.obj = "true";
                                    break;
                                }
                            } while (cursoruser.moveToNext());
                            cursoruser.close();
                        }

                    } while (cursor.moveToNext());
                    cursor.close();
                }
                if (shareList1.size() > 0) {
                    message.obj = "true";
                    for (int i = shareList1.size() - 1; i >= 0; i--) {
                        shareList2.add(shareList1.get(i));
                    }
                }
                handler.sendMessage(message);
            }
        }).start();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(ShareInfoListActivity.this, ShareSpecificInfoActivity.class);
        intent.putExtra("ShareId", shareList2.get(position).getShareId());
        intent.putExtra("ShareUserId", shareList2.get(position).getUserId());
        intent.putExtra("ShareUserName", shareList2.get(position).getUserName());
        intent.putExtra("ShareTime", shareList2.get(position).getShareTime());
        intent.putExtra("ShareContent", shareList2.get(position).getShareContent());
        intent.putExtra("ShareLike", shareList2.get(position).getShareLikes());
        intent.putExtra("ShareIsImageShareLike", shareList2.get(position).isImageShareLike());

        Log.d("viewid", "view.id=" + view.getId());
        startActivity(intent);
    }

    public static void updateData() {
        statice_top.performClick();
//        statice_top.performClick();
    }


    /**
     * 重新计算ListView的高度，解决ScrollView和ListView两个View都有滚动的效果，在嵌套使用时起冲突的问题
     * @param listView
     */
    public void setListViewHeight(ListView listView) {
//        DisplayMetrics metrics = new DisplayMetrics();
//        float dpToCm = (metrics.density * 2.54f *133*shareList2.size())/ metrics.xdpi;
        // 获取ListView对应的Adapter
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }
        int totalHeight = 0;
        for (int i = 0, len = listAdapter.getCount(); i < len; i++) { // listAdapter.getCount()返回数据项的数目
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0); // 计算子项View 的宽高
            totalHeight += listItem.getMeasuredHeight(); // 统计所有子项的总高度
        }
        totalHeight+=20;
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
            Log.d("sharelist", "public void handleMessage(Message msg) {");
            switch (msg.what) {
                case 0:
                    if (msg.obj.equals("true")) {
                        sharesAdapter = new SharesAdapter(ShareInfoListActivity.this, shareList2);
                        listView.setAdapter(sharesAdapter);
                        setListViewHeight(listView);
                    }else {
                        Toast.makeText(ShareInfoListActivity.this, "没有动态", Toast.LENGTH_SHORT).show();
                    }
                    break;
                default:
                    break;
            }

        }
    };

    @Override
    public void onClick(View v) {

    }
}
