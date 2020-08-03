package dodgyDeliveries3.components

import dodgyDeliveries3.Component
import dodgyDeliveries3.Scene
import dodgyDeliveries3.loadDefaultScene

class HealthComponent : Component() {
    var hp: Float = 0F
    var maxHp: Float = 0F
    
    fun Start(){
        hp = 10F
    }

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

    fun die(){
        //ToDo: Find better solution
        loadDefaultScene()
    }

    override fun update() {}

    override fun draw() {}
}
