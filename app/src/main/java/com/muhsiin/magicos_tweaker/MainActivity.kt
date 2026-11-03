package com.muhsiin.magicos_tweaker

import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
    var animationScale by remember { mutableStateOf(0.5f) }
    var consoleLog by remember { mutableStateOf("No tweaks applied yet.") }
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
            .verticalScroll(scrollState),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        
        Text(
            text = "MagicOS Tweaker",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        // Status Card
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

        Spacer(modifier = Modifier.height(24.dp))

        if (!isAvailable) {
            Text(
                text = "Please open the Shizuku app and start the service first.",
                color = MaterialTheme.colorScheme.outline,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        } else if (!hasPermission) {
            Button(
                onClick = onRequestPermission,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Grant Shell Permission")
            }
        } else {
            // Tweaks Section Controls
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Window & Global Animations",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 18.sp
                    )
                    Text(
                        text = "Current Modifier: ${animationScale}x",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )

                    Slider(
                        value = animationScale,
                        onValueChange = { animationScale = String.format("%.1f", it).toFloat() },
                        valueRange = 0.0f..2.0f,
                        steps = 3 // Snaps nicely to 0.0, 0.5, 1.0, 1.5, 2.0
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Button(
                        onClick = {
                            consoleLog = TweakerService.setAnimationScale(
                                animationScale,
                                animationScale,
                                animationScale
                            )
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Apply Speed Tweak")
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        OutlinedButton(onClick = {
                            animationScale = 0.0f
                            consoleLog = TweakerService.setAnimationScale(0.0f, 0.0f, 0.0f)
                        }) { Text("Turn Off") }

                        OutlinedButton(onClick = {
                            animationScale = 0.5f
                            consoleLog = TweakerService.setAnimationScale(0.5f, 0.5f, 0.5f)
                        }) { Text("Super Fast") }

                        OutlinedButton(onClick = {
                            animationScale = 1.0f
                            consoleLog = TweakerService.setAnimationScale(1.0f, 1.0f, 1.0f)
                        }) { Text("Reset Stock") }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Output / Console Log Panel
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.Black)
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(
                        text = "Console Output:",
                        color = Color.Green,
                        fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace,
                        fontSize = 12.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = consoleLog,
                        color = Color.White,
                        fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace,
                        fontSize = 12.sp
                    )
                }
            }
        }
    }
}
