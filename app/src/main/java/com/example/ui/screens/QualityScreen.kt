package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import com.example.domain.models.QualityReport
import com.example.ui.StudioViewModel
import com.example.ui.components.*
import com.example.ui.theme.*

@Composable
fun QualityScreen(
    viewModel: StudioViewModel,
    onBack: () -> Unit,
    onNavigateToExport: () -> Unit
) {
    val activeProject by viewModel.activeProject.collectAsState()
    val qualityReport by viewModel.currentQualityReport.collectAsState()
    val storyboard by viewModel.currentStoryboard.collectAsState()

    Scaffold(
        containerColor = StudioDarkBg,
        topBar = {
            Column {
                StudioHeader(
                    title = activeProject?.name ?: "Quality Control Director",
                    subtitle = "Step 5 of 6: Multimodal Continuity Audit",
                    showBackButton = true,
                    onBackClick = onBack
                )
                StageBreadcrumbs(
                    currentStageIndex = 4,
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
                        val score = qualityReport?.overallContinuity ?: 91
                        Text(
                            text = "Score: $score% Overall",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = if (score >= 90) StudioCyan else StudioAmber,
                                fontWeight = FontWeight.Bold
                            )
                        )
                        Text(
                            text = "Ready for Production Export",
                            style = MaterialTheme.typography.bodySmall.copy(color = StudioTextSecondary, fontSize = 11.sp)
                        )
                    }

                    Button(
                        onClick = onNavigateToExport,
                        colors = ButtonDefaults.buttonColors(containerColor = StudioVioletPrimary),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.testTag("btn_proceed_to_export")
                    ) {
                        Text("Export Video & Package", fontWeight = FontWeight.Bold)
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
            if (qualityReport != null) {
                item {
                    QualityControlDashboard(
                        report = qualityReport!!,
                        onApplyAIFix = { shotNum ->
                            viewModel.applyAIFixToShot(shotNum)
                        }
                    )
                }

                // Re-run Audit Button
                item {
                    OutlinedButton(
                        onClick = {
                            viewModel.runQualityControlAudit()
                        },
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = StudioCyan),
                        border = androidx.compose.foundation.BorderStroke(1.dp, StudioCyan.copy(alpha = 0.5f)),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("btn_rerun_qc_audit")
                    ) {
                        Icon(imageVector = Icons.Default.Refresh, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Re-run Quality Verification Audit", fontSize = 13.sp)
                    }
                }
            } else {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 48.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            CircularProgressIndicator(color = StudioVioletPrimary)
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = "Running Multimodal Continuity Audit...",
                                style = MaterialTheme.typography.bodyMedium.copy(color = StudioTextPrimary)
                            )
                        }
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}
