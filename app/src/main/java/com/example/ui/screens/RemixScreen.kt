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
import com.example.domain.models.RemixControlMode
import com.example.ui.StudioViewModel
import com.example.ui.components.*
import com.example.ui.theme.*

@Composable
fun RemixScreen(
    viewModel: StudioViewModel,
    onBack: () -> Unit,
    onNavigateToStoryboard: () -> Unit
) {
    val activeProject by viewModel.activeProject.collectAsState()
    val referenceAnalysis by viewModel.currentAnalysis.collectAsState()
    val remixSettings by viewModel.currentRemixSettings.collectAsState()
    val remixConcept by viewModel.currentRemixConcept.collectAsState()
    val remixGenProgress by viewModel.remixGenProgress.collectAsState()

    Scaffold(
        containerColor = StudioDarkBg,
        topBar = {
            Column {
                StudioHeader(
                    title = activeProject?.name ?: "Remix Concept Director",
                    subtitle = "Step 3 of 6: Structural Adaptation",
                    showBackButton = true,
                    onBackClick = onBack
                )
                StageBreadcrumbs(
                    currentStageIndex = 2,
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
                    if (remixConcept == null) {
                        Button(
                            onClick = {
                                viewModel.generateRemixConcept()
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = StudioVioletPrimary),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp)
                                .testTag("btn_synthesize_concept")
                        ) {
                            Icon(imageVector = Icons.Default.AutoAwesome, contentDescription = null)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Synthesize Remix Concept", fontWeight = FontWeight.Bold)
                        }
                    } else {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Concept Synthesized ✓",
                                style = MaterialTheme.typography.labelSmall.copy(color = StudioCyan, fontWeight = FontWeight.Bold)
                            )
                            Text(
                                text = "Physical reasoning verified",
                                style = MaterialTheme.typography.bodySmall.copy(color = StudioTextSecondary, fontSize = 11.sp)
                            )
                        }
                        Button(
                            onClick = {
                                viewModel.approveConceptAndGenerateStoryboard {
                                    onNavigateToStoryboard()
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = StudioVioletPrimary),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.testTag("btn_approve_and_generate_sb")
                        ) {
                            Text("Approve & Build Storyboard", fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.width(6.dp))
                            Icon(imageVector = Icons.Default.ArrowForward, contentDescription = null, modifier = Modifier.size(16.dp))
                        }
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
            // Generating / Progress Indicator Card
            if (remixGenProgress.isGenerating) {
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
                                    text = "AI Physical Reasoning Engine...",
                                    style = MaterialTheme.typography.titleSmall.copy(
                                        color = StudioTextPrimary,
                                        fontWeight = FontWeight.Bold
                                    )
                                )
                                Text(
                                    text = "${(remixGenProgress.progressFraction * 100).toInt()}%",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = StudioCyan,
                                        fontWeight = FontWeight.Bold
                                    )
                                )
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = remixGenProgress.stepText,
                                style = MaterialTheme.typography.bodySmall.copy(color = StudioVioletLight, fontSize = 12.sp)
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            LinearProgressIndicator(
                                progress = { remixGenProgress.progressFraction.coerceIn(0f, 1f) },
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

            // Prompt Section
            item {
                RemixPromptSection(
                    userPrompt = remixSettings.userPrompt,
                    onPromptChange = { viewModel.updateRemixPrompt(it) },
                    onQuickPromptSelected = { prompt ->
                        viewModel.updateRemixPrompt(prompt)
                        viewModel.generateRemixConcept()
                    }
                )
            }

            // Dimension Controls Matrix (3-Way: Preserve / Adapt / Replace)
            item {
                DimensionControlsMatrix(
                    controls = remixSettings.controls,
                    onControlChange = { dim, mode ->
                        viewModel.setDimensionControl(dim, mode)
                    },
                    onApplyBatch = { mode ->
                        viewModel.applyPresetControls(mode)
                    }
                )
            }

            // Synthesized Concept & Structural Diffs View (if available)
            if (remixConcept != null) {
                item {
                    ComparisonCardsView(
                        referenceAnalysis = referenceAnalysis,
                        remixConcept = remixConcept!!
                    )
                }

                // Re-synthesize Button
                item {
                    OutlinedButton(
                        onClick = {
                            viewModel.generateRemixConcept()
                        },
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = StudioCyan),
                        border = androidx.compose.foundation.BorderStroke(1.dp, StudioCyan.copy(alpha = 0.5f)),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("btn_re_synthesize_concept")
                    ) {
                        Icon(imageVector = Icons.Default.Refresh, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Re-synthesize with Updated Controls", fontSize = 13.sp)
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}
