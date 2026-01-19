package com.meesam.plugins

import com.meesam.data.repositories.AuthRepository
import com.meesam.data.repositories.IAuthRepository
import com.meesam.services.AuthService
import com.meesam.services.IAuthService
import io.ktor.server.application.Application
import io.ktor.server.application.install
import org.koin.dsl.module
import org.koin.ktor.plugin.Koin
import org.koin.logger.slf4jLogger

val appModule = module {
    // Bind the interface to the implementation
    single<IAuthRepository> { AuthRepository() }

    // Inject the repository into the service
    single<IAuthService> { AuthService(get()) }
}

fun Application.configureDependencies(){
    install(Koin) {
        slf4jLogger()
        modules(appModule)
    }
}