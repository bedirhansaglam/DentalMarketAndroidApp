package com.example.bedirhan.dentalmarket;

import android.app.ActionBar;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.text.TextDirectionHeuristicCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.bedirhan.dentalmarket.app.AppController;
import com.example.bedirhan.dentalmarket.models.Product;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ProductDetailActivity extends AppCompatActivity {

    ImageView p_image,s_image;
    TextView p_name,p_description,p_price,s_name,s_telephone,s_email,s_cphone;
    ImageButton btn_detay;
    LinearLayout ly_description,ly_s_detail,ll_shops;
    int kontrol=1;
    String shop_id;
    Button btn_add_basket;
    Spinner sp_urun_adet;

    //_s takılı değişkenler satıcıya ait ürünleri bulmak için oluşturuldu
    String[] p_id_s,p_name_s,p_imageurl_s,p_price_s,p_description_s,p_brand_id_s,p_shop_id_s;//ürün özellikleri için değişken
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);


        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#337ab7")));

        sp_urun_adet=(Spinner)findViewById(R.id.urun_adet);
        //Layout dosyasındaki toollar tanımlanıyor...
        p_image=(ImageView)findViewById(R.id.product_image);
        p_name=(TextView)findViewById(R.id.product_name);
        p_description=(TextView)findViewById(R.id.product_description);
        p_price=(TextView)findViewById(R.id.p_price);

        s_image=(ImageView)findViewById(R.id.shop_image);
        s_name=(TextView)findViewById(R.id.shop_name);
        s_telephone=(TextView)findViewById(R.id.s_telefon);
        s_email=(TextView)findViewById(R.id.s_eposta);
        s_cphone=(TextView)findViewById(R.id.s_ceptel);
        shop_id=getIntent().getStringExtra("shop_id");
        btn_detay=(ImageButton)findViewById(R.id.btn_firma_detay);

        ly_description=(LinearLayout)findViewById(R.id.ly_main);
        ly_s_detail=(LinearLayout)findViewById(R.id.ly_shop_detail);
        ll_shops=(LinearLayout)findViewById(R.id.ll_shopsproducts);


        btn_add_basket=(Button)findViewById(R.id.btn_add_basket);









        //tanımlanan toollar set ediliyor...
        p_name.setText(getIntent().getStringExtra("p_name"));

        //Android sürümü kontrol ediliyor 6.0 dan büyükse if kısmı uygulanıyor
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            p_description.setText(Html.fromHtml(getIntent().getStringExtra("p_description"),Html.FROM_HTML_MODE_LEGACY));
        } else {//6.0dan küçükse bu kısım uygulanıyor
            p_description.setText(Html.fromHtml(getIntent().getStringExtra("p_description")));
        }

       //null kontrolü
       if(!getIntent().getStringExtra("p_price").isEmpty() && !getIntent().getStringExtra("p_price").equals("null") && getIntent().getStringExtra("p_price")!=null)
       {
           p_price.setText(getIntent().getStringExtra("p_price")+" ₺");
       }
       else{
           p_price.setText("");}


      //  p_price.setText(NullControl(getIntent().getStringExtra("p_price"))+" ₺");
        Picasso.with(this).load("http://dental-market-medanis-1.c9users.io/"+getIntent().getStringExtra("p_image")).into(p_image);

        Picasso.with(this).load("http://dental-market-medanis-1.c9users.io/"+getIntent().getStringExtra("s_image")).into(s_image);
        s_name.setText(NullControl(getIntent().getStringExtra("s_name")));
        s_telephone.setText(NullControl(getIntent().getStringExtra("s_telephone")));
        s_email.setText(NullControl(getIntent().getStringExtra("s_email")));
        s_cphone.setText(NullControl(getIntent().getStringExtra("s_cphone")));


        //Firmadetay bölümü firmayla ilgili daha fazla bilgiye sahip olmak için tıklanan buton.
        btn_detay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Burada LinearLayoutlarla gizli bir panel yapılmıştır.LL'ın boyutları değiştirilerek panel gösterilip gizlenmektedir...
                //Eğer kontrol 1 e eşitse ok yönünü aşağıya çevir ve paneli göster
                if(kontrol==1)
                {kontrol=2;btn_detay.setBackgroundResource(R.drawable.downarrow);
                final LinearLayout.LayoutParams params=(LinearLayout.LayoutParams)ly_s_detail.getLayoutParams();
                final LinearLayout.LayoutParams params2=(LinearLayout.LayoutParams)ly_description.getLayoutParams();
                params.weight=1.5f;
                params2.weight=1.0f;
               ly_s_detail.setLayoutParams(params);
               ly_description.setLayoutParams(params2);

                }
                //değilse Eğer kontrol 2 ye eşitse ok yönünü yukarıya çevir ve paneli gizle
                else if(kontrol==2)
                {
                    kontrol=1;btn_detay.setBackgroundResource(R.drawable.uparrow);
                    final LinearLayout.LayoutParams params=(LinearLayout.LayoutParams)ly_s_detail.getLayoutParams();
                    final LinearLayout.LayoutParams params2=(LinearLayout.LayoutParams)ly_description.getLayoutParams();
                    params.weight=0.0f;
                    params2.weight=3.0f;
                    ly_s_detail.setLayoutParams(params);
                    ly_description.setLayoutParams(params2);

                }

            }
        });

        //MAĞAZAYA AİT BÜTÜN ÜRÜNLERİ GÖRMEK İÇİN KULLANILAN BUTON
        ll_shops.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
           getProducts(shop_id);
            }
        });

        //Ürün resmine tıklandığında resmin büyümesi için alan
        p_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ProductDetailActivity.this);
                final ImageView resim = new ImageView(ProductDetailActivity.this);
                Picasso.with(ProductDetailActivity.this).load("http://dental-market-medanis-1.c9users.io/"+getIntent().getStringExtra("p_image")).into(resim); // Resmi internettten çektiğim için Picasso yu kullanıyorum.Sen direkt ImageView dan src oalrak ta alabilirsin tabi
                builder.setView(resim);
                builder.setTitle(getIntent().getStringExtra("p_name"));
                builder.setNegativeButton(R.string.close, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
            }
        });

        //Sepete ekle butonu -başlangıç-
        btn_add_basket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences prefs = getApplicationContext().getSharedPreferences("basket7",MODE_PRIVATE);
                int size=prefs.getInt("p_name_size",0);
                int control=0;
                for(int i=1;i<size;i++)
                {
                    int isdelete=prefs.getInt("p_delete_"+String.valueOf(i),0);
                    if(isdelete==0)//Önceden silinmiş öğe var ise onun yerine yeni ürünü ekle
                    {
                        sizeControl(i,String.valueOf(p_name.getText()),getIntent().getStringExtra("p_image"),String.valueOf(sp_urun_adet.getSelectedItemId()+1),getIntent().getStringExtra("p_price"),String.valueOf(s_name.getText()),getApplicationContext());
                        control=1; // ürün eklenmiş ise yeni alan oluşturmaya ihtiyaç kalmadan önceden açılmış bölüme eklenmiştir
                        break;
                    }

                }

                if(control==0) //Önceden açılmış alan olmadığı için yeni bir alan açılarak yeni ürün bu bölüme eklenir.
                {addBasket(String.valueOf(p_name.getText()),getIntent().getStringExtra("p_image"),String.valueOf(sp_urun_adet.getSelectedItemId()+1),getIntent().getStringExtra("p_price"),String.valueOf(s_name.getText()),getApplicationContext());}

                //Sepete ekleme işlemi gerçekleştikten sonra kullanıcıya bir mesaj verilir.
                AlertDialog.Builder message = new AlertDialog.Builder(ProductDetailActivity.this);
                message.setTitle(R.string.card_ok);
                message.setNegativeButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                message.show();
            }
        });
        //Sepete ekle butonu -son-

    }

    protected String NullControl(String value)
    {
        if(!value.isEmpty() && !value.equals("null") && value!=null)
        {
            return value;
        }
        else return "-";
    }

    private void getProducts(final String shop_id)
    {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ShopsProductUrl.url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        JSONArray jsonArray;

                        Intent productIntent=new Intent(ProductDetailActivity.this,ShopsProductsActivity.class);
                        Log.d("Urunler",response);

                        try {
                            jsonArray=new JSONArray(response);


                            p_id_s=new String[jsonArray.length()];
                            p_shop_id_s=new String[jsonArray.length()];
                            p_price_s=new String[jsonArray.length()];
                            p_brand_id_s=new String[jsonArray.length()];
                            p_name_s=new String[jsonArray.length()];
                            p_description_s=new String[jsonArray.length()];
                            p_imageurl_s=new String[jsonArray.length()];

                            for (int i = 0; i < jsonArray.length(); i++) {
                                try {

                                    //Gelenveriyi json objelere ayırıyoruz.
                                    JSONObject obj = jsonArray.getJSONObject(i);
                                    //  Product product=new Product(obj.getString("product_id"),obj.getString("title"),obj.getString("image"),obj.getString("price"),obj.getString("brand_id"),obj.getString("shop_id"),obj.getString("description"));
                                    //  productList.add(product);
                                    //ayrılan objelerin özellikllerini String[] lere atıyoruz...


                                    p_id_s[i]= obj.getString("product_id");
                                    p_shop_id_s[i]= obj.getString("shop_id");
                                    p_imageurl_s[i]= obj.getString("image");
                                    p_name_s[i]=obj.getString("title");
                                    p_brand_id_s[i]=obj.getString("brand_id");
                                    p_description_s[i]= obj.getString("description");
                                    p_price_s[i]=obj.getString("price");

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }

                        }
                        catch (JSONException e)
                        {
                            e.printStackTrace();
                        }

                        productIntent.putExtra("p_id",p_id_s);
                        productIntent.putExtra("p_name",p_name_s);
                        productIntent.putExtra("p_imageurl",p_imageurl_s);
                        productIntent.putExtra("p_description",p_description_s);
                        productIntent.putExtra("p_shopid",p_shop_id_s);
                        productIntent.putExtra("p_price",p_price_s);
                        productIntent.putExtra("p_brandid",p_brand_id_s);
                        productIntent.putExtra("shop_logo","http://dental-market-medanis-1.c9users.io/"+getIntent().getStringExtra("s_image"));
                        productIntent.putExtra("shop_name",s_name.getText());
                        productIntent.putExtra("shop_id",shop_id);
                        startActivity(productIntent);

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ProductDetailActivity.this,error.toString(),Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("shop_id",shop_id);
                Log.d("parametre",String.valueOf(params));
                return params;
            }

        };

        AppController.getInstance().addToRequestQueue(stringRequest);

    }


    /*---Başlangıç------------ Sepete ürün eklemek için iki fonksiyon yazıldı,addBasket ve sizeControl-----------------------*/
    /*
    * addBasket fonksiyonunda sepetin boyutu genişletilerek yeni ürün ekleniyor.*/
    private boolean addBasket(String p_name,String p_imageurl,String p_count,String p_price,String shop_name, Context mContext)
    {
        SharedPreferences prefs = mContext.getSharedPreferences("basket7", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        int size=prefs.getInt("p_name_size",0); //önceki kayıtların sayısı çekiliyor
        int n_size=size+1;
        editor.putInt("p_name_size",n_size); //yeni kayıt için yer açılıyor
        editor.putInt("position_"+String.valueOf(n_size),n_size);
        editor.putInt("p_delete_"+String.valueOf(n_size),1);
        editor.putString("p_name_"+String.valueOf(n_size),p_name); //yeni kayıt p_name_size+1 şeklinde kayıt ediliyor
        editor.putString("p_imageurl_"+String.valueOf(n_size),p_imageurl);
        editor.putString("p_count_"+String.valueOf(n_size),p_count);
        editor.putString("p_price_"+String.valueOf(n_size),p_price);
        editor.putString("shop_name_"+String.valueOf(n_size),shop_name);

        Log.d("Değerler",p_name+" "+p_imageurl+" "+p_count+" "+p_price+" "+shop_name);
        Log.d("Boyut",String.valueOf(n_size));
        return editor.commit();
       // return false;
    }
    /*sizeControl fonksiyonunda sepetin boyutuna göre sepette boş yer varmı diye kontrol ediliyor.Sepette önceden boş yer varsa oraya ekleniyor.*/
    private boolean sizeControl(int size,String p_name,String p_imageurl,String p_count,String p_price,String shop_name, Context mContext)
    {
        SharedPreferences prefs = mContext.getSharedPreferences("basket7", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        int n_size=size;
        editor.putInt("p_delete_"+String.valueOf(n_size),1);
        editor.putInt("position_"+String.valueOf(n_size),n_size);
        editor.putString("p_name_"+String.valueOf(n_size),p_name); //yeni kayıt p_name_size+1 şeklinde kayıt ediliyor
        editor.putString("p_imageurl_"+String.valueOf(n_size),p_imageurl);
        editor.putString("p_count_"+String.valueOf(n_size),p_count);
        editor.putString("p_price_"+String.valueOf(n_size),p_price);
        editor.putString("shop_name_"+String.valueOf(n_size),shop_name);
        return editor.commit();
    }
    /*----Son-------------------*/
}
