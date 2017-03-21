package com.zhuolang.main.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zhuolang.main.R;
import com.zhuolang.main.model.NowLend;
import com.zhuolang.main.model.UserNowLend;
import com.zhuolang.main.ui.activity.NowLendUserinfoActivity;
import com.zhuolang.main.utils.TimeUtil;

import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2016/11/25.
 */
public class UserLendListAdapter extends BaseAdapter {
    private Context context;
    private List<UserNowLend> list;

    private LayoutInflater inflater;
    private ViewHolder holder;

    //初始化把上下文，数据列表传递过来
    public UserLendListAdapter(Context context, List<UserNowLend> list) {
        this.context = context;
        this.list = list;
        //初始化开始初始化布局填充器
        inflater = LayoutInflater.from(context);
        //viewholder是优化listview的
        holder = new ViewHolder();
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        //判断布局有没有填充过，例如一个listview有多个item，只需要在第一个item的时候创建，后面的可以使用已经创建的了，可以省时间和空间
//        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_userlend_list, null);
            //第一次创建这个布局的话就寻找控件，记得是基于这个converView布局寻找
            holder.tv_num = (TextView) convertView.findViewById(R.id.tv_item_userlend_num);
            holder.tv_userName = (TextView) convertView.findViewById(R.id.tv_item_userlend_username);
            holder.tv_bookName = (TextView) convertView.findViewById(R.id.tv_item_userlend_bookname);
            holder.tv_lendTime = (TextView) convertView.findViewById(R.id.tv_item_userlend_lendtime);
            holder.tv_returnTime = (TextView) convertView.findViewById(R.id.tv_item_userlend_returntime);
            holder.tv_days = (TextView) convertView.findViewById(R.id.tv_item_userlend_days);
            holder.tv_bookAmount = (TextView) convertView.findViewById(R.id.tv_item_userlend_bookamcount);
            //第一次填充布局就缓存控件
//            convertView.setTag(holder);
//        } else {
//            holder = (ViewHolder) convertView.getTag();
//        }

        String lendTime=list.get(position).getLendRead().getLoadTime();
        Date date1 = new Date();

        int days = TimeUtil.oleTimeTonowTime(lendTime, TimeUtil.dateToStrNoTime(date1));
        Log.d("testrun", "TimeUtil.dateToStrNoTime(date1)" + TimeUtil.dateToStrNoTime(date1));
        holder.tv_num.setText((position + 1) + "");
        holder.tv_userName.setText(list.get(position).getUserName());
        holder.tv_bookName.setText(list.get(position).getBookName());
        holder.tv_days.setText("已借天数："+days);
        holder.tv_lendTime.setText("借出日期："+lendTime);
        holder.tv_returnTime.setText("最迟还期："+TimeUtil.oleTimeAddDay(lendTime,60));
        holder.tv_bookAmount.setText("已借："+list.get(position).getLendRead().getNumber()+"本");
        holder.tv_userName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(context, NowLendUserinfoActivity.class);
                intent.putExtra("userId",list.get(position).getLendRead().getUserId());
                context.startActivity(intent);
            }
        });
        return convertView;
    }


    private class ViewHolder {
        TextView tv_num;
        TextView tv_userName;
        TextView tv_bookName;
        TextView tv_lendTime;
        TextView tv_returnTime;
        TextView tv_days;
        TextView tv_bookAmount;
    }

}
