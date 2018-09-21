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

data class DistanceTrackerStats(
    var minDistance: Double,
    var minL: Double,
    var minV0: Double,
    var minAngle: Double,
    var minDate: String,
    var timeToGetToSaturn: Double
)

class DistanceTracker(override val steps: Int, val date: String): StatsManager {


    //Best for this Date
    var best: DistanceTrackerStats = DistanceTrackerStats(Double.MAX_VALUE, 0.0,0.0,0.0,"",0.0)

    //Have to track old distances
    var minJupiterDistance = Double.MAX_VALUE
    var minSaturnDistance = Double.MAX_VALUE


    //CurrentParameters
    var currentL = 0.0
    var currentV0 = 0.0
    var currentAngle = 0.0
    var currentTimeToGetToSaturn = 0.0

    fun setupInitialConditions(L: Double, V0: Double, angle: Double) {
        currentL = L
        currentV0 = V0
        currentAngle = angle

        minJupiterDistance = Double.MAX_VALUE
        minSaturnDistance = Double.MAX_VALUE
    }

    override fun notify(time: Double, system: Collection<Particle>) {

        val saturn = system.first { it.id == Planets.SATURN.ordinal }
        val jupiter = system.first { it.id == Planets.JUPITER.ordinal }

        //I was lazy not to hardcode this
        val ship = system.first { it.id == 1337 }

        val saturnDistance = (ship.position - saturn.position).norm()
        if(minSaturnDistance > saturnDistance) {
            currentTimeToGetToSaturn = time
            minSaturnDistance = saturnDistance
        }

        minJupiterDistance = min(minJupiterDistance, (ship.position - jupiter.position).norm())

        val distance = minJupiterDistance + minSaturnDistance
        if(best.minDistance > distance) {
            best = DistanceTrackerStats(distance, currentL, currentV0, currentAngle, date, currentTimeToGetToSaturn)
        }
    }

    fun flush() {
        File("stats/distance").appendText(date +","
                + best.minDistance + ","
                + best.minV0 + ","
                + best.minL + ","
                + best.minAngle + ","
                + best.timeToGetToSaturn +"\n")
    }
}