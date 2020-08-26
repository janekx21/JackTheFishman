package dodgyDeliveries3.components

import dodgyDeliveries3.Component
import kotlin.math.max
import kotlin.math.min

class Health(var maxHp: Float = 3f, var hp: Float = maxHp) : Component() {

    val alive: Boolean
        get() = hp > 0f

    fun applyDamage(amount: Float) {
        if (alive) {
            hp = max(hp - amount, 0f)
        }
    }

    fun applyHeal(amount: Float) {
        if (alive) {
            hp = min(hp + amount, maxHp)
        }
    }
}
