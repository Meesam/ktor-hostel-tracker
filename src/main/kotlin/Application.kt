package com.meesam

import com.meesam.data.db.DatabaseFactory
import com.meesam.data.repositories.AuthRepository
import com.meesam.data.repositories.IAuthRepository
import com.meesam.plugins.configureHTTP
import com.meesam.plugins.configureMonitoring
import com.meesam.plugins.configureRouting
import com.meesam.plugins.configureSecurity
import com.meesam.plugins.configureSerialization
import com.meesam.plugins.configureStatusPages
import com.meesam.services.AuthService
import com.meesam.services.IAuthService
import io.ktor.server.application.*
import org.koin.dsl.module
import org.koin.ktor.plugin.Koin
import org.koin.logger.slf4jLogger


fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

val appModule = module {
    // Bind the interface to the implementation
    single<IAuthRepository> { AuthRepository() }

    // Inject the repository into the service
    single<IAuthService> { AuthService(get()) }
}

fun Application.module() {
    DatabaseFactory.init(environment)
    configureSerialization()
    configureStatusPages()
    configureMonitoring()
    configureHTTP()
    configureSecurity()
    install(Koin) {
        slf4jLogger()
        modules(appModule)
    }
    configureRouting()
}
