object Player : GameObject("player") {
    init {
        addComponent<Transform>()
        addComponent<PlayerController>()
    }
}
