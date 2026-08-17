package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.domain.models.ShotGenStatus
import com.example.domain.models.StoryboardShot
import com.example.ui.theme.*

@Composable
fun StoryboardShotCard(
    shot: StoryboardShot,
    onRenderClick: () -> Unit = {},
    onPromptEdit: (String) -> Unit = {},
    onAIFixClick: (() -> Unit)? = null
) {
    var isExpanded by remember { mutableStateOf(false) }
    var isEditingPrompt by remember { mutableStateOf(false) }
    var editedPromptText by remember { mutableStateOf(shot.generationPrompt) }
    var isPlayingPreview by remember { mutableStateOf(false) }

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = StudioCardBg),
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            if (shot.status == ShotGenStatus.COMPLETED) StudioCyan.copy(alpha = 0.5f) else StudioBorder
        ),
        modifier = Modifier
            .fillMaxWidth()
            .testTag("storyboard_shot_card_${shot.shotNumber}")
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Header: Shot Number, Duration, Status Badge
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = StudioVioletPrimary
                    ) {
                        Text(
                            text = "Shot #${shot.shotNumber}",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp
                            ),
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "${"%.1f".format(shot.duration)}s",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = StudioCyan,
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp
                        )
                    )
                }

                // Status Badge
                val (statusColor, statusText) = when (shot.status) {
                    ShotGenStatus.READY -> Pair(StudioTextMuted, "Ready")
                    ShotGenStatus.QUEUED -> Pair(StudioAmber, "Queued")
                    ShotGenStatus.GENERATING -> Pair(StudioVioletPrimary, "Generating...")
                    ShotGenStatus.COMPLETED -> Pair(StudioCyan, "Rendered ✓")
                    ShotGenStatus.FAILED -> Pair(Color(0xFFE53935), "Failed")
                }

                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = statusColor.copy(alpha = 0.15f),
                    border = androidx.compose.foundation.BorderStroke(1.dp, statusColor.copy(alpha = 0.4f))
                ) {
                    Text(
                        text = statusText,
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = statusColor,
                            fontWeight = FontWeight.Bold,
                            fontSize = 10.sp
                        ),
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Story Purpose
            Text(
                text = shot.storyPurpose,
                style = MaterialTheme.typography.titleSmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = StudioTextPrimary,
                    fontSize = 14.sp
                )
            )

            Spacer(modifier = Modifier.height(6.dp))

            // Visual Description
            Text(
                text = shot.visualDescription,
                style = MaterialTheme.typography.bodySmall.copy(
                    color = StudioTextSecondary,
                    fontSize = 12.sp,
                    lineHeight = 16.sp
                )
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Badges row: Camera, Lighting, Transition
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = StudioCardElevated,
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = "Cam: ${shot.cameraFraming}",
                        style = MaterialTheme.typography.labelSmall.copy(color = StudioTextSecondary, fontSize = 9.sp),
                        maxLines = 1,
                        modifier = Modifier.padding(6.dp)
                    )
                }
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = StudioCardElevated,
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = "Light: ${shot.lighting}",
                        style = MaterialTheme.typography.labelSmall.copy(color = StudioTextSecondary, fontSize = 9.sp),
                        maxLines = 1,
                        modifier = Modifier.padding(6.dp)
                    )
                }
            }

            // Generating Progress Bar
            if (shot.status == ShotGenStatus.GENERATING) {
                Spacer(modifier = Modifier.height(10.dp))
                Column(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Rendering Neural Latents...",
                            style = MaterialTheme.typography.labelSmall.copy(color = StudioVioletLight, fontSize = 10.sp)
                        )
                        Text(
                            text = "${(shot.progress * 100).toInt()}%",
                            style = MaterialTheme.typography.labelSmall.copy(color = StudioCyan, fontSize = 10.sp)
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    LinearProgressIndicator(
                        progress = { shot.progress.coerceIn(0f, 1f) },
                        color = StudioVioletPrimary,
                        trackColor = StudioCardElevated,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(6.dp)
                            .clip(RoundedCornerShape(3.dp))
                    )
                }
            }

            // Completed Video Preview Box
            if (shot.status == ShotGenStatus.COMPLETED) {
                Spacer(modifier = Modifier.height(10.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(140.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color(0xFF0F1424))
                        .border(1.dp, StudioCyan.copy(alpha = 0.4f), RoundedCornerShape(10.dp))
                        .clickable { isPlayingPreview = !isPlayingPreview },
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(StudioCyan.copy(alpha = 0.9f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = if (isPlayingPreview) Icons.Default.Pause else Icons.Default.PlayArrow,
                                contentDescription = null,
                                tint = Color.Black,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = if (isPlayingPreview) "Playing 8K Render Preview (${shot.duration}s)..." else "Shot #${shot.shotNumber} Rendered • Tap to Play",
                            style = MaterialTheme.typography.labelSmall.copy(color = StudioTextPrimary, fontSize = 10.sp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Expandable Prompt Inspector & Actions
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextButton(
                    onClick = { isExpanded = !isExpanded },
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Icon(
                        imageVector = if (isExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                        contentDescription = null,
                        tint = StudioCyan,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = if (isExpanded) "Hide Production Prompt" else "Inspect Production Prompt",
                        fontSize = 11.sp,
                        color = StudioCyan
                    )
                }

                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    if (onAIFixClick != null) {
                        Button(
                            onClick = onAIFixClick,
                            colors = ButtonDefaults.buttonColors(containerColor = StudioAmber),
                            shape = RoundedCornerShape(8.dp),
                            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                            modifier = Modifier.testTag("btn_ai_fix_shot_${shot.shotNumber}")
                        ) {
                            Icon(
                                imageVector = Icons.Default.AutoAwesome,
                                contentDescription = null,
                                tint = Color.Black,
                                modifier = Modifier.size(12.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("AI Fix", color = Color.Black, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }

                    Button(
                        onClick = onRenderClick,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (shot.status == ShotGenStatus.COMPLETED) StudioCardElevated else StudioVioletPrimary
                        ),
                        shape = RoundedCornerShape(8.dp),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                        modifier = Modifier.testTag("btn_render_shot_${shot.shotNumber}")
                    ) {
                        Icon(
                            imageVector = if (shot.status == ShotGenStatus.COMPLETED) Icons.Default.Refresh else Icons.Default.Videocam,
                            contentDescription = null,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = if (shot.status == ShotGenStatus.COMPLETED) "Re-render" else "Render Shot",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            AnimatedVisibility(visible = isExpanded) {
                Column(modifier = Modifier.padding(top = 10.dp)) {
                    if (!isEditingPrompt) {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = StudioCardElevated,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(10.dp)) {
                                Text(
                                    text = shot.generationPrompt,
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        color = StudioTextPrimary,
                                        fontSize = 11.sp,
                                        lineHeight = 15.sp
                                    )
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.End
                                ) {
                                    TextButton(
                                        onClick = {
                                            editedPromptText = shot.generationPrompt
                                            isEditingPrompt = true
                                        },
                                        contentPadding = PaddingValues(horizontal = 6.dp, vertical = 2.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Edit,
                                            contentDescription = null,
                                            tint = StudioVioletLight,
                                            modifier = Modifier.size(12.dp)
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text("Edit Prompt", fontSize = 10.sp, color = StudioVioletLight)
                                    }
                                }
                            }
                        }
                    } else {
                        OutlinedTextField(
                            value = editedPromptText,
                            onValueChange = { editedPromptText = it },
                            modifier = Modifier.fillMaxWidth(),
                            minLines = 3,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = StudioCardElevated,
                                unfocusedContainerColor = StudioCardElevated,
                                focusedTextColor = StudioTextPrimary,
                                unfocusedTextColor = StudioTextPrimary
                            )
                        )
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 6.dp),
                            horizontalArrangement = Arrangement.End
                        ) {
                            TextButton(onClick = { isEditingPrompt = false }) {
                                Text("Cancel", color = StudioTextSecondary, fontSize = 11.sp)
                            }
                            Button(
                                onClick = {
                                    onPromptEdit(editedPromptText)
                                    isEditingPrompt = false
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = StudioCyan),
                                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp)
                            ) {
                                Text("Save Prompt", color = Color.Black, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }
    }
}
