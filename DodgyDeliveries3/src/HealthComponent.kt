class HealthComponent(gameObject: GameObject) : Component(gameObject) {

    var hp: Float = 0f

    var maxHp: Float = 0f

    fun takeDamage(dmg: Float) {
        if (hp > 0) {
            hp -= dmg
            if (hp < 0) {
                hp = 0f
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
