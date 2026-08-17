package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.ui.StudioViewModel
import com.example.ui.screens.*
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.theme.StudioDarkBg

class MainActivity : ComponentActivity() {
    private val viewModel: StudioViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                ViralRemixStudioApp(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun ViralRemixStudioApp(viewModel: StudioViewModel) {
    val navController = rememberNavController()
    val snackbarHostState = remember { SnackbarHostState() }
    val notificationMessage by viewModel.notificationMessage.collectAsState()

    LaunchedEffect(notificationMessage) {
        notificationMessage?.let { msg ->
            snackbarHostState.showSnackbar(msg)
            viewModel.clearNotification()
        }
    }

    Scaffold(
        containerColor = StudioDarkBg,
        snackbarHost = { SnackbarHost(snackbarHostState) },
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "home",
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            composable("home") {
                HomeScreen(
                    viewModel = viewModel,
                    onNavigateToUpload = { navController.navigate("upload") },
                    onNavigateToProject = { projectId, stage ->
                        viewModel.selectProject(projectId)
                        when (stage) {
                            com.example.data.models.ProjectStage.UPLOAD -> navController.navigate("upload")
                            com.example.data.models.ProjectStage.ANALYZED -> navController.navigate("analysis")
                            com.example.data.models.ProjectStage.REMIX_DRAFT,
                            com.example.data.models.ProjectStage.COMPARISON -> navController.navigate("remix")
                            com.example.data.models.ProjectStage.STORYBOARD,
                            com.example.data.models.ProjectStage.GENERATING -> navController.navigate("storyboard")
                            com.example.data.models.ProjectStage.COMPLETED -> navController.navigate("export")
                        }
                    },
                    onNavigateToSettings = { navController.navigate("settings") }
                )
            }

            composable("upload") {
                UploadScreen(
                    viewModel = viewModel,
                    onBack = { navController.popBackStack() },
                    onNavigateToAnalysis = { navController.navigate("analysis") }
                )
            }

            composable("analysis") {
                AnalysisScreen(
                    viewModel = viewModel,
                    onBack = { navController.popBackStack() },
                    onNavigateToRemix = { navController.navigate("remix") }
                )
            }

            composable("remix") {
                RemixScreen(
                    viewModel = viewModel,
                    onBack = { navController.popBackStack() },
                    onNavigateToStoryboard = { navController.navigate("storyboard") }
                )
            }

            composable("storyboard") {
                StoryboardScreen(
                    viewModel = viewModel,
                    onBack = { navController.popBackStack() },
                    onNavigateToQuality = { navController.navigate("quality") }
                )
            }

            composable("quality") {
                QualityScreen(
                    viewModel = viewModel,
                    onBack = { navController.popBackStack() },
                    onNavigateToExport = { navController.navigate("export") }
                )
            }

            composable("export") {
                ExportScreen(
                    viewModel = viewModel,
                    onBack = { navController.popBackStack() },
                    onNavigateToHome = {
                        navController.navigate("home") {
                            popUpTo("home") { inclusive = true }
                        }
                    }
                )
            }

            composable("settings") {
                SettingsScreen(
                    viewModel = viewModel,
                    onBack = { navController.popBackStack() }
                )
            }
        }
    }
}

