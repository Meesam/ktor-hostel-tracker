package com.meesam.plugins

import com.meesam.data.repositories.AuthRepository
import com.meesam.data.repositories.CoachingRepository
import com.meesam.data.repositories.HostelImageRepository
import com.meesam.data.repositories.HostelPropertiesRepository
import com.meesam.data.repositories.HostelRepository
import com.meesam.data.repositories.IAuthRepository
import com.meesam.data.repositories.ICoachingRepository
import com.meesam.data.repositories.IHostelImageRepository
import com.meesam.data.repositories.IHostelPropertiesRepository
import com.meesam.data.repositories.IHostelRepository
import com.meesam.data.repositories.IUserAddressRepository
import com.meesam.data.repositories.UserAddressRepository
import com.meesam.services.AuthService
import com.meesam.services.CoachingService
import com.meesam.services.HostelImageService
import com.meesam.services.HostelPropertyService
import com.meesam.services.HostelService
import com.meesam.services.IAuthService
import com.meesam.services.ICoachingService
import com.meesam.services.IHostelImageService
import com.meesam.services.IHostelPropertyService
import com.meesam.services.IHostelService
import com.meesam.services.IUserAddressService
import com.meesam.services.UserAddressService
import io.ktor.server.application.Application
import io.ktor.server.application.install
import org.koin.dsl.module
import org.koin.ktor.plugin.Koin
import org.koin.logger.slf4jLogger

val appModule = module {
    // Bind the interface to the implementation
    single<IAuthRepository> { AuthRepository() }
    single<IHostelRepository> { HostelRepository() }
    single<IHostelPropertiesRepository> { HostelPropertiesRepository() }
    single<ICoachingRepository> { CoachingRepository() }
    single<IUserAddressRepository> { UserAddressRepository() }
    single<IHostelImageRepository> { HostelImageRepository() }

    // Inject the repository into the service
    single<IAuthService> { AuthService(get()) }
    single<IHostelService> { HostelService(get()) }
    single<IHostelPropertyService> { HostelPropertyService(get()) }
    single<ICoachingService> { CoachingService(get()) }
    single<IUserAddressService> { UserAddressService(get()) }
    single<IHostelImageService> { HostelImageService(get()) }
}

fun Application.configureDependencies(){
    install(Koin) {
        slf4jLogger()
        modules(appModule)
    }
}