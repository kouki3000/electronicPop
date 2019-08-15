package com.example.electronicpop;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBScanExpression;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ComparisonOperator;
import com.amazonaws.services.dynamodbv2.model.Condition;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class admin extends AppCompatActivity {

    private String user_id;
    Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        mHandler = new Handler();

        //前のアクティビティからのデータ受け取り
        Intent intent = getIntent();
        user_id = intent.getStringExtra(MainActivity.USER_ID);

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
                Map<String, Condition> conditions = new HashMap<String, Condition>();
                Condition priceCondition = new Condition()
                        .withComparisonOperator(ComparisonOperator.EQ.toString())
                        .withAttributeValueList(new AttributeValue().withS(user_id));
                conditions.put("user_id", priceCondition);
                DynamoDBScanExpression scanExpression = new DynamoDBScanExpression();
                scanExpression.setScanFilter(conditions);
                final List<pop_db> popItems = mapper.scan(pop_db.class, scanExpression);
                Log.d("Item:", popItems.toString());
                // メインスレッドでui更新をする
                mHandler.post(new Runnable() {
                    public void run() {
                        for (pop_db item : popItems) {
                            System.out.println("---------");
                            System.out.format("pop_id=%s, price=%s %n", item.getPop_id(), item.getPrice());
                        }
                        InitTable(popItems);
                    }
                });
            }
        }).start();

    }

    private void InitTable(List<pop_db> Items){
        TableRow.LayoutParams row_style = new TableRow.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.MATCH_PARENT, 2.0f);

        TableLayout pop_list_table = findViewById(R.id.pop_List);
        TableRow label = new TableRow(this);
        pop_list_table.addView(label);
        label.addView(createText("デバイス名"), row_style);
        label.addView(createText("商品名"), row_style);
        label.addView(createText("広告ID"), row_style);
        label.addView(createText("値段"), row_style);
        label.addView(createText("分割払い頭金"), row_style);
        label.addView(createText(""));
        label.addView(createText(""));
        for (pop_db item: Items){
            pop_list_table.addView(createRow(item));
        }

    }

    private TextView createText(String text){
        TextView Text = new TextView(this);
        Text.setText(text);
        return Text;
    }

    private TableRow createRow(pop_db item){
        TableRow.LayoutParams row_style = new TableRow.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.MATCH_PARENT, 2.0f);

        TableRow row = new TableRow(this);
        row.addView(createText("device"), row_style);
        row.addView(createText(item.getProduct_name()), row_style);
        row.addView(createText(item.getPop_id()), row_style);
        row.addView(createText(String.valueOf(item.getPrice())), row_style);
        row.addView(createText(String.valueOf(item.getDown_payment())), row_style);
        return row;
    }

}
