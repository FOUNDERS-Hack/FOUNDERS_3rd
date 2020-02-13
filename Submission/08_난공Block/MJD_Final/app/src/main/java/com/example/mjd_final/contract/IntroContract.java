package com.example.mjd_final.contract;

public interface IntroContract {
    interface View {
        void setLoading(boolean isLoading);

        void toastMessage(String message);

        void showDialog(String title, String btnText, String message, DialogOnClickListener callback);

        void launchDeepLink(String deeplink);

        void showMainActivity(boolean animated);
    }

    interface Presenter {
        boolean initializeKeyStore();
    }

    interface DialogOnClickListener {
        void dialogOnClick();
    }
}
