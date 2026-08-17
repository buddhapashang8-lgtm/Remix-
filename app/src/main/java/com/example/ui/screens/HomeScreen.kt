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
import com.example.data.models.ProjectEntity
import com.example.data.models.ProjectStage
import com.example.domain.models.PresetVideos
import com.example.domain.models.ReferenceVideo
import com.example.ui.StudioViewModel
import com.example.ui.components.StudioHeader
import com.example.ui.theme.*

@Composable
fun HomeScreen(
    viewModel: StudioViewModel,
    onNavigateToUpload: () -> Unit,
    onNavigateToProject: (String, ProjectStage) -> Unit,
    onNavigateToSettings: () -> Unit
) {
    val projects by viewModel.allProjects.collectAsState()
    var selectedFilterTab by remember { mutableStateOf(0) }
    val filterTabs = listOf("All Projects", "In Progress", "Completed")
    var showNewProjectDialog by remember { mutableStateOf(false) }
    var newProjectName by remember { mutableStateOf("") }
    var selectedPresetForNew by remember { mutableStateOf<ReferenceVideo?>(PresetVideos.samples.first()) }

    val filteredProjects = when (selectedFilterTab) {
        1 -> projects.filter { it.stage != ProjectStage.COMPLETED }
        2 -> projects.filter { it.stage == ProjectStage.COMPLETED }
        else -> projects
    }

    Scaffold(
        containerColor = StudioDarkBg,
        topBar = {
            StudioHeader(
                title = "Viral Remix Studio",
                subtitle = "AI Video Structural Director",
                actions = {
                    IconButton(
                        onClick = onNavigateToSettings,
                        modifier = Modifier
                            .testTag("btn_settings")
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(StudioCardBg)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = "Settings",
                            tint = StudioTextSecondary,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    newProjectName = "Viral Remix #${projects.size + 1}"
                    showNewProjectDialog = true
                },
                containerColor = StudioVioletPrimary,
                contentColor = Color.White,
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.testTag("fab_new_project")
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = "New Project")
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(text = "New Remix", fontWeight = FontWeight.Bold)
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
            // Quick Start Hero Card
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF161530)),
                    border = androidx.compose.foundation.BorderStroke(1.dp, StudioVioletPrimary.copy(alpha = 0.4f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.AutoAwesome,
                                contentDescription = null,
                                tint = StudioCyan,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Analyze Idea. Remix Concept. Create New.",
                                style = MaterialTheme.typography.titleSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = StudioTextPrimary
                                )
                            )
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Extract the story architecture, camera trajectories, pacing, and viral DNA from any reference video — then synthesize coherent original creations.",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = StudioTextSecondary,
                                fontSize = 12.sp,
                                lineHeight = 16.sp
                            )
                        )
                    }
                }
            }

            // Quick Start Viral Templates Section
            item {
                Column {
                    Text(
                        text = "Quick Start Viral Templates",
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = StudioTextPrimary
                        )
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    PresetVideos.samples.forEach { preset ->
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = StudioCardBg,
                            border = androidx.compose.foundation.BorderStroke(1.dp, StudioBorder),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                                .clickable {
                                    viewModel.createProject(preset.title, preset) { pid ->
                                        onNavigateToUpload()
                                    }
                                }
                                .testTag("template_${preset.title.take(8)}")
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(40.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(StudioCardElevated),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.PlayCircle,
                                        contentDescription = null,
                                        tint = StudioVioletPrimary,
                                        modifier = Modifier.size(24.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = preset.title,
                                        style = MaterialTheme.typography.bodyMedium.copy(
                                            fontWeight = FontWeight.Bold,
                                            color = StudioTextPrimary
                                        )
                                    )
                                    Text(
                                        text = "${preset.durationSeconds}s • ${preset.resolution} • ${preset.aspectRatio}",
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            color = StudioTextSecondary,
                                            fontSize = 11.sp
                                        )
                                    )
                                }
                                Icon(
                                    imageVector = Icons.Default.ChevronRight,
                                    contentDescription = null,
                                    tint = StudioTextMuted
                                )
                            }
                        }
                    }
                }
            }

            // Projects List Section
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Projects (${projects.size})",
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = StudioTextPrimary
                        )
                    )

                    // Filter tabs
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = StudioCardBg
                    ) {
                        Row(modifier = Modifier.padding(2.dp)) {
                            filterTabs.forEachIndexed { index, tab ->
                                val isSelected = selectedFilterTab == index
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(6.dp))
                                        .background(if (isSelected) StudioVioletPrimary else Color.Transparent)
                                        .clickable { selectedFilterTab = index }
                                        .padding(horizontal = 8.dp, vertical = 4.dp)
                                ) {
                                    Text(
                                        text = tab,
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            color = if (isSelected) Color.White else StudioTextSecondary,
                                            fontSize = 10.sp
                                        )
                                    )
                                }
                            }
                        }
                    }
                }
            }

            if (filteredProjects.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 32.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                imageVector = Icons.Default.FolderOpen,
                                contentDescription = null,
                                tint = StudioTextMuted,
                                modifier = Modifier.size(48.dp)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "No projects in this category yet.",
                                style = MaterialTheme.typography.bodyMedium.copy(color = StudioTextSecondary)
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Tap '+ New Remix' to start your first creation.",
                                style = MaterialTheme.typography.labelSmall.copy(color = StudioVioletLight)
                            )
                        }
                    }
                }
            } else {
                items(filteredProjects) { project ->
                    ProjectCardItem(
                        project = project,
                        onClick = {
                            viewModel.selectProject(project.id)
                            onNavigateToProject(project.id, project.stage)
                        },
                        onDuplicate = {
                            viewModel.duplicateActiveProject { _ -> }
                        },
                        onDelete = {
                            viewModel.deleteProject(project.id)
                        }
                    )
                }
            }

            item {
                Spacer(modifier = Modifier.height(72.dp))
            }
        }
    }

    if (showNewProjectDialog) {
        AlertDialog(
            onDismissRequest = { showNewProjectDialog = false },
            title = {
                Text(text = "Create New Viral Remix Project", color = StudioTextPrimary)
            },
            text = {
                Column {
                    OutlinedTextField(
                        value = newProjectName,
                        onValueChange = { newProjectName = it },
                        label = { Text("Project Name") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("input_new_project_name")
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "Choose Reference Template:",
                        style = MaterialTheme.typography.labelSmall.copy(color = StudioTextSecondary)
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    PresetVideos.samples.forEach { sample ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { selectedPresetForNew = sample }
                                .padding(vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = selectedPresetForNew?.uri == sample.uri,
                                onClick = { selectedPresetForNew = sample }
                            )
                            Text(
                                text = sample.title,
                                style = MaterialTheme.typography.bodySmall.copy(color = StudioTextPrimary),
                                maxLines = 1
                            )
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        showNewProjectDialog = false
                        viewModel.createProject(newProjectName, selectedPresetForNew) { _ ->
                            onNavigateToUpload()
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = StudioVioletPrimary),
                    modifier = Modifier.testTag("btn_confirm_create_project")
                ) {
                    Text("Create Project")
                }
            },
            dismissButton = {
                TextButton(onClick = { showNewProjectDialog = false }) {
                    Text("Cancel", color = StudioTextSecondary)
                }
            },
            containerColor = StudioCardBg
        )
    }
}

@Composable
fun ProjectCardItem(
    project: ProjectEntity,
    onClick: () -> Unit,
    onDuplicate: () -> Unit,
    onDelete: () -> Unit
) {
    var showMenu by remember { mutableStateOf(false) }

    Card(
        onClick = onClick,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = StudioCardBg),
        border = androidx.compose.foundation.BorderStroke(1.dp, StudioBorder),
        modifier = Modifier
            .fillMaxWidth()
            .testTag("project_card_${project.id}")
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = project.name,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = StudioTextPrimary
                        ),
                        maxLines = 1
                    )
                    Text(
                        text = "Stage: ${project.stage.name} • ${"%.1f".format(project.durationSeconds)}s • ${project.shotCount} shots",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = StudioCyan,
                            fontSize = 11.sp
                        )
                    )
                }

                Box {
                    IconButton(onClick = { showMenu = true }) {
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = "Options",
                            tint = StudioTextSecondary
                        )
                    }
                    DropdownMenu(
                        expanded = showMenu,
                        onDismissRequest = { showMenu = false },
                        modifier = Modifier.background(StudioCardElevated)
                    ) {
                        DropdownMenuItem(
                            text = { Text("Open Project", color = StudioTextPrimary) },
                            onClick = {
                                showMenu = false
                                onClick()
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Delete Project", color = Color(0xFFE53935)) },
                            onClick = {
                                showMenu = false
                                onDelete()
                            }
                        )
                    }
                }
            }
        }
    }
}
