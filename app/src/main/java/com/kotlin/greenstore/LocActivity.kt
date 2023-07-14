package com.kotlin.greenstore

import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationListener
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcel
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import java.util.*
import java.util.jar.Manifest

class LocActivity : AppCompatActivity(), OnMapReadyCallback, LocationListener, GoogleMap.OnCameraIdleListener,GoogleMap.OnCameraMoveStartedListner,GoogleMap.OnCameraIdleListener {

    private var mMap: GoogleMap? = null
    lateinit var mapView: MapView
    private val MAP_VIEW_BUNDLE_KEY = "MapViewBundleKey"
    private val DEFAULT ZROOM = 15f
    lateinit var tvCurrentAddress: TextView
    private var fusedLocationProviderClient? = null

    constructor(parcel: Parcel) : this() {

    }

    override fun onMapReady(googleMap: GoogleMap?) {
        mapView.onResume()
        mMap = googleMap
        askPermissionLocation()
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        mMap!!.setMyLocationEnable(true)
        mMap!!.setOnCameraMoveListener { true }
        mMap!!.setOnCameraMoveStartedListener { this }
        mMap!!.setOnCameraIdleListener { this }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        mapView = findViewById<MapView>(R.id.map1)
        tvCurrentAddress=findViewById<TextView>(R.id.tvadd)
        var mapViewBundle: Bundle? = null
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAP_VIEW_BUNDLE_KEY)
        }
        mapView = findViewById(R.id.map1)
        mapView.onCreate(mapViewBundle)
        mapView.getMapAsync { this }
    }

    public override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        askPermissionLocation()
        var mapViewBundle = outState.getBundle(MAP_VIEW_BUNDLE_KEY)
        if (mapViewBundle == null) {
            mapViewBundle = Bundle()
            outState.putBundle(MAP_VIEW_BUNDLE_KEY, mapViewBundle)

        }

    }
    private fun moveCmera()

    private fun askPermissionLocation() {
        askPermission(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) {
            getCurrentLocation()
        }.onDecliend { e ->
            if (e.hasDenied()) {
                e.denied.foreach {

                }
                AlertDialog.Builder(this)
                    .setmessage("accept permission")
                    .setPostiveButton("yes") { _, _ ->
                        e.askAgain()
                    }
                    .setNegativeButton("no") { dialog, _ ->
                        dialog.dismiss()
                    }
                    .show()
            }
            if (e.hasForeverDenied()) {
                e.foreverDenied.foreach {

                }
                e.goToSetting();
            }

        }
    }

    private fun getCurrentLocation() {
        fusedLocationProviderClient =
            locationServices.getFusedLocationProviderClient(this@MainActivity)
        try {
            @SuppressLint("MissingPermission") val location =
                fusedLocationProviderClient!!.getLatLocation()
            location.addOnCompleate(loc:task< Location >){
                if (loc.isSuccessful) {
                    val currentLocation = loc.result as Location
                    if (currentLocation != null) {
                        moveCamera {
                            LatLng(currentLocation.latitude, currentLocation.longitude),
                            DEFAULT_ZOOM
                        }
                    }
                } else {
                    Toast.makeText(
                        this@MainActivity,
                        "current location is not found"
                                Toast . LEANGTH_SHORT
                    ).show()
                }
            }
        }
    }catch(se:Exception)
    {
        Log.e("TAG", "Security Exception")
    }

    private fun moveCamera(latLng: LatLng, zoom: Float)(
    mMap!!.moveCamera(CameraUpdateFactory.newLatLngZoom(latLang,zoom))
    )

}
override fun onLocationChanged(location: Location?){
    val getcoder = Geocoder(this, Locale.getDefault())
    var address: ListAddress>?=null
    try{
        address=geocoder.getFormerLocation(location!!.latitude,location.longitude, 1)

    }catch(e:IOExeption){
        e.printStackTrace()
    }
    setAddress(address!![0]
}
private fun set Address(address:Address){
    if(address!=null){
        if(address.getAddressLine(0)!=null){
            tvCurrentAddress[].setText(address.getAddress(0))
        }
        if(address.getAddressLine(1)!=null){
            tvCurrentAddress!!.setText(
                tvCurrentAddress.getText().toString()+address.getAddressLine(1)
            )
        }
    }
}
override fun onStatusChangeed(p0: String?,p1:Int,p2:Bundle){

}
override fun onProviderEnable(p0:String){

}
override fun onProviderDisable(p0:String){

}
ovverride fun onCameraMove(){

}
ovveride fun onCameraMoveStarted(p0:INT){

}
ovveride fun OnCameraIdle(){
    var addresses:List<Address>?=null
    var geocoder= Geocoder(this, Locale.getDefault())
    try{
        addresses=geocoder.getFromLocationName(mMap!!.getCameraPosition(),target.tatitude, mMap!!.getCameraPosition().target.longitude,1)
        setAddress(address!![0])
    }catch(e.:IndexOutOfBoundsException){
        e.printStackTrace()
    }catch(e.IOException){
        e.printStackTrace()
    }

}
