package engine.util

interface IJsonUnserializable<T> {
    /**
     * @brief Konstruiert eine Instanz der Klasse [T] mit den JSON-Daten aus [json].
     *
     * [json] ist dabei ein JSON-Wert. Siehe Dokumentation von [IJsonSerializable] für mehr Infos.
     *
     * *Achtung*: Der Kotlin-Datentyp einer JSON-Zahl ist *immer* Double. Also zB: [Double.toFloat] oder
     * [engine.math.FloatExt.fromJson] benutzen, um eine JSON-Zahl in ein Float zu konvertieren.
     *
     * @return Eine Instanz der Klasse [T], konstruiert aus den JSON-Daten [json].
     */
    fun fromJson(json: Any?): T
}