package com.example.notibridge.ui.main

import android.content.Context
import android.os.Bundle
import android.text.TextUtils
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
import com.example.notibridge.services.ForegroundService
import android.provider.Settings
import  android.content.Intent
import android.app.AlertDialog


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if(!isNotificationAccessEnabled()){
            showNotificationAccessDialog()
        }

        ForegroundService.startService(this)


        // 🔹 Create dependencies for PairingViewModel
//        val pairingRepository = PairingRepository()
//        val connectionRepository = ConnectionRepository()

        val prefsManager = PrefsManager(this)
        val secureStore = SecureStore(this)
        val networkManager = NetworkManager()
        val mdnsService = MdnsService(this)



        val pairingRepository = PairingRepository(secureStore, prefsManager, networkManager, mdnsService)
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

    private fun Context.isNotificationAccessEnabled(): Boolean{
        val enabledListeners = Settings.Secure.getString(contentResolver, "enabled_notification_listeners")
        return !TextUtils.isEmpty(enabledListeners) && enabledListeners.contains(packageName)
    }

    private fun Context.showNotificationAccessDialog(){
        val builder = AlertDialog.Builder(this)

        builder.setTitle("Notification Access")
        builder.setMessage("This app requires notification access to function properly. Would you like to enable it?")
        builder.setPositiveButton("Go do Settings?"){_,_ ->
            val intent = Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS)
            startActivity(intent)
        }

        builder.setNegativeButton("Cancel", null)
        builder.show()
    }
}
