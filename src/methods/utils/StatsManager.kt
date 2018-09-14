package methods.utils

import utils.ForceCalculator
import utils.Particle
import utils.Vector

data class Stats(val time: Double, val kinticEnergy : Double, val positions: Map<Int, Vector>)


class StatsManager(val steps: Int) {
    val statList : MutableList<Stats> = mutableListOf()

    fun notify(time: Double, system: Collection<Particle>) {
        val energy = system.fold(0.0) { acc, particle ->
            acc + 0.5*(particle.velocity * particle.velocity)*particle.mass
        }



        val positions : MutableMap<Int, Vector> = mutableMapOf()
        for (particle in system) {
            positions[particle.id] = particle.position.copy()
        }

        statList.add(Stats(time, energy, positions))
    }


}