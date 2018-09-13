import methods.beeman.VelocityDependentBeeman
import methods.gear.GearPredictorCorrector
import methods.gear.SpringGearInitializer
import methods.utils.SimulationProperties
import methods.utils.StatsManager
import methods.utils.StatsPrinter
import methods.verlet.VelocityVerlet
import utils.Particle
import utils.SpringForce
import utils.Vector

class TP4 {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            StatsPrinter.outputDirectory = "testing"

            val builder =
                    SimulationProperties()
                    .setDeltaTime(0.03)
                    .setMaxTime(5.0)
                    .setInitialParticles(listOf(Particle(0, Vector(1.0, 0.0), Vector(-100.0/140.0, 0.0), 0.1, 70.0)))
                    .setForceCalculator(SpringForce())



            var stats = StatsManager(1)
            // Gear
            builder.setProvider(GearPredictorCorrector.GearProvider(SpringGearInitializer()))
                   .setStatsManager(stats)
            builder.build().simulate()
            StatsPrinter.printPositions(stats.statList, "gear")

            // Beeman
            stats = StatsManager(1)
            builder.setProvider(VelocityDependentBeeman.Provider)
                   .setStatsManager(stats)
            builder.build().simulate()
            StatsPrinter.printPositions(stats.statList, "beeman")


            // Verlet
            stats = StatsManager(1)
            builder.setProvider(VelocityVerlet.Provider)
                    .setStatsManager(stats)
            builder.build().simulate()
            StatsPrinter.printPositions(stats.statList, "verlet")



        }
    }
}