package leko.valmx.uhrenprojekt.widgets

import leko.valmx.uhrenprojekt.parents.Widget

class LottoWidget() : Widget() {

    override fun init() {
        title("Lotto")
        description("Die Lottozahlen der letzten Ziehung")
        command(
            "EuroJackPot",
            "euro",
            "Keine Garantie für die Aktualität der Daten"
        )
        command(
            "Lotto (6 aus 49)",
            "lotto",
            description = "Keine Garantie für die Aktualität der Daten"
        )



    }
}