package jackTheFishman

import jackTheFishman.input.Keyboard
import jackTheFishman.input.WindowKeyboard
import jackTheFishman.stubs.WindowStub
import org.junit.Rule
import org.koin.dsl.bind
import org.koin.dsl.module
import org.koin.test.KoinTest
import org.koin.test.KoinTestRule

open class BaseTest : KoinTest {
    private val myModule =
        module {
            single { WindowStub() } bind Window::class
            single { WindowKeyboard(get()) } bind Keyboard::class
        }

    @get:Rule
    val koinTestRule = KoinTestRule.create {
        modules(myModule)
    }
}
