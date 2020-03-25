package fr.isen.dufaza.androidtoolbox

package fr.isen.bechard.androidtoolbox.activities

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import fr.isen.bechard.androidtoolbox.R
import kotlinx.android.synthetic.main.activity_ble_scan.*

class BLEScanActivity : AppCompatActivity() {

    private lateinit var handler: Handler
    private var mScanning: Boolean = false

    private val bluetoothAdapter: BluetoothAdapter? by lazy(LazyThreadSafetyMode.NONE) {
        val bluetoothManager = getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        bluetoothManager.adapter
    }

    private val isBLEEnabled: Boolean
        get() = bluetoothAdapter?.isEnabled == true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ble_scan)


        BoutonLancerScanImageView.setOnClickListener{
            when {
                isBLEEnabled -> {
                    //init scan
                    initScan()
                }
                bluetoothAdapter != null -> {
                    //ask for permission
                    val enableBTIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                    startActivityForResult(enableBTIntent,REQUEST_ENABLE_BT)
                }
                else -> {
                    //device is not compatible with your device
                    bleTextFailed.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun initScan() {
        progressBar.visibility = View.VISIBLE
        bleDivider.visibility = View.GONE

        handler = Handler()

        scanLeDevice(true)
    }

    private fun scanLeDevice(enable: Boolean) {
        bluetoothAdapter?.bluetoothLeScanner?.apply {
            if(enable){
                handler.postDelayed({
                    mScanning = false
                    stopScan(leScanCallback)
                }, SCAN_PERIOD)
                mScanning = true
                startScan(leScanCallback)
            }else{
                mScanning = false
                stopScan(leScanCallback)
            }
        }
    }
    private val leScanCallback = object : ScanCallback() {
        override fun onScanResult(callbackType: Int, result: ScanResult) {
            super.onScanResult(callbackType, result)
            Log.w("BLEScanActivity", "${result?.device}")
            runOnUiThread {
                bleDivider.visibility = View.GONE
            }
        }
    }

    companion object {
        private const val SCAN_PERIOD: Long = 10000
        private const val REQUEST_ENABLE_BT = 44
    }
}
