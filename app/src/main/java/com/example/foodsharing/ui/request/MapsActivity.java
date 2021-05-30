package com.example.foodsharing.ui.request;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import com.example.foodsharing.R;
import com.example.foodsharing.ui.food.FoodFragmentKt;
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
    private GoogleMap mMap;
    private static final int PERMISSION_REQUEST_CODE = 666;
    private LatLng coordinates = null;

    private MaterialButton getAddressBtn = null;

    private double latitude = -34f, longitude = 151f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        initViews();
        if (getIntent().getDoubleArrayExtra(FoodFragmentKt.EXTRA_COORDINATES) != null) {
            latitude = getIntent().getDoubleArrayExtra(FoodFragmentKt.EXTRA_COORDINATES)[0];
            longitude = getIntent().getDoubleArrayExtra(FoodFragmentKt.EXTRA_COORDINATES)[1];
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

        requestPermissions();
    }

    private void createDialog() {
        new MaterialAlertDialogBuilder(this).setTitle("Email")
                .setMessage("URL: ")
//                .setNegativeButton("Отменить", (dialog, which) -> {
//                })
//                .setPositiveButton("Написать", (dialog, which) -> onLogout())
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

        String provider = locationManager.getBestProvider(criteria, true);
        if (provider != null) {
            locationManager.requestLocationUpdates(provider, 10000, 10, new LocationListener() {
                @Override
                public void onLocationChanged(@NonNull Location location) {
//                   TODO
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

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(latitude, longitude);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 12.0f));

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                mMap.clear();
                addMarker(latLng);
                coordinates = latLng;
            }
        });
    }

    private void addMarker(LatLng location) {
        String title = Integer.toString(markers.size());

        Marker marker = mMap.addMarker(new MarkerOptions().position(location).title(title));
        markers.add(marker);
    }
}