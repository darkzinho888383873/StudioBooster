// ui/ui.kt
package ui

import android.os.Build
import androidx.compose.foundation.background
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// ==========================================
// TEMAS
// ==========================================

enum class ThemeMode { WHITE, BLACK }

object AppColors {
    // WHITE
    val whiteBg = Color(0xFFF8FAFC)
    val whiteSurface = Color(0xFFFFFFFF)
    val whiteCard = Color(0xFFF1F5F9)
    val whiteText = Color(0xFF1E293B)
    val whiteSubtext = Color(0xFF64748B)
    val whiteAccent = Color(0xFF7C3AED)
    
    // BLACK
    val blackBg = Color(0xFF0A0A14)
    val blackSurface = Color(0xFF141428)
    val blackCard = Color(0xFF1A1A2E)
    val blackText = Color(0xFFF0F0FF)
    val blackSubtext = Color(0xFF9090B0)
    val blackAccent = Color(0xFF8B5CF6)
    
    @Composable
    fun get(theme: ThemeMode) = when(theme) {
        ThemeMode.WHITE -> Scheme(whiteBg, whiteSurface, whiteCard, whiteText, whiteSubtext, whiteAccent)
        ThemeMode.BLACK -> Scheme(blackBg, blackSurface, blackCard, blackText, blackSubtext, blackAccent)
    }
}

data class Scheme(
    val bg: Color,
    val surface: Color,
    val card: Color,
    val text: Color,
    val subtext: Color,
    val accent: Color
)

// Fontes
val MinecraftFont = FontFamily(Font(R.font.minecraft))
val ComicSansFont = FontFamily(Font(R.font.comic_sans))

// ==========================================
// TELA PRINCIPAL
// ==========================================

@Composable
fun HomeScreen(theme: ThemeMode) {
    val c = AppColors.get(theme)
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(c.bg)
            .padding(20.dp)
    ) {
        // Header
        Text(
            text = "StudioBooster",
            fontFamily = MinecraftFont,
            fontSize = 32.sp,
            color = c.accent
        )
        
        Text(
            text = "One UI ${if(Build.VERSION.SDK_INT >= 35) "7.0" else "6.0"}",
            fontFamily = ComicSansFont,
            fontSize = 14.sp,
            color = c.subtext
        )
        
        Spacer(modifier = Modifier.height(30.dp))
        
        // Cards de status
        StatusCard("RAM Livre", "4.2 GB", c)
        StatusCard("Temperatura", "36°C", c)
        StatusCard("Apps Otimizados", "12", c)
        
        Spacer(modifier = Modifier.weight(1f))
        
        // Botão principal
        BoosterButton(
            text = "OTIMIZAR AGORA",
            onClick = { /* Chamar funções */ },
            colors = c
        )
    }
}

// ==========================================
// TELA ADICIONAR APP/PASTA
// ==========================================

@Composable
fun AddAppFolderScreen(
    theme: ThemeMode,
    onBack: () -> Unit
) {
    val c = AppColors.get(theme)
    var selectedTab by remember { mutableIntStateOf(0) }
    val apps = remember {
        listOf(
            AppItem("com.tencent.ig", "PUBG", true),
            AppItem("com.mojang.minecraft", "Minecraft", true),
            AppItem("com.netflix.mediaclient", "Netflix", false),
            AppItem("com.spotify.music", "Spotify", false)
        )
    }
    val folders = remember {
        listOf(
            "Jogos FPS",
            "Redes Sociais",
            "Streaming",
            "Ferramentas"
        )
    }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(c.bg)
    ) {
        // Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(Icons.Default.Close, "Voltar", tint = c.text)
            }
            
            Text(
                "Adicionar ao Boost",
                fontFamily = ComicSansFont,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = c.text
            )
            
            IconButton(onClick = { /* Selecionar todos */ }) {
                Icon(Icons.Default.SelectAll, "Todos", tint = c.accent)
            }
        }
        
        // Tabs
        TabRow(selectedTab, c)
        
        // Conteúdo
        when(selectedTab) {
            0 -> AppsList(apps, c)
            1 -> FoldersList(folders, c)
        }
        
        // Botão confirmar
        Box(modifier = Modifier.padding(20.dp)) {
            BoosterButton(
                text = "ADICIONAR SELECIONADOS",
                onClick = { /* Salvar */ },
                colors = c
            )
        }
    }
}

// ==========================================
// TELA DE PERFIS
// ==========================================

@Composable
fun ProfilesScreen(theme: ThemeMode) {
    val c = AppColors.get(theme)
    
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(c.bg)
            .padding(16.dp)
    ) {
        item {
            Text(
                "Perfis de Boost",
                fontFamily = MinecraftFont,
                fontSize = 28.sp,
                color = c.text
            )
        }
        
        item { ProfileCard("🎮", "Modo Jogo", "Performance máxima", c) }
        item { ProfileCard("📱", "Modo Streamer", "Rede otimizada", c) }
        item { ProfileCard("🎥", "Modo Cineasta", "Gravação suave", c) }
        item { ProfileCard("📖", "Modo Leitura", "Economia total", c) }
    }
}

// ==========================================
// COMPONENTES
// ==========================================

@Composable
fun StatusCard(title: String, value: String, c: Scheme) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = c.card)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(title, color = c.subtext, fontSize = 14.sp)
            Text(value, color = c.accent, fontSize = 20.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun BoosterButton(text: String, onClick: () -> Unit, colors: Scheme) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = colors.accent
        )
    ) {
        Text(
            text,
            fontFamily = MinecraftFont,
            fontSize = 16.sp,
            color = Color.White
        )
    }
}

@Composable
fun TabRow(selected: Int, c: Scheme) {
    val tabs = listOf("Apps", "Pastas")
    
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(c.card)
    ) {
        tabs.forEachIndexed { index, title ->
            Text(
                text = title,
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(12.dp))
                    .background(if(selected == index) c.accent else Color.Transparent)
                    .clickable { selected = index }
                    .padding(vertical = 12.dp),
                textAlign = TextAlign.Center,
                color = if(selected == index) Color.White else c.subtext,
                fontWeight = if(selected == index) FontWeight.Bold else FontWeight.Normal
            )
        }
    }
}

@Composable
fun AppsList(apps: List<AppItem>, c: Scheme) {
    LazyColumn(modifier = Modifier.padding(16.dp)) {
        items(apps) { app ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = c.card)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = app.isSelected,
                        onCheckedChange = { /* Toggle */ },
                        colors = CheckboxDefaults.colors(checkedColor = c.accent)
                    )
                    Column(modifier = Modifier.padding(start = 12.dp)) {
                        Text(app.name, color = c.text, fontWeight = FontWeight.Bold)
                        Text(app.packageName, color = c.subtext, fontSize = 12.sp)
                    }
                }
            }
        }
    }
}

@Composable
fun FoldersList(folders: List<String>, c: Scheme) {
    LazyColumn(modifier = Modifier.padding(16.dp)) {
        items(folders) { folder ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = c.card)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Default.Folder,
                        contentDescription = null,
                        tint = c.accent
                    )
                    Text(
                        folder,
                        color = c.text,
                        modifier = Modifier.padding(start = 12.dp),
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
fun ProfileCard(emoji: String, title: String, desc: String, c: Scheme) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = c.card)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(emoji, fontSize = 32.sp)
            Column(modifier = Modifier.padding(start = 16.dp)) {
                Text(title, color = c.text, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Text(desc, color = c.subtext, fontSize = 14.sp)
            }
        }
    }
}

// Modelo
data class AppItem(
    val packageName: String,
    val name: String,
    val isSelected: Boolean
)
