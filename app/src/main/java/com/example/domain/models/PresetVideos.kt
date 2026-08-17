package com.example.domain.models

object PresetVideos {
    val samples = listOf(
        ReferenceVideo(
            uri = "preset://motorcycle_hydrofoil_lake",
            title = "Vintage Motorcycle Shoreline to Lake Hydrofoil",
            durationSeconds = 15.0,
            resolution = "1080x1920",
            aspectRatio = "9:16",
            fileSizeFormatted = "24.6 MB",
            isPreset = true,
            description = "A stylish elderly man rides a motorcycle to a rocky lake shoreline; the machine mechanically unfolds hydrofoil struts and marine turbines, allowing him to carve across the water at high speed into the sunset."
        ),
        ReferenceVideo(
            uri = "preset://rooftop_parkour_glider",
            title = "Cyberpunk Rooftop Sprinter to Jet-Wing Soar",
            durationSeconds = 15.0,
            resolution = "1080x1920",
            aspectRatio = "9:16",
            fileSizeFormatted = "31.2 MB",
            isPreset = true,
            description = "A parkour courier sprints across neon rooftops, leaps off a 100-story ledge into empty space, and deploys a compact backpack into a rigid carbon jet-wing harness, soaring through skyscraper canyons."
        ),
        ReferenceVideo(
            uri = "preset://desert_scrambler_hover",
            title = "Desert Nomad Dune Skimmer Metamorphosis",
            durationSeconds = 15.0,
            resolution = "1080x1920",
            aspectRatio = "9:16",
            fileSizeFormatted = "19.8 MB",
            isPreset = true,
            description = "A desert nomad rides a heavy scrambler into shifting quicksand dunes, triggering ground-effect ceramic repulsors to hover and glide across razor-sharp sand slipfaces."
        )
    )
}
