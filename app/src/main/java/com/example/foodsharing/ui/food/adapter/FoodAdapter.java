package com.example.foodsharing.ui.food.adapter;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodsharing.R;
import com.example.foodsharing.model.FoodModel;
import com.example.foodsharing.util.PicassoLoader;

import java.util.ArrayList;
import java.util.List;

public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.FoodViewHolder> {
    private List<FoodModel> data = new ArrayList<>();

    public void setData(ArrayList<FoodModel> newData) {
        data = newData;
    }

    @NonNull
    @Override
    public FoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list, parent, false);
        return new FoodViewHolder(view);
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
        private TextView subtitle;
        private TextView date;
        private ImageView foodImage;

        public FoodViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            subtitle = itemView.findViewById(R.id.address);
            date = itemView.findViewById(R.id.date);
            foodImage = itemView.findViewById(R.id.food_image);
        }

        void bind(FoodModel currentData) {
            title.setText(currentData.getTitle());
            subtitle.setText(currentData.getAddress_title());
            date.setText(currentData.getData());
            PicassoLoader.loadImage(Uri.parse(currentData.getUrl()),foodImage);
        }
    }
}
