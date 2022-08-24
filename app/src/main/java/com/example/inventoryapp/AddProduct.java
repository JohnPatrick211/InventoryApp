package com.example.inventoryapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.method.DigitsKeyListener;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.util.Calendar;

public class AddProduct extends AppCompatActivity {

    Button btn_addproduct;
    EditText addproductname, addproductunit, addproductqty, addproductprice;
    TextView addproductexpirydate, selectimage, addproductinventorycost;
    ImageView productimage;
    SQLiteHelper db;
    Bitmap bitmap = null;
    private static final int GALLERY_ADD_IMAGE = 1;
    private int EXTERNAL_STORAGE_PERMISSION_CODE = 23;
    File finalFile;
    private DatePickerDialog datePickerDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);
        db = new SQLiteHelper(this);
        btn_addproduct = findViewById(R.id.btnsaveproduct);
        addproductname = findViewById(R.id.edt_productname);
        addproductunit = findViewById(R.id.edt_productunit);
        addproductprice = findViewById(R.id.edt_productprice);
        addproductprice.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_NUMBER_FLAG_SIGNED);
        addproductqty = findViewById(R.id.edt_productqty);
        addproductqty.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_NUMBER_FLAG_SIGNED);
        addproductqty.setKeyListener(DigitsKeyListener.getInstance("0123456789"));
        addproductexpirydate = findViewById(R.id.tv_productexpirydate);
        addproductinventorycost = findViewById(R.id.tv_productinventorycost);
        productimage = findViewById(R.id.img_product);
        selectimage = findViewById(R.id.txt_selectphoto);
        initDatePicker();

        btn_addproduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    boolean res = db.CreateProduct(addproductname.getText().toString(), addproductunit.getText().toString(), Double.parseDouble(addproductprice.getText().toString()),addproductexpirydate.getText().toString(),Integer.parseInt(addproductqty.getText().toString()),finalFile.getAbsolutePath());
                    if(!res)
                    {
                        Toast.makeText(AddProduct.this,"Error Occurred",Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        Toast.makeText(AddProduct.this,"Product Added",Toast.LENGTH_SHORT).show();
                    }
                }
                catch (Exception e)
                {
                    Toast.makeText(AddProduct.this,"Please Input the Details and Image of the Product",Toast.LENGTH_SHORT).show();
                }
            }
        });
        addproductexpirydate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePickerDialog.show();
            }
        });
        selectimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_PICK);
                i.setType("image/*");
                startActivityForResult(i,GALLERY_ADD_IMAGE);
                ActivityCompat.requestPermissions(AddProduct.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        EXTERNAL_STORAGE_PERMISSION_CODE);
            }
        });

        addproductprice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                Double v1 = Double.parseDouble(!addproductprice.getText().toString().isEmpty() ?
                        addproductprice.getText().toString() : "0.00");
                int v2 = Integer.parseInt(!addproductqty.getText().toString().isEmpty() ?
                        addproductqty.getText().toString() : "0");
                Double value = v1 * v2;
                addproductinventorycost.setText(value.toString());
            }
        });
        addproductqty.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                Double v1 = Double.parseDouble(!addproductprice.getText().toString().isEmpty() ?
                        addproductprice.getText().toString() : "0.00");
                int v2 = Integer.parseInt(!addproductqty.getText().toString().isEmpty() ?
                        addproductqty.getText().toString() : "0");
                Double value = v1 * v2;
                addproductinventorycost.setText(value.toString());
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==GALLERY_ADD_IMAGE && resultCode==RESULT_OK)
        {

            Uri imgUri = data.getData();
            finalFile = new File(getRealPathFromURI(imgUri));

            try {
                bitmap = BitmapFactory.decodeFile(finalFile.getAbsolutePath());
                productimage.setImageBitmap(bitmap);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }
    public String getRealPathFromURI(Uri uri) {
        String path = "";
        if (getContentResolver() != null) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                path = cursor.getString(idx);
                cursor.close();
            }
        }
        return path;
    }

    private void initDatePicker()
    {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener()
        {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day)
            {
                month = month + 1;
                String date = makeDateString(day, month, year);
                addproductexpirydate.setText(date);
            }
        };

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        int style = AlertDialog.THEME_HOLO_LIGHT;

        datePickerDialog = new DatePickerDialog(this, style, dateSetListener, year, month, day);

    }

    private String makeDateString(int day, int month, int year)
    {
        return getMonthFormat(month) + " " + day + " " + year;
    }

    private String getMonthFormat(int month)
    {
        if(month == 1)
            return "January";
        if(month == 2)
            return "February";
        if(month == 3)
            return "March";
        if(month == 4)
            return "April";
        if(month == 5)
            return "May";
        if(month == 6)
            return "June";
        if(month == 7)
            return "July";
        if(month == 8)
            return "August";
        if(month == 9)
            return "September";
        if(month == 10)
            return "October";
        if(month == 11)
            return "November";
        if(month == 12)
            return "December";

        return "January";
    }

}