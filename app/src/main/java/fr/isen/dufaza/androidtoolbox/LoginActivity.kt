
package fr.isen.dufaza.androidtoolbox

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)


        val sharedPreferences = getSharedPreferences("Pref", Context.MODE_PRIVATE)
        if(sharedPreferences.getString("Login"," ") != " "){
            val changePage = Intent(this, HomeActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(changePage)
        }

        idButtonLog.setOnClickListener {
            val id = idLog.text.toString()
            val mdp = idMdp.text.toString()
            //Pas bien de stocker en dur, il faudrait interroger une DB
            val rightId = "admin"
            val rightMdp = "123"
            var message = "$id authentifié"

            if(id == rightId && mdp == rightMdp){

                val editor = getSharedPreferences("Pref", Context.MODE_PRIVATE).edit()
                editor.putString("Login", id)
                editor.putString("Pass", mdp)
                editor.apply()

                val changePage = Intent(this, HomeActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(changePage)
            }
            else{
                message = "Authentification échouée"
                val toast2 = Toast.makeText(this,message, Toast.LENGTH_LONG)
                toast2.show()
                toast2.setGravity(Gravity.BOTTOM,0,200)
            }
        }

        btn_reset.setOnClickListener {
            idLog.setText("")
            idMdp.setText("")
        }

    }

}