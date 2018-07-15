package com.example.bedirhan.dentalmarket.adapters;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.example.bedirhan.dentalmarket.R;
import com.example.bedirhan.dentalmarket.models.Category;
import com.example.bedirhan.dentalmarket.models.Product;
import com.squareup.picasso.Picasso;

import java.util.List;


public class ProductAdapter extends BaseAdapter {

    private LayoutInflater pInflater;
    private Context pContext;
    private List<Product> productList;

    public ProductAdapter(Activity activity,List<Product> products)
    {
        //XML'i alıp View'a çevirecek inflater'ı örnekleyelim
        pInflater=(LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //gösterilecek listeyi de alalım
        productList=products;
        pContext = activity;
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
    public View getView(int position, View view, ViewGroup viewGroup) {
        View satirView;
        satirView=pInflater.inflate(R.layout.product_line,null);

        TextView textView=(TextView)satirView.findViewById(R.id.product_name);
        ImageView imageView=(ImageView)satirView.findViewById(R.id.product_image);

        Product product=productList.get(position);

        textView.setText(product.getProduct_name());

        if(!product.getProduct_image_url().isEmpty() && !product.getProduct_image_url().equals("null") && product.getProduct_image_url()!=null)
        {
            //Picasso kütüphanesi ile URL den resim çekiyoruz
            Picasso.with(pContext).load("http://dental-market-medanis-1.c9users.io/"+product.getProduct_image_url()).into(imageView);
        }
        else
        {
            Picasso.with(pContext).load("http://dental-market-medanis-1.c9users.io/assets/images/products/thumbs/no-image.png").into(imageView);
            //imageView.setImageResource(R.drawable.categoryicon);
        }

        return satirView;
    }
}
