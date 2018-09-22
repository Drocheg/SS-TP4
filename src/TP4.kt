import methods.beeman.VelocityDependentBeeman
import methods.gear.GearPredictorCorrector
import methods.gear.SpringGearInitializer
import methods.utils.*
import methods.verlet.VelocityVerlet
import utils.*
import utils.Vector
import java.io.File
import java.util.*

class TP4 {
    companion object {
        @JvmStatic
        fun mainX(args: Array<String>) {
            StatsPrinter.outputDirectory = "spring"


            val builder =
                    SimulationProperties()
                            .setDeltaTime(0.05)
                            .setMaxTime(5.0)
                            .setInitialParticles(listOf(Particle(0, Vector(1.0, 0.0), Vector(-100.0 / 140.0, 0.0), 0.1, 70.0)))
                            .setForceCalculator(SpringForce())


            var stats = SystemStats(1)

            // Gear
            builder.setProvider(GearPredictorCorrector.GearProvider(SpringGearInitializer(), true))
                    .setStatsManager(stats)
            builder.build().simulate()
            StatsPrinter.printPositions(stats.statList, "gear")

            // Beeman
            stats = SystemStats(1)
            builder.setProvider(VelocityDependentBeeman.Provider)
                    .setStatsManager(stats)
            builder.build().simulate()
            StatsPrinter.printPositions(stats.statList, "beeman")


            // Verlet
            stats = SystemStats(1)
            builder.setProvider(VelocityVerlet.Provider)
                    .setStatsManager(stats)
            builder.build().simulate()
            StatsPrinter.printPositions(stats.statList, "verlet")
        }

        @JvmStatic
        fun mainLoop(args: Array<String>) {
            StatsPrinter.outputDirectory = "errors_out"

            for (i in 1..100) {
                val deltaTime = 0 + i * (0.05 / 100.0)

                val builder =
                        SimulationProperties()
                                .setDeltaTime(deltaTime)
                                .setMaxTime(5.0)
                                .setInitialParticles(listOf(Particle(0, Vector(1.0, 0.0), Vector(-100.0 / 140.0, 0.0), 0.1, 70.0)))
                                .setForceCalculator(SpringForce())


                var stats = SystemStats(1)

                // Gear
                builder.setProvider(GearPredictorCorrector.GearProvider(SpringGearInitializer(), true))
                        .setStatsManager(stats)
                builder.build().simulate()
                StatsPrinter.printPositions(stats.statList, "gear_${i}")

                // Beeman
                stats = SystemStats(1)
                builder.setProvider(VelocityDependentBeeman.Provider)
                        .setStatsManager(stats)
                builder.build().simulate()
                StatsPrinter.printPositions(stats.statList, "beeman_${i}")


                // Verlet
                stats = SystemStats(1)
                builder.setProvider(VelocityVerlet.Provider)
                        .setStatsManager(stats)
                builder.build().simulate()
                StatsPrinter.printPositions(stats.statList, "verlet_${i}")
            }
        }


        @JvmStatic
        fun mainPlanets(args: Array<String>) {
            StatsPrinter.outputDirectory = "planet"

            var stats = SystemStats(1000)

            val builder =
                    SimulationProperties()
                            .setDeltaTime(200.0)
                            .setMaxTime(6 * 3.154e+7)
                            .setInitialParticles(Planets.values().map { it -> it.generateBasic() })
                            .setForceCalculator(Gravity())
                            .setProvider(VelocityVerlet.Provider)
                            .setStatsManager(stats)

//            StatsPrinter.printPositions(stats.statList, "earth")
//             Gear
//            builder.setProvider(GearPredictorCorrector.GearProvider(SpringGearInitializer()))
//                    .setStatsManager(stats)
//            builder.build().simulate()
////            OvitoPrinter.printPositions(stats.statList,  "gear", shouldPrint = true)
//
//            // Beeman
//            stats = SystemStats(1000)
//            builder.setProvider(VelocityDependentBeeman.Provider)
//                    .setStatsManager(stats)
//            builder.build().simulate()
////            OvitoPrinter.printPositions(stats.statList,  "beeman", shouldPrint = true)


            // Verlet
            stats = SystemStats(40000)
            builder.setProvider(VelocityVerlet.Provider)
                    .setStatsManager(stats)
            builder.build().simulate()
            OvitoPrinter.printPositions(stats.statList, "verlet", shouldPrint = true)
        }

        @JvmStatic
        fun main(args: Array<String>) {
            val builder =
                    SimulationProperties()
                            .setDeltaTime(1.0 * 60 * 10)
                            .setMaxTime(1.0 * 60 * 60 * 24 * 365 * 2)
                            .setForceCalculator(Gravity())
                            .setProvider(VelocityVerlet.Provider)
            CSVReader.daysData().forEach {
                println(it.Date)
                val tracker = DistanceTracker(1, it.Date);
                testManyShips(it.particles, tracker, builder)
                printOneShip(it.particles, tracker.best.minV0,tracker.best.minL,0.0, 1, builder)
            }
        }

        fun printOneShip(planets: Collection<Particle>, v0: Double, L: Double, angle: Double, step: Int, builder: SimulationProperties) {
            var stats = SystemStats(step)
            builder.setStatsManager(stats)
            val earth = planets.first { it.id == Planets.EARTH.ordinal }
            val earthVersor = earth.position.versor()
            builder.setInitialParticles(planets.plus(
                        Particle(1337,
                                earth.position + earthVersor.scaledBy(L),
                                earth.velocity + earth.velocity.versor().scaledBy(v0),
                                100.0,
                                721.9
                        )))
                    .build().simulate()
            OvitoPrinter.printPositions(stats.statList, "verlet_v" + v0 + "_L" + L, shouldPrint = true)
        }


        fun testManyShips(planets: Collection<Particle>, distanceTracker: DistanceTracker, builder: SimulationProperties) {
            builder.setStatsManager(distanceTracker)
            val earth = planets.first { it.id == Planets.EARTH.ordinal }
            val earthVersor = earth.position.versor()
            val earthVVersor = earth.velocity.versor();
            val minL = 6400
            val maxL = minL + 10000
            val maxV0 = 20
            for (cV in 0..maxV0*25) {
                val v = cV / 25.0
                println(v)
                for (L in minL..maxL step 500) {
//                  val L = 12400.0
//                    for (angle in -90..90 step 5){
                        (builder.stats as DistanceTracker).setupInitialConditions(L.toDouble(), v, 0.0) // TODO angle
                    try {
                        builder.setInitialParticles(planets.map { it.clone() }.plus(
                                Particle(1337,
                                        earth.position + earthVersor.scaledBy(L.toDouble()),
                                        earth.velocity + earth.velocity.versor().scaledBy(v),
                                        100.0,
                                        721.9
                                )
                        ))
                                .build().simulate()
//                    }
                    } catch (e: Exception) {
                        println("Exception height $L velocity $v ")
                        println(e)
                    }
                }
            }
            distanceTracker.flush()
        }
    }
}