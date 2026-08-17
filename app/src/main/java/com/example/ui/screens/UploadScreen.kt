package com.example.ui.screens

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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.domain.models.PresetVideos
import com.example.domain.models.ReferenceVideo
import com.example.ui.StudioViewModel
import com.example.ui.components.StageBreadcrumbs
import com.example.ui.components.StudioHeader
import com.example.ui.components.VideoPlayerCard
import com.example.ui.theme.*

@Composable
fun UploadScreen(
    viewModel: StudioViewModel,
    onBack: () -> Unit,
    onNavigateToAnalysis: () -> Unit
) {
    val activeProject by viewModel.activeProject.collectAsState()
    val referenceVideo by viewModel.currentReferenceVideo.collectAsState()
    val analysisProgress by viewModel.analysisProgress.collectAsState()

    var selectedTab by remember { mutableStateOf(0) }
    var customVideoUrl by remember { mutableStateOf("") }

    Scaffold(
        containerColor = StudioDarkBg,
        topBar = {
            Column {
                StudioHeader(
                    title = activeProject?.name ?: "Upload Reference",
                    subtitle = "Step 1 of 6: Ingest & Preview",
                    showBackButton = true,
                    onBackClick = onBack
                )
                StageBreadcrumbs(
                    currentStageIndex = 0,
                    onStageClick = { /* stage navigation */ }
                )
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
            // Live Video Preview Card
            item {
                VideoPlayerCard(
                    video = referenceVideo,
                    title = "Selected Reference Video",
                    onAnalyzeClick = {
                        viewModel.startReferenceAnalysis {
                            onNavigateToAnalysis()
                        }
                    }
                )
            }

            // Analyzing Progress Card
            if (analysisProgress.isAnalyzing) {
                item {
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF1B1638)),
                        border = androidx.compose.foundation.BorderStroke(1.dp, StudioVioletPrimary),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Multimodal Temporal Analysis...",
                                    style = MaterialTheme.typography.titleSmall.copy(
                                        color = StudioTextPrimary,
                                        fontWeight = FontWeight.Bold
                                    )
                                )
                                Text(
                                    text = "${(analysisProgress.progressFraction * 100).toInt()}%",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = StudioCyan,
                                        fontWeight = FontWeight.Bold
                                    )
                                )
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = analysisProgress.stepText,
                                style = MaterialTheme.typography.bodySmall.copy(color = StudioVioletLight, fontSize = 12.sp)
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            LinearProgressIndicator(
                                progress = { analysisProgress.progressFraction.coerceIn(0f, 1f) },
                                color = StudioVioletPrimary,
                                trackColor = StudioCardElevated,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(6.dp)
                                    .clip(RoundedCornerShape(3.dp))
                            )
                        }
                    }
                }
            }

            // Source Selector Tabs
            item {
                Column {
                    Text(
                        text = "Choose Reference Source",
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = StudioTextPrimary
                        )
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    TabRow(
                        selectedTabIndex = selectedTab,
                        containerColor = StudioCardBg,
                        contentColor = StudioVioletPrimary,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .border(1.dp, StudioBorder, RoundedCornerShape(12.dp))
                    ) {
                        Tab(
                            selected = selectedTab == 0,
                            onClick = { selectedTab = 0 },
                            text = { Text("Viral Presets (Instant)", fontSize = 11.sp) }
                        )
                        Tab(
                            selected = selectedTab == 1,
                            onClick = { selectedTab = 1 },
                            text = { Text("Custom Video / URL", fontSize = 11.sp) }
                        )
                    }
                }
            }

            if (selectedTab == 0) {
                // Preset List
                items(PresetVideos.samples.size) { index ->
                    val preset = PresetVideos.samples[index]
                    val isSelected = referenceVideo?.uri == preset.uri
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = if (isSelected) StudioCardElevated else StudioCardBg,
                        border = androidx.compose.foundation.BorderStroke(
                            1.dp,
                            if (isSelected) StudioVioletPrimary else StudioBorder
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { viewModel.setReferenceVideo(preset) }
                            .testTag("preset_video_item_$index")
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = preset.title,
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = StudioTextPrimary
                                    )
                                )
                                if (isSelected) {
                                    Icon(
                                        imageVector = Icons.Default.CheckCircle,
                                        contentDescription = null,
                                        tint = StudioCyan,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = preset.description,
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = StudioTextSecondary,
                                    fontSize = 11.sp,
                                    lineHeight = 15.sp
                                )
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                Text(
                                    text = "${preset.durationSeconds}s duration",
                                    style = MaterialTheme.typography.labelSmall.copy(color = StudioCyan, fontSize = 10.sp)
                                )
                                Text(
                                    text = "• ${preset.resolution}",
                                    style = MaterialTheme.typography.labelSmall.copy(color = StudioTextSecondary, fontSize = 10.sp)
                                )
                                Text(
                                    text = "• ${preset.aspectRatio} Aspect Ratio",
                                    style = MaterialTheme.typography.labelSmall.copy(color = StudioTextSecondary, fontSize = 10.sp)
                                )
                            }
                        }
                    }
                }
            } else {
                // Custom Upload / URL
                item {
                    Card(
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = StudioCardBg),
                        border = androidx.compose.foundation.BorderStroke(1.dp, StudioBorder),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "Paste Video URL or File Path:",
                                style = MaterialTheme.typography.bodySmall.copy(color = StudioTextPrimary)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            OutlinedTextField(
                                value = customVideoUrl,
                                onValueChange = { customVideoUrl = it },
                                placeholder = { Text("https://example.com/viral_clip.mp4", color = StudioTextMuted) },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("input_custom_video_url")
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            Button(
                                onClick = {
                                    if (customVideoUrl.isNotBlank()) {
                                        val custom = ReferenceVideo(
                                            uri = customVideoUrl,
                                            title = "Uploaded Video (${customVideoUrl.takeLast(16)})",
                                            durationSeconds = 15.0,
                                            description = "Custom uploaded video file."
                                        )
                                        viewModel.setReferenceVideo(custom)
                                    }
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = StudioVioletPrimary),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text("Load Video File")
                            }
                        }
                    }
                }
            }

            // Primary CTA
            item {
                Button(
                    onClick = {
                        viewModel.startReferenceAnalysis {
                            onNavigateToAnalysis()
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = StudioVioletPrimary),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .testTag("btn_start_analysis_cta")
                ) {
                    Icon(imageVector = Icons.Default.AutoAwesome, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Extract Viral DNA & Analyze",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            item {
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}
