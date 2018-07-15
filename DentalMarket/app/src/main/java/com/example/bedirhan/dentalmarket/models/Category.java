package com.example.bedirhan.dentalmarket.models;

public class Category {
   private String category_id;
    private String category_name;
    private String category_image;

    public Category(String c_id,String c_name,String c_image)
    {
        //kapsülleme işlemi yapılıyor.
        category_id=c_id;
        category_name=c_name;
        category_image=c_image;
    }

    public String getCategory_id(){return category_id;}
    public String getCategory_name(){return category_name;}
    public String getCategory_image(){return category_image;}
    public void setCategory_id(String value){ category_id=value;}
    public void setCategory_name(String value){category_name=value;}
    public void setCategory_image(String value){category_image=value;}
}
