package com.muhsiin.magicos_tweaker

import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import rikka.shizuku.Shizuku

class MainActivity : ComponentActivity() {

    private val shizukuListener = Shizuku.OnBinderReceivedListener {
        checkShizukuPermission()
    }

    // State tracking variables for our UI
    private var isShizukuAvailable by mutableStateOf(false)
    private var hasPermission by mutableStateOf(false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        checkShizukuPermission()
        Shizuku.addBinderReceivedListener(shizukuListener)

        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ShizukuStatusScreen(
                        isAvailable = isShizukuAvailable,
                        hasPermission = hasPermission,
                        onRequestPermission = {
                            try {
                                Shizuku.requestPermission(1001)
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }
                    )
                }
            }
        }
    }

    private fun checkShizukuPermission() {
        val available = Shizuku.pingBinder()
        isShizukuAvailable = available
        
        if (available) {
            hasPermission = if (Shizuku.isPreV11() || Shizuku.getVersion() < 11) {
                checkSelfPermission(Shizuku.MOCK_PERMISSION) == PackageManager.PERMISSION_GRANTED
            } else {
                Shizuku.checkSelfPermission() == PackageManager.PERMISSION_GRANTED
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        Shizuku.removeBinderReceivedListener(shizukuListener)
    }
}

@Composable
fun ShizukuStatusScreen(
    isAvailable: Boolean,
    hasPermission: Boolean,
    onRequestPermission: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "MagicOS Tweaker",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Shizuku Status",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 18.sp,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                
                Text(
                    text = if (isAvailable) "✓ Service Running" else "✗ Service Disconnected",
                    color = if (isAvailable) Color(0xFF2E7D32) else Color(0xFFC62828),
                    fontWeight = FontWeight.Medium
                )
                
                Text(
                    text = if (hasPermission) "✓ Shell Permission Granted" else "✗ No Shell Permission",
                    color = if (hasPermission) Color(0xFF2E7D32) else Color(0xFFC62828),
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        if (isAvailable && !hasPermission) {
            Button(
                onClick = onRequestPermission,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Grant Shell Permission")
            }
        } else if (!isAvailable) {
            Text(
                text = "Please open the Shizuku app and start the service first.",
                color = MaterialTheme.colorScheme.outline,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        } else {
            Text(
                text = "Ready to modify animations!",
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp
            )
        }
    }
}
