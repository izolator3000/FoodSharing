package com.birchsapfestival.foodsharing.ui.profile;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.birchsapfestival.foodsharing.R;
import com.birchsapfestival.foodsharing.ui.main.MainActivity;
import com.birchsapfestival.foodsharing.ui.main.SplashActivity;
import com.birchsapfestival.foodsharing.ui.main.SplashViewState;
import com.birchsapfestival.foodsharing.ui.profile.my_food.MyFoodActivity;
import com.birchsapfestival.foodsharing.ui.request.CreateRequestActivity;
import com.birchsapfestival.foodsharing.util.Constants;
import com.birchsapfestival.foodsharing.util.PicassoLoader;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class ProfileFragment extends Fragment {

    public static final String EXTRA_VIEW_STATE = "EXTRA_VIEW_STATE";
    private ProfileViewModel profileViewModel;

    private MaterialButton gaveFoodBtn, gaveBtn, logOutBtn;
    private ImageView avatarView;
    private TextView firstNameView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        profileViewModel =
                new ViewModelProvider(this).get(ProfileViewModel.class);
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        setOnBtnClickListener();
        profileViewModel.setName();
        profileViewModel.observeUser().observe(getViewLifecycleOwner(), currentUser -> {
            firstNameView.setText(currentUser.getName());
            if (currentUser.getPhotoUrl() != null) {
                PicassoLoader.loadImage(currentUser.getPhotoUrl(), avatarView);
            }
        });

    }

    private void setOnBtnClickListener() {
        gaveFoodBtn.setOnClickListener(v -> startActivity(new Intent(getContext(), MyFoodActivity.class)));

        gaveBtn.setOnClickListener(v -> {
            startActivity(new Intent(getContext(), CreateRequestActivity.class));
            ((MainActivity) requireActivity()).navigateToFoodScreen();
        });

        logOutBtn.setOnClickListener(v -> {
            showLogoutDialog();
        });


        avatarView.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("image/*");
            startActivityForResult(intent, Constants.REQUEST_IMAGE.ordinal());
        });
    }

    private void showLogoutDialog() {
        new MaterialAlertDialogBuilder(getContext()).setTitle("Выйти из аккаунта")
                .setMessage("Вы уверены, что хотите выйти из аккаунта? Все ваши данные будут сохранены.")
                .setNegativeButton("Нет", (dialog, which) -> {
                })
                .setPositiveButton("Да", (dialog, which) -> onLogout())
                .show();
    }

    private void onLogout() {
        AuthUI.getInstance().signOut(getContext())
                .addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        Intent intent = new Intent(getContext(), SplashActivity.class);
                        intent.putExtra(EXTRA_VIEW_STATE, SplashViewState.EMPTY);
                        startActivity(intent);
                        requireActivity().finish();
                    }
                });
    }

    private void createDialog() {
        new MaterialAlertDialogBuilder(getContext()).setTitle("Download photo")
                .setMessage("Do you want download photo?")
                .setNegativeButton("NO", (dialog, which) -> Toast.makeText(getContext(), "NO", Toast.LENGTH_SHORT).show())
                .setPositiveButton("YES", (dialog, which) -> Toast.makeText(getContext(), "YES", Toast.LENGTH_SHORT).show())
                .show();
    }

    private void initView(View view) {
        gaveFoodBtn = view.findViewById(R.id.gave_food_btn);
        gaveBtn = view.findViewById(R.id.gave_btn);
        logOutBtn = view.findViewById(R.id.log_out);
        avatarView = view.findViewById(R.id.avatar_view);
        firstNameView = view.findViewById(R.id.first_name);
    }
}