package com.example.firebasegogo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ArrayListAdapter extends ArrayAdapter<Item> implements Filterable {
    ArrayList<Item> Items;
    CustomFilter filter;
    ArrayList<Item> filterList;

    private static class ViewHolder {
        TextView name;
        TextView price;
        ImageView img;
    }
    public ArrayListAdapter(Context context, ArrayList<Item> item){
        super(context, R.layout.listview_menu, item);
        this.Items = item;
        this.filterList = item;
    }
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return Items.size();
    }
    @Override
    public Item getItem(int pos) {
        // TODO Auto-generated method stub
        return Items.get(pos);
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
    @Override
    public Filter getFilter() {
        // TODO Auto-generated method stub
        if(filter == null)
        {
            filter=new CustomFilter();
        }

        return filter;
    }
    class CustomFilter extends Filter
    {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            // TODO Auto-generated method stub

            FilterResults results=new FilterResults();

            if(constraint != null && constraint.length()>0)
            {
                //CONSTARINT TO UPPER
                constraint=constraint.toString().toUpperCase();

                ArrayList<Item> filters=new ArrayList<Item>();

                //get specific items
                for(int i=0;i<filterList.size();i++)
                {
                    if(filterList.get(i).getName().toUpperCase().contains(constraint))
                    {
                        Item p=new Item(filterList.get(i).getName(),filterList.get(i).getPrice(), filterList.get(i).getImage());

                        filters.add(p);
                    }
                }

                results.count=filters.size();
                results.values=filters;

            }else
            {
                results.count=filterList.size();
                results.values=filterList;

            }

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            // TODO Auto-generated method stub

            Items=(ArrayList<Item>) results.values;
            notifyDataSetChanged();
        }

    }

}

