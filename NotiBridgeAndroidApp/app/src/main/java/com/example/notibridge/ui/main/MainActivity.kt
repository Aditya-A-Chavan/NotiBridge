package com.example.notibridge.ui.main

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.notibridge.ui.theme.NotiBridgeTheme
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanOptions


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NotiBridgeTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color.White // Explicitly set background color
                ) {
                    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                        val deviceName = "${Build.MANUFACTURER} ${Build.MODEL}"
                        Greeting(
                            name = deviceName,
                            modifier = Modifier.padding(innerPadding)
                        )

                        QRScannerScreen()
                    }
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Surface(color = Color.LightGray) {
        Text(
            text = "Your Device Name is: $name!",
            modifier = modifier.padding(10.dp)
        )
    }
}

@Preview
@Composable
fun QRScannerScreen(){
    val context = LocalContext.current
    var scannedText by remember { mutableStateOf("Scan a QR Code") }

    val qrScannerLauncher = rememberLauncherForActivityResult(ScanContract()) { result ->
        if (result.contents != null){
            scannedText = "Scanned: ${result.contents}"
        }
    }


    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ){
        Text(text = scannedText, modifier = Modifier.padding(16.dp))

        Button(
            onClick = {
                val options = ScanOptions().apply {
                    setDesiredBarcodeFormats( ScanOptions.QR_CODE)
                    setPrompt("Scan a QR Code")
                    setBeepEnabled(true)
                    setBarcodeImageEnabled(true)
                }
                qrScannerLauncher.launch(options)
            }
        ){
            Text(text = "Open QR Scanner")
        }
    }
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    NotiBridgeTheme {
        Greeting("Android")
    }
}