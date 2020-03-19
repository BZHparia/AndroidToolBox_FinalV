package fr.isen.dufaza.androidtoolbox

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        val sharedPreferences = getSharedPreferences("Pref", Context.MODE_PRIVATE)


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
}
