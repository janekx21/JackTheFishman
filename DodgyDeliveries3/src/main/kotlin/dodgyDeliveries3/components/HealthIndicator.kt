package dodgyDeliveries3.components

import dodgyDeliveries3.Component
import jackTheFishman.engine.graphics.Texture2D
import kotlin.math.floor

class HealthIndicator(var images: Array<Texture2D> = emptyArray(), var healthComponent: Health? = null) : Component() {

    val image : Texture2D
    get() {
        return images[floor((images.size - 1) * healthComponent!!.percentage).toInt()]
    }

    override fun start() {
        //TODO: fill array with images
    }

    fun changeImage(){
        //TODO: iterate through image array
    }
        
}
