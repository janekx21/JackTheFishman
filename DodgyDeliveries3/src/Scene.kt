import com.beust.klaxon.JsonArray
import com.beust.klaxon.JsonObject
import engine.Json
import engine.util.ICreateViaPath
import engine.util.IJsonUnserializable
import java.io.File
import kotlin.reflect.full.primaryConstructor

class Scene {
    private val allGameObjects = arrayListOf<GameObject>()

    fun spawn(go: GameObject) {
        go.onEnable()
        allGameObjects.add(go)
    }

    inline fun <reified T : GameObject> spawn(name: String): T {
        val gameObject = T::class.primaryConstructor!!.call(name)
        spawn(gameObject)
        return gameObject
    }

    fun destroy(go: GameObject) {
        go.destroyAllComponents()
        go.onDisable()
        allGameObjects.remove(go)
    }

    fun update() {
        for (gameObject in ArrayList(allGameObjects)) {
            gameObject.update()
        }
    }

    fun draw() {
        for (gameObject in ArrayList(allGameObjects)) {
            gameObject.draw()
        }
    }

    fun findViaName(name: String): GameObject {
        val gameObject = allGameObjects.find { it.name == name }
        check(gameObject != null) { "GameObject not found" }
        return gameObject
    }

    fun find(predicate: (GameObject) -> Boolean): GameObject? {
        return allGameObjects.find(predicate)
    }

    companion object : ICreateViaPath<Scene>, IJsonUnserializable<Scene> {
        val active = Scene()

        override fun createViaPath(path: String): Scene {
            val text = File(path).readText()
            return fromJson(Json.decode(text))
        }

        override fun fromJson(json: Any?): Scene {
            val map = json as Map<*, *>
            val array = map["gameObjects"] as List<*>

            return Scene().also { scene ->
                array.forEach {
                    scene.spawn(GameObject.fromJson(it))
                }
            }
        }
    }

    fun toJson(): Any? {
        return mapOf(
            "gameObjects" to allGameObjects.map {
                it.toJson()
            }
        )
    }
}