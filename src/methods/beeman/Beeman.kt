package methods.beeman

import methods.utils.InitialConditions
import methods.utils.MethodManager
import methods.utils.MethodProvider
import utils.Particle
import utils.Vector

class BeemanParticle(
        override val position: Vector,
        override val velocity: Vector,
        override val radius: Double,
        override val mass: Double) : Particle {
}

class BeemanManager(conditions: InitialConditions): MethodManager {

    private val particles: ArrayList<BeemanParticle> = ArrayList()

    companion object Provider: MethodProvider {
        override fun generateManager(conditions: InitialConditions): MethodManager = BeemanManager(conditions)
    }

    init {
        print(conditions)
    }


    override fun initializeParticles(particles: Collection<Particle>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun nextStep() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun subscribe(step: Int, listener: (Collection<Particle>) -> Unit) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getParticles(): Collection<Particle> {
        return particles
    }
}