package com.samsung.uni_block_app.view;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.samsung.uni_block_app.R;
import com.samsung.uni_block_app.util.Util;

public class UIUtil {

    private UIUtil() {
    }

    public static void displayBalance(String balance, TextView balanceTextView, ImageButton refreshBalanceButton, ProgressBar refreshBalanceProgressBar) {
        String trimmedBalance = Util.trimBalanceString(balance);
        balanceTextView.setText(trimmedBalance);

        toggleProgressBar(View.GONE, refreshBalanceProgressBar, refreshBalanceButton);
        Log.i(Util.LOG_TAG, "Balance Displayed on UI.");
    }

    public static void toggleProgressBar(int visibility, ProgressBar refreshBalanceProgressBar, ImageButton refreshBalanceButton) {
        if (visibility == View.VISIBLE) {
            refreshBalanceProgressBar.setVisibility(View.VISIBLE);
            refreshBalanceButton.setVisibility(View.GONE);
        } else if (visibility == View.GONE) {
            refreshBalanceProgressBar.setVisibility(View.GONE);
            refreshBalanceButton.setVisibility(View.VISIBLE);
        }
    }

    public static void showToast(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    public static void displayTransactionHash(String transactionHash, Activity activity) {
        DialogInterface.OnClickListener positiveActionListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        };
        DialogInterface.OnClickListener negativeActionListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                ClipboardManager clipboardManager = (ClipboardManager) activity.getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clipData = ClipData.newPlainText("Transaction Hash", transactionHash);
                clipboardManager.setPrimaryClip(clipData);
                Toast.makeText(activity, "Transaction Hash Copied", Toast.LENGTH_SHORT).show();
            }
        };
        AlertUtil.showConfirmationAlertDialog(activity, "Transaction Successful. Transaction Hash: " + transactionHash, R.string.close, positiveActionListener, R.string.copy_to_clipboard, negativeActionListener);
    }

    public static void setButtonEnabled(Button button, boolean isButtonEnabled) {
        Activity parentActivityOfButton = (Activity) button.getContext();
        if (isButtonEnabled) {
            button.setBackgroundTintList(ColorStateList.valueOf(parentActivityOfButton.getColor(R.color.text_color_balance)));
        } else {
            button.setBackgroundTintList(ColorStateList.valueOf(Color.LTGRAY));
        }
        button.setClickable(isButtonEnabled);
    }

}
