package dodgyDeliveries3

import jackTheFishman.engine.Serialisation
import jackTheFishman.engine.util.ICreateViaPath
import java.io.File

data class Scene(val allGameObjects: ArrayList<GameObject> = arrayListOf()) {

    private val gameObjectsToDestroy: ArrayList<GameObject> = arrayListOf()

    init {
        for (go in allGameObjects) {
            go.setOrigin(this)
        }
        for (go in allGameObjects) {
            go.start()
        }
    }

    fun spawn(go: GameObject) {
        go.setOrigin(this)
        allGameObjects.add(go)
        go.start()
    }

    fun destroy(go: GameObject) {
        gameObjectsToDestroy.add(go)
    }

    private fun clearGameObjectsToDestroy() {
        for(gameObjects in ArrayList(gameObjectsToDestroy)) {
            gameObjects.destroyAllComponents()
            gameObjects.stop()
            allGameObjects.remove(gameObjects)
        }
        gameObjectsToDestroy.clear()
    }

    fun update() {
        for (gameObject in ArrayList(allGameObjects)) {
            gameObject.update()
        }
        clearGameObjectsToDestroy()
    }

    fun draw() {
        for (gameObject in ArrayList(allGameObjects)) {
            gameObject.draw()
        }
    }

    fun findViaName(name: String): GameObject {
        val gameObject = allGameObjects.find { it.name == name }
        check(gameObject != null) { "dodgyDeliveries3.GameObject not found" }
        return gameObject
    }

    fun find(predicate: (GameObject) -> Boolean): GameObject? {
        return allGameObjects.find(predicate)
    }

    companion object : ICreateViaPath<Scene> {
        var active = Scene()
        override fun createViaPath(path: String): Scene {
            val json = File(path).readText()
            val scene = Serialisation.klaxon.parse<Scene>(json)
            check(scene != null) {"Scene could not pe loaded at $path"}
            return scene
        }
    }
}
