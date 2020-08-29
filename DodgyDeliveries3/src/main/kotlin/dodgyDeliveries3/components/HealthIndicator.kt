package dodgyDeliveries3.components

import dodgyDeliveries3.Component
import jackTheFishman.engine.Loader
import jackTheFishman.engine.graphics.Texture2D
import org.joml.Vector2f
import kotlin.math.floor

class HealthIndicator(var images: Array<Texture2D> = emptyArray(), var healthComponent: Health? = null) : Component() {

    val image : Texture2D
    get() {
        return images[floor((images.size - 1) * healthComponent!!.percentage).toInt()]
    }


    override fun start() {
        for(i in 0..images.size){
            images[i] = Loader.createViaPath<Texture2D>("textures/healthIndication/pizza" + i.toString() + ".png")
        }
        displayImage()
    }

    override fun update() {
        //changeImage()
    }

    fun displayImage(){
        gameObject.addComponent<ImageComponent>().also {
            it.texture = image
            it.scaledPosition = Vector2f(100f, 100f)
            it.logicalSize = Vector2f(100f, 100f)
        }
    }

    fun changeImage(){
        gameObject.getComponent<ImageComponent>().texture = image;
    }
}
