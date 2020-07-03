class HealthComponent(gameObject: GameObject) : Component(gameObject) {

    var hp: Float
        get() = hp
        set(value) {hp = value}

    var maxHp: Float
        get() = maxHp
        set(value) {maxHp = value}

    public fun takeDamage(dmg: Float){
        if (hp > 0){
            hp -= dmg
            if(hp < 0){
                hp = 0F
            }
        }
    }

    public fun heal(heal: Float){
        if(hp < maxHp){
            hp += heal
            if(hp > maxHp){
                hp = maxHp
            }
        }
    }

    override fun update() {}

    override fun draw() {}
}