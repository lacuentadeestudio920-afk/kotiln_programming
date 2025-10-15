package com.example.myapplication.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                AppRoot()
            }
        }
    }
}

@Composable
private fun AppRoot() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        CounterApp()
        Spacer(modifier = Modifier.height(32.dp))
        StopWatchApp()
    }
}

/* ─────────────────── Counter ─────────────────── */

@Composable
fun CounterApp() {
    var count by remember { mutableStateOf(0) }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = "Count: $count",
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row {
            Button(onClick = { count++ }) { Text("Increase") }
            Spacer(modifier = Modifier.width(8.dp))
            Button(onClick = { count = 0 }) { Text("Reset") }
        }
    }
}

/* ─────────────────── Stopwatch ─────────────────── */

@Composable
fun StopWatchApp() {
    var timeInMillis by remember { mutableStateOf(0L) }
    var isRunning by remember { mutableStateOf(false) }

    // ✅ 개선된 LaunchedEffect: isActive 체크로 안전한 취소 처리
    LaunchedEffect(isRunning) {
        if (isRunning) {
            val startTime = System.currentTimeMillis() - timeInMillis
            while (isActive && isRunning) {
                timeInMillis = System.currentTimeMillis() - startTime
                delay(10L)
            }
        }
    }

    StopwatchScreen(
        timeInMillis = timeInMillis,
        onStartClick = { isRunning = true },
        onStopClick = { isRunning = false },
        onResetClick = {
            isRunning = false
            timeInMillis = 0L
        }
    )
}

/** UI 전담(Stateless) */
@Composable
private fun StopwatchScreen(
    timeInMillis: Long,
    onStartClick: () -> Unit,
    onStopClick: () -> Unit,
    onResetClick: () -> Unit
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = formatTime(timeInMillis),
            fontSize = 40.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Button(onClick = onStartClick) { Text("Start") }
            Spacer(modifier = Modifier.width(8.dp))
            Button(onClick = onStopClick) { Text("Stop") }
            Spacer(modifier = Modifier.width(8.dp))
            Button(onClick = onResetClick) { Text("Reset") }
        }
    }
}

/** mm:ss:cs(센티초) 포맷 */
private fun formatTime(timeInMillis: Long): String {
    val minutes = (timeInMillis / 1000) / 60
    val seconds = (timeInMillis / 1000) % 60
    val centis  = (timeInMillis % 1000) / 10
    return String.format("%02d:%02d:%02d", minutes, seconds, centis)
}

/* ─────────────────── Previews ─────────────────── */

@Preview(showBackground = true)
@Composable
private fun CounterPreview() {
    MaterialTheme { CounterApp() }
}

@Preview(showBackground = true)
@Composable
private fun StopWatchPreview() {
    MaterialTheme { StopWatchApp() }
}