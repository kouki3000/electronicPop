package com.example.electronicpop;
import android.os.StatFs;

import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.*;

@DynamoDBTable(tableName = "user_test_nakamura")
public class user_db {

    private String user_id;
    private String password;
    private String created_at;


    /* パーティションキーで指定した属性名 */
    @DynamoDBHashKey(attributeName = "user_id")
    public String getUser_id() {return user_id; }
    public void setUser_id(String user_id) { this.user_id = user_id; }

    /* 任意の属性名 AWSコンソールで事前定義不要*/
    @DynamoDBAttribute(attributeName = "password")
    public String getPassword() {return password; }
    public void setPassword(String password) { this.password = password; }

    @DynamoDBAttribute(attributeName = "created_at")
    public String getCreated_at() {return created_at; }
    public void setCreated_at(String created_at) { this.created_at = created_at; }

}
