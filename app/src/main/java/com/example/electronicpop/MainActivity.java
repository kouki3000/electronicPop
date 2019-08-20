package com.example.electronicpop;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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

public class MainActivity extends AppCompatActivity {
    //private pop_db setData;
    private user_db setData;
    private EditText user_id_editText;
    private EditText password_editText;
    private Button login_button;
    private String mode;
    public static final String USER_ID
            = "com.example.electronicpop.DATA";

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
        /*
        new Thread(new Runnable() {
            @Override
            public void run() {

                //final DateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                //final Date date = new Date(System.currentTimeMillis());
                Date date = new Date();

                pop_db setData = new pop_db();
                setData.setPop_id("iphone_xs_003");
                setData.setProduct_name("iphone_1111");
                setData.setPrice(40000);
                setData.setDown_payment(50);
                setData.setAdvertisement("aaaa今ならお得！！！！");
                setData.setUser_id("user0001");
                setData.setCreated_at(date.toString());
                //setData.setUpdate_at("time2");
                setData.setTemplate_type(0);
                setData.setProduct_info_url("http://product/info/");
                System.out.println(setData);
                mapper.save(setData);


                // 全レコード取得方法
                List<pop_db> newsItem = mapper.scan(pop_db.class, new DynamoDBScanExpression());

                // Item read
                Log.d("News Item:", newsItem.toString());
                for (pop_db item : newsItem) {
                    System.out.format("ID=%s, Price=%s %n",
                            item.getPop_id(), item.getPrice());
                }
                pop_db item = mapper.load(pop_db.class, "iphone_xs_002");
                System.out.println("---------");
                System.out.println(item.getProduct_name());
                //item.setPrice(1413240);
                //mapper.save(item);

                // Priceを指定してscanする方法
                Map<String, Condition> conditions = new HashMap<String, Condition>();
                Condition priceCondition = new Condition()
                        .withComparisonOperator(ComparisonOperator.EQ.toString())
                        .withAttributeValueList(new AttributeValue().withN(Integer.toString(20000)));
                conditions.put("price", priceCondition);
                DynamoDBScanExpression scanExpression = new DynamoDBScanExpression();
                scanExpression.setScanFilter(conditions);
                List<pop_db> newItems = mapper.scan(pop_db.class, scanExpression);
                Log.d("News Item:", newsItem.toString());
                for (pop_db item2 : newItems) {
                    System.out.println("---------");
                    System.out.format("pop_id=%s, price=%s %n",
                            item2.getPop_id(), item2.getPrice());
                }

            }

        }).start();
        */
        user_id_editText = findViewById(R.id.user_id_text);
        password_editText = findViewById(R.id.password_text);
        login_button = findViewById(R.id.login_button);

        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        // loadでできるかな
                        user_db user_item = mapper.load(user_db.class, user_id_editText.getText().toString());
                        //System.out.format("id=%s, pass=%s, at=%s %n", user_item.getUser_id(), user_item.getPassword(), user_item.getCreated_at());

                        //if (user_item == null){
                            //ユーザIDが間違ってます
                        //}

                        if (user_item.getPassword().equals(password_editText.getText().toString())){
                            mode = "admin";
                            if (mode.equals("admin")){
                                //管理者画面へ遷移
                                Intent intent = new Intent(getApplication(), admin.class);
                                intent.putExtra(USER_ID, user_item.getUser_id());
                                startActivity(intent);
                            }
                            else {
                                //ポップ選択画面へ遷移
                            }
                        }
                        else{
                            //パスワードが間違ってます
                        }
                    }
                }).start();

            }
            });



    }

}
