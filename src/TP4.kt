import methods.beeman.Simulator
import methods.beeman.VelocityDependentBeeman
import methods.utils.SystemProperties
import methods.utils.StatsManager
import methods.utils.StatsPrinter
import utils.Particle
import utils.SpringForce
import utils.Vector

class TP4 {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val stats = StatsManager(1)

            val manager: Simulator =
                    SystemProperties()
                    .setDeltaTime(0.007)
                    .setMaxTime(5.0)
                    .setInitialParticles(listOf(Particle(0, Vector(1.0, 0.0), Vector(-100.0/140.0, 0.0), 0.1, 70.0)))
                    .setForceCalculator(SpringForce())
                    .setProvider(VelocityDependentBeeman.Provider)
                    .trackStats(stats)
                    .build()

            manager.simulate()

            StatsPrinter.outputDirectory = "test"
            StatsPrinter.printPositions(stats.statList)
        }
    }
}