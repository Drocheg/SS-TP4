package methods.utils

import utils.ForceCalculator
import utils.Particle
import utils.Planets
import utils.Vector
import java.io.File
import kotlin.math.min

data class Stats(val time: Double, val kinticEnergy : Double, val positions: Map<Int, Vector>)

interface StatsManager {
    fun notify(time: Double, system: Collection<Particle>)

    val steps: Int
}


class SystemStats(override val steps: Int): StatsManager {
    val statList : MutableList<Stats> = mutableListOf()

    override fun notify(time: Double, system: Collection<Particle>) {
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

class DistanceTracker(override val steps: Int, val date: String): StatsManager {

    var minJupiterDistance = Double.MAX_VALUE
    var minSaturnDistance = Double.MAX_VALUE

    var minDistance = Double.MAX_VALUE
    var minL = 0.0
    var minV0 = 0.0

    var currentL = 0.0
    var currentV0 = 0.0

    fun setupInitialConditions(L: Double, V0: Double) {
        currentL = L
        currentV0 = V0
        minJupiterDistance = Double.MAX_VALUE
        minSaturnDistance = Double.MAX_VALUE
    }

    override fun notify(time: Double, system: Collection<Particle>) {

        val saturn = system.first { it.id == Planets.SATURN.ordinal }
        val jupiter = system.first { it.id == Planets.JUPITER.ordinal }

        //I was lazy not to hardcode this
        val ship = system.first { it.id == 1337 }

        minJupiterDistance = min(minJupiterDistance, (ship.position - jupiter.position).norm())
        minSaturnDistance = min(minSaturnDistance, (ship.position - saturn.position).norm())

        val distance = minJupiterDistance + minSaturnDistance
        if(minDistance > distance) {
            minDistance = distance
            minL = currentL
            minV0 = currentV0
        }
    }

    fun flush() {
        File("stats/distance").appendText(date +","
                + minL + ","
                + minV0 + ","
                + minDistance + "\n")
    }
}