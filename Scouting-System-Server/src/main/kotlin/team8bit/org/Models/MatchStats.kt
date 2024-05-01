package team8bit.org.Models

import kotlinx.serialization.Serializable //want to make our models serializable

@Serializable
data class MatchStats(
    val MatchNum: Int,
    val red1: Team,
    val red2: Team,
    val red3: Team,
    val blue1: Team,
    val blue2: Team,
    val blue3: Team
)

@Serializable
data class Team(
  var TeamNum: Int,
  var TeamName: String,
  var AvgSpeaker: Double,
  var AvgAmp: Double,
  var AvgClimb: Double,
  var AvgTrap: Double,
)