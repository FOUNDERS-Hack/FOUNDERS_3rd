package com.android.samsung.codelab.guestbookdapp;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.text.emoji.EmojiCompat;
import android.support.text.emoji.FontRequestEmojiCompatConfig;
import android.support.text.emoji.bundled.BundledEmojiCompatConfig;
import android.support.v4.provider.FontRequest;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.samsung.codelab.guestbookdapp.contract.IntroContract;
import com.android.samsung.codelab.guestbookdapp.databinding.ActivityMainBinding;
import com.android.samsung.codelab.guestbookdapp.presenter.IntroPresenter;
import com.android.samsung.codelab.guestbookdapp.util.PrefsHelper;

public class MainActivity extends AppCompatActivity implements IntroContract.View {

    //private static final boolean USE_BUNDLED_EMOJI = false;
    private static final String TAG = MainActivity.class.getSimpleName();
    private ActivityMainBinding binding;
    private IntroContract.Presenter contract;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        setLoading(false);

        //주소발급
        IntroPresenter presenter = new IntroPresenter(this);
        contract = presenter;

        binding.setIntroPresenter(presenter);
        //initEmojiCompat();

        String cachedSeedHash = PrefsHelper.getInstance().getCachedSeedHash();

        ConstraintLayout initLayout = findViewById(R.id.cl_init);
        ConstraintLayout splashLayout = findViewById(R.id.cl_splash);

        boolean alreadyInit = (!TextUtils.isEmpty(cachedSeedHash));

        initLayout.setVisibility(alreadyInit ? View.GONE : View.VISIBLE);
        splashLayout.setVisibility(alreadyInit ? View.VISIBLE : View.GONE);

        if (alreadyInit) {
            final Handler handler = new Handler();
            handler.postDelayed(() -> contract.initializeKeystore(), 800);
        }
    }

    @Override
    public void setLoading(boolean isLoading) {
        binding.setIsLoading(isLoading);
    }


    @Override
    public void toastMessage(String message) {
        runOnUiThread(() -> Toast.makeText(this, message, Toast.LENGTH_LONG).show());
    }

    @Override
    public void showDialog(String title, String btnText, String message, IntroContract.DialogOnClickListener callback) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title)
                .setMessage(message)
                .setPositiveButton(btnText, (dialog, which) -> callback.dialogOnClick())
                .show();
    }

    @Override
    public void launchDeepLink(String deepLink) {
        Uri uri = Uri.parse(deepLink);
        Intent displayIntent = new Intent(Intent.ACTION_VIEW, uri);
        displayIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(uri);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    public void showTimelineActivity(boolean animated) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setClass(getApplicationContext(), TimelineActivity.class);
        int flag = Intent.FLAG_ACTIVITY_CLEAR_TASK;
        if (!animated) {
            flag |= Intent.FLAG_ACTIVITY_NO_ANIMATION;
        }
        intent.setFlags(flag);
        startActivity(intent);
        if (!animated) {
            overridePendingTransition(0, 0);
        }
        finish();
    }


/*
    private void initEmojiCompat() {
        final EmojiCompat.Config config;
        if (USE_BUNDLED_EMOJI) {
            // Use the bundled font for EmojiCompat
            config = new BundledEmojiCompatConfig(getApplicationContext());
        } else {
            // Use a downloadable font for EmojiCompat
            final FontRequest fontRequest = new FontRequest(
                    "com.google.android.gms.fonts",
                    "com.google.android.gms",
                    "Noto Color Emoji Compat",
                    R.array.com_google_android_gms_fonts_certs);
            config = new FontRequestEmojiCompatConfig(getApplicationContext(), fontRequest);
        }

        config.setReplaceAll(true)
                .registerInitCallback(new EmojiCompat.InitCallback() {
                    @Override
                    public void onInitialized() {
                        Log.i(TAG, "EmojiCompat initialized");
                    }

                    @Override
                    public void onFailed(@Nullable Throwable throwable) {
                        Log.e(TAG, "EmojiCompat initialization failed", throwable);
                    }
                });

        EmojiCompat.init(config);
    }

 */


}
