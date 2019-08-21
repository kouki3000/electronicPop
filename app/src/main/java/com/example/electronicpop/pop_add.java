package com.example.electronicpop;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

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

public class pop_add extends AppCompatActivity {

    private String user_id;
    private DynamoDBMapper mapper;
    Handler mHandler;
    Map<String, String> product_id_to_url = new HashMap<String, String>();
    private EditText pop_id_EditText;
    private EditText product_name_EditText;
    private EditText price_EditText;
    private EditText down_payment_EditText;
    private Spinner pop_template_type_Spinner;
    private Spinner product_info_url_Spinner;
    private Button confirm_button;
    private Button cancel_button;
    private EditText ad_EditText_01;
    private EditText ad_EditText_02;
    private EditText ad_EditText_03;
    private EditText ad_EditText_04;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pop_add);

        mHandler = new Handler();

        //前のアクティビティからのデータ受け取り
        Intent intent = getIntent();
        user_id = intent.getStringExtra("USER_ID");

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
                Map<String, Condition> conditions_pop = new HashMap<String, Condition>();
                Condition user_Condition = new Condition()
                        .withComparisonOperator(ComparisonOperator.EQ.toString())
                        .withAttributeValueList(new AttributeValue().withS(user_id));
                conditions_pop.put("user_id", user_Condition);
                DynamoDBScanExpression scanExpression = new DynamoDBScanExpression();
                scanExpression.setScanFilter(conditions_pop);
                final List<product_db> productItems = mapper.scan(product_db.class, scanExpression);
                mHandler.post(new Runnable() {
                    public void run() {
                        InitSpinner(productItems);
                    }
                });
            }
        }).start();

        pop_id_EditText = findViewById(R.id.pop_id_text_id);
        product_name_EditText = findViewById(R.id.product_name_text_id);
        price_EditText = findViewById(R.id.price_number_id);
        down_payment_EditText = findViewById(R.id.down_payment_number_id);
        pop_template_type_Spinner = findViewById(R.id.pop_template_type_spinner_id);
        product_info_url_Spinner = findViewById(R.id.product_info_url_spinner_id);
        ad_EditText_01 = findViewById(R.id.ad001);
        ad_EditText_02 = findViewById(R.id.ad002);
        ad_EditText_03 = findViewById(R.id.ad003);
        ad_EditText_04 = findViewById(R.id.ad004);

        // 追加ボタンを押すと入力内容を確認しデータベースへ書き込みし、pop_listへ戻る
        confirm_button = findViewById(R.id.add_confirm_button_id);
        confirm_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String pop_id_check_result = checkEditText(pop_id_EditText);
                String product_name_check_result = checkEditText(product_name_EditText);
                String price_check_result = checkEditText(price_EditText);
                String down_payment_check_result = checkEditText(down_payment_EditText);

                if (pop_id_check_result.equals("True") && product_name_check_result.equals("True") && price_check_result.equals("True") && down_payment_check_result.equals("True")) {
                    // 入力が十分の場合
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            pop_db pop_id_check = mapper.load(pop_db.class, pop_id_EditText.getText().toString());
                            if (pop_id_check == null){
                                // 入力されたpop_idはdbに存在しないためcreate可能
                                String ad_text = ad_EditText_01.getText().toString() + "," + ad_EditText_02.getText().toString() + "," +ad_EditText_03.getText().toString() + "," +ad_EditText_04.getText().toString();
                                Date date = new Date();
                                pop_db setData = new pop_db();
                                setData.setPop_id(pop_id_EditText.getText().toString());
                                setData.setProduct_name(product_name_EditText.getText().toString());
                                setData.setPrice(Integer.valueOf(price_EditText.getText().toString()));
                                setData.setDown_payment(Integer.valueOf(down_payment_EditText.getText().toString()));
                                setData.setAdvertisement(ad_text);
                                setData.setUser_id(user_id);
                                setData.setCreated_at(date.toString());
                                setData.setUpdate_at(date.toString());
                                setData.setTemplate_type(0);
                                setData.setProduct_info_url(product_id_to_url.get(product_info_url_Spinner.getSelectedItem().toString()));
                                mapper.save(setData);

                                // pop_listページに戻る
                                mHandler.post(new Runnable() {
                                    public void run() {
                                        Intent intent = new Intent(getApplication(), pop_list.class);
                                        intent.putExtra("USER_ID", user_id);
                                        startActivity(intent);
                                    }
                                });

                            }
                            else{
                                // pop_idが使用されているのでやり直してください
                                mHandler.post(new Runnable() {
                                    public void run() {
                                        Toast toast = Toast.makeText(pop_add.this, "入力された広告IDは使用されています！", Toast.LENGTH_LONG);
                                        toast.show();
                                    }
                                });

                            }
                        }
                    }).start();
                }
                else{
                    String error_text = "";
                    if (!pop_id_check_result.equals("True")){
                        error_text = error_text + "広告IDが" + pop_id_check_result + "\n";
                    }
                    if (!product_name_check_result.equals("True")){
                        error_text = error_text + "商品名が" + product_name_check_result + "\n";
                    }
                    if (!price_check_result.equals("True")){
                        error_text = error_text + "価格が" + price_check_result + "\n";
                    }
                    if (!down_payment_check_result.equals("True")){
                        error_text = error_text + "分割払い頭金が" + down_payment_check_result + "\n";
                    }


                    Toast toast = Toast.makeText(pop_add.this, error_text, Toast.LENGTH_LONG);
                    toast.show();
                }
            }
        });

        // キャンセルボタンを押すとpop_listのページへ戻る
        cancel_button = findViewById(R.id.add_cancel_button_id);
        cancel_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplication(), pop_list.class);
                intent.putExtra("USER_ID", user_id);
                startActivity(intent);
            }
        });
    }

    private void InitSpinner(List<product_db> Items){
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        for (product_db item : Items){
            adapter.add(item.getProduct_id());
            product_id_to_url.put(item.getProduct_id(), item.getUrl());
        }
        product_info_url_Spinner.setAdapter(adapter);
    }

    private String checkEditText(EditText target){
        if (target.getText().toString().length() == 0){
            return "入力されていません！";
        }
        else{
            return "True";
        }
    }
}
