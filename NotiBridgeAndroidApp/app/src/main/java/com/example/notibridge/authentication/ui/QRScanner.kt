package com.example.notibridge.authentication.ui

import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.notibridge.authentication.viewmodel.PairingViewModel
import com.journeyapps.barcodescanner.ScanOptions
import com.journeyapps.barcodescanner.ScanContract
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

@Composable
fun QRScannerScreen(
    navController: NavController,
    pairingViewModel: PairingViewModel // ✅ Passed from parent
) {
    val context = LocalContext.current
    val pairingState by pairingViewModel.pairingState.collectAsState()

    val barcodeLauncher = rememberLauncherForActivityResult(ScanContract()) { result ->
        if (result.contents != null) {
            try {
                val json = Json.parseToJsonElement(result.contents).jsonObject
                val deviceId = json["device_id"]?.jsonPrimitive?.content ?: ""
                val pairingKey = json["pairing_key"]?.jsonPrimitive?.content ?: ""
//                val hostname = json["hostname"]?.jsonPrimitive?.content ?: ""

                Log.d("QRSCANNER DATA", "$deviceId $pairingKey" )

                Log.d("QRScanner", "Extracted Device ID: $deviceId")
                pairingViewModel.pairDevice(pairingKey, deviceId)
            } catch (e: Exception) {
                Log.e("QRScanner", "Error parsing QR code", e)
            }
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(onClick = { barcodeLauncher.launch(ScanOptions()) }) {
            Text("Scan QR Code")
        }

        Spacer(modifier = Modifier.height(20.dp))

        when (pairingState) {
            PairingViewModel.PairingState.PAIRED_CONNECTED -> {
                Text("Pairing Successful!", color = MaterialTheme.colorScheme.primary)
                LaunchedEffect(pairingState) { // ✅ Launch navigation only when pairing state changes
                    navController.navigate("connected") {
                        popUpTo("qrScanner") { inclusive = true } // ✅ Clears back stack correctly
                    }
                }
            }
            PairingViewModel.PairingState.PAIRED_DISCONNECTED -> {
                Text("Pairing Failed: Disconnected", color = MaterialTheme.colorScheme.error)
            }
            PairingViewModel.PairingState.UNPAIRED -> {
                Text("Please Pair Device", color = MaterialTheme.colorScheme.secondary)
            }
            PairingViewModel.PairingState.ERROR -> {
                Text("Pairing Failed: Error Occurred", color = MaterialTheme.colorScheme.error)
            }
        }
    }
}
