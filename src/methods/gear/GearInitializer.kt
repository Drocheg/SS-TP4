package methods.gear

import utils.Particle
import utils.Vector
import utils.times

interface GearInitializer {
    fun calculateInitialDerivatives(p : Particle) : List<Vector>
}

class SpringGearInitializer(private val k : Double = 10_000.0, private val gamma: Double = 100.0) : GearInitializer {

    override fun calculateInitialDerivatives(p : Particle) : List<Vector> {
        val r = p.position
        val r1 = p.velocity
        val r2 = (-k * r  - gamma * r1) * (1 / p.mass)
        val r3 = (-k * r1 - gamma * r2) * (1 / p.mass)
        val r4 = (-k * r2 - gamma * r3) * (1 / p.mass)
        val r5 = (-k * r3 - gamma * r4) * (1 / p.mass)

        return listOf(r, r1, r2, r3, r4, r5)
    }
}