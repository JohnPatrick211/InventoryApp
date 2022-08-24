package com.example.inventoryapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.method.DigitsKeyListener;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.Calendar;

public class UpdateProduct extends AppCompatActivity {

    Button btn_updateproduct,btn_deleteproduct;
    EditText editproductname, editproductunit, editproductqty, editproductprice;
    TextView editproductexpirydate, editselectimage, editproductinventorycost;
    ImageView editproductimage;
    SQLiteHelper db;
    Bitmap bitmap = null;
    private static final int GALLERY_ADD_IMAGE = 1;
    private int EXTERNAL_STORAGE_PERMISSION_CODE = 23;
    File finalFile;
    private DatePickerDialog datePickerDialog;
    boolean isnull = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_product);

        db = new SQLiteHelper(this);
        btn_updateproduct = findViewById(R.id.btnupdateproduct);
        btn_deleteproduct = findViewById(R.id.btndeleteproduct);
        editproductname = findViewById(R.id.edit_edt_productname);
        editproductunit = findViewById(R.id.edit_edt_productunit);
        editproductprice = findViewById(R.id.edit_edt_productprice);
        editproductprice.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_NUMBER_FLAG_SIGNED);
        editproductqty = findViewById(R.id.edit_edt_productqty);
        editproductqty.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_NUMBER_FLAG_SIGNED);
        editproductqty.setKeyListener(DigitsKeyListener.getInstance("0123456789"));
        editproductexpirydate = findViewById(R.id.edit_tv_productexpirydate);
        editproductinventorycost = findViewById(R.id.edit_tv_productinventorycost);
        editproductimage = findViewById(R.id.edit_img_product);
        editselectimage = findViewById(R.id.edit_txt_selectphoto);
        int productid = getIntent().getExtras().getInt("intentproduct_id");
        editproductname.setText(getIntent().getExtras().getString("intentproductname"));
        editproductunit.setText(getIntent().getExtras().getString("intentproductunit"));
        String convert_price = new Double(getIntent().getExtras().getDouble("intentproductprice")).toString();
        editproductprice.setText(convert_price);
        editproductexpirydate.setText(getIntent().getExtras().getString("intentproductdateexpired"));
        Picasso.get().load(new File(getIntent().getExtras().getString("intentproductimage"))).into(editproductimage);
        String convert_qty = new Integer(getIntent().getExtras().getInt("intentproductqty")).toString();
        editproductqty.setText(convert_qty);
        String convert_inventorycost = new Double(getIntent().getExtras().getDouble("intentproductinventorycost")).toString();
        editproductinventorycost.setText(convert_inventorycost);
        String convert_id =  new Integer(productid).toString();
        String firstimage = getIntent().getExtras().getString("intentproductimage");
        initDatePicker();
        btn_updateproduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if(isnull == false)
                    {
                        boolean isUpdate = db.updateProduct(convert_id, editproductname.getText().toString(),editproductunit.getText().toString()
                                ,Double.parseDouble(editproductprice.getText().toString()),editproductexpirydate.getText().toString()
                                ,Integer.parseInt(editproductqty.getText().toString()),firstimage);
                        if(isUpdate == true)
                            Toast.makeText(UpdateProduct.this,"Product Data Update",Toast.LENGTH_LONG).show();
                        else
                            Toast.makeText(UpdateProduct.this,"Product Data not Updated",Toast.LENGTH_LONG).show();
                    }
                    else
                    {
                        boolean isUpdate = db.updateProduct(convert_id, editproductname.getText().toString(),editproductunit.getText().toString()
                                ,Double.parseDouble(editproductprice.getText().toString()),editproductexpirydate.getText().toString()
                                ,Integer.parseInt(editproductqty.getText().toString()),finalFile.getAbsolutePath());
                        if(isUpdate == true)
                            Toast.makeText(UpdateProduct.this,"Product Data Update",Toast.LENGTH_LONG).show();
                        else
                            Toast.makeText(UpdateProduct.this,"Product Data not Updated",Toast.LENGTH_LONG).show();
                    }
                }catch(Exception e)
                {
                    Toast.makeText(UpdateProduct.this,"Please Input the Details of the Product",Toast.LENGTH_SHORT).show();
                }
            }
        });

        btn_deleteproduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(UpdateProduct.this);
                builder.setMessage("Are you sure you want to delete this Product?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        int deletedRows = db.deleteProduct(convert_id);
                        if(deletedRows > 0)
                        {
                            Toast.makeText(UpdateProduct.this,"Product Data Deleted",Toast.LENGTH_LONG).show();
                            onBackPressed();
                        }
                        else
                        {
                            Toast.makeText(UpdateProduct.this,"Product Data not Deleted",Toast.LENGTH_LONG).show();
                        }
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                builder.show();
            }
        });

        editproductexpirydate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePickerDialog.show();
            }
        });
        editselectimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_PICK);
                i.setType("image/*");
                startActivityForResult(i,GALLERY_ADD_IMAGE);
                ActivityCompat.requestPermissions(UpdateProduct.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        EXTERNAL_STORAGE_PERMISSION_CODE);
            }
        });

        editproductprice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                Double v1 = Double.parseDouble(!editproductprice.getText().toString().isEmpty() ?
                        editproductprice.getText().toString() : "0.00");
                int v2 = Integer.parseInt(!editproductqty.getText().toString().isEmpty() ?
                        editproductqty.getText().toString() : "0");
                Double value = v1 * v2;
                editproductinventorycost.setText(value.toString());
            }
        });
        editproductqty.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                Double v1 = Double.parseDouble(!editproductprice.getText().toString().isEmpty() ?
                        editproductprice.getText().toString() : "0.00");
                int v2 = Integer.parseInt(!editproductqty.getText().toString().isEmpty() ?
                        editproductqty.getText().toString() : "0");
                Double value = v1 * v2;
                editproductinventorycost.setText(value.toString());
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
                editproductimage.setImageBitmap(bitmap);
                isnull = true;
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
                editproductexpirydate.setText(date);
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