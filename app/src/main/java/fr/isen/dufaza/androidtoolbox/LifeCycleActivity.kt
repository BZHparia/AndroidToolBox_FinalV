package fr.isen.dufaza.androidtoolbox

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_life_cycle.*

class LifeCycleActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_life_cycle)
        Log.d("TAG", "Creating")

        val newFragment = CycleFragment1()
        val cycleFragment2 = CycleFragment2()

        val transaction = supportFragmentManager.beginTransaction()
        transaction.add(R.id.idLayoutActivityLifeCycle, newFragment)
        transaction.commit()

        buttonFragment.setOnClickListener {
            if (newFragment.isResumed) {
                Log.d( "TAG", "Frag1 is resumed")
                supportFragmentManager.beginTransaction().replace(R.id.idLayoutActivityLifeCycle, cycleFragment2).commit()
            }
            else if (cycleFragment2.isResumed){
                Log.d( "TAG", "Frag2 is resumed")
                supportFragmentManager.beginTransaction().replace(R.id.idLayoutActivityLifeCycle, newFragment).commit()
            }
        }
    }

    private fun notification(message: String, isActive: Boolean) {
        if (isActive){
            idTitleLifeCycle.text = message
        }
        else {
            Log.d("tag",message)
        }
    }

    override fun onStart() {
        super.onStart()
        notification("onStart", true)
    }

    override fun onResume() {
        super.onResume()
        notification("onResume", true)
    }

    override fun onPause() {
        super.onPause()
        notification("onPause", false)
    }

    override fun onStop() {
        super.onStop()
        notification("onStop", false)
    }

    override fun onDestroy() {
        super.onDestroy()
        notification("onDestroy", false)
    }
}