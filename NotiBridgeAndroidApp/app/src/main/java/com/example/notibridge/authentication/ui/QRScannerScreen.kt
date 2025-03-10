package com.example.notibridge.authentication.ui

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.notibridge.authentication.viewmodel.PairingViewModel
import com.google.gson.Gson
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanOptions

@Composable
fun QRScannerScreen(pairingViewModel: PairingViewModel = viewModel()) {
    var scannedText by remember { mutableStateOf("Scan a QR Code") }
    val isPaired by pairingViewModel.isPaired.collectAsState()

    val qrScannerLauncher = rememberLauncherForActivityResult(ScanContract()) { result ->
        if (result.contents != null) {
            scannedText = "Scanned: ${result.contents}"
            val pairingData = extractPairingData(result.contents)
            if (pairingData != null) {
                pairingViewModel.initiatePairing(pairingData.pairingKey, pairingData.deviceId, pairingData.ip)
            } else {
                scannedText = "Invalid QR Code!"
            }
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(onClick = { qrScannerLauncher.launch(ScanOptions()) }) {
            Text(text = "Scan QR Code")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = scannedText)
        Text(text = if (isPaired) "Paired!" else "Not Paired")
    }
}

fun extractPairingData(result: String): PairingData? {
    return try {
        Gson().fromJson(result, PairingData::class.java)
    } catch (e: Exception) {
        null
    }
}

data class PairingData(val deviceId: String, val ip: String, val pairingKey: String)
