package com.iftech.car.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import com.iftech.car.R;

/**
 * created by tanghuosong 2017/5/9
 * description: 提示框消息类
 **/
public class NormalDialog {

    DialogListener dialogListener ;
    private Context context;
    public NormalDialog(Context context){
        this.context = context;
    }

    public void setDialogListener(DialogListener dialogListener) {
        this.dialogListener = dialogListener;
    }

    public void showNormalDialog(String message){
        AlertDialog.Builder builder=new AlertDialog.Builder(context);
        builder.setTitle("温馨提示");
        builder.setMessage(message);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialogListener.enSureClock();
                dialog.cancel();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
        dialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(context.getResources().getColor(R.color.colorPrimary));
        dialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(context.getResources().getColor(R.color.black));
        dialog.setCanceledOnTouchOutside(false);//禁止点击 dialog 外部取消弹窗
    }

    public interface DialogListener{
        void enSureClock();
    }
}
