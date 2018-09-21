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
            stats = SystemStats(1000)
            builder.setProvider(VelocityVerlet.Provider)
                    .setStatsManager(stats)
            builder.build().simulate()
            OvitoPrinter.printPositions(stats.statList, "verlet", shouldPrint = true)
        }

        @JvmStatic
        fun main(args: Array<String>) {
            File("stats/distance").delete()
            val builder =
                    SimulationProperties()
                            .setDeltaTime(1.0 * 60 * 60 * 1)
                            .setMaxTime(1.0 * 60 * 60 * 24 * 365 * 3)
                            .setForceCalculator(Gravity())
                            .setProvider(VelocityVerlet.Provider)
            CSVReader.daysData().filter { it.Date.contains("Sep-05") }.forEach {
                println(it.Date)
//                testManyShips(it.particles, DistanceTracker(1, it.Date), builder)
                printOneShip(it.particles, 14.59,12400.0,0.0, 50, builder)
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
                                (Vector(-earthVersor.y, earthVersor.x).scaledBy(v0) + earth.velocity),
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
            val minL = 6400
            val maxL = minL + 10000
            val maxV0 = 20
            for (cV in 0..maxV0*100) {
                val v = cV / 100.0
                println(v)
//                for (L in minL..maxL step 1000) {
                  val L = 12400.0
//                    for (angle in -90..90 step 5){
                        distanceTracker.setupInitialConditions(L.toDouble(), v.toDouble(), 0.0) // TODO angle
                        builder.setInitialParticles(planets.plus(
                                Particle(1337,
                                        earth.position + earthVersor.scaledBy(L.toDouble()),
                                        (Vector(-earthVersor.y, earthVersor.x).scaledBy(v.toDouble()) + earth.velocity),
                                        100.0,
                                        721.9
                                )
                        ))
                                .build().simulate()
//                    }
//                }
            }
            distanceTracker.flush()
        }
    }
}