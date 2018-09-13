package methods.verlet

import methods.beeman.Simulator
import methods.utils.SimulationProperties
import methods.utils.SimulatorProvider
import utils.times


class VelocityVerlet(properties: SimulationProperties) : Simulator(properties) {

    companion object Provider: SimulatorProvider {
        override fun generate(properties: SimulationProperties): Simulator = VelocityVerlet(properties)
    }


    init {
        particles = properties.intialParticles.toList()
    }


    override fun updateParticles() {
        // 1. Half step
        particles.forEach {
            val accel = forceCalculator.calculateAcceleration(it, particles)
            val halfStepVelocity = it.velocity + (dt / 2.0) * accel
            val nextPosition = it.position +  dt * halfStepVelocity

            it.velocity = halfStepVelocity
            it.position = nextPosition
        }

        // 2. Complete velocity step
        particles.forEach {
            val accel = forceCalculator.calculateAcceleration(it, particles) // a(t + dt)

            // it.velocity tiene la v(t + dt/2)
            val updatedVelocity = it.velocity + 0.5 * dt * accel
            it.velocity = updatedVelocity
        }
    }

}