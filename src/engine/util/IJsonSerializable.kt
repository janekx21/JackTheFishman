package engine.util

interface IJsonSerializable {
    /**
     * @brief Gibt eine Serialisierung für dieses Objekt zurück.
     *
     * Der Rückgabewert muss ein JSON-Wert sein.
     *
     * JSON-Werte können folgendes sein:
     *  - ein String
     *  - eine Zahl (integer, double, float, etc)
     *  - ein Boolean
     *  - null
     *  - eine Liste/Array aus JSON-Werten
     *  - ein Map, die folgende Eigenschaften erfüllt:
     *     - alle Keys/Indizes sind Strings
     *     - alle Werte sind JSON-Werte
     *
     * Beispiele zulässiger toJson-Implementationen:
     * ```
     * fun toJson(): Any? = 1
     * ```
     * ```
     * fun toJson(): Any? {
     *   return mapOf(
     *     "hallo" to "welt",
     *     "test" to 1,
     *     "irgendnen null wert" to null
     *   )
     * }
     * ```
     * ```
     * val myColor: Vector3fc
     *
     * fun toJson(): Any? {
     *   return mapOf(
     *     "farbe" to myColor.toJson()
     *   )
     * }
     * ```
     *
     * Beispiele *nicht*-zulässiger toJson-Implementationen:
     * ```
     * val myColor: Vector3fc
     *
     * fun toJson(): Any? {
     *   return mapOf(
     *     "farbe" to myColor   // myColor ist kein JSON-Wert, sondern eine Instanz der Klasse Vector3fc.
     *   )
     * }
     * ```
     *
     * @return ein JSON-Wert, der eine Serialisierung dieses Objektes ist.
     */
    fun toJson(): Any?
}