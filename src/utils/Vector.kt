package utils

import kotlin.math.sqrt

data class Vector(val x: Double, val y: Double) {
    companion object {
        val ZERO = Vector(0.0,0.0)

        fun dot(v1: Vector, v2: Vector): Double {
            return v1 * v2
        }

        fun delta(v1: Vector, v2: Vector): Vector {
            val d = v2 - v1
            return d
        }
    }

    operator fun times(v: Vector): Double {
        return x * v.x + y * v.y
    }

    operator fun times(t: Double): Vector {
        return this.scaledBy(t)
    }

    operator fun minus(v: Vector): Vector {
        return Vector(x - v.x, y - v.y)
    }

    operator fun plus(v: Vector): Vector {
        return Vector(v.x + x, v.y + y)
    }

    fun scaledBy(d: Double): Vector {
        return Vector(d * x, d * y)
    }

    fun norm() : Double {
        return sqrt(dot(this, this))
    }

    fun versor() : Vector {
        return this.scaledBy(1/norm())
    }

}


operator fun Double.times(v: Vector) : Vector {
    return v.scaledBy(this)
}