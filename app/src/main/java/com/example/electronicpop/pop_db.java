package com.example.electronicpop;
import android.os.StatFs;

import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.*;

@DynamoDBTable(tableName = "pop_test_nakamura")
public class pop_db {

    private String pop_id;
    private String product_name;
    private int price;
    private int down_payment;
    private String advertisement; //　後でlistにする or 特殊文字で１文にする
    private String user_id;
    private String created_at;
    private String update_at;
    private int template_type;
    private String product_info_url;

    /* パーティションキーで指定した属性名 */
    @DynamoDBHashKey(attributeName = "pop_id")
    public String getPop_id() {return pop_id; }
    public void setPop_id(String pop_id) { this.pop_id = pop_id; }

    /* 任意の属性名 AWSコンソールで事前定義不要*/
    @DynamoDBAttribute(attributeName = "product_name")
    public String getProduct_name() {return product_name; }
    public void setProduct_name(String product_name) { this.product_name = product_name; }

    @DynamoDBAttribute(attributeName = "price")
    public int getPrice() {return price; }
    public void setPrice(int price) { this.price = price; }

    @DynamoDBAttribute(attributeName = "down_payment")
    public int getDown_payment() {return down_payment; }
    public void setDown_payment(int down_payment) { this.down_payment = down_payment; }

    @DynamoDBAttribute(attributeName = "advertisement")
    public String getAdvertisement() {return advertisement; }
    public void setAdvertisement(String advertisement) { this.advertisement = advertisement; }

    @DynamoDBAttribute(attributeName = "user_id")
    public String getUser_id() {return user_id; }
    public void setUser_id(String user_id) { this.user_id = user_id; }

    @DynamoDBAttribute(attributeName = "created_at")
    public String getCreated_at() {return created_at; }
    public void setCreated_at(String created_at) { this.created_at = created_at; }

    @DynamoDBAttribute(attributeName = "update_at")
    public String getUpdate_at() {return update_at; }
    public void setUpdate_at(String update_at) { this.update_at = update_at; }

    @DynamoDBAttribute(attributeName = "template_type")
    public int getTemplate_type() {return template_type; }
    public void setTemplate_type(int template_type) { this.template_type = template_type; }

    @DynamoDBAttribute(attributeName = "product_info_url")
    public String getProduct_info_url() {return product_info_url; }
    public void setProduct_info_url(String product_info_url) { this.product_info_url = product_info_url; }

}
