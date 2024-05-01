package team8bit.org.Services

import java.sql.DriverManager
import team8bit.org.Models.*
import java.math.BigDecimal
import java.math.RoundingMode
import kotlin.math.roundToInt

public class DbService{

  val jdbcUrl = "jdbc:postgresql://localhost:5432/Scouting-System"
  val username = "gatlinfarrington" //might need to change on mac mini
  val password = "password" //might need to change on mac mini

  val connection = DriverManager.getConnection(jdbcUrl, username, password)


  fun Double.round(decimals: Int = 3): Double = "%.${decimals}f".format(this).toDouble()

  fun getTeamStats(team: String): Team{
/*
    data class Team(
  val TeamNum: Int,
  val TeamName: String,
  val AvgSpeaker: Double,
  val AvgAmp: Double,
  val AvgClimb: Double,
  val AvgTrap: Double,
)

val r1 = Team(1165, "Team Paradise", 3.5, 2.1, .1, .8)

*/
      Class.forName("org.postgresql.Driver")
      val statement = connection.createStatement()

      var TeamNum: Int = 0
      var TeamName: String = ""
      var AvgAutoSpeaker: Double = 0.0
      var AvgAutoAmp: Double  = 0.0
      var AvgSpeaker: Double  = 0.0
      var AvgAmp: Double  = 0.0
      var AvgClimb: Double  = 0.0
      var AvgTrap: Double = 0.0

      try{
        //get the team name from the number
        val teamSet = statement.executeQuery("""
        SELECT
          "TEAM_NUMBER",
          "TEAM_NAME"
        FROM
          public."Teams"
        WHERE
          "TEAM_NUMBER" = ${team};
        """)
        //set the returned info from the returned data
        while(teamSet.next()){
          TeamNum = teamSet.getInt("TEAM_NUMBER")
          TeamName = teamSet.getString("TEAM_NAME")
        }
        //get all of that team's averages from the db
        val resultSet = statement.executeQuery("""SELECT 
          AVG("AUTO_SPEAKER") AS avg_auto_speaker,
          AVG("AUTO_AMP") AS avg_auto_amp,
          AVG("TELE_AMP") AS avg_tele_amp,
          AVG("TELE_SPEAKER") AS avg_tele_speaker,
          AVG("TELE_AMP_MISS") AS avg_tele_amp_miss,
          AVG("TELE_SPEAKER_MISS") AS avg_tele_speaker_miss,
          COUNT("ENDGAME") * 100.0 / COUNT(*) AS avg_climb,
          AVG("SCORED_TRAP") AS avg_trap
        FROM 
          public."MatchData"
        WHERE 
          "ENDGAME" IS NOT NULL
          AND "TEAM_NUMBER" = ${team};""")
        
        // fill teh data to build out the team model
        if (resultSet.next()) {
            AvgAutoSpeaker = resultSet.getDouble("avg_auto_speaker").round()
            AvgAutoAmp = resultSet.getDouble("avg_auto_amp").round()
            AvgAmp = (resultSet.getDouble("avg_tele_amp") + AvgAutoAmp).round()
            AvgSpeaker = (resultSet.getDouble("avg_tele_speaker") + AvgAutoSpeaker).round()
            AvgClimb = resultSet.getDouble("avg_climb").round()
            AvgTrap = resultSet.getDouble("avg_trap").round()

        }
        //build the team model
        val team = Team(TeamNum, TeamName, AvgSpeaker, AvgAmp, AvgClimb, AvgTrap)
  
        resultSet.close()
        statement.close()

        //for debugging, could be removed but it doesn't really matter
        println("${team.TeamName}, ${team.TeamNum}, ${team.AvgAmp}, ${team.AvgSpeaker}, ${team.AvgClimb}, ${team.AvgTrap}")
        return team
      }catch (e: Exception){
        //if there is an issue, return an empty team
        e.printStackTrace()
        
      }
      return Team(TeamNum, TeamName, 0.0, 0.0, 0.0, 0.0)
  }

  fun getNextMatch(team: Int): MatchStats{

    Class.forName("org.postgresql.Driver")
    val statement = connection.createStatement()

    //execute our query
    val matchSet = statement.executeQuery("""
    SELECT * FROM
      public."MatchSchedule"
    WHERE
      "RED_1" = ${team}
      OR "RED_2" = ${team}
      OR "RED_3" = ${team}
      OR "BLUE_1" = ${team}
      OR "BLUE_2" = ${team}
      OR "BLUE_3" = ${team}
    AND 
      "MATCH_NUM" > (SELECT MAX("MATCH") FROM public."MatchData")
    LIMIT 1
    """)

    //prepare the teams to be returned
    var r1 = Team(0, "", 0.0, 0.0, 0.0, 0.0)
    var r2 = Team(0, "", 0.0, 0.0, 0.0, 0.0)
    var r3 = Team(0, "", 0.0, 0.0, 0.0, 0.0)

    var b1 = Team(0, "", 0.0, 0.0, 0.0, 0.0)
    var b2 = Team(0, "", 0.0, 0.0, 0.0, 0.0)
    var b3 = Team(0, "", 0.0, 0.0, 0.0, 0.0)
    //build the teams to add to the match
    if (matchSet.next()) {
        println("${matchSet.getString("RED_1")}, ${matchSet.getString("RED_2")}, ${matchSet.getString("RED_3")}, ${matchSet.getString("BLUE_1")}, ${matchSet.getString("BLUE_2")}, ${matchSet.getString("BLUE_3")}")
        //build the red teams
        r1 = getTeamStats(matchSet.getString("RED_1"))
        r2 = getTeamStats(matchSet.getString("RED_2"))
        r3 = getTeamStats(matchSet.getString("RED_3"))
        //build the blue teams
        b1 = getTeamStats(matchSet.getString("blue_1"))
        b2 = getTeamStats(matchSet.getString("BLUE_2"))
        b3 = getTeamStats(matchSet.getString("BLUE_3"))
    }
    //create the match to be returned
    var match = MatchStats(matchSet.getInt("MATCH_NUM"), r1, r2, r3, b1, b2, b3)

    statement.close()
    matchSet.close()
    return match
  }

