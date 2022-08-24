package com.example.inventoryapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    FloatingActionButton addproduct;
    RecyclerView recyclerView;
    SQLiteHelper db;
    List<ProductModel> productModel;
    RecyclerView.Adapter ProductAdapter;
    ProductAdapter.RecyclerViewClickListener listener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addproduct = findViewById(R.id.floatingbutton_addproduct);
        db = new SQLiteHelper(this);
        recyclerView = findViewById(R.id.recyclerview_products);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        productModel = new ArrayList<>();
        setOnClickListener();
        getProduct();

        addproduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    Intent i = new Intent(getApplicationContext(),AddProduct.class);
                    startActivity(i);
            }
        });
    }
    private void getProduct()
    {
        Cursor cursor = db.getAllProduct();
        if(cursor.getCount() == 0)
        {
            Toast.makeText(MainActivity.this,"No item in the Inventory", Toast.LENGTH_SHORT).show();

        }
        else
        {
            while (cursor.moveToNext())
            {
                ProductModel product = new ProductModel(
                        cursor.getInt(0)
                        ,cursor.getString(1)
                        ,cursor.getString(2)
                        ,cursor.getDouble(3)
                        ,cursor.getString(4)
                        ,cursor.getInt(6)
                        ,cursor.getDouble(7),
                        cursor.getString(5));
                productModel.add(product);
                ProductAdapter = new ProductAdapter(MainActivity.this,  productModel,listener);
                recyclerView.setAdapter(ProductAdapter);
                ProductAdapter.notifyDataSetChanged();

            }
        }
    }

    private void setOnClickListener() {
        listener = new ProductAdapter.RecyclerViewClickListener() {
            @Override
            public void onClick(View v, int position) {
                Intent intent = new Intent(MainActivity.this, UpdateProduct.class);
                intent.putExtra("intentproduct_id",productModel.get(position).getProductid());
                intent.putExtra("intentproductname",productModel.get(position).getProductname());
                intent.putExtra("intentproductimage",productModel.get(position).getProductimage());
                intent.putExtra("intentproductunit",productModel.get(position).getProductunit());
                intent.putExtra("intentproductprice",productModel.get(position).getProductprice());
                intent.putExtra("intentproductdateexpired",productModel.get(position).getProductExpirydate());
                intent.putExtra("intentproductqty",productModel.get(position).getAvail_inventory());
                intent.putExtra("intentproductinventorycost",productModel.get(position).getProductinventorycost());
                startActivity(intent);
            }
        };
    }

    protected void onResume() {
        super.onResume();
        productModel.clear();
        getProduct();
    }
}