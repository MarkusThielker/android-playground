package dev.thielker.playground.android.ui.lobby

import android.app.Application
import android.content.Context
import android.net.nsd.NsdManager
import android.net.nsd.NsdServiceInfo
import android.provider.Settings
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import timber.log.Timber
import java.net.ServerSocket

class ConnectionViewModel(application: Application) : AndroidViewModel(application) {

    private val app = getApplication<Application>()

    // service management
    private val nsdManager = app.getSystemService(Context.NSD_SERVICE) as NsdManager
    private lateinit var mService: NsdServiceInfo
    private lateinit var serverSocket: ServerSocket

    private val deviceName: String =
        Settings.Global.getString(app.contentResolver, Settings.Global.DEVICE_NAME)

    // service information
    var mServiceName by mutableStateOf(deviceName)
    private var mServiceType = "_lobbyService._tcp"
    private var mLocalPort = 0

    // input validation
    var maxServiceNameLength = 25

    // current state
    var isHosting by mutableStateOf(false)
    val services = mutableStateListOf<String>()
    val clients = mutableStateListOf<String>()

    // connection state listener
    private val registrationListener = object : NsdManager.RegistrationListener {

        override fun onServiceRegistered(NsdServiceInfo: NsdServiceInfo) {

            isHosting = true
            mServiceName =
                NsdServiceInfo.serviceName // Update to react no changes by the system due to conflicts

            Timber.d("Service registered as \"$mServiceName\"")
        }

        override fun onRegistrationFailed(serviceInfo: NsdServiceInfo, errorCode: Int) {
            Timber.e("Registration failed [error = $errorCode]")
        }

        override fun onServiceUnregistered(serviceInfo: NsdServiceInfo) {

            isHosting = false
            startDiscovery()

            Timber.d("Service unregistered successful")
        }

        override fun onUnregistrationFailed(serviceInfo: NsdServiceInfo, errorCode: Int) {
            Timber.e("Service unregistration failed [error = $errorCode]")
        }
    }

    private val discoveryListener = object : NsdManager.DiscoveryListener {

        override fun onDiscoveryStarted(regType: String) {
            Timber.d("Service discovery started")
        }

        override fun onServiceFound(service: NsdServiceInfo) {

            Timber.d("Service discovery success - $service")

            when {

                service.serviceType != "$mServiceType." ->
                    Timber.i("Unknown Service Type: ${service.serviceType}")

                service.serviceName == mServiceName ->
                    Timber.i("Same machine: $mServiceName")

                else -> {

                    services.add(service.serviceName)

                    nsdManager.resolveService(
                        service,
                        resolveListener
                    )
                }
            }
        }

        override fun onServiceLost(service: NsdServiceInfo) {
            services.remove(service.serviceName)
            Timber.e("Service lost: $service")
        }

        override fun onDiscoveryStopped(serviceType: String) {
            Timber.i("Discovery stopped: $serviceType")
        }

        override fun onStartDiscoveryFailed(serviceType: String, errorCode: Int) {
            nsdManager.stopServiceDiscovery(this)
            Timber.e("Discovery failed: Error code:$errorCode")
        }

        override fun onStopDiscoveryFailed(serviceType: String, errorCode: Int) {
            nsdManager.stopServiceDiscovery(this)
            Timber.e("Discovery failed: Error code:$errorCode")
        }
    }

    private val resolveListener = object : NsdManager.ResolveListener {

        override fun onResolveFailed(serviceInfo: NsdServiceInfo, errorCode: Int) {
            Timber.e("Resolve failed: $errorCode")
        }

        override fun onServiceResolved(serviceInfo: NsdServiceInfo) {

            Timber.i("Resolve Succeeded. $serviceInfo")

            if (serviceInfo.serviceName == mServiceName) {
                Timber.e("Same machine: $mServiceName")
                return
            }

            mService = serviceInfo
        }
    }

    init {
        initializeServerSocket()
        startDiscovery()
    }

    /** Initialize a server socket on the next available port. */
    private fun initializeServerSocket() {
        serverSocket = ServerSocket(0).also { socket -> mLocalPort = socket.localPort }
    }

    /** Create the NsdServiceInfo object, and populate it. */
    fun registerService() = viewModelScope.launch {

        if (mServiceName.isBlank() || mServiceName.length > maxServiceNameLength) {
            Timber.e("Service name is invalid")
            return@launch
        }

        stopDiscovery()

        val serviceInfo = NsdServiceInfo().apply {
            serviceName = mServiceName
            serviceType = mServiceType
            port = mLocalPort
        }

        nsdManager.registerService(
            serviceInfo,
            NsdManager.PROTOCOL_DNS_SD,
            registrationListener
        )
    }

    /** Unregister the service */
    fun unregisterService() = viewModelScope.launch {
        nsdManager.unregisterService(registrationListener)
    }

    /** Start discovery for service with apps service type */
    private fun startDiscovery() = viewModelScope.launch {

        services.clear()

        nsdManager.discoverServices(
            mServiceType,
            NsdManager.PROTOCOL_DNS_SD,
            discoveryListener
        )
    }

    /** Stop discovery for service with apps service type */
    private fun stopDiscovery() = viewModelScope.launch {
        nsdManager.stopServiceDiscovery(discoveryListener)
    }
}
