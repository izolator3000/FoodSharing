package com.example.foodsharing.ui.food;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodsharing.R;
import com.example.foodsharing.model.FoodModel;
import com.example.foodsharing.ui.food.adapter.FoodAdapter;
import com.example.foodsharing.ui.profile.ProfileViewModel;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class FoodFragment extends Fragment {

    private FoodViewModel foodViewModel;

    private RecyclerView listOfFood;
    private FoodAdapter foodAdapter = new FoodAdapter();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        foodViewModel =
                new ViewModelProvider(this).get(FoodViewModel.class);
        return inflater.inflate(R.layout.fragment_food, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        foodViewModel.getData();

        foodViewModel.observeData().observe(getViewLifecycleOwner(), new Observer<List<Map<String, Object>>>() {
            @Override
            public void onChanged(List<Map<String, Object>> maps) {
                Log.e(getClass().getSimpleName(), maps.toString());
                for (Map<String, Object> document : maps) {
                    Log.d(getClass().getSimpleName(), document.get("title").toString());
                    Log.d(getClass().getSimpleName(), document.get("address_title").toString());
                    Log.d(getClass().getSimpleName(), document.get("data").toString());
                }
            }
        });
        foodAdapter.setData(new ArrayList<>(Arrays.asList(new FoodModel("TitleOne", "SubtitleOne", "DateOne")
                , new FoodModel("TitleTwo", "SubtitleTwo", "DateTwo"),
                new FoodModel("TitleThree", "SubtitleThree", "DateThree"),
                new FoodModel("TitleFour", "SubtitleFour", "DateFour"),
                new FoodModel("TitleFive", "SubtitleOFive", "DateOFive"),
                new FoodModel("TitleSix", "SubtitleSix", "DateSix"),
                new FoodModel("TitleSeven", "SubtitleSeven", "DateSeven"),
                new FoodModel("TitleEight", "SubtitleEight", "DateEight"),
                new FoodModel("TitleNine", "SubtitleNine", "DateNine"),
                new FoodModel("TitleTen", "SubtitleTen", "DateTen"))));
        listOfFood = view.findViewById(R.id.list_of_food);
        listOfFood.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        listOfFood.setAdapter(foodAdapter);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(requireContext(), RecyclerView.VERTICAL);
        dividerItemDecoration.setDrawable(Objects.requireNonNull(ResourcesCompat.getDrawable(getResources(), R.drawable.divider, null)));
        listOfFood.addItemDecoration(dividerItemDecoration);
    }
}