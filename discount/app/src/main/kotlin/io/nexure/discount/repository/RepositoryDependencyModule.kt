package io.nexure.discount.repository

import org.kodein.di.DI
import org.kodein.di.bindSingleton

val repositoryModule = DI.Module(
    "repository_module"
) {
    bindSingleton { ProductRepository() }
}
