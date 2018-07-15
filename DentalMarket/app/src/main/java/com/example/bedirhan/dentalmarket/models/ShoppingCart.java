package com.example.bedirhan.dentalmarket.models;



public class ShoppingCart {
    private String product_id;
    private String product_name;
    private String product_image_url;
    private String product_price;
    private String shop_name;
    private String product_count;
    private int p;

    public ShoppingCart(String p_id,String p_name,String p_image,String p_price,String s_name,String p_count,int position)
    {
        product_id=p_id;
        product_name=p_name;
        product_image_url=p_image;
        product_price=p_price;
        shop_name=s_name;
        product_count=p_count;
        p=position;
    }
    public String getProduct_id() {return product_id;}
    public String getProduct_name() {return  product_name;}
    public String getProduct_image_url(){return product_image_url;}
    public String getProduct_price(){return product_price;}
    public String getShop_name(){return  shop_name;    }
    public String getProduct_count(){return product_count;}
    public int getPosition(){return p;}
    public void setProduct_count(String count)
    {
        product_count=count;

    }
}
