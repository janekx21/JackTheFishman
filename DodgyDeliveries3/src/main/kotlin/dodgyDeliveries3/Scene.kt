package dodgyDeliveries3

import jackTheFishman.engine.Serialisation
import jackTheFishman.engine.util.ICreateViaPath
import java.io.File

data class Scene(val allGameObjects: ArrayList<GameObject> = arrayListOf()) {

    private val gameObjectsToSpawn: ArrayList<GameObject> = arrayListOf()
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
        gameObjectsToSpawn.add(go)
    }

    fun destroy(go: GameObject) {
        gameObjectsToDestroy.add(go)
    }

    fun update() {
        for (gameObject in allGameObjects) {
            gameObject.update()
        }
        applyGameObjectsToDestroy()
        applyGameObjectsToSpawn()
    }

    private fun applyGameObjectsToDestroy() {
        for(gameObjects in gameObjectsToDestroy) {
            gameObjects.destroyAllComponents()
            gameObjects.stop()
            allGameObjects.remove(gameObjects)
        }
        gameObjectsToDestroy.clear()
    }

    private fun applyGameObjectsToSpawn() {
        for(go in gameObjectsToSpawn) {
            go.setOrigin(this)
            allGameObjects.add(go)
            go.start()
        }
        gameObjectsToSpawn.clear()
    }

    fun draw() {
        for (gameObject in allGameObjects) {
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
