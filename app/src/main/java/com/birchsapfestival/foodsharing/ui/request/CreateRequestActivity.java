package com.birchsapfestival.foodsharing.ui.request;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.birchsapfestival.foodsharing.R;
import com.birchsapfestival.foodsharing.model.FoodModel;
import com.birchsapfestival.foodsharing.util.Constants;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;

public class CreateRequestActivity extends AppCompatActivity {

    private ImageView avatarView;
    private MaterialButton gaveBtn, mapsBtn;
    private TextInputEditText titleInputLayout;
    private CalendarView calendarView;
    private Spinner filter;
    private CreateRequestViewModel createRequestViewModel;

    FoodModel model;

    public static final String EXTRA_MAP_BUNDLE = "EXTRA_MAP_BUNDLE";

    public static final String EXTRA_BUNDLE = "BUNDLE";
    public static final int MAX_LENGHT = 53;
    public static final int ACTIVITY_REQUEST_CODE = 100;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_request);

        initViews();
        createRequestViewModel =
                new ViewModelProvider(this).get(CreateRequestViewModel.class);
        avatarView.setOnClickListener(v -> {
            Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            galleryIntent.setType("image/*");
            startActivityForResult(galleryIntent, Constants.REQUEST_IMAGE.ordinal());
        });
        StringBuilder selectedDate = new StringBuilder();
        calendarView.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            selectedDate.setLength(0);
            selectedDate.append(dayOfMonth).append("/").append(month).append("/").append(year);
            Log.e("CalendarView: ", selectedDate.toString());
        });

        gaveBtn.setOnClickListener(v -> {
            if (titleInputLayout.getText().toString().equals("")) {
                Toast.makeText(this, getString(R.string.input_title), Toast.LENGTH_SHORT).show();
            } else if (titleInputLayout.getText().toString().length() > MAX_LENGHT) {
                Toast.makeText(this, getString(R.string.input_title_max_len), Toast.LENGTH_LONG).show();
            } else {
                model.setTitle(Objects.requireNonNull(titleInputLayout.getText()).toString());
                model.setData("Срок годности: " + selectedDate);
                String email = createRequestViewModel.getEmail();
                String phoneNumber = createRequestViewModel.getPhoneNumber();
                model.setEmail(email);
                model.setPhoneNumber(phoneNumber);
                model.setFilter(filter.getSelectedItem().toString());
                createRequestViewModel.pushRequest(model);
                finish();
            }
        });

        mapsBtn.setOnClickListener(v -> {
            Intent intent = new Intent(this, MapsActivity.class);
            startActivityForResult(intent, ACTIVITY_REQUEST_CODE);
        });
    }

    void initViews() {
        avatarView = findViewById(R.id.avatar_view);
        gaveBtn = findViewById(R.id.gave_btn);
        titleInputLayout = findViewById(R.id.title_text_input_lay);
        calendarView = findViewById(R.id.calendarView);
        filter = findViewById(R.id.collect_from_field);
        mapsBtn = findViewById(R.id.maps_button);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.REQUEST_IMAGE.ordinal() && resultCode == RESULT_OK && null != data) {
            Uri selectedImageUri = data.getData();
            Log.e(getClass().getSimpleName(), selectedImageUri.toString());
            avatarView.setImageURI(selectedImageUri);
        }

        if (requestCode == ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            Bundle bundle = data.getParcelableExtra(EXTRA_BUNDLE);
            model = new FoodModel();
            model.setAddress(bundle.getParcelable(EXTRA_MAP_BUNDLE));
            Toast.makeText(this, "SEND DATA", Toast.LENGTH_SHORT).show();
        }
    }
}
