package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import com.example.ui.StudioViewModel
import com.example.ui.components.*
import com.example.ui.theme.*

@Composable
fun StoryboardScreen(
    viewModel: StudioViewModel,
    onBack: () -> Unit,
    onNavigateToQuality: () -> Unit
) {
    val activeProject by viewModel.activeProject.collectAsState()
    val storyboard by viewModel.currentStoryboard.collectAsState()
    val charBible by viewModel.currentCharBible.collectAsState()
    val objBible by viewModel.currentObjBible.collectAsState()
    val envBible by viewModel.currentEnvBible.collectAsState()
    val refFrames by viewModel.currentReferenceFrames.collectAsState()

    val shots = storyboard?.shots ?: emptyList()
    val completedShotsCount = shots.count { it.status == ShotGenStatus.COMPLETED }
    val allCompleted = shots.isNotEmpty() && completedShotsCount == shots.size

    Scaffold(
        containerColor = StudioDarkBg,
        topBar = {
            Column {
                StudioHeader(
                    title = activeProject?.name ?: "Storyboard & Production",
                    subtitle = "Step 4 of 6: Consistency Bibles & Generation",
                    showBackButton = true,
                    onBackClick = onBack
                )
                StageBreadcrumbs(
                    currentStageIndex = 3,
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
                    Column {
                        Text(
                            text = "Rendered: $completedShotsCount / ${shots.size} Shots",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = if (allCompleted) StudioCyan else StudioAmber,
                                fontWeight = FontWeight.Bold
                            )
                        )
                        Text(
                            text = if (allCompleted) "Ready for Multimodal Audit" else "Render shots to audit",
                            style = MaterialTheme.typography.bodySmall.copy(color = StudioTextSecondary, fontSize = 11.sp)
                        )
                    }

                    Button(
                        onClick = {
                            viewModel.runQualityControlAudit {
                                onNavigateToQuality()
                            }
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (completedShotsCount > 0) StudioVioletPrimary else StudioCardElevated
                        ),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.testTag("btn_proceed_to_qc")
                    ) {
                        Text("Audit Quality", fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.width(6.dp))
                        Icon(imageVector = Icons.Default.ArrowForward, contentDescription = null, modifier = Modifier.size(16.dp))
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
            // Consistency Bibles Collapsible / Tabs Section
            item {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Production Continuity Bibles",
                            style = MaterialTheme.typography.titleSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = StudioTextPrimary
                            )
                        )
                        Text(
                            text = "Lock to preserve traits",
                            style = MaterialTheme.typography.labelSmall.copy(color = StudioCyan)
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    ConsistencyBiblesTabs(
                        characterBible = charBible,
                        objectBible = objBible,
                        environmentBible = envBible,
                        referenceFrames = refFrames,
                        onToggleCharLock = { viewModel.toggleCharacterLock() },
                        onToggleObjLock = { viewModel.toggleObjectLock() },
                        onToggleEnvLock = { viewModel.toggleEnvironmentLock() },
                        onToggleFrameLock = { viewModel.toggleReferenceFrameLock(it) }
                    )
                }
            }

            // Storyboard Header with Batch Render Actions
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Production Storyboard (${shots.size} Shots)",
                            style = MaterialTheme.typography.titleSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = StudioTextPrimary
                            )
                        )
                        Text(
                            text = "Total Duration: ${"%.1f".format(storyboard?.totalDuration ?: 14.5)}s",
                            style = MaterialTheme.typography.bodySmall.copy(color = StudioTextSecondary, fontSize = 11.sp)
                        )
                    }

                    Button(
                        onClick = { viewModel.generateAllShots() },
                        colors = ButtonDefaults.buttonColors(containerColor = StudioVioletPrimary),
                        shape = RoundedCornerShape(10.dp),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                        modifier = Modifier.testTag("btn_render_all_shots")
                    ) {
                        Icon(imageVector = Icons.Default.PlayArrow, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Render All Shots", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }

            // Storyboard Shots List
            items(shots) { shot ->
                StoryboardShotCard(
                    shot = shot,
                    onRenderClick = { viewModel.generateShotVideo(shot.shotNumber) },
                    onPromptEdit = { newPrompt -> viewModel.updateShotPrompt(shot.shotNumber, newPrompt) },
                    onAIFixClick = null
                )
            }

            item {
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}
