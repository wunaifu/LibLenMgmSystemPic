package com.zhuolang.main.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zhuolang.main.R;
import com.zhuolang.main.model.Book;
import com.zhuolang.main.model.Notice;

import java.util.List;

/**
 * Created by Administrator on 2016/11/25.
 */
public class NoticeListAdapter extends BaseAdapter {
    private Context context;
    private List<Notice> list;

    private LayoutInflater inflater;
    private ViewHolder holder;

    //初始化把上下文，数据列表传递过来
    public NoticeListAdapter(Context context, List<Notice> list) {
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
    public View getView(int position, View convertView, ViewGroup parent) {
        //判断布局有没有填充过，例如一个listview有多个item，只需要在第一个item的时候创建，后面的可以使用已经创建的了，可以省时间和空间
//        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_noticelist, null);
            //第一次创建这个布局的话就寻找控件，记得是基于这个converView布局寻找
            holder.tv_noticeTitle = (TextView) convertView.findViewById(R.id.tv_item_noticelist_title);
            holder.tv_noticeTime = (TextView) convertView.findViewById(R.id.tv_item_noticelist_time);
            holder.tv_noticeConten = (TextView) convertView.findViewById(R.id.tv_item_noticelist_content);

            //第一次填充布局就缓存控件
//            convertView.setTag(holder);
//        } else {
//            holder = (ViewHolder) convertView.getTag();
//        }
        if (list.size() <= 0) {

        } else {
            holder.tv_noticeTitle.setText(list.get(position).getNoticeTitle());
            holder.tv_noticeTime.setText(list.get(position).getNoticeTime());
            holder.tv_noticeConten.setText(list.get(position).getNoticeContent());
        }

        return convertView;
    }


    private class ViewHolder {
        TextView tv_noticeTitle;
        TextView tv_noticeTime;
        TextView tv_noticeConten;

    }

}
