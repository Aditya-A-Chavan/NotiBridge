package com.example.notibridge.authentication.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.notibridge.authentication.viewmodel.PairingViewModel

@Composable
fun PairingScreen(pairingViewModel: PairingViewModel = viewModel()) {
    var showQRScanner by remember { mutableStateOf(true) }

    if (showQRScanner) {
        QRScannerScreen(pairingViewModel) // Navigate to QR Scanner
    } else {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Pair your device", style = MaterialTheme.typography.headlineMedium)
            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = { showQRScanner = true }) {
                Text(text = "Scan QR Code")
            }
        }
    }
}
