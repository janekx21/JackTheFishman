package jackTheFishman.serialisation

import jackTheFishman.BaseTest
import org.junit.Test
import kotlin.test.assertEquals

internal class JsonSerialisationTest : BaseTest() {

    data class Foo(val int: Int, val string: String, val bool: Boolean, val array: ArrayList<Int>)

    data class Bar(val foo: Foo, val other: Float)

    @Test
    fun `can serialize`() {
        val bar = Bar(Foo(32, "foo", false, arrayListOf(1, 2, 3)), 12.5f)

        val json = JsonSerialisation.json(bar)

        val expectedJson = """{"foo" : {"array" : [1, 2, 3], "bool" : false, "int" : 32, "string" : "foo"}, "other" : 12.5}"""
        assertEquals(expectedJson, json)
    }

    @Test
    fun `can deserialize`() {
        val json = """{"foo" : {"array" : [1, 2, 3], "bool" : false, "int" : 32, "string" : "foo"}, "other" : 12.5}"""

        val obj = JsonSerialisation.parse<Bar>(json)

        val expectedBar = Bar(Foo(32, "foo", false, arrayListOf(1, 2, 3)), 12.5f)
        assertEquals(expectedBar, obj)
    }
}
