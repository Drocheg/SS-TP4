package methods.beeman

import methods.utils.SystemProperties
import methods.utils.MethodManager
import methods.utils.SimulatorProvider
import utils.Particle
import utils.Vector
import utils.times

class BeemanParticle(from: Particle, var prevAccel: Vector) : Particle(from.id, from.position, from.velocity, from.radius, from.mass);

class VelocityDependentBeeman(properties: SystemProperties) : Simulator(properties) {

    companion object Provider: SimulatorProvider {
        override fun generate(properties: SystemProperties): Simulator = VelocityDependentBeeman(properties)
    }

    init {

        val prevStateParticles = properties.intialParticles.map {
            val accel = forceCalculator.calculateAcceleration(it, properties.intialParticles)
            val prevPosition = it.position - dt*it.velocity + ((dt*dt)/2.0)*accel // Euler one step back
            val prevVelocity = it.velocity - dt * accel
            Particle(it.id, prevPosition, prevVelocity, it.radius, it.mass)
        }

        particles = properties.intialParticles.map {
            val prevStateParticle = prevStateParticles.find { p -> p.id == it.id }!! // Guaranteed not null
            BeemanParticle(it, forceCalculator.calculateAcceleration(prevStateParticle, prevStateParticles))
        }
    }


    override fun updateParticles() {
        val predictedParticles = mutableListOf<BeemanParticle>()
        val beemanParticles : List<BeemanParticle> = particles as List<BeemanParticle>

        // 1. Predecir las siguientes posiciones y velocidades para poder conseguir a(t+dt)
        for (p in beemanParticles) {
            val accel = forceCalculator.calculateAcceleration(p, particles)
            val nextPosition = p.position +
                    dt * p.velocity +
                    ((2.0 / 3.0) * dt * dt) * accel -
                    ((1.0 / 6.0) * dt * dt) * p.prevAccel


            val nextPredictedVelocity = p.velocity +
                    ((3.0 / 2.0) * dt) * accel -
                    (0.5 * dt) * p.prevAccel

            val pp = Particle(p.id, nextPosition, nextPredictedVelocity, p.radius, p.mass)
            val predicted = BeemanParticle(pp, accel)   // En predicted.prevAccel queda a(t)!
            predictedParticles.add(predicted)
        }

        // Realmente avanzar
        for(p in beemanParticles) {
            val predicted = predictedParticles.find { it.id == p.id }!!
            val nextPosition = predicted.position

            val predictedAccel = forceCalculator.calculateAcceleration(predicted, predictedParticles)
            val currAccel = predicted.prevAccel

            val nextVelocityCorrected = p.velocity +
                    (dt/3.0) * predictedAccel +      // a(t + dt) predicha
                    ((5.0/6.0) * dt) * currAccel -  // a(t)
                    (dt/6.0) * p.prevAccel         // a(t - dt)

            // Modify status
            p.prevAccel = currAccel
            p.velocity = nextVelocityCorrected
            p.position = nextPosition
        }

    }

}


