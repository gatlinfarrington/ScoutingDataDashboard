package team8bit.org

import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.cors.routing.*
import team8bit.org.plugins.*


fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    configureHTTP()
    install(CORS) {
        anyHost() // CAUTION: In a production environment, you should specify the allowed hosts
        allowCredentials = true
        allowNonSimpleContentTypes = true 
    }
    configureRouting()
}
