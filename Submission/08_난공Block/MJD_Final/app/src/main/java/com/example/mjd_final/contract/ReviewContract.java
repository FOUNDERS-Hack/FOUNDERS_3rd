package com.example.mjd_final.contract;

public interface ReviewContract {
    interface ViewContract {
        void setLoadingProgress(boolean isLoading);

        void toastMessage(String message);
    }

    interface PresenterContract {
        void giveAuth(String userId, String storeName);

        void writeReview(String userId, String storeName, String comment);

        void voteReview(String userId, int reviewId, boolean approve);

        void setLoadingProgress(boolean isLoading);

        void toastMessage(String message);

        void finishActivity();
    }
}
