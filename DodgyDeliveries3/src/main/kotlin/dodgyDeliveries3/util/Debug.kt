package dodgyDeliveries3.util

import dodgyDeliveries3.components.Camera
import dodgyDeliveries3.components.Transform
import jackTheFishman.engine.Loader
import jackTheFishman.engine.graphics.Mesh
import jackTheFishman.engine.graphics.Shader
import org.joml.Vector3f
import org.joml.Vector3fc
import java.lang.management.ManagementFactory


object Debug {

    val active = {
        var flag = false
        for (arg in ManagementFactory.getRuntimeMXBean().inputArguments) {
            if (arg.contains("jdwp=")) {
                flag = true
            }
        }
        flag
    }()

    data class Sphere(val position: Vector3fc, val radius: Float, val color: Vector3fc)
    data class Cube(val position: Vector3fc, val size: Vector3fc, val color: Vector3fc)

    private val wiredSphere = Loader.createViaPath<Mesh>("models/wiredSphere.fbx")
    private val wiredCube = Loader.createViaPath<Mesh>("models/wiredCube.fbx")

    private val debugShader = Loader.createViaPath<Shader>("shaders/debug.shader").also {
        it.setUniform("LightDirection", Vector3f(.1f, -1f, .1f).normalize())
    }
    private val spheres = arrayListOf<Sphere>()
    private val cubes = arrayListOf<Cube>()

    fun drawWiredSphere(position: Vector3fc, radius: Float, color: Vector3fc) {
        if (active) {
            spheres.add(Sphere(position, radius, color))
        }
    }

    fun drawWiredCube(position: Vector3fc, size: Vector3f, color: Vector3fc) {
        if (active) {
            cubes.add(Cube(position, size, color))
        }
    }

    fun draw() {
        if (active) {
            debugShader.use {
                for (sphere in spheres) {
                    debugShader.setUniform("Color", Vector3f(sphere.color))
                    val world = Transform().run {
                        position = Vector3f(sphere.position)
                        scale = Vector3f(sphere.radius)
                        generateMatrix()
                    }
                    check(Camera.main != null) { "Debugging needs a Camera" }
                    debugShader.setMatrix(
                        world,
                        Camera.main!!.generateViewMatrix(),
                        Camera.main!!.getProjectionMatrix()
                    )
                    wiredSphere.draw()
                }

                for (cube in cubes) {
                    debugShader.setUniform("Color", Vector3f(cube.color))
                    val world = Transform().run {
                        position = Vector3f(cube.position)
                        scale = Vector3f(cube.size)
                        generateMatrix()
                    }
                    check(Camera.main != null) { "Debugging needs a Camera" }
                    debugShader.setMatrix(
                        world,
                        Camera.main!!.generateViewMatrix(),
                        Camera.main!!.getProjectionMatrix()
                    )
                    wiredCube.draw()
                }
            }
            spheres.clear()
        }
    }
}