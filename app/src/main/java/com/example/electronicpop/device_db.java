package com.example.electronicpop;
import android.os.StatFs;

import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.*;

@DynamoDBTable(tableName = "device_test_nakamura")
public class device_db {

    private String device_id;
    private boolean is_master;
    private String user_id;
    private String pop_id;
    private String created_at;
    private String updated_at;


    /* パーティションキーで指定した属性名 */
    @DynamoDBHashKey(attributeName = "device_id")
    public String getDevice_id() {return device_id; }
    public void setDevice_id(String device_id) { this.device_id = device_id; }

    /* 任意の属性名 AWSコンソールで事前定義不要*/
    @DynamoDBAttribute(attributeName = "is_master")
    public boolean getIs_master() {return is_master; }
    public void setIs_master(boolean is_master) { this.is_master = is_master; }

    @DynamoDBAttribute(attributeName = "user_id")
    public String getUser_id() {return user_id; }
    public void setUser_id(String user_id) { this.user_id = user_id; }

    @DynamoDBAttribute(attributeName = "pop_id")
    public String getPop_id() {return pop_id; }
    public void setPop_id(String pop_id) { this.pop_id = pop_id; }

    @DynamoDBAttribute(attributeName = "created_at")
    public String getCreated_at() {return created_at; }
    public void setCreated_at(String created_at) { this.created_at = created_at; }

    @DynamoDBAttribute(attributeName = "updated_at")
    public String getUpdated_at() {return updated_at; }
    public void setUpdated_at(String updated_at) { this.updated_at = updated_at; }
}

