package com.example.electronicpop;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.Toast;

public class DeletePopDialog extends DialogFragment {

    private AlertDialog.Builder alert;

    public Dialog onCreateDialog(Bundle savedInstanceState){
        // ダイアログ生成  AlertDialogのBuilderクラスを指定してインスタンス化します
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
        alert = new AlertDialog.Builder(getActivity());
        final String pop_id = getArguments().getString("pop_id");

        // タイトル設定
        dialogBuilder.setTitle("以下のPOPを削除します！よろしいですか？");
        // 表示する文章設定
        dialogBuilder.setMessage(pop_id);

        // OKボタン作成
        dialogBuilder.setPositiveButton("削除", new DialogInterface.OnClickListener(){

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // トーストを出す
                Toast toast = Toast.makeText(getActivity(), "削除しました", Toast.LENGTH_SHORT);
                toast.show();
                pop_list mainActivity = (pop_list) getActivity();
                mainActivity.deletePop(pop_id);
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
