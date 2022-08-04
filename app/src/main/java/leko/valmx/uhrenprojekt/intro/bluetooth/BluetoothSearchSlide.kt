package leko.valmx.uhrenprojekt.intro.bluetooth

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.github.appintro.SlidePolicy
import kotlinx.android.synthetic.main.fragment_bluetooth.*
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import leko.valmx.uhrenprojekt.R
import leko.valmx.uhrenprojekt.bluetooth.Blue
import quevedo.soares.leandro.blemadeeasy.models.BLEDevice
import java.util.*

@DelicateCoroutinesApi
class BluetoothSearchSlide(override var isPolicyRespected: Boolean = true) : Fragment(),
    SlidePolicy {

    override fun onUserIllegallyRequestedNextPage() {}

    private var devices = LinkedList<BLEDevice>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_bluetooth, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        scanForDevices() // Erster Scan wird initiiert

        pick_card.setOnClickListener {
            BlueToothPickSheet(devices).show(parentFragmentManager, "")
        }

        initViews()

    }

    fun initViews() {
        connect.setOnClickListener {
            GlobalScope.launch {
                Log.i("Connecting","Started Connecting to $selectedDevice")
                val connection = Blue.ble.connect(selectedDevice!!)?.let {
                    it.write("00000000-0000-0000-0000-000000000000", "Testing")
                    Log.i("Connecting","$it")
                }
                if (connection != null) {
                    connect.post {
                        card_found_device.visibility = GONE
                        pick_card.visibility = GONE
                        card_success.visibility = VISIBLE
                    }
                }

            }

        }

        btn_search.setOnClickListener {
            scanForDevices()
            feedbackView.startFeedBack(searchDuration)
        }
    }


    /**
    Sucht nach neuen Geräten & published diese im Recyclerview
     */

    private val possibleClockNames =
        arrayListOf("HMSoft", "MLT-BT05", "BT05", "[TV] Samsung 5 Series (40)","[TV] Samsung Q60 Series (43)")

    private val searchDuration = 30000

    private fun scanForDevices() {
        devices.clear()
        Blue.ble.scanAsync(
            duration = searchDuration.toLong(),
            onDiscover = { device ->
                Log.i(tag, "Found ${device.name} + ${device.device.bluetoothClass}")

//                if (device.name == "") return@scanAsync

                if (possibleClockNames.contains(device.name)) selectDevice(device)
                devices.add(device)

            },
            onFinish = { _ -> },
            onError = { _ -> }
        )
    }

    var selectedDevice: BLEDevice? = null

    fun selectDevice(device: BLEDevice) {
        selectedDevice = device
        device_name.text = "${device.name} gefunden"

        if (card_found_device.visibility == GONE) {
            card_found_device.alpha = 0F
            card_found_device.visibility = VISIBLE
            card_found_device.animate().apply {
                alpha(1F)
            }
        }

    }


}