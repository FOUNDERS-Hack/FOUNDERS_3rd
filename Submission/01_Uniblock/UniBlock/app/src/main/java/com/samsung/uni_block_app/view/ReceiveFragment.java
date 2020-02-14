package com.samsung.uni_block_app.view;


import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.samsung.uni_block_app.R;
import com.samsung.uni_block_app.databinding.FragmentReceiveBinding;
import com.samsung.uni_block_app.util.Util;
import com.samsung.uni_block_app.viewmodel.AccountInformationViewModel;

/**
 * A simple {@link Fragment} subclass.
 */
public class ReceiveFragment extends Fragment {

    private AccountInformationViewModel mAccountInformationViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Always pass activity instead of fragment since parent activity stays alive even if fragment is switched.
        mAccountInformationViewModel = ViewModelProviders.of(getActivity()).get(AccountInformationViewModel.class);
        FragmentReceiveBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_receive, container, false);
        binding.setAccountInformationViewModel(mAccountInformationViewModel);
        binding.setLifecycleOwner(this);
        return binding.getRoot();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button copyButton = view.findViewById(R.id.copy_button);
        copyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClipboardManager clipboardManager = (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
                String msg = "";
                if (mAccountInformationViewModel.getSelectedAccount().getValue() == null ){
                     msg = "Loading Accounts.Please Wait...";
                } else {
                    ClipData clipData = ClipData.newPlainText("Public Address", mAccountInformationViewModel.getSelectedAccount().getValue().getAddress());
                    clipboardManager.setPrimaryClip(clipData);
                    msg = "Address Copied";
                }
                Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
            }
        });

        Button shareButton = view.findViewById(R.id.share_button);
        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mAccountInformationViewModel.getSelectedAccount().getValue() == null) {
                    Toast.makeText(getContext(), "Loading Accounts.Please Wait...", Toast.LENGTH_SHORT).show();
                }
                else {
                    Intent sendIntent = new Intent();
                    sendIntent.setAction(Intent.ACTION_SEND);
                    sendIntent.putExtra(Intent.EXTRA_TEXT, mAccountInformationViewModel.getSelectedAccount().getValue().getAddress());
                    sendIntent.setType("text/plain");
                    startActivity(sendIntent);
                }
            }
        });

        ImageView qrCodeImageView = view.findViewById(R.id.qr_code_imageview);

        mAccountInformationViewModel.getSelectedAccount().observe(this, account ->{
            view.findViewById(R.id.qr_background).setVisibility(View.GONE);
            qrCodeImageView.setImageBitmap(Util.generateQRCode(account.getAddress()));
        });
    }

}
