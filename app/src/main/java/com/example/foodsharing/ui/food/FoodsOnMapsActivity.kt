package com.example.foodsharing.ui.food

import android.Manifest
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.pm.PackageManager
import android.location.Criteria
import android.location.LocationManager
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.foodsharing.R
import com.example.foodsharing.model.FoodModel
import com.example.foodsharing.ui.request.MapsViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.button.MaterialButton
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.util.*

class FoodsOnMapsActivity : FragmentActivity(), OnMapReadyCallback {
    private val markers: MutableList<Marker> = ArrayList()
    private var currentMarker: Marker? = null
    private var mMap: GoogleMap? = null
    private val PERMISSION_REQUEST_CODE = 666
    private var coordinates: LatLng? = null

    private lateinit var getAddressBtn: MaterialButton

    private var latitude = 59.9
    private var longitude = 30.3

    private var model: FoodModel? = null
    private var defineLocation = false
    private val myClipboard: ClipboardManager? = null
    private val myClip: ClipData? = null
    private lateinit var mapsViewModel: MapsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        initViews()

        mapsViewModel = ViewModelProvider(this).get(MapsViewModel::class.java)
        intent.getStringExtra(FoodFragmentKt.EXTRA_OPEN_MAPS_WITH_ALL_FOODS)?.let {
            if (it == "") {
                defineLocation = true
                mapsViewModel.getData()
            }
        }

        model = intent.getSerializableExtra(FoodFragmentKt.EXTRA_FOOD) as FoodModel?
        if (model != null) {
            defineLocation = false
            latitude = model!!.address[0]
            longitude = model!!.address[1]
            getAddressBtn.setText(R.string.go_to_chat)
            getAddressBtn.setOnClickListener { createDialog() }
        }

        if (defineLocation) {
            requestPermissions()
        }
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(this)
    }


    private fun createDialog() {
        // The TextView to show your Text
        val showText = TextView(this)
        showText.text = model!!.email
        showText.setTextIsSelectable(true)
        MaterialAlertDialogBuilder(this).setTitle("Email")
            .setView(showText) //                .setNegativeButton("Отменить", (dialog, which) -> {
            //                })
            //                .setPositiveButton("Скопировать адрес почты", (dialog, which) -> {
            //                    myClipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
            //                    myClip = ClipData.newPlainText("text", model.getEmail());
            //                    myClipboard.setPrimaryClip(myClip);
            //
            //                    Toast.makeText(this, "Text Copied + ", Toast.LENGTH_SHORT).show();
            //                })
            .show()
    }

    private fun initViews() {
        getAddressBtn = findViewById(R.id.getAddressBtn)
        getAddressBtn.visibility = View.GONE
    }

    private fun requestPermissions() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
            || ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            requestLocation()
        } else {
            requestLocationPermissions()
        }
    }

    private fun requestLocationPermissions() {
        if (!ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                Manifest.permission.CALL_PHONE
            )
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ),
                PERMISSION_REQUEST_CODE
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.size == 2 && (grantResults[0] == PackageManager.PERMISSION_GRANTED || grantResults[1] == PackageManager.PERMISSION_GRANTED)
            ) {
                requestLocation()
            }
        }
    }

    private fun requestLocation() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        val locationManager =
            getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val criteria = Criteria()
        criteria.accuracy = Criteria.ACCURACY_COARSE
        val provider = LocationManager.NETWORK_PROVIDER
        locationManager.requestLocationUpdates(provider, 10000, 10f) { location ->
            latitude = location.latitude
            longitude = location.longitude
            val currentPosition =
                LatLng(latitude, longitude)
            currentMarker?.setPosition(currentPosition)
            mMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(currentPosition, 10f))
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
    override fun onMapReady(googleMap: GoogleMap?) {
        mMap = googleMap
        mapsViewModel.observeData().observe(this,
            Observer { foodsViewState ->
                if (foodsViewState is FoodsViewState.Value) {
                    val foods = foodsViewState.foods
                    setMarkers(foods)
                }
            })
        val currentLocation = LatLng(latitude, longitude)
        currentMarker =
            mMap?.addMarker(MarkerOptions().position(currentLocation).title("You are here"))
        mMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 10.0f))
    }

    private fun setMarkers(foods: List<FoodModel>) {
        for (food in foods) {
            addMarker(
                LatLng(
                    food.address[0],
                    food.address[1]
                )
            )
        }
    }

    private fun addMarker(location: LatLng) {
        val marker = mMap!!.addMarker(MarkerOptions().position(location))
        markers.add(marker)
    }

}