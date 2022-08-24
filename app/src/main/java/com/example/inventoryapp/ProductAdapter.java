package com.example.inventoryapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.Viewholder> {

    private List<ProductModel> listproduct;
    private Context context;
    private RecyclerViewClickListener listener;

    public ProductAdapter(Context context, List<ProductModel> listproduct, RecyclerViewClickListener listener) {
        this.listproduct = listproduct;
        this.context = context;
        this.listener = listener;
    }

    public interface RecyclerViewClickListener{
        void onClick(View v, int position);
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.cardview_products,parent,false);
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, int position) {
            ProductModel productModel = listproduct.get(position);
            holder.productname.setText(productModel.getProductname());
            String convert_price = new Double(productModel.getProductprice()).toString();
            holder.productprice.setText("Price: ₱ " + convert_price);
            holder.productunit.setText("Unit: " + productModel.getProductunit());
            holder.productdateofexpiry.setText("Date Expiration: " + productModel.getProductExpirydate());
            String convert_qty = new Integer(productModel.getAvail_inventory()).toString();
            holder.productavailableinventory.setText("Qty: "+ convert_qty);
            String convert_inventorycost = new Double(productModel.getProductinventorycost()).toString();
            holder.productinventorycost.setText("Total Cost: ₱ " + convert_inventorycost);
            Picasso.get().load(new File(productModel.getProductimage())).into(holder.productimage);

    }

    @Override
    public int getItemCount() {
        return listproduct.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView productname, productunit, productprice, productdateofexpiry, productavailableinventory,productinventorycost;
        ImageView productimage;

        public Viewholder(@NonNull View itemView) {
            super(itemView);
            productname = itemView.findViewById(R.id.textView_productname);
            productunit = itemView.findViewById(R.id.textView_unit);
            productprice = itemView.findViewById(R.id.textView_price);
            productdateofexpiry = itemView.findViewById(R.id.textView_dateofexpiry);
            productavailableinventory = itemView.findViewById(R.id.textView_availinventory);
            productimage = itemView.findViewById(R.id.imageview_productimage);
            productinventorycost = itemView.findViewById(R.id.textView_inventorycost);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if(listener !=null)
            {
                int position = getAdapterPosition();
                if(position != RecyclerView.NO_POSITION)
                {
                    listener.onClick(view,position);
                }
            }
        }
    }
}
