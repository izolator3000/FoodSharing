package com.example.foodsharing.ui.request;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.foodsharing.R;
import com.example.foodsharing.model.FoodModel;
import com.example.foodsharing.util.Constants;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Objects;

public class CreateRequestActivity extends AppCompatActivity {

    private ImageView avatarView;
    private MaterialButton gaveBtn;
    private TextInputEditText titleInputLayout, addressInputLayout, fromInputLayout, toInputLayout;
    private CalendarView calendarView;
    private RadioButton thing, collect;
    private CreateRequestViewModel createRequestViewModel;

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
            if (titleInputLayout == null || addressInputLayout == null) {
                Toast.makeText(this, getString(R.string.input_address_and_title), Toast.LENGTH_SHORT).show();
            } else {
                StringBuilder address = new StringBuilder("");

                if (addressInputLayout != null) {
                    address.append(addressInputLayout.getText());
                    if (fromInputLayout != null && toInputLayout != null)
                        address.append(": ").append(fromInputLayout.getText()).append("-").append(toInputLayout.getText());
                }
                FoodModel model = new FoodModel(Objects.requireNonNull(titleInputLayout.getText()).toString(), address.toString(), "Срок годности: " + selectedDate);
                createRequestViewModel.pushRequest(model);
            }
        });
    }

    void initViews() {
        avatarView = findViewById(R.id.avatar_view);
        gaveBtn = findViewById(R.id.gave_btn);
        titleInputLayout = findViewById(R.id.title_text_input_lay);
        addressInputLayout = findViewById(R.id.address_text_input_lay);
        fromInputLayout = findViewById(R.id.from_text_input_lay);
        toInputLayout = findViewById(R.id.to_text_input_lay);
        calendarView = findViewById(R.id.calendarView);
        thing = findViewById(R.id.thing);
        collect = findViewById(R.id.collect_from_field);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.REQUEST_IMAGE.ordinal() && resultCode == RESULT_OK && null != data) {
            Uri selectedImageUri = data.getData();
            Log.e(getClass().getSimpleName(),selectedImageUri.toString());
            avatarView.setImageURI(selectedImageUri);
        } else {
            Toast.makeText(this, "You have not selected and image", Toast.LENGTH_SHORT).show();
        }
    }
}
