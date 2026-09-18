package com.example.data.model

enum class ManagementLevel(
    val levelNumber: Int,
    val title: String,
    val subtitle: String,
    val intervalMinutes: Int,
    val breakSeconds: Int,
    val distanceDescription: String,
    val ruleSummary: String,
    val detailedGuidance: String,
    val badge: String
) {
    LEVEL_1(
        levelNumber = 1,
        title = "Level 1: Gentle Pace",
        subtitle = "Light Reading & Casual Use",
        intervalMinutes = 45,
        breakSeconds = 60,
        distanceDescription = "15-20 feet away",
        ruleSummary = "60-second relaxation break every 45 minutes",
        detailedGuidance = "Ideal for casual browsing or light reading. Take 60 seconds every 45 minutes to shift your gaze out a window, stretch your shoulders, and blink gently.",
        badge = "Casual"
    ),
    LEVEL_2(
        levelNumber = 2,
        title = "Level 2: The 20-20-20 Rule",
        subtitle = "Optometry Standard for Eye Strain",
        intervalMinutes = 20,
        breakSeconds = 20,
        distanceDescription = "20 feet (~6 meters) away",
        ruleSummary = "Look 20 feet away for 20 seconds every 20 minutes",
        detailedGuidance = "Clinically recommended by optometrists worldwide. Every 20 minutes of screen use, shift focus to an object at least 20 feet (6 meters) away for 20 seconds. This releases chronic tension in the ciliary muscles of your eyes and dramatically reduces digital fatigue.",
        badge = "Recommended"
    ),
    LEVEL_3(
        levelNumber = 3,
        title = "Level 3: Deep Work Reset",
        subtitle = "Intensive Coding, Design & Study",
        intervalMinutes = 30,
        breakSeconds = 120,
        distanceDescription = "Stand up & look outside",
        ruleSummary = "2-minute movement & eye break every 30 minutes",
        detailedGuidance = "For sustained high-concentration screen sessions. Stand up, rest your eyes on distant greenery or the horizon, hydrate, and roll your neck and wrists.",
        badge = "Intensive"
    ),
    LEVEL_4(
        levelNumber = 4,
        title = "Level 4: Circadian Shield",
        subtitle = "Maximum Relief & Night Screen Guard",
        intervalMinutes = 15,
        breakSeconds = 30,
        distanceDescription = "Far distance + dim ambient lights",
        ruleSummary = "30-second eye rest every 15 minutes with blue-light warnings",
        detailedGuidance = "Designed for late-night screen users or those experiencing dry eyes, blurred vision, or screen-induced headaches. Fast-cycle micro-breaks protect your melatonin production and ocular moisture.",
        badge = "Maximum Shield"
    );

    companion object {
        fun fromLevelNumber(number: Int): ManagementLevel {
            return entries.find { it.levelNumber == number } ?: LEVEL_2
        }
    }
}
