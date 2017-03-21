package com.zhuolang.main.ui.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.zhuolang.main.R;
import com.zhuolang.main.adapter.BookListAdapter;
import com.zhuolang.main.adapter.NoticeListAdapter;
import com.zhuolang.main.common.APPConfig;
import com.zhuolang.main.database.MyDatabaseHelper;
import com.zhuolang.main.model.Book;
import com.zhuolang.main.model.Notice;
import com.zhuolang.main.ui.activity.AddNoticesActivity;
import com.zhuolang.main.ui.activity.BookListDetailActivity;
import com.zhuolang.main.ui.activity.LendBookListDetailActivity;
import com.zhuolang.main.ui.activity.NoticeinfoActivity;
import com.zhuolang.main.ui.activity.ShareInfoListActivity;
import com.zhuolang.main.utils.SharedPrefsUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wnf on 2016/10/29.
 * “分享圈”界面的fragment
 */

public class ShareTabFragment extends Fragment implements AdapterView.OnItemClickListener{

    private View view = null;
    private int userType;
    private MyDatabaseHelper dbHelper;
    private SQLiteDatabase db;
    private NoticeListAdapter adapter;
    private List<Notice> noticeList1 = new ArrayList<>();
    private List<Notice> noticeList2 = new ArrayList<>();
    private ListView listView;
    private LinearLayout ll_share;
    private LinearLayout ll_notice;
    private LinearLayout ll_top;
    private TextView tv_noNotices;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState){

        view=new View(getActivity());

        view = inflater.inflate(R.layout.share, container, false);
        Log.d("activityID", "这个是shareTabFragment----------:" + this.toString());

        dbHelper = new MyDatabaseHelper(getContext(), "LibrarySystem.db", null, 1);
        db = dbHelper.getWritableDatabase();
        userType = SharedPrefsUtil.getValue(getContext(), APPConfig.USERTYPE, 0);

        tv_noNotices = (TextView) view.findViewById(R.id.tv_share_notice);
        ll_share = (LinearLayout) view.findViewById(R.id.ll_share_share);
        ll_notice = (LinearLayout) view.findViewById(R.id.ll_share_notice);
        ll_top = (LinearLayout) view.findViewById(R.id.ll_share_top);
        listView = (ListView) view.findViewById(R.id.lv_share_list);

        if (userType != 1) {
            ll_notice.setVisibility(View.GONE);
            ll_top.setBackgroundResource(R.drawable.listback03);
        }
        listView.setOnItemClickListener(this);
        ll_notice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddNoticesActivity.class);
                startActivity(intent);
//                getActivity().overridePendingTransition(R.anim.enter_anim, R.anim.exit_anim);
                getActivity().finish();
            }
        });
        ll_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ShareInfoListActivity.class);
                startActivity(intent);
//                getActivity().overridePendingTransition(R.anim.enter_anim, R.anim.exit_anim);
            }
        });
        initMotion();
        return view;
    }

    public void initMotion() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                noticeList1.clear();
                noticeList2.clear();
                Message message = new Message();
                message.what = 0;
                message.obj = "false";
                Cursor cursor = db.query("notice_tab", null, null, null, null, null, null, null);
                if (!cursor.moveToFirst()) {
//                    Toast.makeText(getContext(), "还没发布公告", Toast.LENGTH_SHORT).show();
                } else {
                    do {
                        Notice notice = new Notice();
                        notice.setNoticeId(cursor.getString(cursor.getColumnIndex("NoticeId")));
                        notice.setNoticeTitle(cursor.getString(cursor.getColumnIndex("NoticeTitle")));
                        notice.setNoticeTime(cursor.getString(cursor.getColumnIndex("NoticeTime")));
                        notice.setNoticeContent(cursor.getString(cursor.getColumnIndex("NoticeContent")));
                        noticeList1.add(notice);
                    } while (cursor.moveToNext());
                    cursor.close();
                }
                if (noticeList1.size() > 0) {
                    message.obj = "true";
                    for (int i = noticeList1.size() - 1; i >= 0; i--) {
                        noticeList2.add(noticeList1.get(i));
                    }
                }
                handler.sendMessage(message);
            }
        }).start();

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent();
        intent.setClass(getActivity(), NoticeinfoActivity.class);
        intent.putExtra("NoticeId", noticeList2.get(position).getNoticeId());
        intent.putExtra("noticeTitle", noticeList2.get(position).getNoticeTitle());
        intent.putExtra("noticeTime",noticeList2.get(position).getNoticeTime());
        intent.putExtra("noticeTontent", noticeList2.get(position).getNoticeContent());
        startActivity(intent);
        if (userType == 1) {
            getActivity().finish();
        }
    }

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    if (msg.obj.equals("true")) {
                        adapter = new NoticeListAdapter(getContext(), noticeList2);
                        listView.setAdapter(adapter);
                    }else {
                        tv_noNotices.setText("没有公告信息！");
                    }
                    break;
                default:
                    break;
            }

        }
    };
}
