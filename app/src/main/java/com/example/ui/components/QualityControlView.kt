package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.domain.models.QualityReport
import com.example.ui.theme.*

@Composable
fun QualityControlDashboard(
    report: QualityReport,
    onApplyAIFix: (Int) -> Unit = {}
) {
    val metrics = listOf(
        Pair("Character Consistency (C01)", report.characterConsistency),
        Pair("Vehicle Mechanical Consistency (V01)", report.vehicleConsistency),
        Pair("Environment & Lighting Continuity (E01)", report.environmentConsistency),
        Pair("Action & Kinematic Accuracy", report.actionAccuracy),
        Pair("Camera Trajectory Fidelity", report.cameraAccuracy),
        Pair("Motion Vector & Speed Tracking", report.motionAccuracy),
        Pair("Transformation Causality", report.transformationAccuracy),
        Pair("Temporal Shot Timing", report.temporalAccuracy),
        Pair("Visual Artifact Suppression", report.visualArtifacts)
    )

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Overall Score Card
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = StudioCardBg),
            border = androidx.compose.foundation.BorderStroke(1.dp, StudioCyan.copy(alpha = 0.5f)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "Multimodal Continuity Score",
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = StudioTextPrimary
                        )
                    )
                    Text(
                        text = "Automated Quality Control Verification",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = StudioTextSecondary,
                            fontSize = 11.sp
                        )
                    )
                }

                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .clip(CircleShape)
                        .background(StudioVioletPrimary),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "${report.overallContinuity}%",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            fontSize = 18.sp
                        )
                    )
                }
            }
        }

        // Metrics Breakdown List
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = StudioCardBg),
            border = androidx.compose.foundation.BorderStroke(1.dp, StudioBorder),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Fidelity Breakdown",
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = StudioTextPrimary
                    )
                )
                Spacer(modifier = Modifier.height(10.dp))

                metrics.forEach { (name, score) ->
                    val color = when {
                        score >= 90 -> StudioCyan
                        score >= 80 -> StudioAmber
                        else -> Color(0xFFE53935)
                    }

                    Column(modifier = Modifier.padding(vertical = 4.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = name,
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = StudioTextPrimary,
                                    fontSize = 11.sp
                                )
                            )
                            Text(
                                text = "$score%",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = color,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 11.sp
                                )
                            )
                        }
                        Spacer(modifier = Modifier.height(3.dp))
                        LinearProgressIndicator(
                            progress = { score / 100f },
                            color = color,
                            trackColor = StudioCardElevated,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(4.dp)
                                .clip(RoundedCornerShape(2.dp))
                        )
                    }
                }
            }
        }

        // AI Critique & Targeted Fixes
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = StudioCardBg),
            border = androidx.compose.foundation.BorderStroke(1.dp, StudioBorder),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "AI Director Critique & Remediation",
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = StudioTextPrimary
                    )
                )
                Spacer(modifier = Modifier.height(10.dp))

                report.critiqueNotes.forEach { note ->
                    val isWarning = note.contains("Flag", ignoreCase = true) || note.contains("Artifact", ignoreCase = true)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        Icon(
                            imageVector = if (isWarning) Icons.Default.Warning else Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = if (isWarning) StudioAmber else StudioCyan,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = note,
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = if (isWarning) StudioAmber else StudioTextSecondary,
                                fontSize = 11.sp,
                                lineHeight = 15.sp
                            )
                        )
                    }
                }

                if (report.flaggedShotNumbers.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(12.dp))
                    report.flaggedShotNumbers.forEach { shotNum ->
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = Color(0xFF22170E),
                            border = androidx.compose.foundation.BorderStroke(1.dp, StudioAmber.copy(alpha = 0.5f)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "Shot #$shotNum Remediation Available",
                                        style = MaterialTheme.typography.labelMedium.copy(
                                            color = StudioAmber,
                                            fontWeight = FontWeight.Bold
                                        )
                                    )
                                    Button(
                                        onClick = { onApplyAIFix(shotNum) },
                                        colors = ButtonDefaults.buttonColors(containerColor = StudioAmber),
                                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                                        modifier = Modifier.testTag("btn_apply_ai_fix_dash_$shotNum")
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.AutoAwesome,
                                            contentDescription = null,
                                            tint = Color.Black,
                                            modifier = Modifier.size(12.dp)
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text("Apply AI Fix & Patch", color = Color.Black, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                    }
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = report.shotFixSuggestions[shotNum.toString()] ?: "Refine mechanical kinematics and eliminate motion blur.",
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        color = StudioTextPrimary,
                                        fontSize = 11.sp
                                    )
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
