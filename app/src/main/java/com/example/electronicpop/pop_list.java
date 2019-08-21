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

public class pop_list extends AppCompatActivity {

    private String user_id;
    private DynamoDBMapper mapper;
    Handler mHandler;
    private Button add_pop_button;
    private Button back_to_admin_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pop_list);

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
                final List<pop_db> popItems = mapper.scan(pop_db.class, scanExpression);
                mHandler.post(new Runnable() {
                    public void run() {
                        InitTable(popItems);
                    }
                });
            }
        }).start();

        // pop追加ボタン
        add_pop_button = findViewById(R.id.add_pop);
        add_pop_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*
                // ダイアログより別ページのが良いっぽいから保留
                AddPopDialog dialog = new AddPopDialog();
                dialog.show(getFragmentManager(), "AddDialog");
                */
                Intent intent = new Intent(getApplication(), pop_add.class);
                intent.putExtra("USER_ID", user_id);
                startActivity(intent);
            }
        });

        // adminページへ戻るボタン
        back_to_admin_button = findViewById(R.id.back_to_admin_button);
        back_to_admin_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*
                // ダイアログより別ページのが良いっぽいから保留
                AddPopDialog dialog = new AddPopDialog();
                dialog.show(getFragmentManager(), "AddDialog");
                */
                Intent intent = new Intent(getApplication(), admin.class);
                intent.putExtra("USER_ID", user_id);
                startActivity(intent);
            }
        });
    }

    private void InitTable(List<pop_db> popItems){
        TableRow.LayoutParams row_style = new TableRow.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.MATCH_PARENT, 2.0f);
        TableRow.LayoutParams row_style_button = new TableRow.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.MATCH_PARENT, 1.0f);

        TableLayout pop_list_table = findViewById(R.id.pop_list);
        TableRow label = new TableRow(this);
        pop_list_table.addView(label);
        label.addView(createText("広告ID"), row_style);
        label.addView(createText("商品名"), row_style);
        label.addView(createText("値段"), row_style);
        label.addView(createText(" "), row_style_button);
        label.addView(createText(" "), row_style_button);
        for (pop_db item : popItems){
            pop_list_table.addView(createRow(item));
        }
    }

    private TextView createText(String text){
        TextView Text = new TextView(this);
        Text.setText(text);
        return Text;
    }

    private TableRow createRow(pop_db pop){
        TableRow.LayoutParams row_style = new TableRow.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.MATCH_PARENT, 2.0f);
        TableRow.LayoutParams row_style_button = new TableRow.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.MATCH_PARENT, 1.0f);

        TableRow row = new TableRow(this);
        if (pop != null) {
            row.addView(createText(pop.getPop_id()), row_style);
            row.addView(createText(pop.getProduct_name()), row_style);
            row.addView(createText(String.valueOf(pop.getPrice())), row_style);
        }

        row.addView(createDeleteButton(pop.getPop_id()), row_style_button);
        row.addView(createEditButton(pop.getPop_id()), row_style_button);

        return row;
    }

    private Button createDeleteButton(final String pop_id){
        Button button = new Button(this);
        button.setText("delete");
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                DeletePopDialog dialog = new DeletePopDialog();
                Bundle args = new Bundle();
                args.putString("pop_id", pop_id); //引数
                dialog.setArguments(args);
                dialog.show(getFragmentManager(), "DeleteDialog");
            }
        });
        return button;
    }
    private Button createEditButton(final String pop_id){
        Button button = new Button(this);
        button.setText("edit");
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                /*
                EditPopDialog dialog = new EditPopDialog();
                Bundle args = new Bundle();
                args.putString("pop_id", pop_id); //引数
                dialog.setArguments(args);
                dialog.show(getFragmentManager(), "EditDialog");
                */

                Intent intent = new Intent(getApplication(), pop_edit.class);
                intent.putExtra("USER_ID", user_id);
                intent.putExtra("POP_ID", pop_id);
                startActivity(intent);

            }
        });
        return button;
    }

    public void deletePop(final String pop_id){
        new Thread(new Runnable() {
            @Override
            public void run() {
                pop_db delete_item = mapper.load(pop_db.class, pop_id);
                mapper.delete(delete_item);
            }
        }).start();
        finish();
        startActivity(getIntent());
    }

}
