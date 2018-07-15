package com.example.bedirhan.dentalmarket;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import android.widget.ListView;
import android.widget.TextView;

import com.example.bedirhan.dentalmarket.adapters.ShoppingBasketAdapter;
import com.example.bedirhan.dentalmarket.models.ShoppingCart;

import java.util.ArrayList;
import java.util.List;

import static com.example.bedirhan.dentalmarket.R.id.p_price;
import static com.example.bedirhan.dentalmarket.R.id.tw_fiyat;

public class ShoppingBasketActivity extends AppCompatActivity {

    public static int deneme=0;
    ListView listView;
    private List<ShoppingCart> productList=new ArrayList<ShoppingCart>();
    ShoppingBasketAdapter shopsProductAdapter;
    public static TextView tw_fiyat;
    ShoppingCart product;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_basket);

        listView=(ListView)findViewById(R.id.listview);
        tw_fiyat=(TextView)findViewById(R.id.tw_fiyat);
        /*Sepete eklemek için ürünler çekiliyor...*/
        SharedPreferences prefs = getApplicationContext().getSharedPreferences("basket7",MODE_PRIVATE);
        int size=prefs.getInt("p_name_size",0);
        if(size>0) {
            String p_name[] = new String[size];
            String p_imageurl[] = new String[size];
            String p_count[] = new String[size];
            String shop_name[] = new String[size];
            String p_price[] = new String[size];
            int[] p_delete=new int[size];
            int[] p_position=new int[size];

            for (int i = 0; i < size; i++) {
                p_delete[i]=prefs.getInt("p_delete_"+String.valueOf(i+1),0);
                p_position[i]=prefs.getInt("position_"+String.valueOf(i+1),0);
                p_name[i] = prefs.getString("p_name_"+String.valueOf(i+1), "değer yok");
                p_imageurl[i] = prefs.getString("p_imageurl_"+String.valueOf(i+1), null);
                shop_name[i] = prefs.getString("shop_name_"+String.valueOf(i+1),"");
                p_count[i] = prefs.getString("p_count_" +String.valueOf(i+1), "0");
                p_price[i] = prefs.getString("p_price_" +String.valueOf(i+1), "0");

            }

            for (int i = 0; i <size; i++) {
                 if(p_delete[i]==1){
                     //çekilen ürünler sepete ekleniyor...
                     product = new ShoppingCart(null, p_name[i], p_imageurl[i], p_price[i], shop_name[i], p_count[i],p_position[i]);
                     productList.add(product);
                }

            }

            shopsProductAdapter = new ShoppingBasketAdapter(this, productList);
            shopsProductAdapter.notifyDataSetChanged();
            listView.setAdapter(shopsProductAdapter);
         tw_fiyat.setText(String.valueOf(getallPrice(productList)));


        }
    }

    public static float getallPrice(List<ShoppingCart> productList)
    {
        ShoppingCart p;
        float price=0;
        for(int i=0;i<productList.size();i++)
        {
            p=productList.get(i);
            price+=Float.parseFloat(p.getProduct_price())*Float.parseFloat(p.getProduct_count());
        }

        return price;
    }

}
