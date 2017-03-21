package com.zhuolang.main.adapter;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.zhuolang.main.R;
import com.zhuolang.main.common.APPConfig;
import com.zhuolang.main.database.MyDatabaseHelper;
import com.zhuolang.main.model.NowLend;
import com.zhuolang.main.model.Share;
import com.zhuolang.main.model.User;
import com.zhuolang.main.ui.activity.NowLendUserinfoActivity;
import com.zhuolang.main.utils.SharedPrefsUtil;
import com.zhuolang.main.utils.TimeUtil;

import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2016/11/25.
 */
public class SharesAdapter extends BaseAdapter {
    private Context context;
    private List<Share> list;

    private LayoutInflater inflater;
    private ViewHolder holder;
    private MyDatabaseHelper dpHelper;
    private SQLiteDatabase db;
    private boolean flag = false;
    private String userAccount = "";

    //初始化把上下文，数据列表传递过来
    public SharesAdapter(Context context, List<Share> list) {
        this.context = context;
        this.list = list;
        //初始化开始初始化布局填充器
        inflater = LayoutInflater.from(context);
        //viewholder是优化listview的
        holder = new ViewHolder();
        dpHelper = new MyDatabaseHelper(context, "LibrarySystem.db", null, 1);
        db = dpHelper.getWritableDatabase();
        userAccount = SharedPrefsUtil.getValue(context, APPConfig.ACCOUNT, "");
    }

    @Override//返回listview有多少条数据
    public int getCount() {
        if (list == null) {
            return 0;
        } else {
            return list.size();
        }
    }

    @Override//返回每个listview的item
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }
    /**
     * 这个方法时最重要的
     * 是listview中每个item的设置内容
     * item的显示依靠这个方法
     *
     * @param position    item对应listview中的第几个
     * @param convertView listview中的item子布局
     * @param parent
     * @return
     */
    @Override
    final public View getView(final int position, View convertView, final ViewGroup parent) {
        //判断布局有没有填充过，例如一个listview有多个item，只需要在第一个item的时候创建，后面的可以使用已经创建的了，可以省时间和空间
//        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_sharelist, null);
            //第一次创建这个布局的话就寻找控件，记得是基于这个converView布局寻找
            holder.tv_shareUserName = (TextView) convertView.findViewById(R.id.tv_item_sharelist_username);
            holder.tv_shareTime = (TextView) convertView.findViewById(R.id.tv_item_sharelist_time);
            holder.tv_shareContent = (TextView) convertView.findViewById(R.id.tv_item_sharelist_content);
            holder.tv_shareLikes = (TextView) convertView.findViewById(R.id.tv_item_sharelist_likes);
            holder.iv_like = (ImageView) convertView.findViewById(R.id.iv_item_sharelist_likeimage);
            //第一次填充布局就缓存控件
//            convertView.setTag(holder);
//        } else {
//            holder = (ViewHolder) convertView.getTag();
//        }
        if (list.size() > 0) {
            holder.tv_shareUserName.setText(list.get(position).getUserName());
            holder.tv_shareTime.setText(list.get(position).getShareTime());
            holder.tv_shareContent.setText(list.get(position).getShareContent());
            holder.tv_shareLikes.setText(list.get(position).getShareLikes());
            if (list.get(position).isImageShareLike() == true) {
                holder.iv_like.setImageResource(R.drawable.islike01);
            } else {
                holder.iv_like.setImageResource(R.drawable.like02);
            }
        }
        holder.tv_shareUserName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, NowLendUserinfoActivity.class);
                intent.putExtra("userId", list.get(position).getUserId());
                context.startActivity(intent);
            }
        });

        holder.iv_like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean yOnlikes = false;
                String likesId = "";
                Cursor cursor = db.query("likes_tab", null, null, null, null, null, null, null);
                if (!cursor.moveToFirst()) {

                } else {
                    do {
                        String shareId = cursor.getString(cursor.getColumnIndex("ShareId"));
                        if (shareId.equals(list.get(position).getShareId())) {
                            String userlikeid = cursor.getString(cursor.getColumnIndex("UserLikesId"));
                            if (userlikeid.equals(userAccount)) {
                                yOnlikes = true;
                                likesId= cursor.getString(cursor.getColumnIndex("LikesId"));
                                break;
                            }
                        }
                    } while (cursor.moveToNext());
                    cursor.close();
                }
                if (yOnlikes != true) {//未点过赞
                    //点赞数加一
                    int num = Integer.parseInt(list.get(position).getShareLikes()) + 1;
                    list.get(position).setShareLikes(num + "");
//                    getView(position, v, parent);
                    //点赞数加一更新到share表
                    ContentValues values = new ContentValues();
                    values.put("ShareLike", num);
                    String[] shareids = new String[1];
                    shareids[0] = list.get(position).getShareId();
                    db.update("share_tab", values, "ShareId = ?", shareids);
                    values.clear();
                    //点赞表添加一条已点赞过的数据
                    ContentValues valuesLikes = new ContentValues();
                    valuesLikes.put("ShareId", list.get(position).getShareId());
                    valuesLikes.put("LikesTime", TimeUtil.dateToString(new Date()));
                    valuesLikes.put("UserLikesId", userAccount);
                    db.insert("likes_tab", null, valuesLikes);
                    valuesLikes.clear();
                    //更新listview中该项的点赞数
                    list.get(position).setImageShareLike(true);
                    notifyDataSetChanged();

                } else {//已点过赞
                    //点赞数减一
                    int num = Integer.parseInt(list.get(position).getShareLikes()) - 1;
                    list.get(position).setShareLikes(num + "");
//                    getView(position, v, parent);
                    //点赞数减一更新到share表
                    ContentValues values = new ContentValues();
                    values.put("ShareLike", num);
                    String[] shareids = new String[1];
                    shareids[0] = list.get(position).getShareId();
                    db.update("share_tab", values, "ShareId = ?", shareids);
                    values.clear();
                    //点赞表删除一条已点赞过的数据
                    String[] likesidStr = new String[1];
                    likesidStr[0] = likesId;
                    db.delete("likes_tab", "LikesId = ?", likesidStr);
                    //更新listview中该项的点赞数
                    list.get(position).setImageShareLike(false);
                    notifyDataSetChanged();
                }
            }
        });

        return convertView;
    }


    private class ViewHolder {

        TextView tv_shareUserName;
        TextView tv_shareTime;
        TextView tv_shareContent;
        TextView tv_shareLikes;
        ImageView iv_like;
    }



}
