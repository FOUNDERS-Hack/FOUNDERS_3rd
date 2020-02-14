package com.samsung.uni_block_app.view;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;

import com.samsung.uni_block_app.R;

public class AlertUtil {

    private AlertUtil() {
    }

    public static void showAlertDialog(Activity activity, String message) {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(activity);
        alertBuilder.setMessage(message);
        alertBuilder.setPositiveButton(R.string.close, (dialogInterface, i) -> {
            dialogInterface.dismiss();
        });
        alertBuilder.show();
    }

    public static void showActivityCloseAlertDialog(Activity activity, String message, boolean closeActivityOnDismiss) {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(activity);
        alertBuilder.setMessage(message);
        alertBuilder.setPositiveButton(R.string.close, (dialogInterface, i) -> {
            activity.finish();
        });
        if (closeActivityOnDismiss) {
            alertBuilder.setOnDismissListener(dialogInterface -> {
                activity.finish();
            });
        }
        alertBuilder.show();
    }

    public static void showConfirmationAlertDialog(Activity activity, String message,
                                                   int positiveButtonMessage, DialogInterface.OnClickListener positiveActionListener,
                                                   int negativeButtonMessage, DialogInterface.OnClickListener negativeActionListener) {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(activity);
        alertBuilder.setMessage(message);
        alertBuilder.setPositiveButton(positiveButtonMessage, positiveActionListener);
        alertBuilder.setNegativeButton(negativeButtonMessage, negativeActionListener);
        alertBuilder.show();
    }
}
