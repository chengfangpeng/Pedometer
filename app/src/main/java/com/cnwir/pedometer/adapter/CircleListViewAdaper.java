package com.cnwir.pedometer.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cnwir.pedometer.R;
import com.cnwir.pedometer.domain.Friend;

import java.util.List;

/**
 * Created by heaven on 2015/7/31.
 */
public class CircleListViewAdaper extends BaseAdapter {


    private Context mContext;

    private List<Friend> datas;

    private LayoutInflater inflater;
    public CircleListViewAdaper(Context mContext, List<Friend> datas){

        this.mContext = mContext;
        this.datas = datas;
        inflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public Object getItem(int position) {
        return datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;

        if(convertView == null){
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.circle_listview_item_plus, null);
            holder.avator = (ImageView) convertView.findViewById(R.id.iv_circle_avator);
            holder.realName = (TextView) convertView.findViewById(R.id.circle_realname);
            holder.steps = (TextView) convertView.findViewById(R.id.circle_steps);
            holder.range = (ImageView) convertView.findViewById(R.id.range);
            convertView.setTag(holder);

        }else{

            holder = (ViewHolder) convertView.getTag();
        }

        System.out.println("性别" + datas.get(position).getSex());
        if(1 == Integer.parseInt(datas.get(position).getSex().trim())){

            holder.avator.setImageResource(R.mipmap.man);
        }else{
            holder.avator.setImageResource(R.mipmap.woman);
        }
        holder.range.setVisibility(View.INVISIBLE);
        switch (position){

            case 0:
                holder.range.setVisibility(View.VISIBLE);
                holder.range.setImageResource(R.mipmap.gold);


                break;
            case 1:
                holder.range.setVisibility(View.VISIBLE);
                holder.range.setImageResource(R.mipmap.silver);
                break;
            case 2:
                holder.range.setVisibility(View.VISIBLE);
                holder.range.setImageResource(R.mipmap.copper);
                break;
            default:
                break;

        }
        holder.realName.setText(datas.get(position).getNickname());
        holder.steps.setText(datas.get(position).getTotal());

        return convertView;
    }



    private class ViewHolder{

        ImageView avator, range;
        TextView realName, steps;

    }

}
