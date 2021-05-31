package com.example.foodsharing.ui.food.adapter;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodsharing.R;
import com.example.foodsharing.model.FoodModel;
import com.example.foodsharing.util.PicassoLoader;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.FoodViewHolder> {
    private List<FoodModel> data = new ArrayList<>();
    private OnFoodClickListener listener = null;

    public void setData(List<FoodModel> newData) {
        data = newData;
        notifyDataSetChanged();
    }

    public void attachListener(OnFoodClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public FoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list, parent, false);
        return new FoodViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodViewHolder holder, int position) {
        holder.bind(data.get(position));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class FoodViewHolder extends RecyclerView.ViewHolder {
        private TextView title;
        private TextView date;
        private ImageView foodImage;
        private CardView root;
        private OnFoodClickListener onFoodClickListener;

        public FoodViewHolder(@NonNull View itemView, OnFoodClickListener onFoodClickListener) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            date = itemView.findViewById(R.id.date);
            foodImage = itemView.findViewById(R.id.food_image);
            root = itemView.findViewById(R.id.root);
            this.onFoodClickListener = onFoodClickListener;
        }

        void bind(FoodModel currentData) {
            title.setText(currentData.getTitle());
            date.setText(currentData.getData());
            PicassoLoader.loadImage(Uri.parse(currentData.getUrl()), foodImage);
            root.setOnClickListener(v -> {
                onFoodClickListener.onClick(currentData.getAddress());
            });
        }
    }
}
