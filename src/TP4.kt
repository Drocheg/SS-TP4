import com.sun.xml.internal.bind.v2.schemagen.xmlschema.Particle
import methods.beeman.BeemanManager
import methods.utils.InitialConditions
import methods.utils.MethodManager
import java.util.*

class TP4 {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {

            val manager: MethodManager =
                    InitialConditions()
                    .setDeltaTime(10.0)
                    .setInitialParticles(Collections.emptyList())
                    .setProvider(BeemanManager.Provider)
                    .build()
        }
    }
}