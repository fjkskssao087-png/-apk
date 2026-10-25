package com.example.ui.components

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.DeepBillingTarget
import com.example.ui.theme.ObsidianBg
import com.example.ui.theme.ObsidianCardBorder
import com.example.ui.theme.ObsidianSurface
import com.example.ui.theme.ScopeGold
import com.example.ui.theme.ScopeGoldLight
import com.example.ui.theme.ScopeRed
import com.example.ui.theme.ScopeRedContainer
import com.example.ui.theme.StatusCyan
import com.example.ui.theme.TextDarker
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextWhite

@Composable
fun DeepCodeAuditView(
    target: DeepBillingTarget,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Easy Back navigation via system back key
    BackHandler { onBack() }

    val context = LocalContext.current
    var selectedExplanationTab by remember { mutableStateOf(0) } // 0: للمبتدئ, 1: للعبقري والخبير

    Column(
        modifier = modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
            .testTag("deep_code_audit_view")
    ) {
        // Top Easy Back Navigation Bar
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFF1B2335))
                    .border(1.dp, ScopeGold.copy(alpha = 0.4f), RoundedCornerShape(12.dp))
                    .clickable(onClick = onBack)
                    .padding(horizontal = 12.dp, vertical = 8.dp)
                    .testTag("easy_back_button")
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "الرجوع",
                        tint = ScopeGold,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "الرجوع السهل للقائمة",
                        color = ScopeGold,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(ScopeRedContainer)
                    .border(1.dp, ScopeRed.copy(alpha = 0.5f), RoundedCornerShape(8.dp))
                    .padding(horizontal = 10.dp, vertical = 5.dp)
            ) {
                Text(
                    text = "${target.severity} • ${target.confidence}",
                    color = ScopeRed,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Black
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Target Name & Exact In-File Location
        Text(
            text = target.name,
            fontSize = 22.sp,
            fontWeight = FontWeight.Black,
            color = TextWhite
        )

        Spacer(modifier = Modifier.height(6.dp))

        // Exact Location Box
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(ObsidianSurface)
                .border(1.dp, ObsidianCardBorder, RoundedCornerShape(12.dp))
                .padding(12.dp)
        ) {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "الموقع الدقيق داخل الملفات والحزم:",
                        fontSize = 11.sp,
                        color = TextMuted,
                        fontWeight = FontWeight.Medium
                    )
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(Color(0xFF10192A))
                            .clickable {
                                copyToClipboard(context, "Location", target.location)
                                Toast.makeText(context, "تم نسخ مسار الدالة", Toast.LENGTH_SHORT).show()
                            }
                            .padding(horizontal = 8.dp, vertical = 3.dp)
                    ) {
                        Text(text = "نسخ المسار", color = StatusCyan, fontSize = 9.sp)
                    }
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = target.location,
                    fontSize = 12.sp,
                    color = ScopeGold,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "التوقيع البرمجي: ${target.methodSignature}",
                    fontSize = 11.sp,
                    color = TextMuted,
                    fontFamily = FontFamily.Monospace
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Code Snippet Box (Syntax-highlighted look)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(Color(0xFF090D15))
                .border(1.dp, ScopeRed.copy(alpha = 0.4f), RoundedCornerShape(16.dp))
                .padding(14.dp)
        ) {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Code,
                            contentDescription = null,
                            tint = ScopeRed,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "شكل الكود البرمجي المستخرج من البايت كود:",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextWhite
                        )
                    }
                    IconButton(
                        onClick = {
                            copyToClipboard(context, "Code Snippet", target.codeSnippet)
                            Toast.makeText(context, "تم نسخ الكود", Toast.LENGTH_SHORT).show()
                        },
                        modifier = Modifier.size(28.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.ContentCopy,
                            contentDescription = "نسخ الكود",
                            tint = ScopeGold,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color(0xFF06090F))
                        .border(1.dp, Color(0xFF1E2638), RoundedCornerShape(10.dp))
                        .padding(12.dp)
                ) {
                    Text(
                        text = target.codeSnippet.trimIndent(),
                        color = Color(0xFFFF859E),
                        fontSize = 12.sp,
                        fontFamily = FontFamily.Monospace,
                        lineHeight = 18.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // Dual Explanation Switcher: "شرح مبتدئ" vs "شرح عبقري ومعماري"
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(14.dp))
                .background(ObsidianSurface)
                .border(1.dp, ObsidianCardBorder, RoundedCornerShape(14.dp))
                .padding(4.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            val tab0Modifier = if (selectedExplanationTab == 0) {
                Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(10.dp))
                    .background(Brush.horizontalGradient(listOf(ScopeGold, ScopeGoldLight)))
            } else {
                Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color.Transparent)
            }

            Box(
                modifier = tab0Modifier
                    .clickable { selectedExplanationTab = 0 }
                    .padding(vertical = 10.dp),
                contentAlignment = Alignment.Center
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Lightbulb,
                        contentDescription = null,
                        tint = if (selectedExplanationTab == 0) Color.Black else ScopeGold,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "شرح مبسط (مبتدئ)",
                        color = if (selectedExplanationTab == 0) Color.Black else TextMuted,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            val tab1Modifier = if (selectedExplanationTab == 1) {
                Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(10.dp))
                    .background(Brush.horizontalGradient(listOf(ScopeRed, Color(0xFFD61A42))))
            } else {
                Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color.Transparent)
            }

            Box(
                modifier = tab1Modifier
                    .clickable { selectedExplanationTab = 1 }
                    .padding(vertical = 10.dp),
                contentAlignment = Alignment.Center
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Psychology,
                        contentDescription = null,
                        tint = if (selectedExplanationTab == 1) Color.White else ScopeRed,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "شرح تقني (عبقري / خبير)",
                        color = if (selectedExplanationTab == 1) Color.White else TextMuted,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Explanation Body with Animation
        AnimatedContent(
            targetState = selectedExplanationTab,
            transitionSpec = {
                fadeIn(animationSpec = tween(250)) togetherWith fadeOut(animationSpec = tween(150))
            },
            label = "explanationTransition"
        ) { tabIndex ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(ObsidianSurface)
                    .border(
                        1.dp,
                        if (tabIndex == 0) ScopeGold.copy(alpha = 0.5f) else ScopeRed.copy(alpha = 0.5f),
                        RoundedCornerShape(16.dp)
                    )
                    .padding(16.dp)
            ) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = if (tabIndex == 0) Icons.Default.Lightbulb else Icons.Default.Psychology,
                            contentDescription = null,
                            tint = if (tabIndex == 0) ScopeGold else ScopeRed,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (tabIndex == 0) "الشرح المبسط بلغة واضحة:" else "التحليل المعماري للمتخصصين:",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (tabIndex == 0) ScopeGold else ScopeRed
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = if (tabIndex == 0) target.beginnerExplanation else target.geniusExplanation,
                        fontSize = 12.sp,
                        color = TextWhite,
                        lineHeight = 19.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Hardening / Remediation Box (توصية التحصين الرسمية)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(Color(0xFF0F1E1B))
                .border(1.dp, Color(0xFF00E676).copy(alpha = 0.4f), RoundedCornerShape(16.dp))
                .padding(16.dp)
        ) {
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Shield,
                        contentDescription = null,
                        tint = Color(0xFF00E676),
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "توصية التحصين المعماري ضد التحايل:",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF00E676)
                    )
                }
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = target.hardeningFix,
                    fontSize = 12.sp,
                    color = TextWhite.copy(alpha = 0.9f),
                    lineHeight = 18.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

private fun copyToClipboard(context: Context, label: String, text: String) {
    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val clip = ClipData.newPlainText(label, text)
    clipboard.setPrimaryClip(clip)
}
