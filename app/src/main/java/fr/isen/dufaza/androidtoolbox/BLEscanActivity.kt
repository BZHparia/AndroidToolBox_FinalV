package fr.isen.dufaza.androidtoolbox

import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothClass
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.opengl.Visibility
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import android.widget.AdapterView.GONE
import android.widget.AdapterView.OnItemClickListener
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager.*
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_ble_scan.*
import layout.Device
import javax.security.auth.callback.Callback


class BLEscanActivity : AppCompatActivity() {

    // Debugging
    private val TAG = "DeviceListActivity"
    private val D = true

    // Return Intent extra
    var EXTRA_DEVICE_ADDRESS = "device_address"
    var mBluetoothAdapter: BluetoothAdapter? = null
    // Member fields
     var mBtAdapter: BluetoothAdapter? = null
     var mNewDevicesArrayAdapter: ArrayAdapter<String>? = null
     val REQUEST_ENABLE_BT = 3

      var devicesList : ArrayList<Device>? = null
     var adapter : DeviceAdapter? = null
     var listView : RecyclerView? = null

     var context: Context?=null;

     var pbView : View? = null
     var progressBar : ProgressBar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ble_scan)

        context = this;
        // Get local Bluetooth adapter
        devicesList = ArrayList()
        pbView = findViewById(R.id.pbView)
        progressBar = findViewById(R.id.pbProcessing)
        adapter = DeviceAdapter(this, devicesList!!);
        listView = findViewById(R.id.listBLEdevices)
        listView!!.layoutManager =LinearLayoutManager(this, LinearLayoutManager.VERTICAL ,false)
        listView!!.adapter = adapter


        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter()

        if (mBluetoothAdapter == null) {
            Toast.makeText(this, R.string.toast_bt_na, Toast.LENGTH_LONG).show()

        }else if (!mBluetoothAdapter!!.isEnabled) {
            val enableIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT)

        }

        // Set result CANCELED in case the user backs out
        setResult(Activity.RESULT_CANCELED)
        // Initialize the button to perform device discovery
        // Initialize the button to perform device discovery

         btn_scan.setOnClickListener { v ->
            devicesList!!.clear()
             pbView!!.visibility = View.GONE
             btn_scan.setImageResource(R.drawable.pause)
            doDiscovery()
           // v.visibility = View.GONE
        }



        // Register for broadcasts when a device is discovered
        var filter = IntentFilter(BluetoothDevice.ACTION_FOUND)
        this.registerReceiver(mReceiver, filter)

        // Register for broadcasts when discovery has finished

        // Register for broadcasts when discovery has finished
        filter = IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)
        this.registerReceiver(mReceiver, filter)

        // Get the local Bluetooth adapter

        // Get the local Bluetooth adapter
        mBtAdapter = BluetoothAdapter.getDefaultAdapter()

        // Get a set of currently paired devices

        // Get a set of currently paired devices
        val pairedDevices = mBtAdapter!!.getBondedDevices()

        // If there are paired devices, add each one to the ArrayAdapter

        // If there are paired devices, add each one to the ArrayAdapter
        if (pairedDevices.size > 0) {
            devicesList!!.clear();
            for (device in pairedDevices) {


                var obj = Device();
                obj.deviceId = device.address
                obj.deviceName = device.name

                devicesList!!.add( obj)
            }
            adapter!!.notifyDataSetChanged()

        } else {
            val noDevices = "No devices have been paired"
            Toast.makeText(this,noDevices,Toast.LENGTH_LONG).show()
        }

    }

    override fun onDestroy() {
        super.onDestroy()

        // Make sure we're not doing discovery anymore
        if (mBtAdapter != null) {
            mBtAdapter!!.cancelDiscovery()
        }
        // Unregister broadcast listeners
        unregisterReceiver(mReceiver)
    }

    /**
     * Start device discover with the BluetoothAdapter
     */
    private fun doDiscovery() {
        if (D) // Log.d(TAG, "doDiscovery()");
        // Indicate scanning in the title



        // Turn on sub-title for new devices
        title_new_devices.setVisibility(View.VISIBLE)

        // If we're already discovering, stop it
        if (mBtAdapter!!.isDiscovering) {
            mBtAdapter!!.cancelDiscovery()
        }

        // Request discover from BluetoothAdapter
        mBtAdapter!!.startDiscovery()
    }

    // The on-click listener for all devices in the ListViews
    private val mDeviceClickListener =
        OnItemClickListener { av, v, arg2, arg3 -> // Cancel discovery because it's costly and we're about to connect
            mBtAdapter!!.cancelDiscovery()

            // Get the device MAC address, which is the last 17 chars in the View
            val info = (v as TextView).text.toString()
            val address = info.substring(info.length - 17)

            // Create the result Intent and include the MAC address
            val intent = Intent()
            intent.putExtra(EXTRA_DEVICE_ADDRESS, address)

            // Set result and finish this Activity
            setResult(Activity.RESULT_OK, intent)
            finish()
        }

    // The BroadcastReceiver that listens for discovered devices and
    // changes the title when discovery is finished
    private val mReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent) {
            val action = intent.action

            // When discovery finds a device
            if (BluetoothDevice.ACTION_FOUND == action) {
                // Get the BluetoothDevice object from the Intent
                val device = intent.getParcelableExtra<BluetoothDevice>(BluetoothDevice.EXTRA_DEVICE)
                // If it's already paired, skip it, because it's been listed already
                if (device.bondState != BluetoothDevice.BOND_BONDED) {


                    Log.d("sun",device.name+"'\n"+device.name.toString()
                            +"'\n"+device.bluetoothClass.toString()

                            +"'\n"+device.address.toString())

                    var obj = Device();
                    obj.deviceId = device.address
                    obj.deviceName = device.name
                    devicesList!!.add(obj)
                    adapter!!.notifyDataSetChanged()
                    pbView!!.visibility = View.VISIBLE
                    btn_scan.setImageResource(R.drawable.play_icon)


                }


                // When discovery is finished, change the Activity title
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED == action) {


            }
        }
    }




    class  DeviceAdapter(var context : Context ,var devList : ArrayList<Device> ) : RecyclerView.Adapter<DeviceAdapter.MyViewHolder>() {





        class MyViewHolder : RecyclerView.ViewHolder {

            var tvName :TextView? = null
            var tvId :TextView? = null
            var tvCount :TextView? = null

            constructor(itemView: View) : super(itemView){
                tvId = itemView.findViewById(R.id.tvId)
                tvName = itemView.findViewById(R.id.tvName)
                tvCount = itemView.findViewById(R.id.tvCount)
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

            val view  = LayoutInflater.from(context).inflate(R.layout.layout_device_item,parent,false)

            return MyViewHolder(view)
        }

        override fun getItemCount(): Int {

            return  devList.size
        }

        override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

            holder.tvName!!.text = devList.get(position).deviceName
            holder.tvId!!.text = devList.get(position).deviceId
            holder.tvCount!!.text = (position+1).toString()

            holder.itemView.setOnClickListener(View.OnClickListener {


                var address = holder.tvId!!.text

                context.startActivity(Intent(context,BLEDeviceActivity::class.java).putExtra("id",address).putExtra("name",
                devList!!.get(position).deviceName))

            })
        }


    }


}