package com.samsung.uni_block_app.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.samsung.android.sdk.blockchain.account.ethereum.EthereumAccount;
import com.samsung.uni_block_app.model.ProductModel;
import com.samsung.uni_block_app.services.ProductService;
import com.samsung.uni_block_app.util.Util;

import java.util.ArrayList;

public class ProductViewModel extends AndroidViewModel {

    private ArrayList<ProductModel> planetList;

    private ProductService mProductService = new ProductService();

    public ProductViewModel(@NonNull Application application) {
        super(application);
    }

    public ArrayList<ProductModel> getPlanetList(EthereumAccount account, String contract, String name) {
        Log.i(Util.LOG_TAG, "Calling service for product list.");
        if (planetList == null) {
            planetList = mProductService.getProductList(account,contract, name);
        }
        return planetList;
    }

}
