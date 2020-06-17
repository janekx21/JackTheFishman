class Scene {
    private val allGameObjects = arrayListOf<GameObject>()

    fun spawn(go: GameObject) {
        allGameObjects.add(go)
    }

    fun destroy(go: GameObject) {
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

    companion object {
        val active = Scene()
    }
}