package fr.isen.dufaza.androidtoolbox

import android.bluetooth.BluetoothAdapter
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_bledevice.*

class BLEDeviceActivity : AppCompatActivity() {

    var macAddress: String? = null
    lateinit var name: String
    var container : LinearLayout? = null
    var ivArrow : ImageView? = null

    private var mPrintService: BluetoothPrintService? = null

    private var mBluetoothAdapter: BluetoothAdapter? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bledevice)
        macAddress = getIntent().getStringExtra("id")
        name = getIntent().getStringExtra("name")
        Toast.makeText(this,macAddress,Toast.LENGTH_LONG).show()

        container = findViewById(R.id.con)
        ivArrow = findViewById(R.id.tvCount)

        ivArrow!!.setOnClickListener(View.OnClickListener {
            if(container!!.visibility == View.VISIBLE){
                container!!.visibility = View.GONE
                ivArrow!!.setImageResource(R.drawable.down_icon)
            }else{
                container!!.visibility = View.VISIBLE
                ivArrow!!.setImageResource(R.drawable.up_icon)
            }
        })

        tvName.text = name
        tvStatus.text = "Status : connecting";


        // Get local Bluetooth adapter
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter()

        // If the adapter is null, then Bluetooth is not supported

        // If the adapter is null, then Bluetooth is not supported
        if (mBluetoothAdapter == null) {
            Toast.makeText(this, R.string.toast_bt_na, Toast.LENGTH_LONG).show()
            finish()
        }


    }

    // printer methods...................................................................................
    override fun onStart() {
        super.onStart()

        if (mPrintService == null) setupPrint()

    }

    private fun setupPrint() {
        mPrintService = BluetoothPrintService(this)
        connectDevice();
    }

    @Synchronized
    override fun onResume() {
        super.onResume()
        if (mPrintService != null) {
            // Only if the state is STATE_NONE, do we know that we haven't started already
            if (mPrintService!!.state === BluetoothPrintService.STATE_NONE) {
                // Start the Bluetooth print services
                mPrintService!!.start()
            }
        }
    }

    override fun onDestroy() {

        // Stop the Bluetooth print services
        if (mPrintService != null) {
            mPrintService!!.stop()
          //  isbluetoothConnected = false
        }
        super.onDestroy()
    }



    private fun connectDevice() {


        val device = mBluetoothAdapter!!.getRemoteDevice(macAddress)
        // Attempt to connect to the device
        mPrintService!!.connect(device, true)
        tvUidd.text = "Uuid: "+ device.uuids[0].toString()
    }
}
