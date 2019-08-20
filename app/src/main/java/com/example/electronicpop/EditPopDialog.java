package com.example.electronicpop;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.Toast;

public class EditPopDialog extends DialogFragment {

    private AlertDialog.Builder alert;

    public Dialog onCreateDialog(Bundle savedInstanceState){
        // ダイアログ生成  AlertDialogのBuilderクラスを指定してインスタンス化します
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
        alert = new AlertDialog.Builder(getActivity());
        String pop_id = getArguments().getString("pop_id");

        // タイトル設定
        dialogBuilder.setTitle("以下のPOPをEditします！よろしいですか？");
        // 表示する文章設定
        dialogBuilder.setMessage(pop_id);

        // OKボタン作成
        dialogBuilder.setPositiveButton("Edit", new DialogInterface.OnClickListener(){

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // トーストを出す
                Toast toast = Toast.makeText(getActivity(), "Editしました", Toast.LENGTH_SHORT);
                toast.show();
            }
        });

        // NGボタン作成
        dialogBuilder.setNegativeButton("キャンセル", new DialogInterface.OnClickListener(){

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // 何もしないで閉じる
            }
        });

        // dialogBulderを返す
        return dialogBuilder.create();
    }
}
