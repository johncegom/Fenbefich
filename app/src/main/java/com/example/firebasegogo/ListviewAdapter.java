package com.example.firebasegogo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

public class ListviewAdapter extends ArrayAdapter<String> {
    private String[] name;
    private String[] price;
    private String[] img;
    private Context context;
    public ListviewAdapter(Context context, String[] name, String[] price, String[] img){
        super(context, R.layout.listview_menu);
        this.context = context;
        this.name = name;
        this.price = price;
        this.img = img;
    }
    static class ViewHolder{
        TextView tv1;
        TextView tv2;
        ImageView img;
    }
    @Override
    public int getCount(){
        return name.length;
    }
    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        ViewHolder viewHolder = new ViewHolder();
        if(convertView == null){
            LayoutInflater layoutInflater = (LayoutInflater) context.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            assert layoutInflater != null;
            convertView = layoutInflater.inflate(R.layout.listview_menu,parent,false);
            viewHolder.tv1 = (TextView) convertView.findViewById(R.id.namefield);
            viewHolder.tv2 = (TextView) convertView.findViewById(R.id.pricefield);
            viewHolder.img = (ImageView) convertView.findViewById(R.id.imageView);
            convertView.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Picasso.get().load(img[position]).into(viewHolder.img);
        viewHolder.tv2.setText(price[position]);
        viewHolder.tv1.setText(name[position]);
        return convertView;
    }
}
