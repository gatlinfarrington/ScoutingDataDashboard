package team8bit.org.Models

import kotlinx.serialization.Serializable //want to make our models serializable

@Serializable
data class CompAvgs(
    val AvgAutoSpeaker: Double,
    val AvgAutoAmp: Double,
    val AvgSpeaker: Double,
    val AvgAmp: Double,
    val AvgClimb: Double,
    val AvgTrap: Double
)