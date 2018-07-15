package com.example.bedirhan.dentalmarket;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.bedirhan.dentalmarket.adapters.ProductAdapter;
import com.example.bedirhan.dentalmarket.app.AppController;
import com.example.bedirhan.dentalmarket.models.Product;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductActivity extends AppCompatActivity {

    private ProductAdapter productAdapter;
    private List<Product> productList=new ArrayList<Product>();
    private TextView cat_name;
    Product product;
    String[] p_id,p_name,p_imageurl,p_price,p_description,p_brand_id,p_shop_id;
    String s_name,s_imageurl,s_telephone,s_email,s_cphone;


    TextView action_bar_textview;
    ImageButton ibtn_basket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        //Action bar - Başlangıç-
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        action_bar_textview=(TextView)findViewById(R.id.urun_sayisi);
        ibtn_basket=(ImageButton)findViewById(R.id.action_bar_basket);

        ibtn_basket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent go_to_basket=new Intent(ProductActivity.this,ShoppingBasketActivity.class);
                startActivity(go_to_basket);
            }
        });
        //Action Bar -SON-
        cat_name=(TextView)findViewById(R.id.cat_name);
        cat_name.setText(getIntent().getStringExtra("cat_name"));



        //MainActivityde çekilen veriler , düzenleniyor.
        p_id=new String[getIntent().getStringArrayExtra("p_id").length];
        p_name=new String[getIntent().getStringArrayExtra("p_id").length];
        p_imageurl=new String[getIntent().getStringArrayExtra("p_id").length];
        p_price=new String[getIntent().getStringArrayExtra("p_id").length];
        p_description=new String[getIntent().getStringArrayExtra("p_id").length];
        p_brand_id=new String[getIntent().getStringArrayExtra("p_id").length];
        p_shop_id=new String[getIntent().getStringArrayExtra("p_id").length];
        //içerikler Product activitye aktarılıyor:
        p_id=getIntent().getStringArrayExtra("p_id");
        p_name=getIntent().getStringArrayExtra("p_name");
        p_imageurl=getIntent().getStringArrayExtra("p_imageurl");
        p_price=getIntent().getStringArrayExtra("p_price");
        p_description=getIntent().getStringArrayExtra("p_description");
        p_brand_id=getIntent().getStringArrayExtra("p_brandid");
        p_shop_id=getIntent().getStringArrayExtra("p_shopid");

        //Ürünler ve ürünler listesi oluşturuluyor...
        for(int i=0;i<p_id.length;i++)
        {
            product=new Product(p_id[i],p_name[i],p_imageurl[i],p_price[i],p_brand_id[i],p_shop_id[i],p_description[i]);
            productList.add(product);
        }

        ListView listView=(ListView)findViewById(R.id.list);//ListView tanıtılıyor..
        productAdapter=new ProductAdapter(this,productList);
        listView.setAdapter(productAdapter);

        //listview 'deki ürünlere tıklandığında ürün detay sayfasına gidiliyor..
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                getProductDetail(productList.get(i).getProduct_image_url(),productList.get(i).getProduct_name(),productList.get(i).getProduct_description(),productList.get(i).getProduct_price(),productList.get(i).getShop_id());

            }
        });
    }

    public void getProductDetail(String product_image, String product_name, String p_description, String p_price, final String shop_id)
    {
       final String p_i,p_n,p_d,p_p;
        p_i=product_image;
        p_n=product_name;
        p_d=p_description;
        p_p=p_price;

        StringRequest stringRequest = new StringRequest(Request.Method.POST, ShopUrl.url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Intent productDetailIntent=new Intent(ProductActivity.this,ProductDetailActivity.class);
                        productDetailIntent.putExtra("p_image",p_i);
                        productDetailIntent.putExtra("p_name",p_n);
                        productDetailIntent.putExtra("p_description",p_d);
                        productDetailIntent.putExtra("p_price",p_p);
                        productDetailIntent.putExtra("shop_id",shop_id);

                        Log.d("Cevap",response);
                        JSONArray jsonArray;

                        try {
                            jsonArray=new JSONArray(response);
                                    //Gelenveriyi json objelere ayırıyoruz.
                                  JSONObject obj = jsonArray.getJSONObject(0);
                                    //ayrılan objelerin özellikllerini String lere atıyoruz...
                            s_name=obj.getString("store_name");
                            s_telephone= obj.getString("phone");
                            s_email=obj.getString("email");
                            s_imageurl=obj.getString("photo");
                            s_cphone=obj.getString("mobile_phone");

                        }
                        catch (JSONException e)
                        {
                            e.printStackTrace();
                        }

                        productDetailIntent.putExtra("s_image",s_imageurl);
                        productDetailIntent.putExtra("s_name",s_name);
                        productDetailIntent.putExtra("s_telephone",s_telephone);
                        productDetailIntent.putExtra("s_email",s_email);
                        productDetailIntent.putExtra("s_cphone",s_cphone);
                        startActivity(productDetailIntent);

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ProductActivity.this,error.toString(),Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("shop_id",shop_id);
                return params;
            }

        };
        AppController.getInstance().addToRequestQueue(stringRequest);
    }

    @Override
    protected void onStart() {
        super.onStart();
        int size=Product_Count.Count(getApplicationContext());
        //Eğer ürün var ise sepetteki sayıyı ayarla
        if(size>0)
        {
            action_bar_textview.setVisibility(View.VISIBLE);
            action_bar_textview.setText(String.valueOf(size));
        }
    }
    @Override
    protected void onRestart() {
        super.onRestart();
        int size=Product_Count.Count(getApplicationContext());

        //Eğer ürün var ise sepetteki sayıyı ayarla
        if(size>0)
        {
            action_bar_textview.setVisibility(View.VISIBLE);
            action_bar_textview.setText(String.valueOf(size));
        }
    }
}
