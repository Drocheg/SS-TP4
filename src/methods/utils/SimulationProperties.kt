package methods.utils

import methods.beeman.Simulator
import utils.ForceCalculator
import utils.Particle

class SimulationProperties {

    var deltaTime = 0.0
    var maxTime = 0.0
    lateinit var intialParticles: Collection<Particle>
    lateinit var provider: SimulatorProvider
    lateinit var forceCalculator: ForceCalculator
    var stats : StatsManager? = null

    fun setDeltaTime(dt: Double): SimulationProperties {
        deltaTime = dt
        return this
    }

    fun setMaxTime(mt: Double): SimulationProperties {
        maxTime = mt
        return this
    }

    fun setInitialParticles(particles: Collection<Particle>): SimulationProperties {
        intialParticles = particles
        return this
    }

    fun setProvider(provider: SimulatorProvider): SimulationProperties {
        this.provider = provider
        return this
    }

    fun setForceCalculator(forceCalculator: ForceCalculator) : SimulationProperties {
        this.forceCalculator = forceCalculator
        return this
    }

    fun build(): Simulator = provider.generate(this)

    fun setStatsManager(stats: StatsManager): SimulationProperties {
        this.stats = stats
        return this
    }
}