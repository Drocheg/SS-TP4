package methods.utils

import com.sun.xml.internal.bind.v2.schemagen.xmlschema.Particle

class InitialConditions {

    private var deltaTime = 0.0
    private lateinit var intialParticles: Collection<Particle>
    private lateinit var provider: MethodProvider

    fun setDeltaTime(dt: Double): InitialConditions {
        deltaTime = dt
        return this
    }

    fun setInitialParticles(particles: Collection<Particle>): InitialConditions {
        intialParticles = particles
        return this
    }

    fun setProvider(provider: MethodProvider): InitialConditions {
        this.provider = provider
        return this
    }

    fun build(): MethodManager = provider.generateManager(this)
}