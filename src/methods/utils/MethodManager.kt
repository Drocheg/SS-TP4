package methods.utils

import utils.Particle

interface MethodManager {
    fun getParticles(): Collection<Particle>
    fun simulate()
}