package com.example.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.JsonSerializer
import com.example.domain.models.ShotGenStatus
import com.example.ui.StudioViewModel
import com.example.ui.components.*
import com.example.ui.theme.*

@Composable
fun ExportScreen(
    viewModel: StudioViewModel,
    onBack: () -> Unit,
    onNavigateToHome: () -> Unit
) {
    val context = LocalContext.current
    val activeProject by viewModel.activeProject.collectAsState()
    val storyboard by viewModel.currentStoryboard.collectAsState()
    val remixConcept by viewModel.currentRemixConcept.collectAsState()
    val qualityReport by viewModel.currentQualityReport.collectAsState()

    var selectedAspectRatio by remember { mutableStateOf("9:16") }
    var selectedResolution by remember { mutableStateOf("4K UHD") }
    var isPlayingMaster by remember { mutableStateOf(false) }
    var showExportSuccessDialog by remember { mutableStateOf(false) }
    var exportSuccessMessage by remember { mutableStateOf("") }

    val shots = storyboard?.shots ?: emptyList()

    Scaffold(
        containerColor = StudioDarkBg,
        topBar = {
            Column {
                StudioHeader(
                    title = activeProject?.name ?: "Master Video Export",
                    subtitle = "Step 6 of 6: Delivery & Distribution",
                    showBackButton = true,
                    onBackClick = onBack
                )
                StageBreadcrumbs(
                    currentStageIndex = 5,
                    onStageClick = { /* stage jump */ }
                )
            }
        },
        bottomBar = {
            Surface(
                color = StudioCardBg,
                border = androidx.compose.foundation.BorderStroke(1.dp, StudioBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Button(
                        onClick = onNavigateToHome,
                        colors = ButtonDefaults.buttonColors(containerColor = StudioCardElevated),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(imageVector = Icons.Default.Home, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Projects Home", color = StudioTextPrimary)
                    }

                    Button(
                        onClick = {
                            exportSuccessMessage = "Master Video ($selectedResolution, $selectedAspectRatio) and Production Bundle exported to device storage."
                            showExportSuccessDialog = true
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = StudioCyan),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.testTag("btn_export_master_video")
                    ) {
                        Icon(imageVector = Icons.Default.Download, contentDescription = null, tint = Color.Black, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Export Master Video", color = Color.Black, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Master Video Player Canvas
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF0F1424)),
                    border = androidx.compose.foundation.BorderStroke(1.dp, StudioCyan.copy(alpha = 0.5f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Master Timeline Preview",
                                style = MaterialTheme.typography.titleSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = StudioTextPrimary
                                )
                            )
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = StudioCyan.copy(alpha = 0.2f)
                            ) {
                                Text(
                                    text = "$selectedResolution • $selectedAspectRatio",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = StudioCyan,
                                        fontWeight = FontWeight.Bold
                                    ),
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Video Player Simulation Box
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(220.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color(0xFF080C14))
                                .border(1.dp, StudioBorder, RoundedCornerShape(12.dp))
                                .clickable { isPlayingMaster = !isPlayingMaster }
                                .testTag("master_video_player_box"),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Box(
                                    modifier = Modifier
                                        .size(56.dp)
                                        .clip(CircleShape)
                                        .background(StudioCyan),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = if (isPlayingMaster) Icons.Default.Pause else Icons.Default.PlayArrow,
                                        contentDescription = null,
                                        tint = Color.Black,
                                        modifier = Modifier.size(32.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.height(10.dp))
                                Text(
                                    text = if (isPlayingMaster) "Playing Full Sequence (${storyboard?.totalDuration ?: 14.5}s)..." else "Master Cut Rendered • Tap to Play",
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        color = StudioTextPrimary,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                )
                                Text(
                                    text = remixConcept?.oneLineConcept ?: "AI Synthesized Viral Adaptation",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = StudioTextSecondary,
                                        fontSize = 11.sp
                                    ),
                                    maxLines = 1,
                                    modifier = Modifier.padding(horizontal = 24.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Timeline Shot Segments visualizer
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(8.dp)
                                .clip(RoundedCornerShape(4.dp)),
                            horizontalArrangement = Arrangement.spacedBy(2.dp)
                        ) {
                            shots.forEach { shot ->
                                Box(
                                    modifier = Modifier
                                        .weight(shot.duration.toFloat().coerceAtLeast(0.1f))
                                        .fillMaxHeight()
                                        .background(if (shot.status == ShotGenStatus.COMPLETED) StudioCyan else StudioVioletPrimary)
                                )
                            }
                        }
                    }
                }
            }

            // Export Format & Resolution Selector
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = StudioCardBg),
                    border = androidx.compose.foundation.BorderStroke(1.dp, StudioBorder),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Distribution Platform Format",
                            style = MaterialTheme.typography.titleSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = StudioTextPrimary
                            )
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        // Aspect Ratio Chips
                        Text(
                            text = "Aspect Ratio:",
                            style = MaterialTheme.typography.labelSmall.copy(color = StudioTextSecondary)
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            listOf(
                                Pair("9:16", "Vertical (TikTok/Reels)"),
                                Pair("16:9", "Cinematic (YouTube)"),
                                Pair("1:1", "Square (Instagram)")
                            ).forEach { (ratio, label) ->
                                val isSelected = selectedAspectRatio == ratio
                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = if (isSelected) StudioVioletPrimary else StudioCardElevated,
                                    modifier = Modifier
                                        .weight(1f)
                                        .clickable { selectedAspectRatio = ratio }
                                ) {
                                    Column(
                                        modifier = Modifier.padding(8.dp),
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        Text(
                                            text = ratio,
                                            style = MaterialTheme.typography.labelMedium.copy(
                                                fontWeight = FontWeight.Bold,
                                                color = if (isSelected) Color.White else StudioTextPrimary
                                            )
                                        )
                                        Text(
                                            text = label.take(8),
                                            style = MaterialTheme.typography.labelSmall.copy(
                                                color = if (isSelected) StudioVioletLight else StudioTextSecondary,
                                                fontSize = 9.sp
                                            )
                                        )
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Resolution Chips
                        Text(
                            text = "Rendering Quality:",
                            style = MaterialTheme.typography.labelSmall.copy(color = StudioTextSecondary)
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            listOf("1080p HD", "4K UHD", "8K Master Neural").forEach { res ->
                                val isSelected = selectedResolution == res
                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = if (isSelected) StudioCyan.copy(alpha = 0.2f) else StudioCardElevated,
                                    border = androidx.compose.foundation.BorderStroke(
                                        1.dp,
                                        if (isSelected) StudioCyan else Color.Transparent
                                    ),
                                    modifier = Modifier
                                        .weight(1f)
                                        .clickable { selectedResolution = res }
                                ) {
                                    Box(
                                        modifier = Modifier.padding(8.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = res,
                                            style = MaterialTheme.typography.labelSmall.copy(
                                                color = if (isSelected) StudioCyan else StudioTextPrimary,
                                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                            )
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Production Asset Bundle Cards
            item {
                Column {
                    Text(
                        text = "Production Asset Deliverables",
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = StudioTextPrimary
                        )
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // 1. Production Prompts & Bible JSON
                    ExportAssetRow(
                        title = "Production Prompt Bible (.JSON)",
                        subtitle = "Complete camera, lighting, and consistency prompts for Sora / Veo / Midjourney",
                        icon = Icons.Default.Code,
                        onAction = {
                            val promptsText = shots.joinToString("\n\n") { "Shot #${it.shotNumber} (${it.duration}s):\n${it.generationPrompt}" }
                            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                            clipboard.setPrimaryClip(ClipData.newPlainText("Production Prompts", promptsText))
                            viewModel.notify("Production Prompts copied to clipboard!")
                        },
                        actionLabel = "Copy Prompts"
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // 2. EDL / XML Shot Decision List
                    ExportAssetRow(
                        title = "EDL / Final Cut Pro XML",
                        subtitle = "Nonlinear editing timeline cuts with exact timecodes and marker notes",
                        icon = Icons.Default.Timeline,
                        onAction = {
                            viewModel.notify("EDL timeline file downloaded.")
                        },
                        actionLabel = "Download EDL"
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // 3. Quality & Consistency Certificate
                    ExportAssetRow(
                        title = "Multimodal Continuity Report",
                        subtitle = "Verified ${qualityReport?.overallContinuity ?: 91}% consistency audit score certificate",
                        icon = Icons.Default.Verified,
                        onAction = {
                            viewModel.notify("Continuity report exported.")
                        },
                        actionLabel = "Export PDF"
                    )
                }
            }

            item {
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }

    if (showExportSuccessDialog) {
        AlertDialog(
            onDismissRequest = { showExportSuccessDialog = false },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = Icons.Default.CheckCircle, contentDescription = null, tint = StudioCyan)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Export Ready!", color = StudioTextPrimary)
                }
            },
            text = {
                Text(exportSuccessMessage, color = StudioTextSecondary, fontSize = 13.sp)
            },
            confirmButton = {
                Button(
                    onClick = { showExportSuccessDialog = false },
                    colors = ButtonDefaults.buttonColors(containerColor = StudioCyan)
                ) {
                    Text("OK", color = Color.Black, fontWeight = FontWeight.Bold)
                }
            },
            containerColor = StudioCardBg
        )
    }
}

@Composable
fun ExportAssetRow(
    title: String,
    subtitle: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    onAction: () -> Unit,
    actionLabel: String
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = StudioCardBg),
        border = androidx.compose.foundation.BorderStroke(1.dp, StudioBorder),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(StudioCardElevated),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(imageVector = icon, contentDescription = null, tint = StudioVioletLight, modifier = Modifier.size(20.dp))
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold, color = StudioTextPrimary)
                    )
                    Text(
                        text = subtitle,
                        style = MaterialTheme.typography.labelSmall.copy(color = StudioTextSecondary, fontSize = 10.sp),
                        maxLines = 1
                    )
                }
            }

            Button(
                onClick = onAction,
                colors = ButtonDefaults.buttonColors(containerColor = StudioCardElevated),
                shape = RoundedCornerShape(8.dp),
                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp)
            ) {
                Text(text = actionLabel, color = StudioCyan, fontSize = 11.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}
