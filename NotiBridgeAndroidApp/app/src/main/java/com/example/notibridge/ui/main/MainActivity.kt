package com.example.notibridge.ui.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.*
import androidx.lifecycle.lifecycleScope
import com.example.notibridge.authentication.viewmodel.PairingViewModel
import com.example.notibridge.ui.theme.NotiBridgeTheme
import com.example.notibridge.authentication.ui.PairingScreen
import com.example.notibridge.authentication.ui.PairingSuccessScreen
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private val pairingViewModel: PairingViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        lifecycleScope.launch {
            val isPaired = pairingViewModel.isPaired.first()

            setContent {
                NotiBridgeTheme {
                    val isPairedState by pairingViewModel.isPaired.collectAsState() // ✅ Live updates

                    if (isPairedState) {
                        PairingSuccessScreen(pairingViewModel)
                    } else {
                        PairingScreen(pairingViewModel)
                    }
                }
            }
        }

    }
}
