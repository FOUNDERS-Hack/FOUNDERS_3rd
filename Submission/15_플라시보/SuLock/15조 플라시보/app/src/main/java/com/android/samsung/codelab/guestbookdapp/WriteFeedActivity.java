package com.android.samsung.codelab.guestbookdapp;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.samsung.codelab.guestbookdapp.contract.WriteFeedContract;
import com.android.samsung.codelab.guestbookdapp.databinding.ActivityWriteFeedBinding;
import com.android.samsung.codelab.guestbookdapp.model.UserInfo;
import com.android.samsung.codelab.guestbookdapp.presenter.WriteFeedPresenter;

public class WriteFeedActivity extends AppCompatActivity implements WriteFeedContract.ViewContract {

    private static final String TAG = WriteFeedActivity.class.getSimpleName();
    WriteFeedContract.PresenterContract contract;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");

        ActivityWriteFeedBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_write_feed);

        Toolbar myToolbar = findViewById(R.id.toolbar); //should be changed
        setSupportActionBar(myToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        binding.setFeed(UserInfo.getInstance().getFeedToWrite());

        WriteFeedPresenter presenter = new WriteFeedPresenter(this);
        binding.setWriteFeedPresenter(presenter);
        contract = presenter;

        //setEmojiBottomSheet();

        EditText comment1EditText = findViewById(R.id.txt_user_comment1);
        EditText comment2EditText = findViewById(R.id.txt_user_comment2);
        EditText comment3EditText = findViewById(R.id.txt_user_comment3);
        EditText comment4EditText = findViewById(R.id.txt_user_comment4);
        EditText comment5EditText = findViewById(R.id.txt_user_comment5);
        EditText comment6EditText = findViewById(R.id.txt_user_comment6);
//        EditText comment7EditText = findViewById(R.id.txt_user_comment7);

//        commentEditText.addTextChangedListener(new TextWatcher() {
//            String previousString = "";
//
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//                previousString = s.toString();
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                if (commentEditText.getLineCount() > 2) {
//                    commentEditText.setText(previousString);
//                    commentEditText.setSelection(commentEditText.length());
//                }
//
//            }
//        });

        comment1EditText.addTextChangedListener(new TextWatcher() {
            String previousString = "";

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                previousString = s.toString();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (comment1EditText.getLineCount() > 2) {
                    comment1EditText.setText(previousString);
                    comment1EditText.setSelection(comment1EditText.length());
                }

            }
        });

        comment2EditText.addTextChangedListener(new TextWatcher() {
            String previousString = "";

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                previousString = s.toString();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (comment2EditText.getLineCount() > 2) {
                    comment2EditText.setText(previousString);
                    comment2EditText.setSelection(comment2EditText.length());
                }

            }
        });
        comment3EditText.addTextChangedListener(new TextWatcher() {
            String previousString = "";

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                previousString = s.toString();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (comment3EditText.getLineCount() > 2) {
                    comment3EditText.setText(previousString);
                    comment3EditText.setSelection(comment3EditText.length());
                }

            }
        });
        comment4EditText.addTextChangedListener(new TextWatcher() {
            String previousString = "";

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                previousString = s.toString();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (comment4EditText.getLineCount() > 2) {
                    comment4EditText.setText(previousString);
                    comment4EditText.setSelection(comment4EditText.length());
                }

            }
        });
        comment5EditText.addTextChangedListener(new TextWatcher() {
            String previousString = "";

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                previousString = s.toString();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override   //줄 수 제한 (현재 3줄)
            public void afterTextChanged(Editable s) {
                if (comment5EditText.getLineCount() > 2) {
                    comment5EditText.setText(previousString);
                    comment5EditText.setSelection(comment5EditText.length());
                }

            }
        });
        comment6EditText.addTextChangedListener(new TextWatcher() {
            String previousString = "";

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                previousString = s.toString();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (comment6EditText.getLineCount() > 2) {
                    comment6EditText.setText(previousString);
                    comment6EditText.setSelection(comment6EditText.length());
                }

            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_write_feed, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_send) {
            contract.actionSend();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void setLoadingProgress(boolean isLoading) {

        runOnUiThread(() -> {
            ProgressBar bar = findViewById(R.id.progress_bar);
//            EditText editTextComment = findViewById(R.id.txt_user_company);
            EditText editTextComment2 = findViewById(R.id.txt_user_comment2);
            EditText editTextComment3 = findViewById(R.id.txt_user_comment3);
            EditText editTextComment4 = findViewById(R.id.txt_user_comment4);
            EditText editTextComment5 = findViewById(R.id.txt_user_comment5);
            EditText editTextComment6 = findViewById(R.id.txt_user_comment6);
//            EditText editTextComment7 = findViewById(R.id.txt_user_comment7);

            bar.setVisibility(isLoading ? View.VISIBLE : View.INVISIBLE);
//            editTextComment.setEnabled(!isLoading);
            editTextComment2.setEnabled(!isLoading);
            editTextComment3.setEnabled(!isLoading);
            editTextComment4.setEnabled(!isLoading);
            editTextComment5.setEnabled(!isLoading);
            editTextComment6.setEnabled(!isLoading);
//            editTextComment7.setEnabled(!isLoading);

        });
    }

    @Override
    public void toastMessage(String message) {
        runOnUiThread(() -> Toast.makeText(this, message, Toast.LENGTH_LONG).show());
    }

    @Override
    public void finishActivity() {
        runOnUiThread(this::finish);
    }

//    @Override
//    public void setEmojiBottomSheet() {
//        EditText userName = findViewById(R.id.txt_user_name);
//
//        FeelBottomSheetDialog di  alog = new FeelBottomSheetDialog();
//        dialog.show(getSupportFragmentManager(), "bottomSheet");
//        dialog.setDialogDismissListener(() -> {
//            final Handler handler = new Handler();
//            handler.postDelayed(() -> {
//                userName.requestFocus();
//                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//                imm.showSoftInput(userName, InputMethodManager.SHOW_IMPLICIT);
//            }, 500);
//        });
//    }

}