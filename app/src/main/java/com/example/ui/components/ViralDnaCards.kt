package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
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
import com.example.domain.models.*
import com.example.ui.theme.*

@Composable
fun ShotSegmentationTimeline(
    shots: List<ShotAnalysis>,
    selectedShotNumber: Int = 1,
    onSelectShot: (Int) -> Unit = {}
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Shot & Event Segmentation (${shots.size} Shots)",
                style = MaterialTheme.typography.titleSmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = StudioTextPrimary
                )
            )
            Text(
                text = "Total: ${"%.1f".format(shots.sumOf { it.duration })}s",
                style = MaterialTheme.typography.labelSmall.copy(color = StudioCyan)
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            shots.forEach { shot ->
                val isSelected = shot.shotNumber == selectedShotNumber
                Card(
                    onClick = { onSelectShot(shot.shotNumber) },
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isSelected) StudioCardElevated else StudioCardBg
                    ),
                    border = androidx.compose.foundation.BorderStroke(
                        1.dp,
                        if (isSelected) StudioVioletPrimary else StudioBorder
                    ),
                    modifier = Modifier
                        .width(180.dp)
                        .testTag("shot_timeline_card_${shot.shotNumber}")
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = if (isSelected) StudioVioletPrimary else StudioCardBg
                            ) {
                                Text(
                                    text = "Shot #${shot.shotNumber}",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = Color.White,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 11.sp
                                    ),
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                            Text(
                                text = "${"%.1f".format(shot.startTime)}s - ${"%.1f".format(shot.endTime)}s",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = StudioCyan,
                                    fontSize = 10.sp
                                )
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = shot.storyPurpose,
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = StudioTextPrimary,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 12.sp
                            ),
                            maxLines = 2
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = shot.cameraFraming,
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = StudioTextSecondary,
                                fontSize = 10.sp
                            ),
                            maxLines = 1
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ConceptAbstractionCard(
    literalConcept: String,
    abstractConcept: String
) {
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
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(StudioVioletPrimary.copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Lightbulb,
                        contentDescription = null,
                        tint = StudioVioletPrimary,
                        modifier = Modifier.size(18.dp)
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(
                        text = "Concept Understanding",
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = StudioTextPrimary
                        )
                    )
                    Text(
                        text = "Literal vs Abstract Reusable Mechanism",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = StudioTextSecondary,
                            fontSize = 11.sp
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Literal Card
            Surface(
                shape = RoundedCornerShape(10.dp),
                color = StudioCardElevated,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(
                        text = "LITERAL (What visibly happens):",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = StudioAmber,
                            fontWeight = FontWeight.Bold,
                            fontSize = 10.sp
                        )
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = literalConcept,
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = StudioTextPrimary,
                            lineHeight = 16.sp
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Abstract Mechanism Card
            Surface(
                shape = RoundedCornerShape(10.dp),
                color = Color(0xFF131D31),
                border = androidx.compose.foundation.BorderStroke(1.dp, StudioCyan.copy(alpha = 0.3f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(
                        text = "ABSTRACT VIRAL DNA (Reusable Mechanism):",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = StudioCyan,
                            fontWeight = FontWeight.Bold,
                            fontSize = 10.sp
                        )
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = abstractConcept,
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = StudioTextPrimary,
                            lineHeight = 16.sp
                        )
                    )
                }
            }
        }
    }
}

@Composable
fun StoryArcFlowView(story: StoryDNA) {
    val steps = listOf(
        Pair("1. HOOK", story.setup.ifBlank { "High-energy approach establishing confident velocity" }),
        Pair("2. SETUP", story.setup.ifBlank { "Cruising on land vehicle toward natural boundary" }),
        Pair("3. PROBLEM", story.problem.ifBlank { "Terrain ends abruptly into deep incompatible medium" }),
        Pair("4. ANTICIPATION", story.anticipation.ifBlank { "Protagonist maintains throttle with zero braking" }),
        Pair("5. SURPRISE", story.surprise.ifBlank { "Pneumatic latches fire rather than crashing" }),
        Pair("6. REVEAL", story.reveal.ifBlank { "Mechanical linkages deploy into new terrain form" }),
        Pair("7. PROOF", story.proof.ifBlank { "Vehicle glides effortlessly above the obstacle" }),
        Pair("8. PAYOFF", story.payoff.ifBlank { "Triumphant high-speed carving into sunset" })
    )

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = StudioCardBg),
        border = androidx.compose.foundation.BorderStroke(1.dp, StudioBorder),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "8-Stage Story Progression Arc",
                style = MaterialTheme.typography.titleSmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = StudioTextPrimary
                )
            )
            Spacer(modifier = Modifier.height(12.dp))

            steps.forEachIndexed { index, pair ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Box(
                        modifier = Modifier
                            .size(20.dp)
                            .clip(CircleShape)
                            .background(if (index in 4..6) StudioVioletPrimary else StudioCardElevated),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "${index + 1}",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 10.sp
                            )
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = pair.first,
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = if (index in 4..6) StudioCyan else StudioTextSecondary,
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp
                            )
                        )
                        Text(
                            text = pair.second,
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = StudioTextPrimary,
                                fontSize = 12.sp
                            )
                        )
                    }
                }
            }
        }
    }
}
