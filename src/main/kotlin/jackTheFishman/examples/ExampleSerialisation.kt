package jackTheFishman.examples

import com.beust.klaxon.Json
import com.beust.klaxon.TypeAdapter
import com.beust.klaxon.TypeFor
import jackTheFishman.engine.Serialisation
import kotlin.reflect.KClass

class ExampleTypeAdapter : TypeAdapter<Foo> {
    override fun classFor(type: Any): KClass<out Foo> = when (type as String) {
        Foo::class.simpleName -> Foo::class
        Bar::class.simpleName -> Bar::class
        else -> throw IllegalArgumentException("Unknown type: $type")
    }
}

@TypeFor(field = "type", adapter = ExampleTypeAdapter::class)
abstract class Foo(var fooVariable: String = "Foo's Variable") {
    val type: String = javaClass.simpleName

    private val a: Float = 6f

    @Json(ignored = true)
    val b: Int = 42
    override fun toString(): String {
        return "a: $a, b: $b, type: $type aka ${javaClass.name}"
    }
}

class Bar(val barVariable: String = "Bar's Variable") : Foo()

fun main() {
    val originalFoo: Foo = Bar()
    val json = Serialisation.klaxon.toJsonString(originalFoo).also { println(it) }
    val newFoo = Serialisation.klaxon.parse<Foo>(json)
    check(newFoo != null) { "Serialising failed" }
    println("$originalFoo -> $newFoo")
    check(newFoo is Bar) { "Type was not correctly serialised" }
    val newFooAsBar = newFoo
    println("BarVar: ${newFooAsBar.barVariable}")
}

