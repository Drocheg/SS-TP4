package methods.utils

import methods.beeman.Simulator
import utils.ForceCalculator

interface SimulatorProvider {
    fun generate(properties: SystemProperties): Simulator
}