package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.SwapHoriz
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
import com.example.domain.models.RemixSettings
import com.example.ui.theme.*

@Composable
fun RemixPromptSection(
    userPrompt: String,
    onPromptChange: (String) -> Unit,
    onQuickPromptSelected: (String) -> Unit
) {
    val quickSuggestions = listOf(
        "Alpine glacier with snow-bike transformation",
        "Cyberpunk rooftop jump to jet-glider flight",
        "Desert canyon to quicksand hovercraft",
        "Surprise Me (AI Physical Reasoning)"
    )

    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "What do you want to change?",
            style = MaterialTheme.typography.titleSmall.copy(
                fontWeight = FontWeight.Bold,
                color = StudioTextPrimary
            )
        )
        Text(
            text = "Describe your new concept, environment, or vehicle adaptation",
            style = MaterialTheme.typography.bodySmall.copy(
                color = StudioTextSecondary,
                fontSize = 11.sp
            )
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Quick Suggestion Chips
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            quickSuggestions.forEach { suggestion ->
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = StudioCardElevated,
                    border = androidx.compose.foundation.BorderStroke(1.dp, StudioBorderLight),
                    modifier = Modifier
                        .clickable { onQuickPromptSelected(suggestion) }
                        .testTag("chip_${suggestion.take(10)}")
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = null,
                            tint = StudioCyan,
                            modifier = Modifier.size(12.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = suggestion,
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = StudioTextPrimary,
                                fontSize = 11.sp
                            )
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Multi-line Prompt Input Field
        OutlinedTextField(
            value = userPrompt,
            onValueChange = onPromptChange,
            placeholder = {
                Text(
                    text = "e.g. Change lake into a snowy mountain glacier, and turn the motorcycle into an aggressive tracked snow-bike.",
                    color = StudioTextMuted,
                    fontSize = 13.sp
                )
            },
            minLines = 3,
            maxLines = 5,
            modifier = Modifier
                .fillMaxWidth()
                .testTag("input_remix_prompt"),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = StudioCardBg,
                unfocusedContainerColor = StudioCardBg,
                focusedBorderColor = StudioVioletPrimary,
                unfocusedBorderColor = StudioBorder,
                focusedTextColor = StudioTextPrimary,
                unfocusedTextColor = StudioTextPrimary
            )
        )
    }
}

@Composable
fun DimensionControlsMatrix(
    controls: Map<String, RemixControlMode>,
    onControlChange: (String, RemixControlMode) -> Unit,
    onApplyBatch: (RemixControlMode) -> Unit
) {
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
                Column {
                    Text(
                        text = "Filmmaking Dimension Controls",
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = StudioTextPrimary
                        )
                    )
                    Text(
                        text = "Explicit Preserve / Adapt / Replace Modes",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = StudioTextSecondary,
                            fontSize = 11.sp
                        )
                    )
                }

                // Batch Actions
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    TextButton(
                        onClick = { onApplyBatch(RemixControlMode.PRESERVE) },
                        contentPadding = PaddingValues(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text("All Preserve", fontSize = 10.sp, color = StudioCyan)
                    }
                    TextButton(
                        onClick = { onApplyBatch(RemixControlMode.REPLACE) },
                        contentPadding = PaddingValues(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text("All Replace", fontSize = 10.sp, color = StudioAmber)
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            controls.entries.chunked(1).forEach { group ->
                val entry = group.first()
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = entry.key,
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = StudioTextPrimary,
                            fontWeight = FontWeight.Medium,
                            fontSize = 12.sp
                        ),
                        modifier = Modifier.weight(1f)
                    )

                    // 3-Way Segmented Control
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = StudioCardElevated,
                        modifier = Modifier.padding(start = 8.dp)
                    ) {
                        Row(modifier = Modifier.padding(2.dp)) {
                            listOf(
                                Pair(RemixControlMode.PRESERVE, "Preserve"),
                                Pair(RemixControlMode.ADAPT, "Adapt"),
                                Pair(RemixControlMode.REPLACE, "Replace")
                            ).forEach { (mode, label) ->
                                val isSelected = entry.value == mode
                                val activeBg = when (mode) {
                                    RemixControlMode.PRESERVE -> StudioVioletPrimary
                                    RemixControlMode.ADAPT -> StudioCyan.copy(alpha = 0.85f)
                                    RemixControlMode.REPLACE -> StudioAmber
                                }
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(6.dp))
                                        .background(if (isSelected) activeBg else Color.Transparent)
                                        .clickable { onControlChange(entry.key, mode) }
                                        .padding(horizontal = 8.dp, vertical = 4.dp)
                                ) {
                                    Text(
                                        text = label,
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            color = if (isSelected) Color.White else StudioTextMuted,
                                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                            fontSize = 10.sp
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
}
