package me.szydelko.time

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import me.szydelko.time.ui.theme.TimeTheme
import java.time.Duration
import java.time.LocalDateTime
import java.time.Period
import java.time.format.DateTimeFormatter

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val end = LocalDateTime.of(2024, 1, 1, 0, 0, 0)
            var current by remember {
                mutableStateOf(LocalDateTime.now())
            }

            var remainingTime by remember {
                mutableStateOf(Duration.between(current, end))
            }

            var shouldStopUpdating by remember {
                mutableStateOf(false)
            }

            TimeTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        DateTimeDisplay(timeDifference = remainingTime)
                        DateTimeDisplay(current = current)
                    }

                    if (shouldStopUpdating) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            OutlinedCard(
                                colors = CardDefaults.cardColors(
                                    containerColor = MaterialTheme.colorScheme.surface,
                                ),
                                border = BorderStroke(1.dp, Color.Black),
                            ) {
                                Text(
                                    text = "Nowy rok!",
                                    modifier = Modifier
                                        .padding(16.dp),
                                    textAlign = TextAlign.Center,
                                )
                            }
                        }
                    }
                }
            }

            // Using coroutine to update the time every second
            LaunchedEffect(key1 = Unit) {
                while (!shouldStopUpdating) {
                    delay(1000L)
                    if (current >= end) {
                        shouldStopUpdating = true
                        break
                    } else {
                        current = LocalDateTime.now()
                        remainingTime = Duration.between(current, end)
                    }
                }
            }
        }
    }
}


@Composable
fun DateTimeDisplay(current: LocalDateTime) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        val formattedDateTime = current.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
        Text(text = formattedDateTime)
    }
}
@Composable
fun DateTimeDisplay(timeDifference: Duration) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        val days = timeDifference.toDays()
        val hours = timeDifference.toHours() % 24
        val minutes = timeDifference.toMinutes() % 60
        val seconds = timeDifference.seconds % 60

        val formattedTimeDifference =
            String.format("%02dd %02dh %02dm %02ds", days, hours, minutes, seconds)

        Text(text = formattedTimeDifference)
    }
}

