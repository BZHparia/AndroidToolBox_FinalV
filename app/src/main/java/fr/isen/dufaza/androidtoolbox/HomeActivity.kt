package fr.isen.dufaza.androidtoolbox

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        val sharedPreferences = getSharedPreferences("Pref", Context.MODE_PRIVATE)

        if(!isPermissionAllowed()){
            requestPermission()
        }


        idButtonCycle.setOnClickListener {
            startActivity( Intent (this@HomeActivity, LifeCycleActivity::class.java))
        }

        idSave.setOnClickListener {
            startActivity( Intent (this@HomeActivity, FormulaireActivity::class.java))
        }

        idBtnPermit.setOnClickListener {
            startActivity( Intent (this@HomeActivity, PermitActivity::class.java))
        }

        idBtnWebServices.setOnClickListener {
            startActivity( Intent (this@HomeActivity, WebServicesActivity::class.java))
        }

        idBtnBLE.setOnClickListener {
            startActivity( Intent (this@HomeActivity, BLEscanActivity::class.java))
        }

        idButtonDeco.setOnClickListener {
            val editor = sharedPreferences.edit()
            editor.putString("Login", " ")
            editor.apply()

            val intent = Intent(this, LoginActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }

        idLogged.setText(sharedPreferences.getString("Login"," "))
    }

    fun isPermissionAllowed(): Boolean {
        return if ( ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            true
        } else {
            false
        }
    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(
            this, arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION
            ), 5555
        )
    }
}
