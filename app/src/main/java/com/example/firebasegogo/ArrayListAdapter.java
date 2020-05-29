package com.example.firebasegogo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ArrayListAdapter extends ArrayAdapter<Item> {
    private static class ViewHolder {
        TextView name;
        TextView price;
        ImageView img;
    }
    public ArrayListAdapter(Context context, ArrayList<Item> item){
        super(context, R.layout.listview_menu, item);
    }
    @Override
    public android.view.View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Item item = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag
        if (convertView == null) {
            // If there's no view to re-use, inflate a brand new view for row
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.listview_menu, parent, false);
            viewHolder.name = (TextView) convertView.findViewById(R.id.namefield);
            viewHolder.price = (TextView) convertView.findViewById(R.id.pricefield);
            viewHolder.img = (ImageView) convertView.findViewById(R.id.imageView);
            // Cache the viewHolder object inside the fresh view
            convertView.setTag(viewHolder);
        } else {
            // View is being recycled, retrieve the viewHolder object from tag
            viewHolder = (ViewHolder) convertView.getTag();
        }
        // Populate the data from the data object via the viewHolder object
        // into the template view.
        Picasso.get().load(item.getImage()).into(viewHolder.img);
        viewHolder.price.setText(item.getPrice());
        viewHolder.name.setText(item.getName());
        // Return the completed view to render on screen
        return convertView;
    }
}

