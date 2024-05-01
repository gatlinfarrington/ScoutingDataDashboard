package team8bit.org.plugins

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import team8bit.org.Models.*
import team8bit.org.Services.*;
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.encodeToString



/*
Typically you would call this a controller, a controller is a group of endpoints
When we think about Data Access in an API, we would use what is typically referred to as a *service*

We can create a "Services" directory, and put our class that handles reading the data from the scouting app and mapping it to models in that services directory
Then we can instantiate the object in here, and use it in the endpoints, passing in the appropriate data through:
1. Request Bodies
2. Query Parameters
3. Path Parameters
*/

fun Application.configureRouting() {
    routing {
        get("/") {
            call.respondText("Hello World!")
        }
        get("/team-list"){
            // IMPLEMENT
            //create a model for a team list object

            //serialize the model

            //retuurn the model

            val db = DbService()
            db.getTeamStats("9432")

            call.respondText("Change this to Serialized team list")
        }
        get("/next-match"){
            // // IMPLEMENT
            // val r1 = Team(1165, "Team Paradise", 3.5, 2.1, .1, .8)
            // val r2 = Team(498, "Cobra Commanders", 5.6, 1.8, .2, .9)
            // val r3 = Team(9432, "Team 8-Bit", 9.432, 2.349, .9432, .2349)

            // val b1 = Team(3019, "Firebirds", 5.3, 0.0, .2, .1)
            // val b2 = Team(6413, "Degrees of Freedom", 3.2, 5.6, .75, 1.1)
            // val b3 = Team(6036, "Peninsula Robotics", 11.65, .678, .6036, .3606)

            
            // //create a model for a match schedule object
            // val match = MatchStats(1, r1, r2, r3, b1, b2, b3)

            val db = DbService()
            val match = db.getNextMatch(4414)
            //serialize the model
            val json = Json.encodeToString(match)
            //retuurn the model
            call.respondText(json)
        }
        get("/team-stats/{team}") { //team is a path parameter
            //IMPLEMENT

            //create a model for a team-stats object

            //serialize the model

            //retuurn the model

            //notice how we can get the team from the path parameter and use it in our code.
            call.respondText(call.parameters["team"].toString()) //change this to return serialized model
        }
        get("/top-8"){
            print("TOP 8 REQUEST")
            //create the object and fill it out (or create a service and perform our business logic in that service)
            // val top8List = Top8List()

            // val team1 = Top8Team("Team 1", 8.5, 9.0, 1)
            // val team2 = Top8Team("Team 2", 8.0, 8.5, 2)

            // top8List.addTeam(team1)
            // top8List.addTeam(team2)
            val db = DbService()
            val top8List = db.getTop8()
            //Serialize the object
            val json = Json.encodeToString(top8List)
            call.respond(json)
        }
        get("/comp-avgs"){
            // val avgs = CompAvgs(1.35, .42, 6.8, 3.2, .76, .12) //fake data
            val db = DbService()
            val avgs = db.getCompAvgs()
            val json = Json.encodeToString(avgs) //turn to JSON
            call.respond(json) 
        }
    }
}
