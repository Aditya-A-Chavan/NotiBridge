package com.example.notibridge.authentication.ui

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
//import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.notibridge.authentication.viewmodel.PairingViewModel

@Composable
fun DisconnectedScreen(
    pairingViewModel: PairingViewModel,
    onReconnectSuccess: () -> Unit,
    onScanQr: () -> Unit
) {
    val pairingState by pairingViewModel.pairingState.collectAsState()

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        when (pairingState) {
            PairingViewModel.PairingState.PAIRED_DISCONNECTED -> {
                CircularProgressIndicator()
                pairingViewModel.attemptReconnection()
                Log.e("Disconnected Screen", "Attempting recon")
                Spacer(modifier = Modifier.height(16.dp))
                Text("Reconnecting...")
            }
            PairingViewModel.PairingState.UNPAIRED -> {
                Button(onClick = onScanQr) {
                    Text("Scan QR Code to Pair")
                }
            }
            else -> {}
        }
    }

    // If reconnected, trigger the success callback
    LaunchedEffect(pairingState) {
        if (pairingState == PairingViewModel.PairingState.PAIRED_CONNECTED) {
            onReconnectSuccess()
        }
    }
}