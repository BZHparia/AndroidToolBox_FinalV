package fr.isen.dufaza.androidtoolbox

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_life_cycle.*

class LifeCycleActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_life_cycle)

        kill_btn.setOnClickListener {
            onDestroy()
        }
    }


    override fun onStart() {
        super.onStart()
        returnlog.append("onStart()\n")
    }

    override fun onResume() {
        super.onResume()
        returnlog.append("onResume()\n")
    }

    override fun onPause() {
        super.onPause()
        returnlog.append("onPause()\n")
    }

    override fun onStop() {
        super.onStop()
        returnlog.append("onStop()\n")
    }

    override fun onDestroy() {
        super.onDestroy()
        returnlog.append("onDestroy()\n")
    }
}