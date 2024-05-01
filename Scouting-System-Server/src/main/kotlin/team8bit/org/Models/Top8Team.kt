package team8bit.org.Models

import kotlinx.serialization.Serializable //want to make our models serializable

/*
typically don't put multiple classes in the same object, but I got lazy so here we are
You all will probably want to change these up a good amount anyway, so I am quite confident that you will probably clean these up and seperate them into their own files anyway
*/

@Serializable //tag object as serializable, otherwise we won't be able to convert them to JSON
data class Top8Team(
    val name: String,
    val avgSpeaker: Double,
    val avgAmp: Double,
    val ranking: Int
)

@Serializable
class Top8List(
    private val teams: MutableList<Top8Team> = mutableListOf()
) {
    fun addTeam(team: Top8Team) {
        if (teams.size < 8) {
            teams.add(team)
        } else {
            throw IllegalStateException("Top8List already contains 8 teams.")
        }
    }

    fun getTeams(): List<Top8Team> {
        return teams.toList()
    }
}
