package leko.valmx.uhrenprojekt.parents

import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import leko.valmx.uhrenprojekt.bluetooth.autoconnect.ConnectBottomSheet
import leko.valmx.uhrenprojekt.bluetooth.Blue
import quevedo.soares.leandro.blemadeeasy.BLE
import quevedo.soares.leandro.blemadeeasy.BluetoothConnection
import java.util.concurrent.LinkedBlockingQueue

@Suppress("DEPRECATION")
open class UhrAppActivity : AppCompatActivity(), Runnable {

    companion object : Runnable {
        var connection: BluetoothConnection? = null

        var executionThreadRunning = false
        var currentlyExecuted: Command? = null

        var onCmd: OnCommandListener? = null
        var scheduledCommands = LinkedBlockingQueue<Command>()


        fun send(cmd: Command) {

            scheduledCommands.add(cmd)

            if (executionThreadRunning) return

            executionThreadRunning = true
            onCmd?.onExecutionStart()
            run()
        }

        override fun run() {

            onCmd?.onCommandSizeUpdated(scheduledCommands.size)

            currentlyExecuted = scheduledCommands.poll()

            if (currentlyExecuted == null) {
                executionThreadRunning = false
                onCmd?.onExecutionFinish()
                return
            }

            connection?.write(
                "0000FFE1-0000-1000-8000-00805F9B34FB",
                currentlyExecuted?.cmd ?: "euro"
            )
            if (onCmd != null)
                Handler().postDelayed(this, 10000)
        }

    }

    class Command(var cmd: String, var desc: String = "No Description.")

    interface OnCommandListener {

        fun onCommandSizeUpdated(newSize: Int)

        fun onExecutionFinish()

        fun onExecutionStart()

        fun onDisconnect()

    }

    lateinit var ble: BLE

    override fun onStart() {
        initBLE()
        super.onStart()
        android.os.Handler().postDelayed(this, 20_000)

        if (this is OnCommandListener) onCmd = this

    }

    override fun onPostResume() {
        super.onPostResume()

    }

    private fun initBLE() {
        ble = BLE(this).apply {

            if (connection == null || !connection!!.isActive)

                ConnectBottomSheet(this@UhrAppActivity).show(this@UhrAppActivity, 100)

        }
    }

    override fun run() {
        if (Blue.connection != null && !Blue.connection!!.isActive) {
            ConnectBottomSheet(this).show(this, null)
        }

        android.os.Handler().postDelayed(this, 20000)

    }

    override fun onPause() {
        super.onPause()
        onCmd = null
    }

}