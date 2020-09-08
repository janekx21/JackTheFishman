package dodgyDeliveries3.util

import jackTheFishman.engine.Input
import jackTheFishman.engine.Serialisation
import jackTheFishman.engine.util.ICreateViaPath
import java.io.File

data class Configuration(val volume: Float, val fullscreen: Boolean, val showGrid: Boolean) {
    companion object : ICreateViaPath<Configuration> {
        override fun createViaPath(path: String): Configuration {
            return Serialisation.klaxon.parse(File(path))!!
        }

        private val defaultPath: File

        init {
            val userHome = System.getProperty("user.home")
            val documents = File(userHome, "Documents")
            val dd3Config = File(documents, "dd3_config.json")
            defaultPath = dd3Config
        }

        fun loadFromDefaultPath(): Configuration = createViaPath(defaultPath.path)

        fun loadFromDefaultPathOrNull(): Configuration? {
            try {
                return loadFromDefaultPath()
            } catch (e: Exception) {}

            return null
        }
    }

    fun save(file: File) {
        file.writeText(Serialisation.klaxon.toJsonString(this))
    }

    fun saveToDefaultPath() = save(defaultPath)
}
