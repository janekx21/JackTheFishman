package dodgyDeliveries3.util.typeAdapter

import com.beust.klaxon.TypeAdapter
import dodgyDeliveries3.Component
import kotlin.reflect.KClass
import kotlin.reflect.full.isSuperclassOf

class ComponentTypeAdapter : TypeAdapter<Component> {
    override fun classFor(type: Any): KClass<out Component> {
        check(type is String) { "type must be a string" }
        val targetClass = Class.forName(type).kotlin
        check(Component::class.isSuperclassOf(targetClass))
        @Suppress("UNCHECKED_CAST")
        return targetClass as KClass<out Component>
    }
    /*
    override fun classFor(type: Any): KClass<out dodgyDeliveries3.Component> = when(type as String) {
        Transform::class.java.name -> Transform::class
        ModelRenderer::class.java.name -> ModelRenderer::class
        Camera::class.java.name -> Camera::class
        PointLight::class.java.name -> PointLight::class
        AudioListener::class.java.name -> AudioListener::class
        BoxCollider::class.java.name -> BoxCollider::class
        CircleCollider::class.java.name -> CircleCollider::class
        else -> throw IllegalArgumentException("Unknown type: $type")
    }
     */
}
