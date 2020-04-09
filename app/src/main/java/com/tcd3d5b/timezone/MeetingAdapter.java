package com.tcd3d5b.timezone;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

public class MeetingAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private Context context;
    ArrayList<HashMap<String, Object>> listItem;

    public MeetingAdapter(Context context, ArrayList<HashMap<String, Object>> listItem) {
        this.mInflater = LayoutInflater.from(context);
        this.listItem = listItem;
        this.context = context;
    }

    @Override
    public int getCount() {
        return listItem.size();
    }

    @Override
    public Object getItem(int position) {
        return listItem.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    static class ViewHolder
    {
        public TextView meetingName, meetingDate, meetingTime, meetingDuration, meetingID;
        public Button button_detail;
    }
    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        MeetingAdapter.ViewHolder holder ;
        if(convertView == null)
        {
            holder = new MeetingAdapter.ViewHolder();
            convertView = mInflater.inflate(R.layout.item_list, null);
            holder.meetingName = (TextView)convertView.findViewById(R.id.meetingName);
            holder.meetingDate = (TextView)convertView.findViewById(R.id.meetingDate);
            holder.meetingTime = (TextView)convertView.findViewById(R.id.meetingTime);
            holder.meetingDuration = (TextView)convertView.findViewById(R.id.meetingDuration);
            holder.meetingID = (TextView)convertView.findViewById(R.id.meetingID);
            holder.button_detail = (Button)convertView.findViewById((R.id.button_detail));
            convertView.setTag(holder);
        }
        else {
            holder = (MeetingAdapter.ViewHolder)convertView.getTag();

        }
        holder.meetingName.setText((String) listItem.get(position).get("meetingName"));
        holder.meetingDate.setText((String) listItem.get(position).get("meetingDate"));
        holder.meetingTime.setText((String) listItem.get(position).get("meetingTime"));
        holder.meetingDuration.setText((String) listItem.get(position).get("meetingDuration"));
        holder.meetingID.setText((String) listItem.get(position).get("meetingID"));
        holder.button_detail.setText((String) listItem.get(position).get("button_detail"));
        final String str = (String) listItem.get(position).get("meetingID");
        holder.button_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                Intent intent=new Intent();
                bundle.putString("meetingID", str);
                intent.putExtra("meeting_detail", bundle);
                context.startActivity(intent);
            }
        });

        return convertView;
    }
}
