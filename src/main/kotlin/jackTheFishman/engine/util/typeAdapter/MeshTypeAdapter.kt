package jackTheFishman.engine.util.typeAdapter

import com.beust.klaxon.TypeAdapter
import jackTheFishman.engine.graphics.Mesh
import jackTheFishman.engine.graphics.MeshViaPath
import kotlin.reflect.KClass

class MeshTypeAdapter : TypeAdapter<Mesh> {
    override fun classFor(type: Any): KClass<out Mesh> = when (type as String) {
        Mesh::class.simpleName -> Mesh::class
        MeshViaPath::class.simpleName -> MeshViaPath::class
        else -> throw IllegalArgumentException("Unknown type: $type")
    }
}