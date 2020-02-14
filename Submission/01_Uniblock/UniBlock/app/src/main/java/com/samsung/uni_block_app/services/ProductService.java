package com.samsung.uni_block_app.services;

import android.util.Log;

import com.samsung.android.sdk.blockchain.account.ethereum.EthereumAccount;
import com.samsung.uni_block_app.R;
import com.samsung.uni_block_app.model.ProductModel;
import com.samsung.uni_block_app.util.Util;

import java.util.ArrayList;

public class ProductService {


    private ArrayList<ProductModel> productList = new ArrayList<>();

    public void populateProductListRaw(EthereumAccount account, String contract, String name) {

        TokenService mTokenService = new TokenService();

        String value = mTokenService.getPrice(account, contract, name);

        ArrayList<ProductModel> productListRaw = new ArrayList<>();
        String sellerAddress = "0x59B8B4238D8cBdA87046df15a6eF2815CD807C80";
        productListRaw.add(new ProductModel(
                1,
                "Saturn",
                "Beautiful Planet",
                R.drawable.saturn,
                3.2, sellerAddress
        ));
        productListRaw.add(new ProductModel(
                2,
                "Jupiter",
                "Beautiful Planet",
                R.drawable.jupiter,
                3.0,
                sellerAddress
        ));
        productListRaw.add(new ProductModel(
                3,
                "Mars",
                "Beautiful Planet",
                R.drawable.mars,
                0.28,
                sellerAddress
        ));
        productListRaw.add(new ProductModel(
                4,
                "Earth",
                "Beautiful Planet",
                R.drawable.earth,
                0.15,
                sellerAddress
        ));
        productListRaw.add(new ProductModel(
                5,
                "Moon",
                "Beautiful Planet",
                R.drawable.moon,
                0.15,
                sellerAddress
        ));
        this.productList = productListRaw;
    }

    public ArrayList<ProductModel> getProductList(EthereumAccount account, String contract, String name) {
        if (productList.isEmpty()) {
            populateProductListRaw(account, contract, name);
            Log.i(Util.LOG_TAG, "Populated raw product list.");
        }
        return productList;
    }
}
