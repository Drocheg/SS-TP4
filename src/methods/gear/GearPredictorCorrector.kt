package methods.gear

import methods.beeman.Simulator
import methods.utils.SimulationProperties
import methods.utils.SimulatorProvider
import utils.Particle
import utils.Vector
import utils.times



class GearPredictorCorrector(properties: SimulationProperties, val gearInitializer: GearInitializer) : Simulator(properties) {
    private class GearParticle(from: Particle, var derivatives: List<Vector>) : Particle(from.id, from.position, from.velocity, from.radius, from.mass)

    class GearProvider(private val gearInitializer: GearInitializer) : SimulatorProvider {
        override fun generate(properties: SimulationProperties): Simulator = GearPredictorCorrector(properties, gearInitializer)
    }

    private val c1: Double
    private val c2: Double
    private val c3: Double
    private val c4: Double
    private val c5: Double

    private val a = arrayOf(3.0/16, 251.0/360, 1.0, 11.0/18, 1.0/6, 1.0/60)     // Alpha table
    private val fact = arrayOf(1.0, 1.0, 2.0, 6.0, 24.0, 120.0)     // Factorial table


    init {
        val providedParticles = properties.intialParticles
        particles = providedParticles.map {
            GearParticle(it, gearInitializer.calculateInitialDerivatives(it))
        }

        c1 = dt
        c2 = (dt * c1) / 2.0
        c3 = (dt * c2) / 3.0
        c4 = (dt * c3) / 4.0
        c5 = (dt * c4) / 5.0
    }


    override fun updateParticles() {
        val gearParticles = particles as List<GearParticle>

        // Taylor coefficients

        val predictedParticles = mutableListOf<GearParticle>()
        // 1. Predict
        for (p in gearParticles) {
            val r  = p.derivatives[0]
            val r1 = p.derivatives[1]
            val r2 = p.derivatives[2]
            val r3 = p.derivatives[3]
            val r4 = p.derivatives[4]
            val r5 = p.derivatives[5]

            val r_p  = r  + c1*r1 + c2*r2 + c3*r3 + c4*r4 + c5*r5
            val r1_p = r1 + c1*r2 + c2*r3 + c3*r4 + c4*r5
            val r2_p = r2 + c1*r3 + c2*r4 + c3*r5
            val r3_p = r3 + c1*r4 + c2*r5
            val r4_p = r4 + c1*r5
            val r5_p = r5

            val pp = Particle(p.id, r_p, r1_p, p.radius, p.mass)
            val predicted = GearParticle(pp, listOf(r_p, r1_p, r2_p, r3_p, r4_p, r5_p))
            predictedParticles.add(predicted)
        }


        gearParticles.forEachIndexed { index, p ->
            val predicted = predictedParticles[index]

            val r2_p = predicted.derivatives[2]
            val nextAccel = forceCalculator.calculateAcceleration(predicted, predictedParticles)
            val delta_a = nextAccel - r2_p
            val DR2 = (0.5*dt*dt)*(delta_a)

            val newDerivatives : MutableList<Vector> = mutableListOf()
            for (i in 0 until 6) {
                val r_c = predicted.derivatives[i] + (a[i] * fact[i] / Math.pow(dt, i.toDouble())) * DR2
                newDerivatives.add(r_c)
            }

            // Update particle p
            p.position = newDerivatives[0]
            p.velocity = newDerivatives[1]
            p.derivatives = newDerivatives
        }
    }
}