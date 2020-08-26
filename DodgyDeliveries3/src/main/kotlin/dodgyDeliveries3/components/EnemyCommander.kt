package dodgyDeliveries3.components

import dodgyDeliveries3.Component
import dodgyDeliveries3.Scene
import jackTheFishman.engine.Time
import jackTheFishman.engine.math.*
import org.joml.Vector3f
import org.joml.Vector3fc
import java.util.*
import kotlin.math.sin

class EnemyCommander(var speed: Float = 1f, var moves: Queue<MovementCommand> = LinkedList<MovementCommand>()) :
    Component() {
    data class MovementCommand(val deltaMovement: Vector3fc, val duration: Float, val canShoot: Boolean = true) {
        companion object {
            val down = MovementCommand(Vector3fConst.backwards, 2f)
            val up = MovementCommand(Vector3fConst.forward, 2f)
            val left = MovementCommand(Vector3fConst.left, 2f)
            val right = MovementCommand(Vector3fConst.right, 2f)
            val wait = MovementCommand(Vector3fConst.zero, 2f)

            val twirl = listOf(
                down, down,
                down, down,
                left, up,
                right, right,
                down, left
            )

            val shortRight = listOf(
                down, right, down, left, left
            )

            val shortLeft = listOf(
                down, right, down, left, left
            )
        }
    }

    enum class State {
        JOINING, LIVING, GOING_AWAY
    }

    private var currentCommand: MovementCommand? = null
    private var timer = 0f
    private var liveTime = 0f
    private var targetPosition: Vector3fc? = null

    private var state = State.JOINING
    val canShoot: Boolean
        get() = state == State.LIVING && (currentCommand?.canShoot ?: false)

    override fun update() {
        handleTargetPosition()

        when (state) {
            State.JOINING -> handleJoining()
            State.GOING_AWAY -> handleGoingAway()
            State.LIVING -> handleLiving()
        }

        liveTime += Time.deltaTime
    }

    private fun handleTargetPosition() {
        if (targetPosition == null) {
            targetPosition = transform.position
        }

        val delta = targetPosition!! - transform.position
        transform.position = transform.position.moveTowards(targetPosition!!, Time.deltaTime * delta.length())
    }

    private fun handleJoining() {
        val target = Vector3f(targetPosition)
        target.y = 0f
        targetPosition = targetPosition!!.moveTowards(target, Time.deltaTime * speed)
        if (transform.position.distance(target) <= .1) {
            state = State.LIVING
        }
    }

    private fun handleGoingAway() {
        targetPosition = Vector3fConst.down * Time.deltaTime + targetPosition!!
        if (transform.position.y() < -10) {
            Scene.active.destroy(gameObject)
        }
    }

    private fun handleLiving() {
        handleCommandQueue()

        if (currentCommand != null) {
            val movement = Vector3f(currentCommand!!.deltaMovement).normalize()
            targetPosition = movement * speed * Time.deltaTime + targetPosition!!

            val position = Vector3f(targetPosition)
            position.y = sin(liveTime * 3f) * .2f
            targetPosition = position

            updateTimer()
        }
    }

    private fun handleCommandQueue() {
        if (currentCommand == null) {
            if (moves.isNotEmpty()) {
                currentCommand = moves.poll()
                check(currentCommand != null)
                timer = currentCommand!!.duration
            } else {
                state = State.GOING_AWAY
            }
        }
    }

    private fun updateTimer() {
        timer -= Time.deltaTime
        if (timer <= 0) {
            currentCommand = null
        }
    }
}
