package com.samsung.uni_block_app.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.samsung.uni_block_app.R;
import com.samsung.uni_block_app.model.ProductModel;

import java.util.ArrayList;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {

    private ArrayList<ProductModel> mProductList;
    private OnItemClickListener listener;

    public ProductAdapter(ArrayList<ProductModel> productList, OnItemClickListener listener) {
        this.mProductList = productList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View productView = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_listview_list_item, parent, false);
        return new ViewHolder(productView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.productBind(mProductList.get(position), listener);
    }

    @Override
    public int getItemCount() {
        return mProductList.size();
    }

    public interface OnItemClickListener {
        void OnItemClick(ProductModel productModel, View productView);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final View view;
        private final TextView productName;
        private final TextView productDetails;
        private final TextView productPrice;
        private final ImageButton productImageButton;
        private final ImageButton productPurchaseButton;

        public ViewHolder(View view) {
            super(view);
            this.view = view;
            productName = view.findViewById(R.id.product_name_textView);
            productDetails = view.findViewById(R.id.product_details_textView);
            productPrice = view.findViewById(R.id.product_price_textView);
            productImageButton = view.findViewById(R.id.product_image_button);
            productPurchaseButton = view.findViewById(R.id.product_purchase_button);
        }

        public void productBind(ProductModel mProduct, OnItemClickListener listener) {
            productName.setText(mProduct.getProductName());
            productDetails.setText(mProduct.getProductDetails());
            productPrice.setText("" + mProduct.getProductPrice());
            productImageButton.setImageResource(mProduct.getProductImage());
            productPurchaseButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.OnItemClick(mProduct, view);
                }
            });
        }
    }
}
