package jackTheFishman.serialisation.typeAdapter

import com.beust.klaxon.TypeAdapter
import jackTheFishman.graphics.Mesh
import jackTheFishman.graphics.MeshViaPath
import kotlin.reflect.KClass

class MeshTypeAdapter : TypeAdapter<Mesh> {
    override fun classFor(type: Any): KClass<out Mesh> = when (type as String) {
        Mesh::class.simpleName -> Mesh::class
        MeshViaPath::class.simpleName -> MeshViaPath::class
        else -> throw IllegalArgumentException("Unknown type: $type")
    }
}
