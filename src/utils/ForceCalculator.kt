package utils

interface ForceCalculator {
    fun calculatePotentialEnergy(system: Collection<Particle>) : Double
    fun calculateForce(p: Particle, others: Collection<Particle>) : Vector
    fun calculateAcceleration(p: Particle, others: Collection<Particle>): Vector {
        return calculateForce(p, others) * (1.0/p.mass);
    }
}

class SpringForce : ForceCalculator {
    override fun calculatePotentialEnergy(system: Collection<Particle>): Double = system.fold(0.0) {acum, current -> acum + k * current.position.x}

    val k = 10_000
    val gamma = 100

    override fun calculateForce(p: Particle, others: Collection<Particle>): Vector {
        val r = p.position.x; // Siempre eje x
        val v = p.velocity.x
        val fx = -k * r - gamma* v
        return Vector(fx, 0.0)
    }

}


class Gravity : ForceCalculator {
    private val G = 6.693E-11

    override fun calculatePotentialEnergy(system: Collection<Particle>): Double {
        val potential = system.fold(0.0) { acum, current -> acum + potentialEnergy(current, system) }
        return potential/2  // Conte dos veces a cada energia potencial
    }


    override fun calculateForce(p: Particle, others: Collection<Particle>): Vector {
        return others.asSequence().filter {it.id != p.id }
                                  .fold(Vector.ZERO) { accum, current -> accum + force(p, current) }
    }

    // La fuerza que sufre p1
    private fun force(p1: Particle, p2: Particle) : Vector {
        val deltaR = p2.position - p1.position
        val r = deltaR.norm()
        val rMeters = r * 1000 // km -> m
        val force = G *(p1.mass * p2.mass)/(rMeters*rMeters)
        val cosine = deltaR.x/r
        val sine = deltaR.y/r
        return Vector(force * cosine, force * sine).scaledBy(1.0/1000) // m/s2 -> km/s2
    }

    private fun potentialEnergy(particle: Particle, system: Collection<Particle>): Double {
        return system.asSequence().filter {it.id != particle.id }
                .fold(0.0) { accum, current  ->
                    val deltaR = particle.position - current.position
                    val r = deltaR.norm()
                    val rMeters = r * 1000 // km -> m
                    G * particle.mass * current.mass / rMeters
                }
    }



}