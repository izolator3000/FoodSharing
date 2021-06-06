package com.example.foodsharing.ui.request;

import android.Manifest;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.foodsharing.R;
import com.example.foodsharing.model.FoodModel;
import com.example.foodsharing.ui.food.FoodFragmentKt;
import com.example.foodsharing.ui.food.FoodsViewState;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private List<Marker> markers = new ArrayList<Marker>();
    private Marker currentMarker;
    private GoogleMap mMap;
    private static final int PERMISSION_REQUEST_CODE = 666;
    private LatLng coordinates = null;

    private MaterialButton getAddressBtn = null;

    private double latitude = 59.9f, longitude = 30.3f;

    private FoodModel model = null;

    private ClipboardManager myClipboard = null;
    private ClipData myClip = null;
    private MapsViewModel mapsViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        initViews();
        requestPermissions();
        mapsViewModel =
                new ViewModelProvider(this).get(MapsViewModel.class);
        if (getIntent().getStringExtra(FoodFragmentKt.EXTRA_OPEN_MAPS_WITH_ALL_FOODS).equals("")) {
            mapsViewModel.getData();
        }
        model = (FoodModel) getIntent().getSerializableExtra(FoodFragmentKt.EXTRA_FOOD);
        if (model != null) {
            latitude = model.getAddress().get(0);
            longitude = model.getAddress().get(1);
            getAddressBtn.setText(R.string.go_to_chat);
            getAddressBtn.setOnClickListener(v -> {
                createDialog();
            });
        } else {
            getAddressBtn.setOnClickListener(v -> {
                if (coordinates != null) {
                    Bundle args = new Bundle();
                    args.putParcelable(CreateRequestActivity.EXTRA_MAP_BUNDLE, coordinates);
                    Intent intent = new Intent();
                    intent.putExtra(CreateRequestActivity.EXTRA_BUNDLE, args);
                    setResult(RESULT_OK, intent);
                    finish();
                }
            });
        }

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }


    private void createDialog() {
        // The TextView to show your Text
        TextView showText = new TextView(this);
        showText.setText(model.getEmail());
        showText.setTextIsSelectable(true);
        new MaterialAlertDialogBuilder(this).setTitle("Email")
                .setView(showText)
//                .setNegativeButton("Отменить", (dialog, which) -> {
//                })
//                .setPositiveButton("Скопировать адрес почты", (dialog, which) -> {
//                    myClipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
//                    myClip = ClipData.newPlainText("text", model.getEmail());
//                    myClipboard.setPrimaryClip(myClip);
//
//                    Toast.makeText(this, "Text Copied + ", Toast.LENGTH_SHORT).show();
//                })
                .show();
    }

    private void initViews() {
        getAddressBtn = findViewById(R.id.getAddressBtn);
    }

    private void requestPermissions() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            requestLocation();
        } else {
            requestLocationPermissions();
        }
    }

    private void requestLocationPermissions() {
        if (!ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CALL_PHONE)) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length == 2 && (grantResults[0] == PackageManager.PERMISSION_GRANTED || grantResults[1] == PackageManager.PERMISSION_GRANTED)) {
                requestLocation();
            }
        }
    }

    private void requestLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_COARSE);

        String provider = LocationManager.NETWORK_PROVIDER;
        if (provider != null) {
            locationManager.requestLocationUpdates(provider, 10000, 10, new LocationListener() {
                @Override
                public void onLocationChanged(@NonNull Location location) {
                    latitude = location.getLatitude();
                    longitude = location.getLongitude();

                    LatLng currentPosition = new LatLng(latitude, longitude);
                    currentMarker.setPosition(currentPosition);
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentPosition, 10));
                }
            });
        }
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mapsViewModel.observeData().observe(this, new Observer<FoodsViewState>() {
            @Override
            public void onChanged(FoodsViewState foodsViewState) {
                if (foodsViewState instanceof FoodsViewState.Value) {
                    List<FoodModel> foods = ((FoodsViewState.Value) foodsViewState).getFoods();
                    setMarkers(foods);
                }
            }
        });

        LatLng currentLocation = new LatLng(latitude, longitude);

        currentMarker = mMap.addMarker(new MarkerOptions().position(currentLocation).title("You are here"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 10.0f));

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                mMap.clear();
                addMarker(latLng);
                coordinates = latLng;
            }
        });
    }

    private void setMarkers(List<FoodModel> foods) {
        for (int i = 0; i < foods.size(); i++) {
            addMarker(new LatLng(foods.get(i).getAddress().get(0), foods.get(i).getAddress().get(1)));
        }
    }

    private void addMarker(LatLng location) {
        String title = Integer.toString(markers.size());
        Marker marker = mMap.addMarker(new MarkerOptions().position(location).title(title));
        markers.add(marker);
    }
}