  fun getTop8(): Top8List{
    //how to order the teams
    // 6*SpeakerAuto + 4*AmpAuto + 2*Speaker + 2*Amp + 6*climb + 7*trap

    // POSITIVES: Tele Speaker, Tele Amp, Auto Speaker, Auto Amp, Climb, Trap
    // NEGATIVES: Missed Speaker, Missed Amp

    /* POSITIVE ORDERING
      Trap
      Auto Speaker
      Autp Amp
      Teleop Speaker
      Teleop Amp
      Climb

      NEGATIVES:
      Missed notes = -1 pt
    */


    Class.forName("org.postgresql.Driver")
    val statement = connection.createStatement()

    val Top8Set = statement.executeQuery("""
    SELECT 
      td."TEAM_NAME",
      (AVG(md."AUTO_SPEAKER") + AVG(md."TELE_SPEAKER")) AS "SPEAKER",
      (AVG(md."AUTO_AMP") + AVG(md."TELE_AMP")) AS "AMP",
      score_calc."score"
    FROM 
     public."MatchData" md 
    JOIN 
      public."Teams" td ON td."TEAM_NUMBER" = md."TEAM_NUMBER"
    JOIN 
      (SELECT 
        md_inner."TEAM_NUMBER",
        (6*AVG(md_inner."SCORED_TRAP") + 5*AVG(md_inner."AUTO_SPEAKER") + 4*AVG(md_inner."AUTO_AMP") + 3*AVG(md_inner."TELE_SPEAKER") + 2*AVG(md_inner."TELE_AMP") - AVG(md_inner."TELE_SPEAKER_MISS") - AVG(md_inner."TELE_AMP_MISS")) AS "score"
      FROM 
        public."MatchData" md_inner
      GROUP BY 
        md_inner."TEAM_NUMBER"
      ) AS score_calc ON md."TEAM_NUMBER" = score_calc."TEAM_NUMBER"
    GROUP BY 
      td."TEAM_NUMBER", td."TEAM_NAME", score_calc."score"
    ORDER BY
      score_calc."score" DESC
    LIMIT 8
    """)
    var i = 1
    var t8 = Top8List()
    while(Top8Set.next()){
      val team = Top8Team(Top8Set.getString("TEAM_NAME"), Top8Set.getDouble("SPEAKER").round(), Top8Set.getDouble("AMP").round(), i)
      t8.addTeam(team)
      i++
    }


    return t8
  }

  fun getCompAvgs(): CompAvgs{
    Class.forName("org.postgresql.Driver")
    val statement = connection.createStatement()

    var AvgAutoSpeaker: Double = 0.0
    var AvgAutoAmp: Double = 0.0
    var AvgSpeaker: Double = 0.0
    var AvgAmp: Double = 0.0
    var AvgClimb: Double = 0.0
    var AvgTrap: Double = 0.0

    val Top8Set = statement.executeQuery("""
    SELECT 
    AVG("AUTO_SPEAKER") AS avg_auto_speaker,
    AVG("AUTO_AMP") AS avg_auto_amp,
    AVG("TELE_AMP") AS avg_tele_amp,
    AVG("TELE_SPEAKER") AS avg_tele_speaker,
	AVG("SCORED_TRAP") AS avg_trap
FROM 
    public."MatchData"
    """)
    if(Top8Set.next()){
      AvgAutoSpeaker = Top8Set.getDouble("avg_auto_speaker").round()
      AvgAutoAmp = Top8Set.getDouble("avg_auto_amp").round()
      AvgSpeaker = Top8Set.getDouble("avg_tele_speaker").round()
      AvgAmp = Top8Set.getDouble("avg_tele_amp").round()
      AvgTrap = Top8Set.getDouble("avg_trap").round()
    }
    val climbSet = statement.executeQuery("""
    SELECT 
      COUNT("ENDGAME") * 100.0 / COUNT(*) AS climb
    FROM 
      public."MatchData"
    WHERE
      "ENDGAME" IS NOT NULL
    """)
    if(climbSet.next()){
      AvgClimb = climbSet.getDouble("climb").round()
    }

    var avgs = CompAvgs(AvgAutoSpeaker, AvgAutoAmp, AvgSpeaker, AvgAmp, AvgClimb, AvgTrap)

    return avgs
  }
}
