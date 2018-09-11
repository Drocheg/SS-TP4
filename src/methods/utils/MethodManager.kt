package methods.utils

import utils.Particle

interface MethodManager {

    companion object {
        fun getBuilder(): InitialConditions = InitialConditions()
    }

    fun getParticles(): Collection<Particle>
    fun subscribe(step: Int, listener: (Collection<Particle>) -> Unit)

    fun initializeParticles(particles: Collection<Particle>)
    fun nextStep()

    fun startSimulation() {



    }

}