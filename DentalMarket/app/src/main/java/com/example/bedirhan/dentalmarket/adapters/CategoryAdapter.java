package com.example.bedirhan.dentalmarket.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bedirhan.dentalmarket.R;
import com.example.bedirhan.dentalmarket.models.Category;

import java.util.List;

public class CategoryAdapter extends BaseAdapter{

    private LayoutInflater cInflater;
    private Context cContext;
    private List<Category> categoryList;

    public CategoryAdapter(Activity activity,List<Category> categories)
    {
        //XML'i alıp View'a çevirecek inflater'ı örnekleyelim
        cInflater=(LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //gösterilecek listeyi de alalım
        categoryList=categories;
        cContext = activity;
    }

    @Override
    public int getCount() {
        return categoryList.size();
    }

    @Override
    public Object getItem(int position) {
        return categoryList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        View satirView;
        satirView=cInflater.inflate(R.layout.line,null);

        TextView textView=(TextView)satirView.findViewById(R.id.category_name);
        ImageView imageView=(ImageView)satirView.findViewById(R.id.category_image);

        Category category=categoryList.get(position); //ilgili kategori getiriliyor

        textView.setText(category.getCategory_name()); //kategorinin ismi textviewa aktarılıyor
        imageView.setImageResource(R.drawable.categoryicon);//kategori resmi ayarlanıyor

        return satirView;
    }
}
