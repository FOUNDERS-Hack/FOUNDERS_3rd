package com.android.samsung.codelab.guestbookdapp.contract;

public interface WriteFeedContract {

    interface ViewContract {
       // void setEmojiBottomSheet();

        void setLoadingProgress(boolean isLoading);

        void toastMessage(String message);

        void finishActivity();
    }

    interface PresenterContract {
        void actionSend();
    }
}
