package methods.utils

interface MethodProvider {
    fun generateManager(builder: InitialConditions): MethodManager
}