package dodgyDeliveries3

import jackTheFishman.engine.Physics
import jackTheFishman.engine.Serialisation
import jackTheFishman.engine.util.ICreateViaPath
import org.jbox2d.callbacks.ContactImpulse
import org.jbox2d.collision.Manifold
import org.jbox2d.dynamics.contacts.Contact
import java.io.File

data class Scene(val allGameObjects: ArrayList<GameObject> = arrayListOf()) {
    class ContactListener(private val scene: Scene) : org.jbox2d.callbacks.ContactListener {
        override fun endContact(contact: Contact?) {
            val userDataA = contact!!.fixtureA.userData
            val userDataB = contact.fixtureB.userData

            if (userDataA is Component && userDataB is Component) {
                userDataA.gameObject.endContact(userDataB.gameObject)
                userDataB.gameObject.endContact(userDataA.gameObject)
            }
        }

        override fun beginContact(contact: Contact?) {
            val userDataA = contact!!.fixtureA.userData
            val userDataB = contact.fixtureB.userData

            if (userDataA is Component && userDataB is Component) {
                userDataA.gameObject.beginContact(userDataB.gameObject)
                userDataB.gameObject.beginContact(userDataA.gameObject)
            }
        }

        override fun preSolve(contact: Contact?, oldManifold: Manifold?) {
            // do nothing
        }

        override fun postSolve(contact: Contact?, impulse: ContactImpulse?) {
            // do nothing
        }
    }

    init {
        for (go in allGameObjects) {
            go.setOrigin(this)
        }
        for (go in allGameObjects) {
            go.start()
        }
        Physics.world.setContactListener(ContactListener(this))
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
