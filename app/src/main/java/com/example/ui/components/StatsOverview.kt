package com.example.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Apps
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Layers
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.RestartAlt
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.ObsidianCardBorder
import com.example.ui.theme.ObsidianSurface
import com.example.ui.theme.ScopeGold
import com.example.ui.theme.ScopeGoldLight
import com.example.ui.theme.ScopeRed
import com.example.ui.theme.ScopeRedDark
import com.example.ui.theme.StatusCyan
import com.example.ui.theme.StatusReady
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextWhite

@Composable
fun StatsOverview(
    totalApps: Int,
    userApps: Int,
    billingApps: Int,
    isScanStarted: Boolean = true,
    onStartScan: () -> Unit = {},
    onResetZero: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val displayTotal = if (isScanStarted) totalApps else 0
    val displayUser = if (isScanStarted) userApps else 0
    val displayBilling = if (isScanStarted) billingApps else 0

    Column(modifier = modifier.fillMaxWidth()) {
        // Status & Action Banner
        if (!isScanStarted) {
            // Idle / Zero State Banner
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(ObsidianSurface)
                    .border(
                        1.dp,
                        Brush.horizontalGradient(listOf(ScopeGold.copy(alpha = 0.6f), ScopeRed.copy(alpha = 0.6f))),
                        RoundedCornerShape(16.dp)
                    )
                    .padding(16.dp)
            ) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "لوحة المؤشرات في وضع الاستعداد (0)",
                            color = ScopeGold,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(Color(0xFF261D12))
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(text = "STANDBY: ZERO", color = ScopeGold, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "الإحصائيات صفر حالياً. انقر أدناه لبدء فحص الـ DEX وتدقيق دوال واشتراكات الشراء مباشرة:",
                        color = TextMuted,
                        fontSize = 11.sp,
                        lineHeight = 16.sp
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(Brush.horizontalGradient(listOf(ScopeRed, Color(0xFFD61A42))))
                            .clickable(onClick = onStartScan)
                            .padding(vertical = 12.dp)
                            .testTag("start_scan_button"),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.PlayArrow,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "بدء الفحص الشامل وتفعيل الإحصائيات",
                                color = Color.White,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        } else {
            // Active Scan Completed Banner with Reset Option
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(Color(0xFF0F1E1B))
                    .border(1.dp, StatusReady.copy(alpha = 0.35f), RoundedCornerShape(14.dp))
                    .padding(horizontal = 14.dp, vertical = 10.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = StatusReady,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "اكتمل الفحص واستخراج دوال الشراء",
                            color = StatusReady,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    // Reset to Zero Pill
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFF1B2232))
                            .border(1.dp, ScopeGold.copy(alpha = 0.4f), RoundedCornerShape(8.dp))
                            .clickable(onClick = onResetZero)
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                            .testTag("reset_zero_button")
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.RestartAlt,
                                contentDescription = "تصفير",
                                tint = ScopeGold,
                                modifier = Modifier.size(12.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "تصفير (0)",
                                color = ScopeGold,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Grid of 4 Metric Cards
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            StatMetricCard(
                title = "إجمالي التطبيقات",
                value = "$displayTotal",
                icon = Icons.Default.Apps,
                accentColor = ScopeGold,
                modifier = Modifier.weight(1f)
            )
            StatMetricCard(
                title = "تطبيقات المستخدم",
                value = "$displayUser",
                icon = Icons.Default.Layers,
                accentColor = StatusCyan,
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            StatMetricCard(
                title = "خدمات الدفع والشراء",
                value = "$displayBilling",
                icon = Icons.Default.MonetizationOn,
                accentColor = ScopeRed,
                modifier = Modifier.weight(1f)
            )
            StatMetricCard(
                title = "تشفير التوقيع الحصري",
                value = if (isScanStarted) "SHA-256" else "0",
                icon = Icons.Default.Security,
                accentColor = ScopeGold,
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Luxury Gauge / Confidence Indicator Box
        GaugeSummaryCard(billingApps = displayBilling, totalApps = displayTotal)
    }
}

@Composable
private fun StatMetricCard(
    title: String,
    value: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    accentColor: Color,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(ObsidianSurface)
            .border(1.dp, ObsidianCardBorder, RoundedCornerShape(16.dp))
            .padding(14.dp)
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = title,
                    fontSize = 12.sp,
                    color = TextMuted,
                    fontWeight = FontWeight.Medium
                )
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = accentColor,
                    modifier = Modifier.size(16.dp)
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = value,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = TextWhite
            )
        }
    }
}

@Composable
private fun GaugeSummaryCard(
    billingApps: Int,
    totalApps: Int
) {
    val progress = if (totalApps > 0) (billingApps.toFloat() / totalApps).coerceIn(0.1f, 1f) else 0f
    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = tween(durationMillis = 1000),
        label = "gaugeProgress"
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .background(ObsidianSurface)
            .border(1.dp, ObsidianCardBorder, RoundedCornerShape(18.dp))
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(RoundedCornerShape(2.dp))
                            .background(ScopeRed)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "مؤشر كثافة دوال الدفع والاشتراكات",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextWhite
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "يتم احتساب المؤشر بناءً على فحص الأذونات ومكتبات In-App Billing والمراجع داخل حزم DEX.",
                    fontSize = 11.sp,
                    color = TextMuted,
                    lineHeight = 16.sp
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Circular Radial Arc
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.size(70.dp)
            ) {
                Canvas(modifier = Modifier.size(64.dp)) {
                    // Track
                    drawArc(
                        color = Color(0xFF1E283D),
                        startAngle = 140f,
                        sweepAngle = 260f,
                        useCenter = false,
                        style = Stroke(width = 7.dp.toPx(), cap = StrokeCap.Round)
                    )
                    // Filled Arc
                    if (animatedProgress > 0f) {
                        drawArc(
                            brush = Brush.sweepGradient(
                                listOf(ScopeGold, ScopeRed, ScopeGold)
                            ),
                            startAngle = 140f,
                            sweepAngle = 260f * animatedProgress,
                            useCenter = false,
                            style = Stroke(width = 7.dp.toPx(), cap = StrokeCap.Round)
                        )
                    }
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "${(progress * 100).toInt()}%",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = ScopeGold
                    )
                    Text(
                        text = "RATIO",
                        fontSize = 8.sp,
                        color = TextMuted,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}
