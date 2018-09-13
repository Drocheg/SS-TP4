package utils

interface ForceCalculator {
    fun calculateForce(p: Particle, others: Collection<Particle>) : Vector
    fun calculateAcceleration(p: Particle, others: Collection<Particle>): Vector
}

class SpringForce : ForceCalculator {

    val k = 10_000
    val gamma = 100

    override fun calculateForce(p: Particle, others: Collection<Particle>): Vector {
        val r = p.position.x; // Siempre eje x
        val v = p.velocity.x
        val fx = -k * r - gamma* v
        return Vector(fx, 0.0)
    }

    override fun calculateAcceleration(p: Particle, others: Collection<Particle>): Vector {
        return calculateForce(p, others).scaledBy(1.0/p.mass);
    }


}