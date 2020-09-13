package jackTheFishman

import org.junit.Assert
import kotlin.test.Test

class EventListTest {
    @Test
    fun shouldInvokeOne() {
        val eventList = EventList<Unit>()

        var temp = 0
        eventList.add { temp = 10 }

        eventList.invoke(Unit)
        Assert.assertEquals(10, temp)
    }

    @Test
    fun shouldInvokeOneWithParam() {
        val eventList = EventList<Int>()

        var temp = 0
        eventList.add { temp = it }

        eventList.invoke(42)
        Assert.assertEquals(42, temp)
    }
}
