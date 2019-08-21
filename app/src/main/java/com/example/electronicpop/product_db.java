package com.example.electronicpop;

import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBAttribute;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBHashKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBTable;

@DynamoDBTable(tableName = "product_test_nakamura")
public class product_db {
    private String product_id;
    private String user_id;
    private String url;

    /* パーティションキーで指定した属性名 */
    @DynamoDBHashKey(attributeName = "product_id")
    public String getProduct_id() {return product_id; }
    public void setProduct_id(String product_id) { this.product_id = product_id; }

    /* 任意の属性名 AWSコンソールで事前定義不要*/
    @DynamoDBAttribute(attributeName = "user_id")
    public String getUser_id() {return user_id; }
    public void setUser_id(String user_id) { this.user_id = user_id; }

    /* 任意の属性名 AWSコンソールで事前定義不要*/
    @DynamoDBAttribute(attributeName = "url")
    public String getUrl() {return url; }
    public void setUrl(String url) { this.url = url; }

}
