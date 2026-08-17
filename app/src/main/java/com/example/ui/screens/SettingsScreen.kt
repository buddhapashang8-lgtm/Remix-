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
import com.example.ui.StudioViewModel
import com.example.ui.components.StudioHeader
import com.example.ui.theme.*

@Composable
fun SettingsScreen(
    viewModel: StudioViewModel,
    onBack: () -> Unit
) {
    var selectedGeminiModel by remember { mutableStateOf("gemini-2.5-flash") }
    var selectedVideoEngine by remember { mutableStateOf("Neural Latent Synthesizer (Veo 2 API)") }
    var selectedDefaultRatio by remember { mutableStateOf("9:16 (Vertical Short-form)") }
    var causalityStrictness by remember { mutableStateOf(0.85f) }
    var showResetDialog by remember { mutableStateOf(false) }

    Scaffold(
        containerColor = StudioDarkBg,
        topBar = {
            StudioHeader(
                title = "Studio Settings",
                subtitle = "AI Model & Engine Configuration",
                showBackButton = true,
                onBackClick = onBack
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Gemini Multimodal Reasoning Engine Section
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = StudioCardBg),
                    border = androidx.compose.foundation.BorderStroke(1.dp, StudioBorder),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Psychology,
                                contentDescription = null,
                                tint = StudioVioletPrimary,
                                modifier = Modifier.size(22.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Multimodal Video Reasoning Engine",
                                style = MaterialTheme.typography.titleSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = StudioTextPrimary
                                )
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        listOf(
                            Pair("gemini-2.5-flash", "Gemini 2.5 Flash (Ultra-fast multimodal reasoning & temporal segmentation)"),
                            Pair("gemini-2.5-pro", "Gemini 2.5 Pro (Deep complex narrative causality & physics verification)"),
                            Pair("gemini-2.0-flash", "Gemini 2.0 Flash (Lightweight low-latency director)")
                        ).forEach { (modelKey, label) ->
                            val isSelected = selectedGeminiModel == modelKey
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { selectedGeminiModel = modelKey }
                                    .padding(vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                RadioButton(
                                    selected = isSelected,
                                    onClick = { selectedGeminiModel = modelKey }
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Column {
                                    Text(
                                        text = modelKey,
                                        style = MaterialTheme.typography.bodyMedium.copy(
                                            fontWeight = FontWeight.Bold,
                                            color = if (isSelected) StudioCyan else StudioTextPrimary
                                        )
                                    )
                                    Text(
                                        text = label,
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            color = StudioTextSecondary,
                                            fontSize = 10.sp
                                        )
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Physical Causality & Reasoning Rigor
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
                            Text(
                                text = "Physical Causality Rigor",
                                style = MaterialTheme.typography.titleSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = StudioTextPrimary
                                )
                            )
                            Text(
                                text = "${(causalityStrictness * 100).toInt()}%",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = StudioCyan,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                        }

                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Enforces biomechanical and mechanical feasibility in element substitutions (e.g. wheels to hydrofoil ski blades).",
                            style = MaterialTheme.typography.bodySmall.copy(color = StudioTextSecondary, fontSize = 11.sp)
                        )

                        Spacer(modifier = Modifier.height(10.dp))
                        Slider(
                            value = causalityStrictness,
                            onValueChange = { causalityStrictness = it },
                            colors = SliderDefaults.colors(
                                thumbColor = StudioCyan,
                                activeTrackColor = StudioVioletPrimary
                            )
                        )
                    }
                }
            }

            // Video Generation Provider
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = StudioCardBg),
                    border = androidx.compose.foundation.BorderStroke(1.dp, StudioBorder),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Videocam,
                                contentDescription = null,
                                tint = StudioCyan,
                                modifier = Modifier.size(22.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Video Generation Backend",
                                style = MaterialTheme.typography.titleSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = StudioTextPrimary
                                )
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        listOf(
                            "Neural Latent Synthesizer (Veo 2 API)",
                            "Sora Video Transformer Pipeline",
                            "Runway Gen-3 Alpha Cinematic",
                            "Offline High-Fidelity Simulation"
                        ).forEach { engine ->
                            val isSelected = selectedVideoEngine == engine
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { selectedVideoEngine = engine }
                                    .padding(vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                RadioButton(
                                    selected = isSelected,
                                    onClick = { selectedVideoEngine = engine }
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = engine,
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        color = if (isSelected) StudioCyan else StudioTextPrimary,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                    )
                                )
                            }
                        }
                    }
                }
            }

            // About & System Info
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = StudioCardBg),
                    border = androidx.compose.foundation.BorderStroke(1.dp, StudioBorder),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Viral Remix Studio",
                            style = MaterialTheme.typography.titleSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = StudioTextPrimary
                            )
                        )
                        Text(
                            text = "Version 1.0.0 • Google AI Studio Multimodal Architecture",
                            style = MaterialTheme.typography.labelSmall.copy(color = StudioVioletLight, fontSize = 11.sp)
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "Built with Room database local persistence, Jetpack Compose Material 3 dark aesthetics, and multimodal Gemini temporal reasoning.",
                            style = MaterialTheme.typography.bodySmall.copy(color = StudioTextSecondary, fontSize = 11.sp)
                        )
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}
