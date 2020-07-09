package components

import Component
import GameObject
import engine.math.FloatExt

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

        hp = FloatExt.fromJson(map["hp"])
        maxHp = FloatExt.fromJson(map["maxHp"])
    }
}
