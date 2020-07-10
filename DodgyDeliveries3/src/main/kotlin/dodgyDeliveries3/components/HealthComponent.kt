package dodgyDeliveries3.components

import dodgyDeliveries3.Component
import dodgyDeliveries3.GameObject

class HealthComponent(gameObject: GameObject) : Component(gameObject) {
    var hp: Float = 0F
    var maxHp: Float = 0F

    fun takeDamage(dmg: Float) {
        if (hp > 0) {
            hp -= dmg
            if (hp < 0) {
                hp = 0F
            }
        }
    }

    fun heal(heal: Float) {
        if (hp < maxHp) {
            hp += heal
            if (hp > maxHp) {
                hp = maxHp
            }
        }
    }

    override fun update() {}

    override fun draw() {}

    override fun toJson(): Any? {
        return mapOf(
            "hp" to hp,
            "maxHp" to maxHp
        )
    }

    override fun fromJson(json: Any?) {
        val map = json as Map<*, *>

        hp = (map["hp"] as Double).toFloat()
        maxHp = (map["maxHp"] as Double).toFloat()
    }
}
