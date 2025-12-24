package io.nexure.discount.plugin

import io.nexure.discount.repository.repositoryModule
import org.kodein.di.DI
import io.nexure.discount.service.serviceModule


object DIConfig {
    val di = DI {
        import(serviceModule)
        import(repositoryModule)
    }
}
