package com.example.bedirhan.dentalmarket;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.ContextThemeWrapper;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.bedirhan.dentalmarket.adapters.CategoryAdapter;
import com.example.bedirhan.dentalmarket.app.AppController;
import com.example.bedirhan.dentalmarket.models.Category;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.security.AccessController.getContext;

public class MainActivity extends AppCompatActivity {

    private CategoryAdapter categoryAdapter;
    private List<Category> categoryList=new ArrayList<Category>();
    Category category;
    String[] c_id,c_name; //kategorileri tutmak için değişken
    String[] p_id,p_name,p_imageurl,p_price,p_description,p_brand_id,p_shop_id;//ürün özellikleri için değişken

    TextView action_bar_textview;
    ImageButton ibtn_basket;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Action bar - Başlangıç-
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        action_bar_textview=(TextView)findViewById(R.id.urun_sayisi);
        ibtn_basket=(ImageButton)findViewById(R.id.action_bar_basket);

        ibtn_basket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                PopupMenu popup = new PopupMenu(MainActivity.this, ibtn_basket);
                //Inflating the Popup using xml file
                popup.getMenuInflater().inflate(R.menu.popup_menu, popup.getMenu());

                //registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {

                        switch (item.getItemId()) {
                            case R.id.sepetim:

                                Intent go_to_basket=new Intent(MainActivity.this,ShoppingBasketActivity.class);
                                startActivity(go_to_basket);
                                return true;
                            case R.id.cikis:

                                return true;


                            default:
                                return true;
                        }

                    }
                });

                popup.show();//showing popup menu

            }
        });
        //Action Bar -SON-



        //SplashScreende çakilen datalar gösterilmek için düzenleniyor.
        c_id=new String[getIntent().getStringArrayExtra("Kategori_id").length];
        c_name=new String[getIntent().getStringArrayExtra("Kategori_ad").length];
        c_id=getIntent().getStringArrayExtra("Kategori_id");
        c_name=getIntent().getStringArrayExtra("Kategori_ad");


        //kategori nesnesi oluşturulup , kategori listesine ekleniyor.
        for(int i=0;i<c_id.length;i++)
        {
           category=new Category(c_id[i],c_name[i],null);
            categoryList.add(category);
        }


        ListView listView=(ListView)findViewById(R.id.list);//ListView tanıtılıyor
        categoryAdapter=new CategoryAdapter(this,categoryList);//Listview'ı dolduracak adapter ayarlanıyor
        listView.setAdapter(categoryAdapter); // adapter set ediliyor.

       listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
           @Override
           public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

               getProducts(categoryList.get(i).getCategory_id(),categoryList.get(i).getCategory_name()); //Kategoriye ait ürünleri getir.
           }
       });

    }


    private void getProducts(final String category_id, final String cat_name)
    {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, ProductUrl.url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        JSONArray jsonArray;

                        Intent productIntent=new Intent(MainActivity.this,ProductActivity.class);
                        Log.d("Urunler",response);

                        try {
                            jsonArray=new JSONArray(response);


                            p_id=new String[jsonArray.length()];
                            p_shop_id=new String[jsonArray.length()];
                            p_price=new String[jsonArray.length()];
                            p_brand_id=new String[jsonArray.length()];
                            p_name=new String[jsonArray.length()];
                            p_description=new String[jsonArray.length()];
                            p_imageurl=new String[jsonArray.length()];

                            for (int i = 0; i < jsonArray.length(); i++) {
                                try {

                                    //Gelenveriyi json objelere ayırıyoruz.
                                    JSONObject obj = jsonArray.getJSONObject(i);
                                    //ayrılan objelerin özellikllerini String[] lere atıyoruz...
                                   p_id[i]= obj.getString("product_id");
                                    p_shop_id[i]= obj.getString("shop_id");
                                    p_imageurl[i]= obj.getString("image");
                                    p_name[i]=obj.getString("title");
                                    p_brand_id[i]=obj.getString("brand_id");
                                    p_description[i]= obj.getString("description");
                                    p_price[i]=obj.getString("sale_price");

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }

                        }
                        catch (JSONException e)
                        {
                            e.printStackTrace();
                        }


                        productIntent.putExtra("p_id",p_id);
                        productIntent.putExtra("p_name",p_name);
                        productIntent.putExtra("p_imageurl",p_imageurl);
                        productIntent.putExtra("p_description",p_description);
                        productIntent.putExtra("p_shopid",p_shop_id);
                        productIntent.putExtra("p_price",p_price);
                        productIntent.putExtra("p_brandid",p_brand_id);
                        productIntent.putExtra("cat_name",cat_name);
                        startActivity(productIntent);

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this,error.toString(),Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("category_id",category_id);
                Log.d("parametre",String.valueOf(params));
                return params;
            }

        };

        AppController.getInstance().addToRequestQueue(stringRequest);

    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK) {
           //maindeyken geri tuşunu pasif et
            return true;
        }
        else {
            return super.onKeyDown(keyCode, event);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        //Ürün sayıları çekiliyor
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
        Log.d("MainActivity","onRestart çalıştı");
        int size=Product_Count.Count(getApplicationContext());
        //Eğer ürün var ise sepetteki sayıyı ayarla
        if(size>0)
        {
            action_bar_textview.setVisibility(View.VISIBLE);
            action_bar_textview.setText(String.valueOf(size));
        }
    }
}
