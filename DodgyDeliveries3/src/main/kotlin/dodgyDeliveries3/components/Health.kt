package dodgyDeliveries3.components

import dodgyDeliveries3.Component
import kotlin.math.max
import kotlin.math.min

class Health(var maxHp: Float = 6f, var hp: Float = maxHp) : Component() {
    val alive: Boolean
        get() = hp > 0f

    val percentage: Float
        get() = hp / maxHp

    fun applyDamage(amount: Float) {
        hp = max(hp - amount, 0f)
        gameObject.getComponent<Audio>().play()
    }

    fun applyHeal(amount: Float) {
        if (alive) {
            hp = min(hp + amount, maxHp)
        }
        /*if (!alive) {
            Input.Mouse.setMode(Input.Mouse.CursorMode.NORMAL)
            Scene.active.spawn(loadLooseScreen())
            Time.timeScale = 0f
        }*/
    }
}
