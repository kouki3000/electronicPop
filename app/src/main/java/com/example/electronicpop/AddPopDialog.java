package com.example.electronicpop;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

public class AddPopDialog extends DialogFragment {
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // ダイアログ生成  AlertDialogのBuilderクラスを指定してインスタンス化します
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
        // タイトル設定
        dialogBuilder.setTitle("新たなPOPを作成します！");

        dialogBuilder.setMessage("pop_id");
        final EditText editText_pop_id = new EditText(getActivity());
        dialogBuilder.setView(editText_pop_id);

        // 追加ボタン
        dialogBuilder.setPositiveButton("追加", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // トーストを出す
                Toast toast = Toast.makeText(getActivity(), "追加しました", Toast.LENGTH_SHORT);
                toast.show();
            }
        });

        // キャンセルボタン
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
