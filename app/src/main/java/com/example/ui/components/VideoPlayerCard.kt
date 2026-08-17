package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.domain.models.ReferenceVideo
import com.example.ui.theme.*
import kotlinx.coroutines.delay

@Composable
fun VideoPlayerCard(
    video: ReferenceVideo?,
    title: String = "Reference Video Preview",
    modifier: Modifier = Modifier,
    onAnalyzeClick: (() -> Unit)? = null
) {
    var isPlaying by remember { mutableStateOf(false) }
    var currentSeconds by remember { mutableStateOf(0.0) }
    val totalSeconds = video?.durationSeconds ?: 15.0

    // Auto-advance playhead when playing
    LaunchedEffect(isPlaying) {
        while (isPlaying) {
            delay(100)
            currentSeconds = (currentSeconds + 0.1)
            if (currentSeconds >= totalSeconds) {
                currentSeconds = 0.0
            }
        }
    }

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = StudioCardBg),
        border = androidx.compose.foundation.BorderStroke(1.dp, StudioBorder),
        modifier = modifier
            .fillMaxWidth()
            .testTag("video_player_card")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Videocam,
                        contentDescription = null,
                        tint = StudioVioletPrimary,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = StudioTextPrimary
                        )
                    )
                }

                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = StudioCardElevated
                    ) {
                        Text(
                            text = video?.aspectRatio ?: "9:16",
                            style = MaterialTheme.typography.labelSmall.copy(color = StudioCyan, fontSize = 10.sp),
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = StudioCardElevated
                    ) {
                        Text(
                            text = video?.resolution ?: "1080x1920",
                            style = MaterialTheme.typography.labelSmall.copy(color = StudioTextSecondary, fontSize = 10.sp),
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // 9:16 Video Player Container Canvas
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(260.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(
                        Brush.verticalGradient(
                            listOf(
                                Color(0xFF0F0F1E),
                                Color(0xFF1B1B3A),
                                Color(0xFF0D1B2A)
                            )
                        )
                    )
                    .border(1.dp, StudioBorderLight, RoundedCornerShape(12.dp))
                    .clickable { isPlaying = !isPlaying },
                contentAlignment = Alignment.Center
            ) {
                // Background visual simulation
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.padding(16.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(56.dp)
                            .clip(CircleShape)
                            .background(StudioVioletPrimary.copy(alpha = 0.85f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                            contentDescription = if (isPlaying) "Pause" else "Play",
                            tint = Color.White,
                            modifier = Modifier.size(32.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = video?.title ?: "Select a reference video",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = StudioTextPrimary,
                            fontWeight = FontWeight.SemiBold
                        ),
                        maxLines = 2
                    )
                    Text(
                        text = if (isPlaying) "Playing Neural Simulation..." else "Tap to Play Preview",
                        style = MaterialTheme.typography.labelSmall.copy(color = StudioVioletLight)
                    )
                }

                // Top-right live badge
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(10.dp)
                ) {
                    Surface(
                        shape = RoundedCornerShape(4.dp),
                        color = Color.Black.copy(alpha = 0.6f)
                    ) {
                        Text(
                            text = "${"%.1f".format(currentSeconds)}s / ${"%.1f".format(totalSeconds)}s",
                            style = MaterialTheme.typography.labelSmall.copy(color = Color.White, fontSize = 11.sp),
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }

                // Bottom Timeline Scrubber
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth()
                        .padding(12.dp)
                ) {
                    LinearProgressIndicator(
                        progress = { (currentSeconds / totalSeconds).toFloat().coerceIn(0f, 1f) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(6.dp)
                            .clip(RoundedCornerShape(3.dp)),
                        color = StudioCyan,
                        trackColor = Color.White.copy(alpha = 0.2f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Technical metadata badges
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Size: ${video?.fileSizeFormatted ?: "18.4 MB"} • 60 FPS ProRes",
                    style = MaterialTheme.typography.labelSmall.copy(color = StudioTextSecondary)
                )

                if (onAnalyzeClick != null) {
                    Button(
                        onClick = onAnalyzeClick,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = StudioVioletPrimary,
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(8.dp),
                        contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp),
                        modifier = Modifier.testTag("btn_analyze_video")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(text = "Analyze Reference", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}
