package dodgyDeliveries3

import dodgyDeliveries3.components.Collider
import jackTheFishman.engine.Physics
import jackTheFishman.engine.Serialisation
import jackTheFishman.engine.util.ICreateViaPath
import org.jbox2d.callbacks.ContactImpulse
import org.jbox2d.collision.Manifold
import org.jbox2d.dynamics.contacts.Contact
import java.io.File

private class SceneContactListener : org.jbox2d.callbacks.ContactListener {
    override fun endContact(contact: Contact?) {
        checkNotNull(contact)

        val colliderA = contact.fixtureA.userData
        check(colliderA is Collider)

        val colliderB = contact.fixtureB.userData
        check(colliderB is Collider)

        colliderA.gameObject.endContact(colliderA, colliderB, contact)
        colliderB.gameObject.endContact(colliderB, colliderA, contact)
    }

    override fun beginContact(contact: Contact?) {
        checkNotNull(contact)

        val colliderA = contact.fixtureA.userData
        check(colliderA is Collider)

        val colliderB = contact.fixtureB.userData
        check(colliderB is Collider)

        colliderA.gameObject.beginContact(colliderA, colliderB, contact)
        colliderB.gameObject.beginContact(colliderB, colliderA, contact)
    }

    override fun preSolve(contact: Contact?, oldManifold: Manifold?) {
        // do nothing
    }

    override fun postSolve(contact: Contact?, impulse: ContactImpulse?) {
        // do nothing
    }
}

data class Scene(val allGameObjects: ArrayList<GameObject> = arrayListOf()) {
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
        go.setOrigin(this)
        allGameObjects.add(go)
        go.start()
    }

    fun destroy(go: GameObject) {
        go.destroyAllComponents()
        go.stop()
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