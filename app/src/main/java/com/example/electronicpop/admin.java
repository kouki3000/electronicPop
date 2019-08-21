package com.example.electronicpop;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
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
    private DynamoDBMapper mapper;
    private Button pop_list_button;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        mHandler = new Handler();

        //前のアクティビティからのデータ受け取り
        Intent intent = getIntent();
        user_id = intent.getStringExtra("USER_ID");

        // Amazon Cognito 認証情報プロバイダーを初期化します
        CognitoCachingCredentialsProvider credentialsProvider = new CognitoCachingCredentialsProvider(
                getApplicationContext(),
                "ap-northeast-1:bacfdb0c-ccc4-454b-9949-10bf45527c7d", // ID プールの ID
                Regions.AP_NORTHEAST_1 // リージョン
        );

        AmazonDynamoDBClient ddbClient = new AmazonDynamoDBClient(credentialsProvider);

        Region apNortheast1 = Region.getRegion(Regions.AP_NORTHEAST_1);
        ddbClient.setRegion(apNortheast1);

        //final DynamoDBMapper mapper = new DynamoDBMapper(ddbClient);
        mapper = new DynamoDBMapper(ddbClient);
        new Thread(new Runnable() {
            @Override
            public void run() {
                Map<String, Condition> conditions_device = new HashMap<String, Condition>();
                Condition user_Condition = new Condition()
                        .withComparisonOperator(ComparisonOperator.EQ.toString())
                        .withAttributeValueList(new AttributeValue().withS(user_id));
                conditions_device.put("user_id", user_Condition);
                DynamoDBScanExpression scanExpression = new DynamoDBScanExpression();
                scanExpression.setScanFilter(conditions_device);
                final List<device_db> deviceItems = mapper.scan(device_db.class, scanExpression);

                final Map<String, pop_db> device_id_to_pop = new HashMap<String, pop_db>();
                for (device_db item : deviceItems){
                    Map<String, Condition> conditions_pop = new HashMap<String, Condition>();
                    Condition user_id_Condition = new Condition()
                            .withComparisonOperator(ComparisonOperator.EQ.toString())
                            .withAttributeValueList(new AttributeValue().withS(user_id));
                    Condition pop_id_Condition = new Condition()
                            .withComparisonOperator(ComparisonOperator.EQ.toString())
                            .withAttributeValueList(new AttributeValue().withS(item.getPop_id()));
                    conditions_pop.put("user_id", user_id_Condition);
                    conditions_pop.put("pop_id", pop_id_Condition);
                    if (item.getPop_id() != null) {
                        DynamoDBScanExpression scanExpression_pop = new DynamoDBScanExpression();
                        scanExpression_pop.setScanFilter(conditions_pop);
                        final List<pop_db> pop_Item = mapper.scan(pop_db.class, scanExpression_pop);
                        device_id_to_pop.put(item.getDevice_id(), pop_Item.get(0));
                    }
                    else{
                        device_id_to_pop.put(item.getDevice_id(), null);
                    }
                }
                /*
                for (String key : device_id_to_pop.keySet()){
                    System.out.println("---------");
                    System.out.println(key + " => " + device_id_to_pop.get(key));
                }
                */

                // メインスレッドでui更新をする
                mHandler.post(new Runnable() {
                    public void run() {
                        InitTable(device_id_to_pop);
                    }
                });
            }
        }).start();


        pop_list_button = findViewById(R.id.pop_list_button);
        pop_list_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplication(), pop_list.class);
                intent.putExtra("USER_ID", user_id);
                startActivity(intent);
            }
        });

    }

    private void InitTable(Map<String, pop_db> device_pop){
        TableRow.LayoutParams row_style = new TableRow.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.MATCH_PARENT, 2.0f);

        TableLayout pop_list_table = findViewById(R.id.device_list);
        TableRow label = new TableRow(this);
        pop_list_table.addView(label);
        label.addView(createText("デバイス名"), row_style);
        label.addView(createText("商品名"), row_style);
        label.addView(createText("広告ID"), row_style);
        label.addView(createText("値段"), row_style);
        label.addView(createText("分割払い頭金"), row_style);
        label.addView(createText(""));
        label.addView(createText(""));
        for (String key: device_pop.keySet()){
            pop_list_table.addView(createRow(key, device_pop.get(key)));
        }

    }

    private TextView createText(String text){
        TextView Text = new TextView(this);
        Text.setText(text);
        return Text;
    }

    private TableRow createRow(String device_id, pop_db pop){
        TableRow.LayoutParams row_style = new TableRow.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.MATCH_PARENT, 2.0f);

        TableRow row = new TableRow(this);
        row.addView(createText(device_id), row_style);
        if (pop != null) {
            row.addView(createText(pop.getProduct_name()), row_style);
            row.addView(createText(pop.getPop_id()), row_style);
            row.addView(createText(String.valueOf(pop.getPrice())), row_style);
            row.addView(createText(String.valueOf(pop.getDown_payment())), row_style);
        }
        return row;
    }

}
