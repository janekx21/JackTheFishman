package ui

import linear.Vector

abstract class Component(val type: LayoutType, val volume: Vector) {

    abstract class Group(volume: Vector, protected val childes: List<Component>) : Component(
        LayoutType.flexBox, volume
    ) {
        class Horizontal(volume: Vector, childes: List<Component>) : Group(volume, childes) {
            override fun draw(position: Vector, size: Vector) {
                super.draw(position, size)
                if (childes.isNotEmpty()) {
                    var ges = Vector.zero
                    childes.forEach { t: Component? -> ges += t?.volume ?: Vector.zero }

                    var p = 0f
                    childes.forEach { component: Component? ->
                        if (component != null) {
                            val right = Vector.right * size.x * .5f

                            val scale = Vector(component.volume.x / ges.x, 1f)
                            p += scale.x * .5f
                            val pos = Vector.lerp(position - right, position + right, p)

                            component.draw(pos, Vector(size.x * scale.x, size.y * scale.y))
                            p += scale.x * .5f
                        }
                    }
                }
            }
        }

        class Vertical(volume: Vector, childes: List<Component>) : Group(volume, childes) {
            override fun draw(position: Vector, size: Vector) {
                super.draw(position, size)
                if (childes.isNotEmpty()) {
                    var ges = Vector.zero
                    childes.forEach { t: Component? -> ges += t?.volume ?: Vector.zero }

                    var p = 0f
                    childes.forEach { component: Component? ->
                        if (component != null) {
                            val down = Vector.down * size.y * .5f

                            val scale = Vector(1f, component.volume.y / ges.y)
                            p += scale.y * .5f
                            val pos = Vector.lerp(position - down, position + down, p)

                            component.draw(pos, Vector(size.x * scale.x, size.y * scale.y))
                            p += scale.y * .5f
                        }
                    }
                }
            }
        }
    }

    open class Final(volume: Vector) : Component(LayoutType.final, volume) {

    }


    open fun draw(position: Vector, size: Vector) {
    }
}