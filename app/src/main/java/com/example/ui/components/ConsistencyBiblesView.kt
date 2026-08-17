package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import com.example.domain.models.CharacterBible
import com.example.domain.models.EnvironmentBible
import com.example.domain.models.ObjectBible
import com.example.domain.models.ReferenceFrame
import com.example.ui.theme.*

@Composable
fun ConsistencyBiblesTabs(
    characterBible: CharacterBible?,
    objectBible: ObjectBible?,
    environmentBible: EnvironmentBible?,
    referenceFrames: List<ReferenceFrame>,
    onToggleCharLock: () -> Unit = {},
    onToggleObjLock: () -> Unit = {},
    onToggleEnvLock: () -> Unit = {},
    onToggleFrameLock: (String) -> Unit = {}
) {
    var selectedTab by remember { mutableStateOf(0) }
    val tabTitles = listOf("Character", "Vehicle/Object", "Environment", "Ref Frames")

    Column(modifier = Modifier.fillMaxWidth()) {
        // Tab Header
        TabRow(
            selectedTabIndex = selectedTab,
            containerColor = StudioCardBg,
            contentColor = StudioVioletPrimary,
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .border(1.dp, StudioBorder, RoundedCornerShape(12.dp))
        ) {
            tabTitles.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTab == index,
                    onClick = { selectedTab = index },
                    text = {
                        Text(
                            text = title,
                            fontSize = 11.sp,
                            fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Normal,
                            color = if (selectedTab == index) StudioCyan else StudioTextSecondary
                        )
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        when (selectedTab) {
            0 -> CharacterBibleCard(characterBible, onToggleCharLock)
            1 -> ObjectBibleCard(objectBible, onToggleObjLock)
            2 -> EnvironmentBibleCard(environmentBible, onToggleEnvLock)
            3 -> ReferenceFramesList(referenceFrames, onToggleFrameLock)
        }
    }
}

@Composable
fun CharacterBibleCard(
    bible: CharacterBible?,
    onToggleLock: () -> Unit
) {
    if (bible == null) return
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
                        imageVector = Icons.Default.Person,
                        contentDescription = null,
                        tint = StudioVioletPrimary,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Character Bible (${bible.id})",
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = StudioTextPrimary
                        )
                    )
                }

                IconButton(
                    onClick = onToggleLock,
                    modifier = Modifier.testTag("btn_lock_character")
                ) {
                    Icon(
                        imageVector = if (bible.isLocked) Icons.Default.Lock else Icons.Default.LockOpen,
                        contentDescription = "Lock Character",
                        tint = if (bible.isLocked) StudioCyan else StudioTextSecondary,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            BibleField("Role & Age", "${bible.role} (${bible.ageRange})")
            BibleField("Appearance & Face", "${bible.appearance}. ${bible.face}")
            BibleField("Wardrobe (Locked)", bible.wardrobe)
            BibleField("Footwear & Gear", "${bible.footwear}, ${bible.accessories}")

            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Continuity Rules:",
                style = MaterialTheme.typography.labelSmall.copy(color = StudioCyan, fontWeight = FontWeight.Bold)
            )
            bible.continuityRules.forEach { rule ->
                Text(
                    text = "• $rule",
                    style = MaterialTheme.typography.bodySmall.copy(color = StudioTextSecondary, fontSize = 11.sp)
                )
            }
        }
    }
}

@Composable
fun ObjectBibleCard(
    bible: ObjectBible?,
    onToggleLock: () -> Unit
) {
    if (bible == null) return
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
                        imageVector = Icons.Default.DirectionsCar,
                        contentDescription = null,
                        tint = StudioCyan,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Object / Vehicle Bible (${bible.id})",
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = StudioTextPrimary
                        )
                    )
                }

                IconButton(
                    onClick = onToggleLock,
                    modifier = Modifier.testTag("btn_lock_object")
                ) {
                    Icon(
                        imageVector = if (bible.isLocked) Icons.Default.Lock else Icons.Default.LockOpen,
                        contentDescription = "Lock Object",
                        tint = if (bible.isLocked) StudioCyan else StudioTextSecondary,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            BibleField("Vehicle Name", bible.name)
            BibleField("State A (Initial Vehicle)", bible.stateA)

            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = "5-Step Mechanical Metamorphosis Progression:",
                style = MaterialTheme.typography.labelSmall.copy(color = StudioVioletLight, fontWeight = FontWeight.Bold)
            )
            bible.transformationSteps.forEach { step ->
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = StudioCardElevated,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 2.dp)
                ) {
                    Text(
                        text = step,
                        style = MaterialTheme.typography.bodySmall.copy(color = StudioTextPrimary, fontSize = 11.sp),
                        modifier = Modifier.padding(8.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))
            BibleField("State B (Transformed Vehicle)", bible.stateB)
        }
    }
}

@Composable
fun EnvironmentBibleCard(
    bible: EnvironmentBible?,
    onToggleLock: () -> Unit
) {
    if (bible == null) return
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
                        imageVector = Icons.Default.Landscape,
                        contentDescription = null,
                        tint = StudioAmber,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Environment Bible (${bible.id})",
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = StudioTextPrimary
                        )
                    )
                }

                IconButton(
                    onClick = onToggleLock,
                    modifier = Modifier.testTag("btn_lock_environment")
                ) {
                    Icon(
                        imageVector = if (bible.isLocked) Icons.Default.Lock else Icons.Default.LockOpen,
                        contentDescription = "Lock Environment",
                        tint = if (bible.isLocked) StudioCyan else StudioTextSecondary,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            BibleField("Location", bible.location)
            BibleField("Terrain", bible.terrain)
            BibleField("Weather & Lighting", "${bible.weather} • ${bible.lightingDirection}")
            BibleField("Atmosphere", bible.atmosphere)

            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = "Background Landmarks (Anchors):",
                style = MaterialTheme.typography.labelSmall.copy(color = StudioCyan, fontWeight = FontWeight.Bold)
            )
            bible.backgroundLandmarks.forEach { landmark ->
                Text(
                    text = "• $landmark",
                    style = MaterialTheme.typography.bodySmall.copy(color = StudioTextSecondary, fontSize = 11.sp)
                )
            }
        }
    }
}

@Composable
fun ReferenceFramesList(
    frames: List<ReferenceFrame>,
    onToggleLock: (String) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        frames.forEach { frame ->
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = StudioCardBg),
                border = androidx.compose.foundation.BorderStroke(1.dp, StudioBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = frame.title,
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = StudioTextPrimary
                            )
                        )
                        IconButton(
                            onClick = { onToggleLock(frame.id) },
                            modifier = Modifier.size(28.dp)
                        ) {
                            Icon(
                                imageVector = if (frame.isLocked) Icons.Default.Lock else Icons.Default.LockOpen,
                                contentDescription = "Lock Frame",
                                tint = if (frame.isLocked) StudioCyan else StudioTextSecondary,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                    Text(
                        text = frame.description,
                        style = MaterialTheme.typography.bodySmall.copy(color = StudioTextSecondary, fontSize = 11.sp)
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = StudioCardElevated,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = frame.prompt,
                            style = MaterialTheme.typography.labelSmall.copy(color = StudioVioletLight, fontSize = 10.sp),
                            modifier = Modifier.padding(8.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun BibleField(label: String, value: String) {
    Column(modifier = Modifier.padding(vertical = 3.dp)) {
        Text(
            text = "$label:",
            style = MaterialTheme.typography.labelSmall.copy(color = StudioTextSecondary, fontSize = 10.sp)
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodySmall.copy(color = StudioTextPrimary, fontSize = 11.sp)
        )
    }
}
