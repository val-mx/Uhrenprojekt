package leko.valmx.uhrenprojekt.newP.autoconnect

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.maxkeppeler.sheets.core.Sheet
import kotlinx.android.synthetic.main.sheet_bluetooth_autoconnect.*
import kotlinx.android.synthetic.main.widget_subitem_sheet_multiple_choice.*
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import leko.valmx.uhrenprojekt.R
import leko.valmx.uhrenprojekt.bluetooth.Blue
import leko.valmx.uhrenprojekt.intro.IntroActivity
import leko.valmx.uhrenprojekt.newP.adapters.MultipleChoicePopUpAdapter
import leko.valmx.uhrenprojekt.newP.bundles.misc.MultipleChoiceSheet
import leko.valmx.uhrenprojekt.newP.utils.WidgetHelper

class ConnectBottomSheet : Sheet() {


    override fun onCreateLayoutView(): View {
        return LayoutInflater.from(activity).inflate(R.layout.sheet_bluetooth_autoconnect, null)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (Blue.debug) {
            dismiss()
            return
        }

        if (Blue.connection != null && Blue.connection!!.isActive) dismiss()


        feedBack.startFeedBack(100000)
        startSearch()


    }

    fun show(
        ctx: Context,
        width: Int? = null,
        func: ConnectBottomSheet.() -> Unit = {}
    ): ConnectBottomSheet {
        this.windowContext = ctx
        this.width = width
        UhrAppActivity.isSheetDisplayed = true
        positiveListener = {
            dismiss()
            ConnectBottomSheet().show(ctx, width, func)
        }
        positiveText = "Suche Wiederholen"

        onNegative("Verbinde mit anderem Gerät") {



            ctx.getSharedPreferences(WidgetHelper.PREF_ID,MODE_PRIVATE).edit().putString(Blue.NAME_ID,"").apply()

            startActivity(Intent(ctx, IntroActivity::class.java))


        }

//        displayNegativeButton(false)


        title("Autoverbindung")


        func(this)


        if (!Blue.debug)
            isCancelable = false
        this.show()
        return this
    }

    @OptIn(DelicateCoroutinesApi::class)
    fun startSearch() {
        val ble = Blue.ble
        try {

            GlobalScope.launch {

                val address = Blue.getDeviceName(requireContext())

                text_status.text = "Verbinde mit $address"

                Blue.connection = ble.scanFor(macAddress = address)

                val connection = Blue.connection

                if (!connection!!.isActive) {
                    text_status.text = "Suche fehlgeschlagen"
                    return@launch
                }
                if(text_status != null) text_status.text = "Gerät Gefunden - Verbunden ${connection.isActive}"

                Blue.connection!!.onDisconnect = {
                    ConnectBottomSheet().show(requireContext()) {}
                }
                UhrAppActivity.isSheetDisplayed = false

                dismiss()

            }
        } catch (e: Exception) {
        }


    }


}