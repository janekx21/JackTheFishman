package dodgyDeliveries3.components

import dodgyDeliveries3.Component

class Health : Component() {
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
}
