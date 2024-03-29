package de.lunchtime.location

import platform.CoreLocation.*
import platform.Foundation.NSError
import platform.darwin.NSObject

internal interface LocationManager {
    fun subscribe(onUpdate: (CLLocation?) -> Unit, onError: (NSError) -> Unit)
}

internal class InternalLocationManager: LocationManager {

    private lateinit var onUpdate: (CLLocation?) -> Unit

    private lateinit var onError: (NSError) -> Unit

    private val delegateImpl = object : NSObject(), CLLocationManagerDelegateProtocol {
        override fun locationManager(manager: CLLocationManager, didUpdateLocations: List<*>) {
            onUpdate(didUpdateLocations.first() as? CLLocation)
        }

        override fun locationManager(manager: CLLocationManager, didFailWithError: NSError) {
            onError(didFailWithError)
        }

        override fun locationManager(manager: CLLocationManager, didChangeAuthorizationStatus: CLAuthorizationStatus) {
            println("didChangeAuthorizationStatus")
            println(didChangeAuthorizationStatus)
        }
    }

    private val manager = CLLocationManager().apply { delegate = delegateImpl }

    override fun subscribe(onUpdate: (CLLocation?) -> Unit, onError: (NSError) -> Unit) {
        manager.requestLocation()

        this.onUpdate = onUpdate
        this.onError = onError

    }
}



