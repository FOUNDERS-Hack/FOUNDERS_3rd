package com.samsung.open_crypto_wallet_app.view;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.widget.EditText;
import android.widget.Toast;

import com.samsung.android.sdk.coldwallet.ScwDeepLink;
import com.samsung.android.sdk.coldwallet.ScwErrorCode;
import com.samsung.open_crypto_wallet_app.R;
import com.samsung.open_crypto_wallet_app.Util;
import com.samsung.open_crypto_wallet_app.view_model.AccountViewModel;

public class AlertUtil {

    private static Activity mParentActivity;
    private static NavActivity navActivityInstance;
    private static DialogInterface.OnClickListener mExitCallback = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            mParentActivity.finish();
        }
    };
    private static DialogInterface.OnClickListener mCloseDialogCallBack = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            dialog.dismiss();
        }
    };

    public static void setNavActivityInstance(NavActivity navActivityInstance) {
        AlertUtil.navActivityInstance = navActivityInstance;
    }

    private static AlertDialog.Builder generateAlertBuilder(Activity activity, int titleID, int messageID) {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(activity);
        alertBuilder.setIcon(R.drawable.ic_launcher);
        alertBuilder.setTitle(titleID);
        alertBuilder.setMessage(messageID);
        return alertBuilder;
    }

    private static AlertDialog.Builder generateAlertBuilder(Activity activity, String title, String message) {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(activity);
        alertBuilder.setIcon(R.drawable.ic_launcher);
        alertBuilder.setTitle(title);
        alertBuilder.setMessage(message);
        return alertBuilder;
    }

    private static void showDialogBoxHandlingOnDismiss(AlertDialog.Builder alertBuilder) {
        mParentActivity.runOnUiThread(() -> {
            Dialog dialog = alertBuilder.create();
            dialog.setOnDismissListener(dialog1 -> {
                mParentActivity.finish();
            });
            dialog.show();
        });
    }

    private static void showDialogBox(AlertDialog.Builder alertBuilder) {
        mParentActivity.runOnUiThread(() -> {
            Dialog dialog = alertBuilder.create();
            dialog.show();
        });
    }

    public static void showReceiverAddressEmptyDialog() {
        mParentActivity = navActivityInstance;
        AlertDialog.Builder alertBuilder = generateAlertBuilder(mParentActivity, R.string.send_ether_error_title, R.string.receiver_address_blank);
        alertBuilder.setPositiveButton(R.string.close, mCloseDialogCallBack);
        showDialogBox(alertBuilder);
    }

    public static void showEtherAmountEmptyDialog() {
        mParentActivity = navActivityInstance;
        AlertDialog.Builder alertBuilder = generateAlertBuilder(mParentActivity, R.string.send_ether_error_title, R.string.ether_amount_blank);
        alertBuilder.setPositiveButton(R.string.close, mCloseDialogCallBack);
        showDialogBox(alertBuilder);
    }

    public static void showEtherAmountNotEnoughDialog() {
        mParentActivity = navActivityInstance;
        AlertDialog.Builder alertBuilder = generateAlertBuilder(mParentActivity, R.string.send_ether_error_title, R.string.ether_amount_not_enough);
        alertBuilder.setPositiveButton(R.string.close, mCloseDialogCallBack);
        showDialogBox(alertBuilder);
    }

    public static void showTransactionSpeedNotSelectedDialog() {
        mParentActivity = navActivityInstance;
        AlertDialog.Builder alertBuilder = generateAlertBuilder(mParentActivity, R.string.send_ether_error_title, R.string.transaction_speed_not_selected);
        alertBuilder.setPositiveButton(R.string.close, mCloseDialogCallBack);
        showDialogBox(alertBuilder);
    }

    public static void showTransactionNotSignedDialog() {
        mParentActivity = navActivityInstance;
        AlertDialog.Builder alertBuilder = generateAlertBuilder(mParentActivity, R.string.send_ether_error_title, R.string.transaction_missing);
        alertBuilder.setPositiveButton(R.string.close, mCloseDialogCallBack);
        showDialogBox(alertBuilder);
    }

    public static void showTransactionSigningSuccessful() {
        mParentActivity = navActivityInstance;
        AlertDialog.Builder alertBuilder = generateAlertBuilder(mParentActivity, R.string.transaction_signing_title, R.string.transaction_signing_successful);
        alertBuilder.setPositiveButton(R.string.close, mCloseDialogCallBack);
        showDialogBox(alertBuilder);
    }

    public static void showTransactionSendingAlert(String transactionHash) {
        mParentActivity = navActivityInstance;
        AlertDialog.Builder alertBuilder;
        if (transactionHash == null) {
            alertBuilder = generateAlertBuilder(mParentActivity, "Transaction Failed", "Could not perform transaction");
        } else {
            // TODO 리워드 관련 수정
            alertBuilder = generateAlertBuilder(mParentActivity, "1등입니다. 축하드립니다!", "배팅 금액이 지급되었습니다.");
            alertBuilder.setPositiveButton(R.string.menu_transaction_history, (dialog, which) -> {
                dialog.dismiss();
                navActivityInstance.launchTransactionHistoryFragment();
            });
        }
        alertBuilder.setNegativeButton(R.string.close, mCloseDialogCallBack);
        showDialogBox(alertBuilder);
    }

    public static void showInternetConnectionNotAvailableDialog(Activity activity) {
        mParentActivity = activity;
        AlertDialog.Builder alertBuilder = generateAlertBuilder(activity, R.string.internet_not_available_title, R.string.sbk_connection_error);
        alertBuilder.setPositiveButton(R.string.exit, mExitCallback);
        showDialogBoxHandlingOnDismiss(alertBuilder);
    }

    public static void addAccountNotSupportedDialog(Activity activity) {
        mParentActivity = activity;
        AlertDialog.Builder alertBuilder = generateAlertBuilder(activity, R.string.adding_account_not_supported_title, R.string.add_account_not_supported);
        alertBuilder.setPositiveButton(R.string.exit, mExitCallback);
        showDialogBoxHandlingOnDismiss(alertBuilder);
    }

    public static void showUpdateRequireDialog(Activity activity) {
        mParentActivity = activity;
        AlertDialog.Builder alertBuilder = generateAlertBuilder(activity, R.string.api_level_not_matched_tittle, R.string.api_level_not_matched_message);
        alertBuilder.setNegativeButton(R.string.exit, mExitCallback);
        alertBuilder.setPositiveButton(R.string.update_sbk, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Util.launchDeepLink(mParentActivity, ScwDeepLink.GALAXY_STORE);
                mParentActivity.finish();           // Exiting app so that the app can work with new instance of ScwService at next launch
            }
        });
        showDialogBoxHandlingOnDismiss(alertBuilder);
    }

    public static void SBKWalletNotSetDialog(Activity activity) {
        mParentActivity = activity;
        AlertDialog.Builder alertBuilder = generateAlertBuilder(activity, R.string.sbk_not_set_title, R.string.sbk_not_set_message);
        alertBuilder.setNegativeButton(R.string.exit, mExitCallback);
        alertBuilder.setPositiveButton(R.string.jump_sbk, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Util.launchDeepLink(mParentActivity, ScwDeepLink.MAIN);
                mParentActivity.finish();       // Exiting app so that the app can work with new instance of ScwService at next launch
            }
        });
        showDialogBoxHandlingOnDismiss(alertBuilder);
    }

    public static void addNewAccountDialog(Activity activity) {
        mParentActivity = activity;
        AlertDialog.Builder alertBuilder = generateAlertBuilder(mParentActivity, R.string.add_new_account_title, R.string.add_new_account_message);
        //To be changed with account adding function when implemented
        alertBuilder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        AccountViewModel.createNewAccount();
                    }
                }
        );
        alertBuilder.setNegativeButton(R.string.no, mCloseDialogCallBack);
        showDialogBox(alertBuilder);
    }

    public static void editAccountDialog(Activity activity) {
        mParentActivity = activity;
        AlertDialog.Builder alertBuilder = generateAlertBuilder(mParentActivity, R.string.edit_account_name_title, R.string.edit_account_name_message);
        String previousAccountName = AccountViewModel.getDefaultAccountName();
        final EditText accountNameEditText = new EditText(activity);
        accountNameEditText.setText(previousAccountName);
        alertBuilder.setView(accountNameEditText);

        //To be changed with account adding function when implemented
        alertBuilder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String newAccountName = accountNameEditText.getText().toString();
                        if (newAccountName.isEmpty()) {
                            Toast.makeText(activity, "New account name cannot be empty", Toast.LENGTH_LONG).show();
                        } else if (newAccountName.equals(previousAccountName)) {
                            Toast.makeText(activity, "Account name cannot be same", Toast.LENGTH_LONG).show();
                        } else {
                            AccountViewModel.editAccountName(newAccountName);
                        }
                    }
                }
        );
        alertBuilder.setNegativeButton(R.string.no, mCloseDialogCallBack);
        showDialogBox(alertBuilder);
    }

    public static void handleSBKError(int errorCode) {
        if (errorCode == ScwErrorCode.ERROR_OP_FAIL) showOperationFailedDialog();
        else if (errorCode == ScwErrorCode.ERROR_CHECK_APP_VERSION_FAILED)
            showInternetConnectionNeededDialog();
        else if (errorCode == ScwErrorCode.ERROR_INVALID_SCW_APP_ID)
            showDeveloperOptionDisabledDialog();
        else if (errorCode == ScwErrorCode.ERROR_INVALID_TRANSACTION_FORMAT)
            showInvalidAddressDialog();
        else showUnknownErrorDialog();
    }

    private static void showUnknownErrorDialog() {
        if (navActivityInstance != null) {
            mParentActivity = navActivityInstance;
            final AlertDialog.Builder alertBuilder = generateAlertBuilder(mParentActivity, R.string.sbk_error_title, R.string.sbk_unknown_error);
            alertBuilder.setPositiveButton(R.string.exit, mExitCallback);
            showDialogBox(alertBuilder);
        }
    }

    private static void showInvalidAddressDialog() {
        if (navActivityInstance != null) {
            mParentActivity = navActivityInstance;
            final AlertDialog.Builder alertBuilder = generateAlertBuilder(mParentActivity, R.string.sbk_error_title, R.string.sbk_invalid_address);
            alertBuilder.setPositiveButton(R.string.exit, mExitCallback);
            showDialogBox(alertBuilder);
        }
    }

    private static void showInternetConnectionNeededDialog() {
        if (navActivityInstance != null) {
            mParentActivity = navActivityInstance;
            final AlertDialog.Builder alertBuilder = generateAlertBuilder(mParentActivity, R.string.sbk_error_title, R.string.sbk_connection_error);
            alertBuilder.setPositiveButton(R.string.exit, mExitCallback);
            showDialogBox(alertBuilder);
        }
    }

    private static void showOperationFailedDialog() {
        if (navActivityInstance != null) {
            mParentActivity = navActivityInstance;
            final AlertDialog.Builder alertBuilder = generateAlertBuilder(mParentActivity, R.string.sbk_error_title, R.string.sbk_operation_failed);
            alertBuilder.setPositiveButton(R.string.exit, mExitCallback);
            showDialogBox(alertBuilder);
        }
    }

    private static void showDeveloperOptionDisabledDialog() {
        if (navActivityInstance != null) {
            mParentActivity = navActivityInstance;
            final AlertDialog.Builder alertBuilder = generateAlertBuilder(mParentActivity, R.string.sbk_error_title, R.string.sbk_enable_developer_options);
            alertBuilder.setPositiveButton(R.string.exit, mExitCallback);
            showDialogBox(alertBuilder);
        }
    }

}