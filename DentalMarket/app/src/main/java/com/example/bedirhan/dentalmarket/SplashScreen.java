package com.example.bedirhan.dentalmarket;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.bedirhan.dentalmarket.app.AppController;
import  com.example.bedirhan.dentalmarket.models.Category;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SplashScreen extends AppCompatActivity {

    LinearLayout layout ;//Layout referans
    public String[] category_id,category_name;
    private ProgressDialog pDialog;
    Button btn_login;
    SharedPreferences preferences;//preferences referansı
    SharedPreferences.Editor editor; //preferences editor nesnesi referansı .prefernces nesnesine veri ekleyip cıkarmak için
    EditText et_email,et_password;
    CheckBox checkBox;
    TextView sign_up;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        getSupportActionBar().hide(); //action barı kapat
        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());//preferences objesi
        editor = preferences.edit(); //aynı şekil editor nesnesi oluşturuluyor
        sign_up=(TextView)findViewById(R.id.sign_up);
        btn_login=(Button)findViewById(R.id.btn_login);
        et_email=(EditText)findViewById(R.id.email);
        et_password=(EditText)findViewById(R.id.password);
        checkBox=(CheckBox)findViewById(R.id.checkbox);
        ImageView logo=(ImageView)findViewById(R.id.iw_logo);
        logo.setImageResource(R.drawable.logo);
        layout=(LinearLayout)findViewById(R.id.linear_layout_splash);
        checkBox.setChecked(true);
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Veriler Yükleniyor...");
        StartAnimation();
        //Animasyonu görüntülemek için 3snlik bir timer eklendi , timer sonunda üye girişi yapıldıysa
       new CountDownTimer(3000, 1000) {
            @Override
            //OnTick metodu geri sayım süresince yapılacak değişiklikler
            public void onTick(long millisUntilFinished) {

            }

            @Override
            //süre bittiğinde yapılacaklar
            public void onFinish() {


                //burası 0 yapılacak
                if(preferences.getInt("login",0)==1) //prefencestan bir değer dönmezse bizim girdiğimiz 0 değeri dönecektir.
                {
                    et_email.setText(preferences.getString("email","merhaba"));
                    et_password.setText(preferences.getString("password","dünya"));
                    checkBox.setChecked(true);
                    pDialog.show();
                    getData();
                }
            }
        }.start();

        //Giriş butonu başlangıç
       btn_login.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               String email,password;
               email=String.valueOf(et_email.getText());
               password=String.valueOf(et_password.getText());

               if(email.matches("") || password.matches(""))
               {
               AlertDialog.Builder alertDialog = new AlertDialog.Builder(SplashScreen.this);
               alertDialog.setTitle("Uyarı");
               alertDialog.setMessage("Lütfen bilgileri eksiksiz giriniz");
               alertDialog.setPositiveButton("Tamam", new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog,int which) {

                   }
               });}
               else
               {
                   Login(email,password);
               }

           }
       });
        //Giriş butonu son

        sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signupIntent=new Intent(SplashScreen.this,SignUpActivity.class);
                startActivity(signupIntent);
            }
        });

    }

    private void StartAnimation()
    {
        //Bu animasyon ekran renginin değişmesi ile ilgili
        //Açılışta parlak ve yoğun olan ekran yavas yavas görünür oluor
        Animation anim = AnimationUtils.loadAnimation(this, R.anim.alpha);
        anim.reset();
        LinearLayout l=(LinearLayout) findViewById(R.id.linear_layout_splash);//En dıştaki layout üzerinden yapılıyor
        l.clearAnimation();
        l.startAnimation(anim); //animasyon başlatılıyor

        //Bu animasyon logonun yukarı kaymasını sağlayan animasyon
        //Hızını res/anim altındaki translate.xml dosyasından ayarlayabilirsiniz
        anim = AnimationUtils.loadAnimation(this, R.anim.translate);
        anim.reset();
        ImageView iv = (ImageView) findViewById(R.id.iw_logo);
        iv.clearAnimation();
        iv.startAnimation(anim);

        //Form için animasyon
        Animation animation=AnimationUtils.loadAnimation(this,R.anim.translate_form);
        LinearLayout linearLayout=(LinearLayout)findViewById(R.id.ll_form);
        animation.reset();
        linearLayout.clearAnimation();
        linearLayout.startAnimation(animation);

    }

    public void getData() {
        JsonArrayRequest myRequest=new JsonArrayRequest(Const.url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        hidePDialog();
                        //Verileri Maindeki listeye aktarabilmek için bir Intent oluşturuluyor...
                        Intent Gec=new Intent(SplashScreen.this,MainActivity.class);
                        //Verileri tutmak için String[] ler tanımlanıyor...
                        category_id=new String[response.length()/2];
                        category_name=new String[response.length()/2];

                        for (int i = 0; i < response.length()/2; i++) {
                            try {

                                //Gelenveri json objelere ayırıyoruz.
                                JSONObject obj = response.getJSONObject(i);
                                //ayrılan objelerin özellikllerini Strin[] lere atıyoruz...
                                category_id[i]=obj.getString("category_id");
                                category_name[i]=obj.getString("category");


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }

                        //Çekilen verileri Intente ekliyoruz
                        Gec.putExtra("Kategori_id",category_id);
                        Gec.putExtra("Kategori_ad",category_name);
                        startActivity(Gec);//MainActivity sayfasına geçiyoruz.


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                hidePDialog();

            }
        });

        // Request(İstek)'i Volley'in Requst sırasına atıyoruz
        AppController.getInstance().addToRequestQueue(myRequest);  }

    private void hidePDialog() {
        if (pDialog != null) {
            pDialog.dismiss();
            pDialog = null;
        }
    }

    public void Login(final String email, final String password)
    {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, LoginUrl.url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        int i;
                        i=Integer.valueOf(response);

                        if(i==1)
                        {
                            if(checkBox.isChecked()) //beni hatırla butonu aktif ise
                            {
                            editor.putInt("login",1);
                            editor.putString("email",email);
                            editor.putString("password",password);
                            editor.commit();}
                            else
                            {
                                editor.remove("email");
                                editor.remove("password");
                                editor.commit();
                            }
                            getData();
                        }
                        else if(i==0)
                        {
                            editor.putInt("login",0);
                            editor.commit();
                            AlertDialog.Builder alertDialog = new AlertDialog.Builder(SplashScreen.this);
                            alertDialog.setTitle("Hata");
                            alertDialog.setMessage("Kullanıcı adı veya Şifre hatalı");
                            alertDialog.setPositiveButton("Tamam", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,int which) {

                                }
                            });
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(SplashScreen.this,error.toString(),Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("type","1");
                params.put("email",email);
                params.put("password",password);
                params.put("mobil_uygulama","1");
                Log.d("parametre",String.valueOf(params));
                return params;
            }

        };

        AppController.getInstance().addToRequestQueue(stringRequest);
    }
}

