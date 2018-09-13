package methods.utils

import methods.beeman.Simulator

interface SimulatorProvider {
    fun generate(properties: SimulationProperties): Simulator
}