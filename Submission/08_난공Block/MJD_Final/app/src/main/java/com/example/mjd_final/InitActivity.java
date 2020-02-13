package com.example.mjd_final;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.databinding.DataBindingUtil;

import com.example.mjd_final.contract.IntroContract;
import com.example.mjd_final.databinding.ActivityInitBinding;
import com.example.mjd_final.util.PrefsHelper;;

public class InitActivity extends AppCompatActivity implements IntroContract.View {

    private static final String TAG = InitActivity.class.getSimpleName();
    private ActivityInitBinding binding;
    private IntroContract.Presenter contract;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_init);
        setLoading(false);

        ConnectKeyStore connectKeyStore = new ConnectKeyStore(this);
        contract = connectKeyStore;

        binding.setConnectKeyStore(connectKeyStore);

        String cachedSeedHash = PrefsHelper.getInstance().getCachedSeedHash();

        ConstraintLayout initLayout = findViewById(R.id.cl_init);
        ConstraintLayout splashLayout = findViewById(R.id.cl_splash);

        boolean alreadyInit = (!TextUtils.isEmpty(cachedSeedHash));

        initLayout.setVisibility(alreadyInit ? View.GONE : View.VISIBLE);
        splashLayout.setVisibility(alreadyInit ? View.VISIBLE : View.GONE);

        if (alreadyInit) {
            final Handler handler = new Handler();
            handler.postDelayed(() -> contract.initializeKeyStore(), 800);
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
    public void showMainActivity(boolean animated) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setClass(getApplicationContext(), MainActivity.class);
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

}
