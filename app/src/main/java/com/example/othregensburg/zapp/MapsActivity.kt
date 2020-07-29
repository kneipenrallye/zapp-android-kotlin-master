package com.example.othregensburg.zapp

import android.location.Location
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import androidx.core.content.ContextCompat
import com.google.android.gms.location.*
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions


class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap

    //var for current location
    private var latitude: Double = 0.toDouble()
    private var longitude: Double = 0.toDouble()

    //Normally, properties declared as having a non-null type must be initialized in the constructor ->  null checks
    private lateinit var mLastLocation: Location
    private var mMarker: Marker? = null //Var can be null

    //Location doc
    //https://developers.google.com/android/reference/com/google/android/gms/location/FusedLocationProviderClient
    //https://developers.google.com/maps/documentation/android-sdk/location

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback

    //Global object in class
    companion object {
        private const val MY_PERMISSION_CODE: Int =
            1000 //1000 -> Geofence service not avialable (if user declines)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)


        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        //Request permission needs to be done in runtime for API level 23 (Android 6.0) and above
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkLocationPermission()) {
                buildLocationRequest()
                buildLocationCallback()

                fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
                fusedLocationProviderClient.requestLocationUpdates(
                    locationRequest,
                    locationCallback,
                    Looper.myLooper()
                )
            }
        } else {
            buildLocationRequest()
            buildLocationCallback()

            fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
            fusedLocationProviderClient.requestLocationUpdates(
                locationRequest,
                locationCallback,
                Looper.myLooper()
            )
        }
    }

    private fun buildLocationCallback() {
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(p0: LocationResult?) {

                //!! will throw NullPointerException if the value is null
                mLastLocation =
                    p0!!.locations.get(p0.locations.size - 1) //get last location and "!!" non-null assert needed for method

                //Nullcheck
                if (mMarker != null) {
                    mMarker!!.remove() //non-null assert needed for method
                }

                latitude = mLastLocation.latitude
                longitude = mLastLocation.longitude

                val latLng = LatLng(latitude, longitude)
                val markerOptions = MarkerOptions()
                    .position(latLng)
                    .title("Deine Position")
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                mMarker = mMap.addMarker(markerOptions)

                //Move camera from current view and zoom
                mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng))
                mMap.animateCamera(CameraUpdateFactory.zoomTo(15.2f))
            }
        }
    }

    private fun buildLocationRequest() {
        locationRequest = LocationRequest()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = 5000
        locationRequest.fastestInterval = 3000
        locationRequest.smallestDisplacement = 10f
    }

    //Boolfunc for Permission-detection
    private fun checkLocationPermission(): Boolean {
        if (ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION
                )
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                    MY_PERMISSION_CODE
                )
            } else
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                    MY_PERMISSION_CODE
                )
            return false
        } else
            return true
    }

    //Permisionrequest on runtime -> https://developer.android.com/training/permissions/requesting

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            MY_PERMISSION_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(
                            this,
                            android.Manifest.permission.ACCESS_FINE_LOCATION
                        ) == PackageManager.PERMISSION_GRANTED
                    )
                        if (checkLocationPermission()) {
                            buildLocationRequest()
                            buildLocationCallback()

                            fusedLocationProviderClient =
                                LocationServices.getFusedLocationProviderClient(this)
                            fusedLocationProviderClient.requestLocationUpdates(
                                locationRequest,
                                locationCallback,
                                Looper.myLooper()
                            )
                            mMap.isMyLocationEnabled = true
                        }
                } else {
                    //Possible output
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    //Stop updating location if app has been closed
    override fun onStop() {
        fusedLocationProviderClient.removeLocationUpdates(locationCallback)
        super.onStop()
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        //Init Google Play Service for location
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if (ContextCompat.checkSelfPermission(
                    this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                mMap.isMyLocationEnabled = true
            }
        } else
            mMap.isMyLocationEnabled = true

        //Enable zoom control
        mMap.uiSettings.isZoomControlsEnabled = true

        //Change maptype
        mMap.mapType = GoogleMap.MAP_TYPE_SATELLITE

        //Hardcoded -> will be changed (dynamic) soon with Google PlacesAPI and API-Key
        val RegensburgDom = LatLng(49.019587, 12.097515)
        val Bar13 = LatLng(49.021333, 12.092718)
        val Piratenhoehle = LatLng(49.020813, 12.095305)
        val Tiki_Beat = LatLng(49.019856, 12.089073)
        val Escobar = LatLng(49.017363, 12.093804)
        val Flannigans = LatLng(49.021078, 12.093829)
        val Murphys_Law = LatLng(49.017790, 12.093983)
        val Hemingways = LatLng(49.018060, 12.094900)

        val markerBar13 = MarkerOptions()
            .position(Bar13)
            .title("Bar 13")
            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))

        val markerPiratenhoehle = MarkerOptions()
            .position(Piratenhoehle)
            .title("Piratenhöhle")
            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))

        val markerTiki_Beat = MarkerOptions()
            .position(Tiki_Beat)
            .title("Tiki Beat")
            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))

        val markerEscobar = MarkerOptions()
            .position(Escobar)
            .title("Escobar")
            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))

        val markerFlannigans = MarkerOptions()
            .position(Flannigans)
            .title("Flannigans")
            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))

        val markerMurphys_Law = MarkerOptions()
            .position(Murphys_Law)
            .title("Murphys Law")
            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))

        val markerHemingways = MarkerOptions()
            .position(Hemingways)
            .title("Hemingway's")
            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))

        mMap.addMarker(MarkerOptions().position(RegensburgDom).title("Treffpunkt am Dom"))
        mMap.addMarker(markerBar13)
        mMap.addMarker(markerPiratenhoehle)
        mMap.addMarker(markerTiki_Beat)
        mMap.addMarker(markerEscobar)
        mMap.addMarker(markerFlannigans)
        mMap.addMarker(markerMurphys_Law)
        mMap.addMarker(markerHemingways)

        //If location permission gets denied set default cameralocation
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(RegensburgDom, 15.2f))
    }
}
