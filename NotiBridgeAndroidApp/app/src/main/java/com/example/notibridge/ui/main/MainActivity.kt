package com.example.notibridge.ui.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.notibridge.authentication.ui.*
import com.example.notibridge.authentication.viewmodel.PairingViewModel
import com.example.notibridge.authentication.viewmodel.PairingViewModelFactory
import com.example.notibridge.authentication.repository.PairingRepository
import com.example.notibridge.authentication.repository.ConnectionRepository
import com.example.notibridge.authentication.storage.PrefsManager
import com.example.notibridge.authentication.storage.SecureStore
//import com.example.notibridge.authentication.storage.PrefsManager
//import com.example.notibridge.authentication.storage.SecureStore
import com.example.notibridge.network.NetworkManager
import com.example.notibridge.network.mdns.MdnsService

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 🔹 Create dependencies for PairingViewModel
//        val pairingRepository = PairingRepository()
//        val connectionRepository = ConnectionRepository()

        val prefsManager = PrefsManager(this)
        val secureStore = SecureStore(this)
        val networkManager = NetworkManager()
        val mdnsService = MdnsService(this)


        val pairingRepository = PairingRepository(secureStore, prefsManager, networkManager)
        val connectionRepository = ConnectionRepository(prefsManager, secureStore, networkManager, mdnsService)
        // 🔹 Create ViewModel using factory
        val pairingViewModel: PairingViewModel by viewModels {
            PairingViewModelFactory(pairingRepository, connectionRepository, prefsManager, secureStore)
        }

        setContent {
            val navController = rememberNavController()

            NavHost(navController = navController, startDestination = "disconnected") {
                composable("disconnected") {
                    DisconnectedScreen(
                        pairingViewModel = pairingViewModel,  // ✅ Pass ViewModel
                        onReconnectSuccess = { navController.navigate("connected") },
                        onScanQr = { navController.navigate("qr_scanner") }
                    )
                }
                composable("qr_scanner") {
                    QRScannerScreen(
                        navController = navController,
                        pairingViewModel = pairingViewModel
                    )
                }
                composable("connected") {
                    ConnectedScreen(
                        navController = navController,
                        pairingViewModel = pairingViewModel
                    )
                }
            }
        }
    }
}
