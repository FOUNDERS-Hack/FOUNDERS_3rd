package com.samsung.uni_block_app.view;

import android.content.Intent;

import androidx.fragment.app.Fragment;

import com.google.zxing.integration.android.IntentIntegrator;

public class FragmentAndQRIntentIntegrator extends IntentIntegrator {

    private Fragment callingFragment;

    FragmentAndQRIntentIntegrator(Fragment callingFragment) {
        super(callingFragment.getActivity());
        this.callingFragment = callingFragment;
    }

    @Override
    protected void startActivityForResult(Intent intent, int requestCode) {
        callingFragment.startActivityForResult(intent, requestCode);
    }

}
