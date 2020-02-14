package com.samsung.uni_block_app.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.samsung.uni_block_app.R;
import com.samsung.uni_block_app.model.SetupModel;

import java.util.ArrayList;

public class SetupAdapter extends RecyclerView.Adapter<SetupAdapter.ViewHolder> {

    private ArrayList<? extends SetupModel> mOptionsList;
    private OnItemClickListener listener;

    public SetupAdapter(ArrayList<? extends SetupModel> optionsList, OnItemClickListener listener) {
        this.mOptionsList = optionsList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public SetupAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View walletView = LayoutInflater.from(parent.getContext()).inflate(R.layout.wallet_list_item, parent, false);
        return new ViewHolder(walletView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.walletBind(mOptionsList.get(position), listener);
    }

    @Override
    public int getItemCount() {
        return mOptionsList.size();
    }

    public interface OnItemClickListener {
        void OnItemClick(int position);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final View view;
        private final TextView walletName;
        private final ImageView imageView;
        private final ImageButton selectButton;

        public ViewHolder(View view) {
            super(view);
            this.view = view;
            this.walletName = view.findViewById(R.id.wallet_item_title);
            this.imageView = view.findViewById(R.id.wallet_item_image_view);
            this.selectButton = view.findViewById(R.id.select_button);
        }

        public void walletBind(SetupModel mWalletModel, OnItemClickListener listener) {
            walletName.setText(mWalletModel.getName());
            imageView.setImageResource(mWalletModel.getImageId());
            selectButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.OnItemClick(getAdapterPosition());
                }
            });
        }
    }
}
