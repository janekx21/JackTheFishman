package dodgyDeliveries3

data class Scene(val allGameObjects: ArrayList<GameObject> = arrayListOf()) {
    init {
        for (go in allGameObjects) {
            go.setOrigin(this)
        }
        for (go in allGameObjects) {
            go.onEnable()
        }
    }

    fun spawn(go: GameObject) {
        go.setOrigin(this)
        allGameObjects.add(go)
        go.onEnable()
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
        check(gameObject != null) { "dodgyDeliveries3.GameObject not found" }
        return gameObject
    }

    fun find(predicate: (GameObject) -> Boolean): GameObject? {
        return allGameObjects.find(predicate)
    }

    companion object {
        var active = Scene()
    }
}