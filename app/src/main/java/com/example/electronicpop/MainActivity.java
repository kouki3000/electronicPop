package com.example.electronicpop;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.icu.text.DateFormat;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBScanExpression;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ComparisonOperator;
import com.amazonaws.services.dynamodbv2.model.Condition;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private pop_db setData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Amazon Cognito 認証情報プロバイダーを初期化します
        CognitoCachingCredentialsProvider credentialsProvider = new CognitoCachingCredentialsProvider(
                getApplicationContext(),
                "ap-northeast-1:bacfdb0c-ccc4-454b-9949-10bf45527c7d", // ID プールの ID
                Regions.AP_NORTHEAST_1 // リージョン
        );
        AmazonDynamoDBClient ddbClient = new AmazonDynamoDBClient(credentialsProvider);

        Region apNortheast1 = Region.getRegion(Regions.AP_NORTHEAST_1);
        ddbClient.setRegion(apNortheast1);

        final DynamoDBMapper mapper = new DynamoDBMapper(ddbClient);
        new Thread(new Runnable() {
            @Override
            public void run() {

                //final DateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                //final Date date = new Date(System.currentTimeMillis());


                pop_db setData = new pop_db();
                setData.setPop_id("iphone_xs_001");
                setData.setProduct_name("iphone_xs");
                setData.setPrice(20000);
                setData.setDown_payment(100);
                setData.setAdvertisement("今ならお得！！！！");
                setData.setUser_id("user0001");
                setData.setCreated_at("time");
                setData.setUpdate_at("time2");
                setData.setTemplate_type(0);
                setData.setProduct_info_url("http://product/info/");
                System.out.println(setData);
                mapper.save(setData);
                /*
                // 全レコード取得方法
                List<pop_db> newsItem = mapper.scan(pop_db.class, new DynamoDBScanExpression());

                // Item read
                Log.d("News Item:", newsItem.toString());
                for (pop_db item : newsItem) {
                    System.out.format("ID=%s, Price=%s %n",
                            item.getPop_id(), item.getPrice());
                }
                pop_db item = mapper.load(pop_db.class, 11);
                System.out.println("---------");
                System.out.println(item);
                //item.setPrice(1413240);
                //mapper.save(item);

                // Priceを指定してscanする方法
                Map<String, Condition> conditions = new HashMap<String, Condition>();
                Condition priceCondition = new Condition()
                        .withComparisonOperator(ComparisonOperator.EQ.toString())
                        .withAttributeValueList(new AttributeValue().withN(Integer.toString(100)));
                conditions.put("Price", priceCondition);
                DynamoDBScanExpression scanExpression = new DynamoDBScanExpression();
                scanExpression.setScanFilter(conditions);
                List<pop_db> newItems = mapper.scan(pop_db.class, scanExpression);
                Log.d("News Item:", newsItem.toString());
                for (pop_db item2 : newItems) {
                    System.out.format("ID=%s, Price=%s %n",
                            item2.getPop_id(), item2.getPrice());
                }
                */
            }

        }).start();
    }
}
