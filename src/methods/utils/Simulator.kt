package methods.beeman

import methods.utils.SystemProperties
import utils.Particle

abstract class Simulator(properties: SystemProperties) {

    protected lateinit var particles: List<Particle>
    protected val dt : Double = properties.deltaTime
    private val maxTime = properties.maxTime
    private val listener = properties.stats
    private var count = 0

    protected val forceCalculator = properties.forceCalculator


    fun simulate() {
        var time = 0.0
        while(time < maxTime) {
            updateParticles()
            time += dt
            notifyListener(time)
        }
    }

    private fun notifyListener(time: Double) {
        if(listener == null) return
        count++
        if(count == listener.steps) {
            listener.notify(time, particles)
            count = 0
        }
    }

    protected abstract fun updateParticles()

    fun getParticles(): Collection<Particle> {
        return particles
    }

}