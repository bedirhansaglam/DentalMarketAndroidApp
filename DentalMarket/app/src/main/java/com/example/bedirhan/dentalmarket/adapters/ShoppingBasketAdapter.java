package com.example.bedirhan.dentalmarket.adapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.bedirhan.dentalmarket.MainActivity;
import com.example.bedirhan.dentalmarket.R;
import com.example.bedirhan.dentalmarket.ShoppingBasketActivity;
import com.example.bedirhan.dentalmarket.models.Product;
import com.example.bedirhan.dentalmarket.models.ShoppingCart;
import com.squareup.picasso.Picasso;

import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class ShoppingBasketAdapter extends BaseAdapter {
    private LayoutInflater sInflater;
    private Context sContext;
    private List<ShoppingCart> productList;


    public ShoppingBasketAdapter(Activity activity,List<ShoppingCart> products)
    {
        //XML'i alıp View'a çevirecek inflater'ı örnekleyelim
        sInflater=(LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //gösterilecek listeyi de alalım
        productList=products;
        sContext = activity;

    }
    @Override
    public int getCount() {
        return productList.size();
    }

    @Override
    public Object getItem(int position) {
        return productList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View view, ViewGroup viewGroup) {
        final View satirView;
        //Bu bölüm sonradan değişecek
        satirView=sInflater.inflate(R.layout.basket_line,null);

        TextView textView=(TextView)satirView.findViewById(R.id.product_name);
        TextView shop_name=(TextView)satirView.findViewById(R.id.shop_name);
        final TextView product_price=(TextView)satirView.findViewById(R.id.product_price);
        ImageView imageView=(ImageView)satirView.findViewById(R.id.product_image);
        final Spinner product_count=(Spinner)satirView.findViewById(R.id.urun_adet);
        Button btn_delete=(Button)satirView.findViewById(R.id.btn_delete);

        final ShoppingCart product=productList.get(position);

        textView.setText(product.getProduct_name());
        shop_name.setText("Satıcı Firma: "+product.getShop_name());
        product_price.setText(product.getProduct_price());
        product_count.setSelection(Integer.parseInt(product.getProduct_count())-1);



      if(!product.getProduct_image_url().isEmpty() && !product.getProduct_image_url().equals("null") && product.getProduct_image_url()!=null)
        {
            //Picasso kütüphanesi ile URL den resim çekiyoruz
            Picasso.with(sContext).load("http://dental-market-medanis-1.c9users.io/"+product.getProduct_image_url()).into(imageView);
        }
        else
        {
            Picasso.with(sContext).load("http://dental-market-medanis-1.c9users.io/assets/images/products/thumbs/no-image.png").into(imageView);
        }

        //Sepetten ürünü sil butonu -başlangıç-
        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                AlertDialog.Builder builder1 = new AlertDialog.Builder(sContext);
                builder1.setMessage("Ürünü silmek istediğinize emin misiniz?");
                builder1.setCancelable(true);

                builder1.setPositiveButton(
                        R.string.yes,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();

                                Intent refresh=new Intent(sContext,ShoppingBasketActivity.class);
                                SharedPreferences prefs = sContext.getSharedPreferences("basket7", MODE_PRIVATE);
                                SharedPreferences.Editor editor = prefs.edit();
                                editor.putInt("p_delete_"+String.valueOf(product.getPosition()),0);
                                editor.commit();

                                //refresh.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                               sContext.startActivity(refresh);


                            }
                        });

                builder1.setNegativeButton(
                        R.string.no,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                AlertDialog alert11 = builder1.create();
                alert11.show();


            }
        });
        //Sepetten ürünü sil butonu -son-

        /* Ürün adeti değiştikçe eş zamanlı olarak fiyatlarında güncellenmesi sağlandı*/
        product_count.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                    SharedPreferences prefs = sContext.getSharedPreferences("basket7", MODE_PRIVATE);
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putString("p_count_" + String.valueOf(product.getPosition()), String.valueOf(product_count.getSelectedItemId() + 1));
                    editor.commit();

                    product.setProduct_count(String.valueOf(product_count.getSelectedItemId() + 1));

                    float fiyat=Float.parseFloat(product.getProduct_count())*Float.parseFloat(product.getProduct_price());
                    product_price.setText(String.valueOf(fiyat));
                    ShoppingBasketActivity.tw_fiyat.setText(String.valueOf(ShoppingBasketActivity.getallPrice(productList)));

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        return satirView;
    }
}
