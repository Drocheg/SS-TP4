package utils

open class Particle(val id: Int, var position: Vector, var velocity: Vector, val radius: Double, val mass: Double) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Particle) return false
        return id == other.id
    }

    override fun hashCode(): Int {
        return id
    }

    open fun clone(): Particle {
        return Particle(id, position, velocity, radius, mass)
    }
}