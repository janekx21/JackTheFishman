package dodgyDeliveries3

import dodgyDeliveries3.components.Collider
import jackTheFishman.engine.Physics
import jackTheFishman.engine.Serialisation
import jackTheFishman.engine.util.ICreateViaPath
import org.jbox2d.callbacks.ContactImpulse
import org.jbox2d.collision.Manifold
import org.jbox2d.dynamics.Fixture
import org.jbox2d.dynamics.contacts.Contact
import java.io.File

private class SceneContactListener : org.jbox2d.callbacks.ContactListener {
    override fun endContact(contact: Contact?) {
        checkNotNull(contact)

        val colliderA = getCollider(contact.fixtureA)
        val colliderB = getCollider(contact.fixtureB)

        colliderA.gameObject.endContact(colliderA, colliderB, contact)
        colliderB.gameObject.endContact(colliderB, colliderA, contact)
    }

    override fun beginContact(contact: Contact?) {
        checkNotNull(contact)

        val colliderA = getCollider(contact.fixtureA)
        val colliderB = getCollider(contact.fixtureB)

        colliderA.gameObject.beginContact(colliderA, colliderB, contact)
        colliderB.gameObject.beginContact(colliderB, colliderA, contact)
    }

    override fun preSolve(contact: Contact?, oldManifold: Manifold?) {
        // do nothing
    }

    override fun postSolve(contact: Contact?, impulse: ContactImpulse?) {
        // do nothing
    }

    fun getCollider(fixture: Fixture): Collider =
        fixture.userData
            .also { check(it is Collider) }
            .let { it as Collider }
}

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
        Physics.world.setContactListener(SceneContactListener())
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
        for (gameObjects in gameObjectsToDestroy) {
            gameObjects.stop()
            gameObjects.destroyAllComponents()
            allGameObjects.remove(gameObjects)
        }
        gameObjectsToDestroy.clear()
    }

    private fun applyGameObjectsToSpawn() {
        for (go in gameObjectsToSpawn) {
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

    fun findViaName(name: String): GameObject? {
        val gameObject = allGameObjects.find { it.name == name }
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
            checkNotNull(scene) { "Scene could not be loaded at $path" }
            return scene
        }
    }
}
