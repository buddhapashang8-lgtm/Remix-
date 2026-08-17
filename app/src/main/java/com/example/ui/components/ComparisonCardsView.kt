package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CompareArrows
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.domain.models.ReferenceAnalysis
import com.example.domain.models.RemixConcept
import com.example.domain.models.RemixControlMode
import com.example.ui.theme.*

@Composable
fun ComparisonCardsView(
    referenceAnalysis: ReferenceAnalysis?,
    remixConcept: RemixConcept
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // High-level Concept Comparison
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = StudioCardBg),
            border = androidx.compose.foundation.BorderStroke(1.dp, StudioBorder),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        imageVector = Icons.Default.CompareArrows,
                        contentDescription = null,
                        tint = StudioCyan,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Reference vs. New Remix Concept",
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = StudioTextPrimary
                        )
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // Left: Reference
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = StudioCardElevated,
                        modifier = Modifier.weight(1f)
                    ) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Text(
                                text = "REFERENCE VIDEO",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = StudioTextSecondary,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 10.sp
                                )
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = referenceAnalysis?.summary ?: "Vintage motorcycle transforms into water hydrofoil on mountain lake.",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = StudioTextPrimary,
                                    fontSize = 11.sp,
                                    lineHeight = 15.sp
                                )
                            )
                        }
                    }

                    // Right: New Concept
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = Color(0xFF142036),
                        border = androidx.compose.foundation.BorderStroke(1.dp, StudioVioletPrimary.copy(alpha = 0.5f)),
                        modifier = Modifier.weight(1f)
                    ) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Text(
                                text = "SYNTHESIZED REMIX",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = StudioVioletLight,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 10.sp
                                )
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = remixConcept.oneLineConcept,
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = StudioTextPrimary,
                                    fontSize = 11.sp,
                                    lineHeight = 15.sp
                                )
                            )
                        }
                    }
                }
            }
        }

        // Structural Diffs & Rationale
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = StudioCardBg),
            border = androidx.compose.foundation.BorderStroke(1.dp, StudioBorder),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Structural Breakdown & Adaptations",
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = StudioTextPrimary
                    )
                )
                Spacer(modifier = Modifier.height(10.dp))

                remixConcept.structuralDiffs.forEach { diff ->
                    val badgeColor = when (diff.mode) {
                        RemixControlMode.PRESERVE -> StudioVioletPrimary
                        RemixControlMode.ADAPT -> StudioCyan
                        RemixControlMode.REPLACE -> StudioAmber
                    }

                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = StudioCardElevated,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                    ) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = diff.category,
                                    style = MaterialTheme.typography.labelMedium.copy(
                                        color = StudioTextPrimary,
                                        fontWeight = FontWeight.Bold
                                    )
                                )
                                Surface(
                                    shape = RoundedCornerShape(4.dp),
                                    color = badgeColor.copy(alpha = 0.2f)
                                ) {
                                    Text(
                                        text = diff.mode.name,
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            color = badgeColor,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 9.sp
                                        ),
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "• Ref: ${diff.referenceElement}",
                                style = MaterialTheme.typography.bodySmall.copy(color = StudioTextSecondary, fontSize = 11.sp)
                            )
                            Text(
                                text = "• New: ${diff.remixedElement}",
                                style = MaterialTheme.typography.bodySmall.copy(color = StudioTextPrimary, fontSize = 11.sp)
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "Rationale: ${diff.rationale}",
                                style = MaterialTheme.typography.labelSmall.copy(color = StudioCyanLight, fontSize = 10.sp)
                            )
                        }
                    }
                }
            }
        }

        // Physical Causality Reasoning Box
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF131D31)),
            border = androidx.compose.foundation.BorderStroke(1.dp, StudioCyan.copy(alpha = 0.4f)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Psychology,
                        contentDescription = null,
                        tint = StudioCyan,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "AI Physical Reasoning & Functional Substitution",
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = StudioCyan
                        )
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = remixConcept.reasoningSummary,
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = StudioTextPrimary,
                        fontSize = 12.sp,
                        lineHeight = 17.sp
                    )
                )
            }
        }
    }
}
