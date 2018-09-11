package methods.beeman

import methods.utils.MethodManager
import utils.Particle
import utils.Vector

class BeemanParticle: Particle {

    override val position: Vector
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.

    override val velocity: Vector
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.


}

class BeemanManager: MethodManager {

    private val particles: ArrayList<BeemanParticle> = ArrayList()

    override fun subscribe(step: Int, listener: (Collection<Particle>) -> Unit) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun startSimulaion() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getParticles(): Collection<Particle> {
        return particles
    }
}