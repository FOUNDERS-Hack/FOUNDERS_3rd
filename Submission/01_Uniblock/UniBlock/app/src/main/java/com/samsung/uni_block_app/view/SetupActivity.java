package com.samsung.uni_block_app.view;

import android.os.Bundle;
import android.view.View;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import com.samsung.uni_block_app.R;
import com.samsung.uni_block_app.adapter.CoinTypeAdapter;
import com.samsung.uni_block_app.viewmodel.SetupViewModel;
import com.samsung.android.sdk.blockchain.wallet.HardwareWalletType;

public class SetupActivity extends AppCompatActivity {

    private SetupViewModel mSetupViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        mSetupViewModel = ViewModelProviders.of(this).get(SetupViewModel.class);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_settings);

        HardwareWalletType selectedWallet = (HardwareWalletType) getIntent().getExtras().getSerializable("WALLET_TYPE");
        TextView hardwareWalletTextView = findViewById(R.id.hardware_wallet_textview);
        hardwareWalletTextView.setText(selectedWallet.toString());

        Spinner coinTypeSpinner = findViewById(R.id.coin_type_spinner);
        CoinTypeAdapter coinTypeAdapter = new CoinTypeAdapter(this, mSetupViewModel.getSupportedCoinTypes());
        coinTypeSpinner.setAdapter(coinTypeAdapter);

        TextView restoreWalletTextView = findViewById(R.id.restore_wallet_text_view);
        restoreWalletTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), R.string.under_development, Toast.LENGTH_SHORT).show();
            }
        });

    }

}
