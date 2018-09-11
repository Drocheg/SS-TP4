package methods.utils

import utils.Particle

interface MethodManager {

    fun subscribe(step: Int, listener: (Collection<Particle>) -> Unit)
    fun startSimulaion()
    fun getParticles(): Collection<Particle>

}