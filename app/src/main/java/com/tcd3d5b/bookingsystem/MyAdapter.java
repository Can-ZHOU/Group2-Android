package com.tcd3d5b.bookingsystem;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

class MyAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    ArrayList<HashMap<String, Object>> listItem;

    public MyAdapter(Context context,ArrayList<HashMap<String, Object>> listItem) {
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
        public ImageView img;
        public TextView title;
        public TextView text;
        public Button btn;
    }
    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        ViewHolder holder ;
        if(convertView == null)
        {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.item, null);
            holder.title = (TextView)convertView.findViewById(R.id.ItemTitle);
            holder.text = (TextView)convertView.findViewById(R.id.ItemText);
            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder)convertView.getTag();

        }

        holder.title.setText((String) listItem.get(position).get("ItemTitle"));
        holder.text.setText((String) listItem.get(position).get("ItemText"));

        return convertView;
    }
}

