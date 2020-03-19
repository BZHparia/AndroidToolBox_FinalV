package fr.isen.dufaza.androidtoolbox

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.location.Location
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.provider.ContactsContract
import android.provider.MediaStore
import android.provider.Settings
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.location.*
import kotlinx.android.synthetic.main.activity_permit.*

class PermitActivity : AppCompatActivity() {

    private val namePerson: MutableList<Contact> = ArrayList()
    private lateinit var mFusedLocationClient: FusedLocationProviderClient

    companion object{
        private const val IMAGE_PICK_REQUEST = 1000
        private const val CAMERA_PICK_REQUEST = 4444
        private const val PERMISSION_CONTACT_CODE = 1002
        private const val PERMISSION_ID = 42
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_permit)

        refreshPermitActivity.setOnClickListener{
            finish();
            startActivity( Intent (this@PermitActivity, PermitActivity::class.java));
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.READ_CONTACTS)==
                PackageManager.PERMISSION_DENIED
            ) {

                val permissions = arrayOf(Manifest.permission.READ_CONTACTS)

                requestPermissions(permissions, PERMISSION_CONTACT_CODE)
            } else {

                contactFunction()
            }
        } else{
            contactFunction()
        }
        photoUser.setOnClickListener {
            withItems()
        }

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        getLastLocation()

        idListContact.layoutManager = LinearLayoutManager(this)
        idListContact.adapter = ContactList(namePerson,this)


    }

    @SuppressLint("MissingPermission")
    private fun getLastLocation() {
        if (checkPermissions()) {
            if (isLocationEnabled()) {

                //val addOnCompleteListener: Any =
                    mFusedLocationClient.lastLocation.addOnCompleteListener(this) { task ->
                        val location: Location? = task.result
                        if (location == null) {
                            requestNewLocationData()
                        } else {
                            findViewById<TextView>(R.id.idLatitude).text =
                                location.latitude.toString()
                            findViewById<TextView>(R.id.idLongitude).text =
                                location.longitude.toString()
                        }
                    }
            } else {
                Toast.makeText(this, "Turn on location", Toast.LENGTH_LONG).show()
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
            }
        } else {
            requestPermissions()
        }
    }

    @SuppressLint("MissingPermission")
    private fun requestNewLocationData() {
        val mLocationRequest = LocationRequest()
        mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        mLocationRequest.interval = 0
        mLocationRequest.fastestInterval = 0
        mLocationRequest.numUpdates = 1

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        mFusedLocationClient.requestLocationUpdates(
            mLocationRequest, mLocationCallback,
            Looper.myLooper()
        )
    }

    private val mLocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            val mLastLocation: Location = locationResult.lastLocation
            findViewById<TextView>(R.id.idLatitude).text = mLastLocation.latitude.toString()
            findViewById<TextView>(R.id.idLongitude).text = mLastLocation.longitude.toString()
        }
    }

    private fun isLocationEnabled(): Boolean {
        val locationManager: LocationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    private fun checkPermissions(): Boolean {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            return true
        }
        return false
    }

    private fun requestPermissions() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION),
            PERMISSION_ID
        )
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if ((requestCode == PERMISSION_ID) || (requestCode == PERMISSION_CONTACT_CODE)) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                getLastLocation()
            }
        }
    }

    private fun imageFromGallery(){
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, IMAGE_PICK_REQUEST)
    }

    private fun imageFromCamera(){
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(packageManager)?.also {
                startActivityForResult(takePictureIntent, CAMERA_PICK_REQUEST)
            }
        }
    }

    private fun withItems() {
        val items = arrayOf("Prendre une photo", "Galerie")
        val builder = AlertDialog.Builder(this)
        with(builder)
        {
            setTitle("Choisir : ")
            setItems(items) { _, which ->
                if(items[which] == "Prendre une photo"){
                    imageFromCamera()
                }
                else{
                    imageFromGallery()
                }
            }
            setPositiveButton("Retour", backButtonClick)
            show()
        }
    }

    private val backButtonClick = { _: DialogInterface, _: Int ->
        Toast.makeText(applicationContext, "Aucune photo choisie", Toast.LENGTH_SHORT).show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == IMAGE_PICK_REQUEST){
            photoUser.setImageURI(data?.data)
        }

        else if (resultCode == Activity.RESULT_OK && requestCode == CAMERA_PICK_REQUEST){
            val imageBitmap = data?.extras?.get("data") as Bitmap
            photoUser.setImageBitmap(imageBitmap)
        }
    }

    private fun contactFunction(){

        val cursorN= contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,null,null,null)
        cursorN?.let{ cursor ->
            while (cursor.moveToNext()){
                val name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))
                val number = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                val obj = Contact()
                obj.name = name
                obj.number = number
                namePerson.add(obj)
            }
        cursor.close()
        }
    }
}