package com.example.firebasegogo;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CartAdapter extends BaseAdapter {

    private Context context;
    private int layout;
    private ArrayList<Cart> cartItems;
    DatabaseReference databaseReference;
    public CartAdapter(Context context, int layout, ArrayList<Cart> cartItems) {
        this.context = context;
        this.layout = layout;
        this.cartItems = cartItems;
    }

    @Override
    public int getCount() {
        return cartItems.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            holder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(layout, null);
            holder.textViewName = (TextView) convertView.findViewById(R.id.textViewName);
            holder.editTextQuantity = (EditText) convertView.findViewById(R.id.editTextQuantity);
            holder.btnRemove = (Button) convertView.findViewById(R.id.btnRemove);
            holder.textMenutype = (TextView) convertView.findViewById(R.id.menutype);
            convertView.setTag(holder);
        }

        else {
            holder = (ViewHolder) convertView.getTag();
        }

        Cart item = cartItems.get(position);

        holder.textViewName.setText(item.getName());
        Integer temp = item.getQuantity();
        holder.editTextQuantity.setText(temp.toString());
        holder.textMenutype.setText(item.getType());
        holder.btnRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteItem(cartItems.get(position));
                cartItems.remove(position);
                notifyDataSetChanged();
            }
        });

        return convertView;
    }

    private class ViewHolder {
        TextView textViewName;
        EditText editTextQuantity;
        Button btnRemove;
        TextView textMenutype;
    }
    public void deleteItem(final Cart temp){
        databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://fir-testing-d686c.firebaseio.com/Orders");
        Query itemQuery = databaseReference.orderByChild("Name").equalTo(temp.getName());
        itemQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds: dataSnapshot.getChildren()) {
                    ds.getRef().removeValue();
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("TAG", "onCancelled", databaseError.toException());
            }
        });
    }
}
