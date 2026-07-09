# StudioBooster ProGuard Rules

# Mantém as fontes
-keep class *.ttf

# Mantém as funções de boost
-keep class functions.** { *; }
-keep class boosternet.** { *; }
-keep class ui.** { *; }

# Mantém Compose
-keep class androidx.compose.** { *; }
