package com.example.model

enum class StreamResolution(val label: String, val description: String, val width: Int, val height: Int) {
    RES_720P("720p @ 60 FPS", "Recomendado para Fire Stick Lite (Estável & Sem travamento de RAM)", 1280, 720),
    RES_1080P("1080p @ 60 FPS", "Máxima nitidez (Requer Wi-Fi 5GHz forte)", 1920, 1080),
    RES_AUTO("Automático", "Ajustado automaticamente pelo navegador Boosteroid", 0, 0)
}

enum class AspectMode(val label: String, val scaleCss: String) {
    FIT("16:9 Original (Com barras se necessário)", "object-fit: contain !important;"),
    STRETCH("Esticar Tela Cheia (Sem barras pretas)", "object-fit: fill !important; width: 100vw !important; height: 100vh !important;"),
    ZOOM("Zoom 105% (Preenchimento TV)", "transform: scale(1.05) !important; transform-origin: center center;")
}

enum class UserAgentProfile(val label: String, val userAgentString: String) {
    WINDOWS_CHROME(
        "Windows Chrome (Padrão WebRTC)",
        "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/128.0.0.0 Safari/537.36"
    ),
    CHROME_OS(
        "ChromeOS (Baixo consumo)",
        "Mozilla/5.0 (X11; CrOS x86_64 15329.44.0) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/128.0.0.0 Safari/537.36"
    ),
    ANDROID_TV(
        "Android TV Nativo",
        "Mozilla/5.0 (Linux; Android 11; AFTSSS) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/128.0.0.0 Safari/537.36"
    )
}

data class ServerEndpoint(
    val name: String,
    val region: String,
    val pingHost: String
)

val BOOSTEROID_REGIONS = listOf(
    ServerEndpoint("América do Sul (Brasil)", "São Paulo", "cloud.boosteroid.com"),
    ServerEndpoint("EUA Leste", "Flórida / Virgínia", "cloud.boosteroid.com"),
    ServerEndpoint("EUA Central", "Texas", "cloud.boosteroid.com"),
    ServerEndpoint("Europa Ocidental", "Frankfurt / Paris", "cloud.boosteroid.com")
)

data class BoosteroidSettings(
    val resolution: StreamResolution = StreamResolution.RES_720P, // Default 720p for Fire Stick Lite stability
    val aspectMode: AspectMode = AspectMode.FIT,
    val userAgent: UserAgentProfile = UserAgentProfile.WINDOWS_CHROME,
    val cursorSpeed: Float = 1.5f,
    val fireStickLiteMode: Boolean = true, // Reduces memory usage, limits cache
    val lowLatencyAudio: Boolean = true,
    val showFpsOverlay: Boolean = true,
    val hapticFeedback: Boolean = false
)

data class CloudGameShortcut(
    val id: String,
    val title: String,
    val publisher: String,
    val genre: String,
    val boosteroidSearchQuery: String,
    val bannerColorHex: Long
)

val POPULAR_BOOSTEROID_GAMES = listOf(
    CloudGameShortcut("gta5", "Grand Theft Auto V", "Rockstar Games", "Ação / Mundo Aberto", "Grand Theft Auto V", 0xFFE11D48),
    CloudGameShortcut("cyberpunk", "Cyberpunk 2077", "CD PROJEKT RED", "RPG / Sci-Fi", "Cyberpunk 2077", 0xFF00F2FE),
    CloudGameShortcut("eafc24", "EA SPORTS FC 24", "Electronic Arts", "Esportes / Futebol", "EA SPORTS FC 24", 0xFF10B981),
    CloudGameShortcut("hogwarts", "Hogwarts Legacy", "Warner Bros.", "Aventura / RPG", "Hogwarts Legacy", 0xFF8B5CF6),
    CloudGameShortcut("rdr2", "Red Dead Redemption 2", "Rockstar Games", "Faroeste / Ação", "Red Dead Redemption 2", 0xFFFF6B00),
    CloudGameShortcut("fortnite", "Fortnite", "Epic Games", "Battle Royale", "Fortnite", 0xFF3B82F6),
    CloudGameShortcut("cs2", "Counter-Strike 2", "Valve", "FPS Tático", "Counter-Strike 2", 0xFFF59E0B),
    CloudGameShortcut("rocket", "Rocket League", "Psyonix", "Competitivo", "Rocket League", 0xFF06B6D4)
)
