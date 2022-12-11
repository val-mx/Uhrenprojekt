package leko.valmx.uhrenprojekt.bluetooth

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.view.View
import leko.valmx.uhrenprojekt.etc.developertools.SendingSuccessInterface
import leko.valmx.uhrenprojekt.widgets.WidgetHelper
import quevedo.soares.leandro.blemadeeasy.BLE
import quevedo.soares.leandro.blemadeeasy.BluetoothConnection

object Blue: SendingSuccessInterface{
    lateinit var ble: BLE
    var connection : BluetoothConnection? = null


    var successInterface: SendingSuccessInterface = this as SendingSuccessInterface

    val debug = false
    var success: Int = 0

    @Volatile var isConnected = false

    fun sendCommand(command: String, view: View? = null) {

        val write = connection?.write("0000FFE1-0000-1000-8000-00805F9B34FB", command)!!



    }

    val NAME_ID = "DEVICE_ID"

    fun getDeviceName(ctx: Context): String? =
        ctx.getSharedPreferences(WidgetHelper.PREF_ID, MODE_PRIVATE).getString(
            NAME_ID, ""
        )

    fun initRelyInterface(replyInterface: SendingSuccessInterface){
        successInterface = replyInterface
    }

    override fun callReply(success: Int) {
    }

}