package dodgyDeliveries3.components

class HpText : Text() {
    override fun update() {
        this.text = "${gameObject.getComponent<Health>().hp} HP"
    }
}
