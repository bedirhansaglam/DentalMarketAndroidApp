package com.example.bedirhan.dentalmarket;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.bedirhan.dentalmarket.app.AppController;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity {

    SwitchCompat btn_switch;

    LinearLayout ll_kurumsal,ll_bireysel;
    EditText iban,company_name,vergi_dairesi,vergi_no,name,surname,email,password,phone,smartphone,password_again;
    Button btn_kayit;
    CheckBox cb_sozlesme;
    Spinner day,mounth,year,job,company_type,city,district;
    String city_url,district_url,user_url,company_url;
    String[] city_info,district_info;
    String s_company_name,s_company_type,s_tax,s_tax_no,s_name,s_surname,s_email,s_password,s_birthday,s_job,s_phone,s_cell_phone,s_city,s_district,s_iban;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        btn_switch=(SwitchCompat)findViewById(R.id.switch_button);
        ll_kurumsal=(LinearLayout)findViewById(R.id.ll_kurumsal);
        ll_bireysel=(LinearLayout)findViewById(R.id.ll_bireysel);
        iban=(EditText)findViewById(R.id.iban);
        btn_kayit=(Button)findViewById(R.id.btn_kayit);
        company_name=(EditText)findViewById(R.id.company_name);
        vergi_dairesi=(EditText)findViewById(R.id.vergi_dairesi);
        vergi_no=(EditText)findViewById(R.id.vergi_no);
        name=(EditText)findViewById(R.id.name);
        surname=(EditText)findViewById(R.id.surname);
        email=(EditText)findViewById(R.id.email);
        password=(EditText)findViewById(R.id.password);
        password_again=(EditText)findViewById(R.id.password_again);
        phone=(EditText)findViewById(R.id.phone);
        smartphone=(EditText)findViewById(R.id.smartphone);
        day=(Spinner)findViewById(R.id.day);
        mounth=(Spinner)findViewById(R.id.mounth);
        year=(Spinner)findViewById(R.id.year);
        job=(Spinner)findViewById(R.id.job);
        company_type=(Spinner)findViewById(R.id.company_type);
        city=(Spinner)findViewById(R.id.city);
        district=(Spinner)findViewById(R.id.district);
        cb_sozlesme=(CheckBox)findViewById(R.id.sozlesme);



        city_url="http://dental-market-medanis-1.c9users.io/tr/bayiler/iller";
        district_url="http://dental-market-medanis-1.c9users.io/tr/bayiler/ilceler";
        user_url="http://dental-market-medanis-1.c9users.io/tr/register";
        company_url="http://dental-market-medanis-1.c9users.io/tr/register";

        //Bilgiler eksik alert dialog
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(SignUpActivity.this);
        alertDialog.setTitle(R.string.warning);
        alertDialog.setMessage(R.string.warning_info);
        alertDialog.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {

            }
        });

        //ŞİFRE Yanlış alert dialog
        final AlertDialog.Builder sfalertDialog = new AlertDialog.Builder(SignUpActivity.this);
        sfalertDialog.setTitle(R.string.password_error);
        sfalertDialog.setMessage(R.string.password_error_info);
        sfalertDialog.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {

            }
        });

        //alertdialog sözleşme
        final AlertDialog.Builder sozlesmealertDialog = new AlertDialog.Builder(SignUpActivity.this);
        sozlesmealertDialog.setTitle(R.string.sozlesme);
        sozlesmealertDialog.setMessage(R.string.sozlesme_metmi);
        sozlesmealertDialog.setPositiveButton(R.string.kabul_et, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {
                cb_sozlesme.setChecked(true);
            }
        });
        getCity(); //Şehirleri doldur

        city.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

               getDistrict(String.valueOf(city.getSelectedItemId()+1));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //Bireysel kurumsal form seçimi - başlangıç -
        btn_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {

                if (isChecked) //kurumsal üye kayıtı yapılması için
                {
                    final LinearLayout.LayoutParams params=(LinearLayout.LayoutParams)ll_kurumsal.getLayoutParams();
                    final LinearLayout.LayoutParams params2=(LinearLayout.LayoutParams)ll_bireysel.getLayoutParams();
                    params.weight=0.3f;
                    params2.weight=1.0f;
                    ll_kurumsal.setLayoutParams(params);
                    ll_bireysel.setLayoutParams(params2);
                    iban.setVisibility(View.VISIBLE);
                    job.setVisibility(View.INVISIBLE);

                }
                else{ //bireysel üye kayıtı için
                    final LinearLayout.LayoutParams params=(LinearLayout.LayoutParams)ll_kurumsal.getLayoutParams();
                    final LinearLayout.LayoutParams params2=(LinearLayout.LayoutParams)ll_bireysel.getLayoutParams();
                    params.weight=0.0f;
                    params2.weight=1.0f;
                    ll_kurumsal.setLayoutParams(params);
                    ll_bireysel.setLayoutParams(params2);
                    iban.setVisibility(View.INVISIBLE);
                    job.setVisibility(View.VISIBLE);

                }
            }
        });
        //Bireysel kurumsal form seçimi - son-

        cb_sozlesme.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if(cb_sozlesme.isChecked())
                {
                    sozlesmealertDialog.show();}

            }
        });
        //Kayıt butonu click olayı - başlangıç-

        btn_kayit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                s_company_name= String.valueOf(company_name.getText());
                s_company_type=String.valueOf(company_type.getSelectedItem());
                s_tax=String.valueOf(vergi_dairesi.getText());
                s_tax_no=String.valueOf(vergi_no.getText());
                s_name=String.valueOf(name.getText());
                s_surname=String.valueOf(surname.getText());
                s_birthday=String.valueOf(year.getSelectedItem())+"-"+String.valueOf(mounth.getSelectedItemId()+1)+"-"+String.valueOf(day.getSelectedItem());
                s_email=String.valueOf(email.getText());
                s_password=String.valueOf(password.getText());
                s_job=String.valueOf(job.getSelectedItem());
                s_phone=String.valueOf(phone.getText());
                s_cell_phone=String.valueOf(smartphone.getText());
                s_city=String.valueOf(city.getSelectedItemId()+1);
                s_district=String.valueOf(district.getSelectedItemId()+1);
                s_iban=String.valueOf(iban.getText());

                if(btn_switch.isChecked()) //Kurumsal kayıt -başlangıç-
                {
                    //Null kontrol yapılıyor boş değer girilmesinin önüne geçiliyor
                    if(NullControl(s_company_name) && NullControl(s_tax) && NullControl(s_tax_no) && NullControl(s_name) && NullControl(s_surname) &&  NullControl(s_email)&& NullControl(s_password) && NullControl(s_phone)&& NullControl(s_cell_phone) && NullControl(s_iban) )
                    {
                        if (String.valueOf(password.getText()).equals(String.valueOf(password_again.getText()))) //şifreler aynı ise kayıta devam
                        {

                            if(cb_sozlesme.isChecked()==true) //Sözleşme kabul edilmiş ise kayıt işlemini yap
                            {

                                //Kurumsal kayıt işlemi yapılıyor...
                                setNewCompany(s_company_name,s_company_type,s_tax,s_tax_no,s_name,s_surname,s_email,s_password,s_birthday,s_city,s_district,s_phone,s_cell_phone,s_iban,company_url);
                            }
                            else //sözleşme kabul edilmemişse sözleşmeyi göster
                            {
                                sozlesmealertDialog.show();
                            }


                        } else//Şifreler farklı ise uyarı yap
                        {
                            sfalertDialog.show();
                        }
                    }
                    else
                    {
                        alertDialog.show();//Boş değer var ise uyarı yap
                    }

                }
                //Kurumsal kayıt -son-
                else //Bireysel kayıt yap - başlangıç -
                {

                    //Null kontrol yapılıyor boş değer girilmesinin önüne geçiliyor
                    if(NullControl(s_name) && NullControl(s_surname) &&  NullControl(s_email)&& NullControl(s_password) && NullControl(s_phone)&& NullControl(s_cell_phone) )
                    {
                    if(String.valueOf(password.getText()).equals(String.valueOf(password_again.getText()))) //şifreler aynı ise kayıta devam
                    {
                        if(cb_sozlesme.isChecked()==true) //Sözleşme kabul edilmiş ise kayıt işlemini yap
                        {

                            //bireysel kayıt işlemi yapılıyor
                            setNewUser(s_name,s_surname,s_email,s_password,s_birthday,s_job,s_city,s_district,s_phone,s_cell_phone,user_url);
                        }
                        else //sözleşme kabul edilmemişse sözleşmeyi göster
                        {
                            sozlesmealertDialog.show();
                        }


                    }
                    else//Şifreler farklı ise uyarı yap
                    {
                       sfalertDialog.show();
                    }}
                    else
                    {

                        alertDialog.show();
                    }
                }
                //Bireysel kayıt yap - son-
            }
        });

        //Kayıt butonu click olayı - son-
    }

    private void getCity()
    {
        JsonArrayRequest myRequest=new JsonArrayRequest(city_url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                       city_info=new String[response.length()/2];

                        for (int i = 0; i < response.length()/2; i++) {
                            try {
                                //Gelenveri json objelere ayırıyoruz.
                                JSONObject obj = response.getJSONObject(i);
                                //ayrılan objelerin özellikllerini Strin[] lere atıyoruz...
                                city_info[i]=obj.getString("il_adi");

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }

                        ArrayAdapter<String> adapter=new ArrayAdapter<String>(SignUpActivity.this,android.R.layout.simple_spinner_dropdown_item,city_info);
                        city.setAdapter(adapter);

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        // Request(İstek)'i Volley'in Requst sırasına atıyoruz
        AppController.getInstance().addToRequestQueue(myRequest);
    }
    private void getDistrict(final String city_id)
    {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, district_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.d("Cevap",String.valueOf(response));
                        JSONArray jsonArray;


                        try {
                            jsonArray=new JSONArray(response);

                            district_info=new String[jsonArray.length()];
                            for (int i = 0; i < jsonArray.length(); i++) {
                                try {

                                    //Gelenveriyi json objelere ayırıyoruz.
                                    JSONObject obj = jsonArray.getJSONObject(i);
                                    //ayrılan objelerin özellikllerini String[] lere atıyoruz...
                                    district_info[i]=obj.getString("ilce_adi");

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }

                        }
                        catch (JSONException e)
                        {
                            e.printStackTrace();
                        }


                        ArrayAdapter<String> adapter=new ArrayAdapter<String>(SignUpActivity.this,android.R.layout.simple_spinner_dropdown_item,district_info);
                        district.setAdapter(adapter);

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(SignUpActivity.this,error.toString(),Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("il_kodu",city_id);
                Log.d("parametre",String.valueOf(params));
                return params;
            }

        };

        AppController.getInstance().addToRequestQueue(stringRequest);
    }
    private void setNewUser(final String name, final String surname, final String email, final String password, final String birthday, final String job_p, final String city, final String district, final String phone, final String smartphone, String url)
    {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.d("Cevap",response);

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(SignUpActivity.this,error.toString(),Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("first_name",name);
                params.put("last_name",surname);
                params.put("email",email);
                params.put("password",password);
                params.put("b_day",birthday);
                params.put("job",job_p);
                params.put("city",city);
                params.put("district",district);
                params.put("phone",phone);
                params.put("mobile_phone",smartphone);
                Log.d("parametre",String.valueOf(params));
                return params;
            }

        };

        AppController.getInstance().addToRequestQueue(stringRequest);

    }
    private void setNewCompany(final String company_name, final String company_type, final String vergi_dairesi, final String vergi_no, final String name, final String surname, final String email, final String password, final String birthday, final String city, final String district, final String phone, final String smartphone,final String iban ,String url)
    {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.d("Cevap",response);

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(SignUpActivity.this,error.toString(),Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("type","2");
                params.put("company_name",company_name);
                params.put("company_type",company_type);
                params.put("tax_administration",vergi_dairesi);
                params.put("tax_number",vergi_no);
                params.put("c_first_name",name);
                params.put("c_lastname",surname);
                params.put("email",email);
                params.put("password",password);
                params.put("b_day",birthday);
                params.put("c_city",city);
                params.put("c_district",district);
                params.put("c_phone",phone);
                params.put("c_mobile_phone",smartphone);
                params.put("iban",iban);
                Log.d("parametre",String.valueOf(params));
                return params;
            }

        };

        AppController.getInstance().addToRequestQueue(stringRequest);
    }
    protected boolean NullControl(String value)
    {
        if(!value.isEmpty() && !value.equals("null") && value!=null)
        {
            return true;
        }
        else
        {
            return false;}
    }
}
