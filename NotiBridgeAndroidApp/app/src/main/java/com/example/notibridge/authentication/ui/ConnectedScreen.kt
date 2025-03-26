package com.example.notibridge.authentication.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.notibridge.authentication.viewmodel.PairingViewModel
import com.example.notibridge.services.ForegroundService

@Composable
fun ConnectedScreen(
    navController: NavController,
    pairingViewModel: PairingViewModel
) {
    // Collect the current pairing state
    val pairingState by pairingViewModel.pairingState.collectAsState()

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Show the authenticated message
        Text(text = "AUTHENTICATED", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(20.dp))

        // Button to unpair the device
        Button(onClick = { pairingViewModel.unpairDevice() }) {
            Text("Unpair")
        }
    }

    // Observe pairing state and navigate to the disconnected screen if unpaired
    LaunchedEffect(pairingState) {
        if (pairingState == PairingViewModel.PairingState.UNPAIRED) {
            navController.navigate("disconnected") {
                popUpTo("connected") { inclusive = true }
            }
        }
    }
}
