package com.example.mjd_final;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.ObservableArrayList;
import androidx.fragment.app.Fragment;

import com.example.mjd_final.contract.AccountContract;
import com.example.mjd_final.model.Review;
import com.example.mjd_final.model.UserInfo;


public class AccountFragment extends Fragment{

    private ObservableArrayList<Review> myReviewList;
    private AccountContract.Presenter contract;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.activity_account_fragment, container, false);
        TextView nickName = (TextView) root.findViewById(R.id.nickname);
        String publicAddress = UserInfo.getInstance().getAddress();
        nickName.setText(publicAddress);

        /*try {
            BigInteger ethBalance = RemoteManager.getInstance().getBalance(publicAddress);
            String balanceString = getEthValueFromWei(ethBalance, -1) + " ETH";
            balance.setText(balanceString);

        } catch(Exception e) {e.printStackTrace();}*/

        this.myReviewList = new ObservableArrayList<>();

        return root;
    }
}
