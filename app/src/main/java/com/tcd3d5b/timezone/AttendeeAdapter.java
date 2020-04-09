package com.tcd3d5b.timezone;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

public class AttendeeAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    ArrayList<HashMap<String, Object>> listItem;

    public AttendeeAdapter(Context context, ArrayList<HashMap<String, Object>> listItem) {
        this.mInflater = LayoutInflater.from(context);
        this.listItem = listItem;
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
        public TextView attendeeName, attendeeLocation, attendeeLocalTime, attendeeStandardTime;
    }
    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        ViewHolder holder ;
        if(convertView == null)
        {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.attendee_list, null);
            holder.attendeeName = (TextView)convertView.findViewById(R.id.attendeeName);
            holder.attendeeLocation = (TextView)convertView.findViewById(R.id.attendeeLocation);
            holder.attendeeLocalTime = (TextView)convertView.findViewById(R.id.attendeeLocalTime);
            holder.attendeeStandardTime = (TextView)convertView.findViewById(R.id.attendeeStandardTime);
            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder)convertView.getTag();

        }
        holder.attendeeName.setText((String) listItem.get(position).get("attendeeName"));
        holder.attendeeLocation.setText((String) listItem.get(position).get("attendeeLocation"));
        holder.attendeeLocalTime.setText((String) listItem.get(position).get("attendeeLocalTime"));
        holder.attendeeStandardTime.setText((String) listItem.get(position).get("attendeeStandardTime"));

        return convertView;
    }
}
