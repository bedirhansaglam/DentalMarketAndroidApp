package com.example.bedirhan.dentalmarket;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import static android.content.Context.MODE_PRIVATE;

public class Product_Count {

    public static int Count(Context Context)
    {
        int count=0;
        SharedPreferences prefs = Context.getSharedPreferences("basket7",MODE_PRIVATE);
        int size=prefs.getInt("p_name_size",0);
        int[] p_delete=new int[size];
        for (int i = 0; i < size; i++) {
            p_delete[i]=prefs.getInt("p_delete_"+String.valueOf(i+1),0);} //silinmemiş ürünleri listele
        for (int i = 0; i <size; i++) {
            if(p_delete[i]==1){
              count+=Integer.parseInt(prefs.getString("p_count_" +String.valueOf(i+1), "0")); // ürünlerin sayılarını topla
            }

        }

        return count; // ürün sayılarını gönder
    }
}
