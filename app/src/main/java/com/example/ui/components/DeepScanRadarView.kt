package com.example.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.Memory
import androidx.compose.material.icons.filled.Terminal
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.DeepScanProgress
import com.example.ui.theme.ObsidianBg
import com.example.ui.theme.ObsidianCardBorder
import com.example.ui.theme.ObsidianSurface
import com.example.ui.theme.ScopeGold
import com.example.ui.theme.ScopeGoldLight
import com.example.ui.theme.ScopeRed
import com.example.ui.theme.StatusCyan
import com.example.ui.theme.StatusReady
import com.example.ui.theme.TextDarker
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextWhite
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun DeepScanRadarView(
    targetName: String,
    progress: DeepScanProgress,
    onCancelScan: () -> Unit,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "radarTransition")
    val sweepAngle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(2200, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "sweepAngle"
    )

    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 0.85f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulseScale"
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(22.dp))
            .background(ObsidianSurface)
            .border(
                1.5.dp,
                Brush.horizontalGradient(listOf(ScopeRed, ScopeGold)),
                RoundedCornerShape(22.dp)
            )
            .padding(18.dp)
            .testTag("deep_scan_radar_view")
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            // Header Bar
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .clip(CircleShape)
                            .background(ScopeRed)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "رادار الفحص المجهري العميق والذكاء الاصطناعي",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = ScopeGold
                    )
                }
                IconButton(
                    onClick = onCancelScan,
                    modifier = Modifier.size(28.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "إلغاء الفحص",
                        tint = TextMuted,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Radar Graphic Center
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.size(140.dp)
            ) {
                Canvas(modifier = Modifier.size(130.dp)) {
                    val center = Offset(size.width / 2, size.height / 2)
                    val maxRadius = size.width / 2

                    // Rings
                    drawCircle(
                        color = ScopeGold.copy(alpha = 0.15f),
                        radius = maxRadius,
                        center = center,
                        style = Stroke(width = 1.5.dp.toPx())
                    )
                    drawCircle(
                        color = ScopeRed.copy(alpha = 0.25f),
                        radius = maxRadius * 0.7f,
                        center = center,
                        style = Stroke(width = 1.5.dp.toPx())
                    )
                    drawCircle(
                        color = ScopeGold.copy(alpha = 0.35f),
                        radius = maxRadius * 0.4f,
                        center = center,
                        style = Stroke(width = 1.5.dp.toPx())
                    )

                    // Crosshairs
                    drawLine(
                        color = Color(0xFF232D42),
                        start = Offset(center.x, 0f),
                        end = Offset(center.x, size.height),
                        strokeWidth = 1.dp.toPx()
                    )
                    drawLine(
                        color = Color(0xFF232D42),
                        start = Offset(0f, center.y),
                        end = Offset(size.width, center.y),
                        strokeWidth = 1.dp.toPx()
                    )

                    // Sweeper Beam
                    val rad = Math.toRadians(sweepAngle.toDouble())
                    val endX = center.x + (maxRadius * cos(rad)).toFloat()
                    val endY = center.y + (maxRadius * sin(rad)).toFloat()

                    drawLine(
                        brush = Brush.linearGradient(
                            listOf(ScopeGold, ScopeRed.copy(alpha = 0.2f)),
                            start = center,
                            end = Offset(endX, endY)
                        ),
                        start = center,
                        end = Offset(endX, endY),
                        strokeWidth = 3.dp.toPx(),
                        cap = StrokeCap.Round
                    )
                }

                // Center Icon with Pulse
                Box(
                    modifier = Modifier
                        .size((42 * pulseScale).dp)
                        .clip(CircleShape)
                        .background(Color(0xFF251421))
                        .border(1.5.dp, ScopeGold, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Memory,
                        contentDescription = null,
                        tint = ScopeGoldLight,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Target Name & Stage Badge
            Text(
                text = "جاري فحص: $targetName",
                fontSize = 15.sp,
                fontWeight = FontWeight.Black,
                color = TextWhite
            )

            Spacer(modifier = Modifier.height(4.dp))

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0xFF1E1712))
                    .border(1.dp, ScopeGold.copy(alpha = 0.5f), RoundedCornerShape(8.dp))
                    .padding(horizontal = 10.dp, vertical = 4.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.AutoAwesome,
                        contentDescription = null,
                        tint = ScopeGold,
                        modifier = Modifier.size(12.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = progress.currentStage.ifEmpty { "المرحلة: استخراج بايت كود DEX والاشتراكات..." },
                        color = ScopeGold,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Linear Progress Bar & Percentage
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "الملف الجاري فحصه: ${progress.scannedFilesCount} / ${progress.totalFilesCount}",
                        fontSize = 11.sp,
                        color = TextMuted
                    )
                    Text(
                        text = "${(progress.progressFraction * 100).toInt()}%",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = ScopeGold
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))

                LinearProgressIndicator(
                    progress = { progress.progressFraction },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .clip(RoundedCornerShape(4.dp)),
                    color = ScopeRed,
                    trackColor = Color(0xFF1B2333)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Currently Active File Box
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFF0F1522))
                    .border(1.dp, ObsidianCardBorder, RoundedCornerShape(12.dp))
                    .padding(10.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        imageVector = Icons.Default.Folder,
                        contentDescription = null,
                        tint = StatusCyan,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "الملف قيد التدقيق حالياً:",
                            fontSize = 10.sp,
                            color = TextDarker
                        )
                        Text(
                            text = progress.currentFile.ifEmpty { "classes.dex (DEX Header & String Pools)" },
                            fontSize = 11.sp,
                            color = StatusCyan,
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Bold,
                            maxLines = 1
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Real-time Scrolling Logs Box (Terminal Look)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(95.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color(0xFF080B10))
                    .border(1.dp, Color(0xFF1E283A), RoundedCornerShape(10.dp))
                    .padding(8.dp)
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Terminal,
                            contentDescription = null,
                            tint = ScopeGold,
                            modifier = Modifier.size(12.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "سجل الفحص المباشر (ScopeDex Terminal)",
                            color = ScopeGold,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    progress.recentLogs.takeLast(4).forEach { logLine ->
                        Text(
                            text = logLine,
                            color = if (logLine.contains("ScopeAI") || logLine.contains("نجاح")) ScopeGoldLight else TextMuted,
                            fontSize = 10.sp,
                            fontFamily = FontFamily.Monospace,
                            maxLines = 1
                        )
                    }
                }
            }
        }
    }
}
