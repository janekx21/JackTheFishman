package jackTheFishman.framework.util

/**
 * Interface for closing sources before program exit
 */
interface Finalized {
    /**
     * Event that will be called when this source is about to be closed
     */
    fun finalize()

    companion object {
        private val shouldBeFinalize = arrayListOf<Finalized>()
        fun push(obj: Finalized) {
            shouldBeFinalize.add(obj)
        }

        fun finalizeAll() {
            for (obj in shouldBeFinalize) {
                obj.finalize()
            }
            shouldBeFinalize.clear()
        }
    }
}
