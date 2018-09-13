package methods.utils

import methods.beeman.Simulator
import utils.ForceCalculator
import utils.Particle

class SystemProperties {

    var deltaTime = 0.0
    var maxTime = 0.0
    lateinit var intialParticles: Collection<Particle>
    lateinit var provider: SimulatorProvider
    lateinit var forceCalculator: ForceCalculator
    var stats : StatsManager? = null

    fun setDeltaTime(dt: Double): SystemProperties {
        deltaTime = dt
        return this
    }

    fun setMaxTime(mt: Double): SystemProperties {
        maxTime = mt
        return this
    }

    fun setInitialParticles(particles: Collection<Particle>): SystemProperties {
        intialParticles = particles
        return this
    }

    fun setProvider(provider: SimulatorProvider): SystemProperties {
        this.provider = provider
        return this
    }

    fun setForceCalculator(forceCalculator: ForceCalculator) : SystemProperties {
        this.forceCalculator = forceCalculator
        return this
    }

    fun build(): Simulator = provider.generate(this)

    fun trackStats(stats: StatsManager): SystemProperties {
        this.stats = stats
        return this
    }
}