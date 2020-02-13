package com.android.samsung.codelab.guestbookdapp.presenter;

import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import com.android.samsung.codelab.guestbookdapp.contract.IntroContract;
import com.android.samsung.codelab.guestbookdapp.model.UserInfo;
import com.android.samsung.codelab.guestbookdapp.util.PrefsHelper;
import com.samsung.android.sdk.coldwallet.ScwDeepLink;
import com.samsung.android.sdk.coldwallet.ScwService;

import java.util.ArrayList;
import java.util.List;

public class IntroPresenter implements IntroContract.Presenter {

    private static final String TAG = IntroPresenter.class.getSimpleName();
    private IntroContract.View mContract;

    public IntroPresenter(IntroContract.View mContract) {
        this.mContract = mContract;
    }

    @Override
    public boolean initializeKeystore() {
        String cachedSeedHash = PrefsHelper.getInstance().getCachedSeedHash();

        // Check Samsung blockchain keystore is supported or not.
        if (ScwService.getInstance() == null) {
            mContract.toastMessage("Samsung blockchain Keystore is not supported on your device.");
        }
        // check installed api level
        else if (ScwService.getInstance().getKeystoreApiLevel() < 1) {
            // if api level is lower, jump to galaxy apps to update blockchain keystore app.
            mContract.showDialog(""
                    , "OK"
                    , "The api level is too low. Jump to galaxy store"
                    , () -> mContract.launchDeepLink(ScwDeepLink.GALAXY_STORE));
        }
        // check seed hash exist.
        else if (ScwService.getInstance().getSeedHash().length() == 0) {
            // if seed hash is empty, jump to blockchain keystore to create or import wallet
            mContract.showDialog(""
                    , "OK"
                    , "The seed hash is empty." +
                            "Jump to blockchain keystore to create/import wallet."
                    , () -> mContract.launchDeepLink(ScwDeepLink.MAIN));
        }
        // check seed hash cached
        else if (!TextUtils.equals(cachedSeedHash, ScwService.getInstance().getSeedHash())) {
            // if the seed hash is different from cached, update seed hash and address
            // go to next activity
            final String ethereumHdPath = "m/44'/60'/0'/0/0";
            getEthereumAddress(ethereumHdPath
                    , (success, errorCode, address, seedHash) -> {
                        if (success) {
                            updateAddress(address);
                            updateSeedHash(seedHash);
                            mContract.showTimelineActivity(true);
                        } else {
                            mContract.toastMessage("Cannot get address. error code :" + errorCode);
                        }
                        mContract.setLoading(false);
                    });
            return false;
        }
        // success
        else {
            // set address from cached value
            // go to next activity
            String address = PrefsHelper.getInstance().getCachedAddress();

            updateSeedHash(cachedSeedHash);
            updateAddress(address);
            mContract.showTimelineActivity(false);
        }

        return true;
    }

    public void onClickBlockchainKeystore() {
        mContract.setLoading(true);
        boolean immediateInit = initializeKeystore();
        if (immediateInit) {
            mContract.setLoading(false);
        }
    }

    public void onClickInAppKeystore() {
        Log.d(TAG, "In-app Keystore is not supported");
    }

    private void getEthereumAddress(String hdpath, GetEthereumAddressCallback callback) {
        ArrayList<String> path = new ArrayList<>();
        path.add(hdpath);
        ScwService.getInstance().getAddressList(new ScwService.ScwGetAddressListCallback() {
            @Override
            public void onSuccess(List<String> list) {
                String seedHash = ScwService.getInstance().getSeedHash();
                String address = list.get(0);
                callback.OnAddressReceived(true, 0, address, seedHash);
            }

            @Override
            public void onFailure(int errorCode, @Nullable String errorMsg) {
                callback.OnAddressReceived(false, errorCode, "", "");
            }

        }, path);

    }

    private void updateSeedHash(String seedHash) {
        PrefsHelper.getInstance().updateSeedHash(seedHash);
        UserInfo.getInstance().setSeedHash(seedHash);
    }

    private void updateAddress(String address) {
        PrefsHelper.getInstance().updateAddress(address);
        UserInfo.getInstance().setAddress(address);
    }

    interface GetEthereumAddressCallback {
        void OnAddressReceived(boolean success, int errorCode, String address, String seedHash);
    }
}
