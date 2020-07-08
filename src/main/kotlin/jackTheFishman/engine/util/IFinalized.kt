package jackTheFishman.engine.util

/** @author Janek Winkler
 ** Interface for closing sources before program exit.
 **/
interface IFinalized {
    fun finalize()
    companion object {
        private val shouldBeFinalize = arrayListOf<IFinalized>()
        fun push(obj: IFinalized) {
            shouldBeFinalize.add(obj)
        }

        fun finalizeAll() {
            for(obj in shouldBeFinalize) {
                obj.finalize()
            }
            shouldBeFinalize.clear()
        }
    }
}
