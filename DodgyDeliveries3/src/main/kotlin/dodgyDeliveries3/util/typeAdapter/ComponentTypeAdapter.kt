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
}
