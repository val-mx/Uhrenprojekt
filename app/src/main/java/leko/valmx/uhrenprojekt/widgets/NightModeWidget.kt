package leko.valmx.uhrenprojekt.widgets

import android.widget.Toast
import com.maxkeppeler.sheets.time.TimeFormat
import com.maxkeppeler.sheets.time.TimeSheet
import leko.valmx.uhrenprojekt.bluetooth.Blue
import leko.valmx.uhrenprojekt.parents.Widget
import leko.valmx.uhrenprojekt.popup.InputBottomSheet
import leko.valmx.uhrenprojekt.popup.InvalidInputInterface

class NightModeWidget : Widget(), InvalidInputInterface {
    override fun init() {
        title("Nachtmodus")

        redirect("Beginn", "Setze den Beginn des Nachtmodus") {
            InputBottomSheet(
                "Nachtmodus - Beginn",
                "Wann soll der Nachtmodus einsetzen?",
                "hh:mm",
                "seton",
                this
            ).show(this.context)
        }

        redirect("Ende", "Setze das Ende des Nachtmodus") {
            TimeSheet().show(context) {

                format(TimeFormat.HH_MM)

                title("Start Um:")



                onPositive {

                    val hh = it / (1000 * 60)
                    val mm = (it % (1000 * 60)) / (1000)
                    Blue.sendCommand("setoff $hh:$mm")

                }

            }
        }

        redirect("Helligkeit im Nachtmodus", "Setze die Helligkeit der Uhr im Nachtmodus") {
            InputBottomSheet(
                "Nachtmodus - Helligkeit", "Wie hoch soll die Helligkeit des Nachtmodus werden" +
                        "? (0 - 255)", "hh:mm", "setnb", this
            ).show(this.context)
        }

    }

    override fun onInvalidInput() {
        Toast.makeText(this.context, "Fehlerhafte Eingabe", Toast.LENGTH_SHORT).show()
    }

}