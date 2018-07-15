package com.example.bedirhan.dentalmarket.models;


public class Product {
    private String product_id;
    private String product_name;
    private String product_image_url;
    private String product_price;
    private String product_description;
    private String brand_id;
    private String shop_id;

    public Product(String p_id,String p_name,String p_image_url,String p_price,String p_brand_id,String p_shop_id,String p_description)
    {
        product_id=p_id;
        product_name=p_name;
        product_image_url=p_image_url;
        product_price=p_price;
        brand_id=p_brand_id;
        shop_id=p_shop_id;
        product_description=p_description;
    }

    public String getProduct_id() {return product_id;}
    public String getProduct_name() {return  product_name;}
    public String getProduct_image_url(){return product_image_url;}
    public String getProduct_price(){return product_price;}
    public String getBrand_id(){return brand_id;}
    public String getShop_id(){return shop_id;}
    public String getProduct_description(){return product_description;}
}
