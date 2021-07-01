package com.birchsapfestival.foodsharing.ui.food.adapter;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.birchsapfestival.foodsharing.R;
import com.birchsapfestival.foodsharing.model.FoodModel;
import com.birchsapfestival.foodsharing.util.PicassoLoader;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.FoodViewHolder> implements Filterable {

    private List<FoodModel> data = new ArrayList<>();
    private List<FoodModel> dataFiltered;
    private OnFoodClickListener listener = null;

    public void setData(List<FoodModel> newData) {
        data = newData;

        dataFiltered = newData;
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
        holder.bind(dataFiltered.get(position));
    }

    @Override
    public int getItemCount() {
        return dataFiltered == null ? 0 : dataFiltered.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                if (constraint.length() == 0) {
                    dataFiltered = data;
                } else {
                    ArrayList<FoodModel> filteredFood = new ArrayList<>();
                    for (FoodModel food : data) {
                        if (food.getType() != null && food.getType().contentEquals(constraint)) {
                            filteredFood.add(food);
                        }
                    }
                    dataFiltered = filteredFood;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = dataFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                dataFiltered = (ArrayList<FoodModel>) results.values;
                notifyDataSetChanged();
            }
        };
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
                onFoodClickListener.onClick(currentData);
            });
        }
    }
}
