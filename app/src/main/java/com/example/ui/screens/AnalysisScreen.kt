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
import com.example.domain.models.ShotAnalysis
import com.example.ui.StudioViewModel
import com.example.ui.components.*
import com.example.ui.theme.*

@Composable
fun AnalysisScreen(
    viewModel: StudioViewModel,
    onBack: () -> Unit,
    onNavigateToRemix: () -> Unit
) {
    val activeProject by viewModel.activeProject.collectAsState()
    val referenceVideo by viewModel.currentReferenceVideo.collectAsState()
    val analysis by viewModel.currentAnalysis.collectAsState()
    val viralDNA by viewModel.currentViralDNA.collectAsState()

    var selectedShotNumber by remember { mutableStateOf(1) }
    var selectedAnalysisTab by remember { mutableStateOf(0) }
    val tabTitles = listOf("Viral DNA", "Story Arc", "Cinematography", "Audio & Sound")

    Scaffold(
        containerColor = StudioDarkBg,
        topBar = {
            Column {
                StudioHeader(
                    title = activeProject?.name ?: "Viral DNA Analysis",
                    subtitle = "Step 2 of 6: Structural Deconstruction",
                    showBackButton = true,
                    onBackClick = onBack
                )
                StageBreadcrumbs(
                    currentStageIndex = 1,
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
                            text = "Viral DNA Extracted",
                            style = MaterialTheme.typography.labelSmall.copy(color = StudioCyan, fontWeight = FontWeight.Bold)
                        )
                        Text(
                            text = "${analysis?.shots?.size ?: 4} Shots • Ready to Remix",
                            style = MaterialTheme.typography.bodySmall.copy(color = StudioTextPrimary)
                        )
                    }
                    Button(
                        onClick = onNavigateToRemix,
                        colors = ButtonDefaults.buttonColors(containerColor = StudioVioletPrimary),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.testTag("btn_proceed_to_remix")
                    ) {
                        Text("Proceed to Remix", fontWeight = FontWeight.Bold)
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
            // Reference Video Thumbnail & Summary Card
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = StudioCardBg),
                    border = androidx.compose.foundation.BorderStroke(1.dp, StudioBorder),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Analytics,
                                    contentDescription = null,
                                    tint = StudioVioletPrimary,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = referenceVideo?.title ?: "Reference Video Analysis",
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = StudioTextPrimary
                                    )
                                )
                            }
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = StudioVioletPrimary.copy(alpha = 0.2f)
                            ) {
                                Text(
                                    text = "${analysis?.totalDuration ?: 14.5}s Total",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = StudioVioletLight,
                                        fontWeight = FontWeight.Bold
                                    ),
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = analysis?.summary ?: "Video summary extracting high-impact story progression.",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = StudioTextSecondary,
                                fontSize = 12.sp,
                                lineHeight = 16.sp
                            )
                        )
                    }
                }
            }

            // Shot Segmentation Timeline (Horizontal)
            item {
                ShotSegmentationTimeline(
                    shots = analysis?.shots ?: emptyList(),
                    selectedShotNumber = selectedShotNumber,
                    onSelectShot = { selectedShotNumber = it }
                )
            }

            // Selected Shot Deep Inspector
            item {
                val currentShot = analysis?.shots?.find { it.shotNumber == selectedShotNumber }
                    ?: analysis?.shots?.firstOrNull()
                if (currentShot != null) {
                    SelectedShotDetailCard(shot = currentShot)
                }
            }

            // Concept Abstraction: Literal vs Abstract Viral Mechanism
            item {
                ConceptAbstractionCard(
                    literalConcept = viralDNA?.literalConcept ?: "Motorcycle rides across gravel toward a lake and transforms into hydrofoil.",
                    abstractConcept = viralDNA?.abstractConcept ?: "A land vehicle approaches a fatal terrain boundary, surprises viewers by deploying aerodynamic/hydrodynamic adaptations, and masters the new medium."
                )
            }

            // Tabs for deep dive
            item {
                TabRow(
                    selectedTabIndex = selectedAnalysisTab,
                    containerColor = StudioCardBg,
                    contentColor = StudioVioletPrimary,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .border(1.dp, StudioBorder, RoundedCornerShape(12.dp))
                ) {
                    tabTitles.forEachIndexed { index, title ->
                        Tab(
                            selected = selectedAnalysisTab == index,
                            onClick = { selectedAnalysisTab = index },
                            text = {
                                Text(
                                    text = title,
                                    fontSize = 11.sp,
                                    fontWeight = if (selectedAnalysisTab == index) FontWeight.Bold else FontWeight.Normal,
                                    color = if (selectedAnalysisTab == index) StudioCyan else StudioTextSecondary
                                )
                            }
                        )
                    }
                }
            }

            // Tab Content
            when (selectedAnalysisTab) {
                0 -> {
                    // Viral DNA Tab: Hook, Pacing, Core Viral Elements
                    item {
                        ViralHookDnaCard(dna = viralDNA)
                    }
                }
                1 -> {
                    // Story Arc Tab: 8-Stage Flow
                    item {
                        StoryArcFlowView(story = viralDNA?.story ?: com.example.domain.models.StoryDNA())
                    }
                }
                2 -> {
                    // Cinematography Tab: Camera framing, lighting, motion vector
                    item {
                        CinematographyDnaCard(analysis = analysis)
                    }
                }
                3 -> {
                    // Audio & Sound Design Tab: Beat sync, foley, transition rises
                    item {
                        SoundDesignDnaCard(analysis = analysis)
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}

@Composable
fun SelectedShotDetailCard(shot: ShotAnalysis) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = StudioCardElevated),
        border = androidx.compose.foundation.BorderStroke(1.dp, StudioVioletPrimary.copy(alpha = 0.5f)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Shot #${shot.shotNumber} Deep Inspection",
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = StudioCyan
                    )
                )
                Text(
                    text = "${"%.1f".format(shot.startTime)}s - ${"%.1f".format(shot.endTime)}s (${"%.1f".format(shot.duration)}s)",
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = StudioVioletLight,
                        fontWeight = FontWeight.Bold
                    )
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Story Purpose: ${shot.storyPurpose}",
                style = MaterialTheme.typography.bodySmall.copy(
                    color = StudioTextPrimary,
                    fontWeight = FontWeight.SemiBold
                )
            )

            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = shot.description,
                style = MaterialTheme.typography.bodySmall.copy(
                    color = StudioTextSecondary,
                    fontSize = 11.sp
                )
            )

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = StudioCardBg,
                    modifier = Modifier.weight(1f)
                ) {
                    Column(modifier = Modifier.padding(8.dp)) {
                        Text(
                            text = "Camera & Angle",
                            style = MaterialTheme.typography.labelSmall.copy(color = StudioAmber, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                        )
                        Text(
                            text = "${shot.cameraFraming} • ${shot.cameraAngle}",
                            style = MaterialTheme.typography.bodySmall.copy(color = StudioTextPrimary, fontSize = 10.sp)
                        )
                    }
                }

                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = StudioCardBg,
                    modifier = Modifier.weight(1f)
                ) {
                    Column(modifier = Modifier.padding(8.dp)) {
                        Text(
                            text = "Subject Movement",
                            style = MaterialTheme.typography.labelSmall.copy(color = StudioCyan, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                        )
                        Text(
                            text = "${shot.subjectMovement} • ${shot.movementDirection}",
                            style = MaterialTheme.typography.bodySmall.copy(color = StudioTextPrimary, fontSize = 10.sp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ViralHookDnaCard(dna: com.example.domain.models.ViralDNA?) {
    if (dna == null) return
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = StudioCardBg),
        border = androidx.compose.foundation.BorderStroke(1.dp, StudioBorder),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Viral Retention & Hook Mechanism",
                style = MaterialTheme.typography.titleSmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = StudioTextPrimary
                )
            )
            Spacer(modifier = Modifier.height(12.dp))

            Surface(
                shape = RoundedCornerShape(10.dp),
                color = StudioCardElevated,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(
                        text = "HOOK MECHANISM (0.0s - 2.5s)",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = StudioCyan,
                            fontWeight = FontWeight.Bold,
                            fontSize = 10.sp
                        )
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = if (dna.hook.mechanism.isNotEmpty()) dna.hook.mechanism else dna.hook.description,
                        style = MaterialTheme.typography.bodySmall.copy(color = StudioTextPrimary, fontSize = 11.sp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Surface(
                shape = RoundedCornerShape(10.dp),
                color = StudioCardElevated,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(
                        text = "PACING & TENSION PROFILE",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = StudioAmber,
                            fontWeight = FontWeight.Bold,
                            fontSize = 10.sp
                        )
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = if (dna.pacingDNA.segments.isNotEmpty()) {
                            dna.pacingDNA.segments.joinToString(" → ") { "${it.label} (${it.energyLevel})" }
                        } else {
                            "Setup (LOW) → Tension Climb (MED) → Climax (PEAK) → Resolution"
                        },
                        style = MaterialTheme.typography.bodySmall.copy(color = StudioTextPrimary, fontSize = 11.sp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Surface(
                shape = RoundedCornerShape(10.dp),
                color = StudioCardElevated,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(
                        text = "REPEAT WATCHABILITY DRIVER",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = StudioVioletLight,
                            fontWeight = FontWeight.Bold,
                            fontSize = 10.sp
                        )
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = dna.abstractConcept.ifEmpty { "High-speed transformation kinematics and micro-mechanics." },
                        style = MaterialTheme.typography.bodySmall.copy(color = StudioTextPrimary, fontSize = 11.sp)
                    )
                }
            }
        }
    }
}

@Composable
fun CinematographyDnaCard(analysis: com.example.domain.models.ReferenceAnalysis?) {
    if (analysis == null) return
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = StudioCardBg),
        border = androidx.compose.foundation.BorderStroke(1.dp, StudioBorder),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Cinematic Trajectory & Lighting Palette",
                style = MaterialTheme.typography.titleSmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = StudioTextPrimary
                )
            )
            Spacer(modifier = Modifier.height(10.dp))

            analysis.shots.forEach { shot ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Shot #${shot.shotNumber}",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = StudioCyan,
                            fontWeight = FontWeight.Bold
                        )
                    )
                    Text(
                        text = "${shot.cameraMovement} • ${shot.lighting}",
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

@Composable
fun SoundDesignDnaCard(analysis: com.example.domain.models.ReferenceAnalysis?) {
    if (analysis == null) return
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = StudioCardBg),
        border = androidx.compose.foundation.BorderStroke(1.dp, StudioBorder),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Audio & Foley Design Blueprint",
                style = MaterialTheme.typography.titleSmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = StudioTextPrimary
                )
            )
            Spacer(modifier = Modifier.height(10.dp))

            analysis.shots.forEach { shot ->
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = StudioCardElevated,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 3.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Audiotrack,
                            contentDescription = null,
                            tint = StudioVioletLight,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(
                                text = "Shot #${shot.shotNumber} Foley & Audio Sync",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = StudioVioletLight,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                            Text(
                                text = if (shot.audio.isNotEmpty()) shot.audio else shot.soundEffects.joinToString(", ").ifEmpty { "High-frequency engine revs & wind foley" },
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
