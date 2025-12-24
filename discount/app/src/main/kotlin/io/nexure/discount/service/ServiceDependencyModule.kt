package io.nexure.discount.service

import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.bindSingleton
import org.kodein.di.instance
import org.kodein.di.singleton

val serviceModule = DI.Module(
    "service_module"
) {
    bindSingleton { ProductService(instance()) }
}
