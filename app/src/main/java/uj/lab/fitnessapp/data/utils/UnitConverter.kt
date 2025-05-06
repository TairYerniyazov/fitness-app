package uj.lab.fitnessapp.data.utils

object UnitConverter {
    private const val KG_TO_POUNDS = 2.20462
    private const val METER_TO_MILES = 0.000621371
    private const val METER_TO_KM = 0.001

    fun displayWeight(kg: Double?, isImperial: Boolean): Pair<Double, String> {
        val value = kg ?: 0.0
        return if (isImperial) {
            Pair(value * KG_TO_POUNDS, "lbs")
        } else {
            Pair(value, "kg")
        }
    }

    fun storeWeight(displayValue: Double, isImperial: Boolean): Double {
        return if (isImperial) {
            displayValue / KG_TO_POUNDS
        } else {
            displayValue
        }
    }

    fun displayDistance(meters: Int?, isImperial: Boolean): Pair<Double, String> {
        val value = meters ?: 0
        return if (isImperial) {
            Pair(value * METER_TO_MILES, "mi")
        } else {
            Pair(value * METER_TO_KM, "km")
        }
    }

    fun storeDistance(displayValue: Double, isImperial: Boolean): Int {
        return if (isImperial) {
            (displayValue / METER_TO_MILES).toInt()
        } else {
            (displayValue / METER_TO_KM).toInt()
        }
    }
}