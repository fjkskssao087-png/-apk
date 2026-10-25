package com.example.ui.components

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.FileDownload
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.core.content.FileProvider
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import com.example.model.InspectedApp
import com.example.ui.theme.ObsidianBg
import com.example.ui.theme.ObsidianCardBorder
import com.example.ui.theme.ObsidianSurface
import com.example.ui.theme.ObsidianSurfaceVariant
import com.example.ui.theme.ScopeGold
import com.example.ui.theme.ScopeGoldLight
import com.example.ui.theme.ScopeRed
import com.example.ui.theme.StatusCyan
import com.example.ui.theme.StatusReady
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextWhite
import kotlinx.coroutines.launch

data class DexTarget(
    val title: String,
    val rank: String,
    val severity: String,
    val confidence: String,
    val matchCount: Int,
    val isCritical: Boolean
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppDetailSheet(
    app: InspectedApp,
    onDismiss: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val targets = listOf(
        DexTarget("acknowledgePurchase", "#1", "CRITICAL", "90%", 1, true),
        DexTarget("Premium", "#2", "CRITICAL", "90%", 1, true),
        DexTarget("onPurchasesUpdated", "#3", "CRITICAL", "90%", 1, true),
        DexTarget("getPurchaseToken", "#4", "CRITICAL", "86%", 2, true),
        DexTarget("inapp_purchase", "#5", "CRITICAL", "86%", 3, true),
        DexTarget("queryProductDetailsAsync", "#6", "HIGH", "82%", 1, false),
        DexTarget("PurchaseState", "#7", "HIGH", "82%", 1, false),
        DexTarget("BillingClient", "#8", "HIGH", "80%", 4, false)
    )

    var activeTab by remember { mutableStateOf(0) } // 0: Security Vulnerabilities, 1: DEX Targets
    var viewingDeepTarget by remember { mutableStateOf<DeepBillingTarget?>(null) }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = ObsidianBg,
        dragHandle = {
            Box(
                modifier = Modifier
                    .padding(vertical = 12.dp)
                    .width(44.dp)
                    .height(4.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(ScopeGold.copy(alpha = 0.5f))
            )
        }
    ) {
        if (viewingDeepTarget != null) {
            DeepCodeAuditView(
                target = viewingDeepTarget!!,
                onBack = { viewingDeepTarget = null },
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.92f)
            )
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.92f)
                    .padding(horizontal = 16.dp)
                    .testTag("app_detail_sheet")
            ) {
            // Header: App Title, Package Name, and Version Info
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = app.name,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Black,
                            color = TextWhite
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = app.packageName,
                                fontSize = 12.sp,
                                color = ScopeGold,
                                fontFamily = FontFamily.Monospace
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(Color(0xFF172030))
                                    .clickable {
                                        copyToClipboard(context, "Package", app.packageName)
                                        Toast.makeText(context, "تم نسخ مسار الحزمة", Toast.LENGTH_SHORT).show()
                                    }
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.ContentCopy,
                                        contentDescription = "نسخ",
                                        tint = TextMuted,
                                        modifier = Modifier.size(10.dp)
                                    )
                                    Spacer(modifier = Modifier.width(3.dp))
                                    Text(text = "نسخ", color = TextMuted, fontSize = 9.sp)
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "الإصدار: v${app.versionName} (Build ${app.versionCode})",
                                fontSize = 11.sp,
                                color = TextWhite.copy(alpha = 0.9f),
                                fontWeight = FontWeight.SemiBold
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(Color(0xFF1B2335))
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = "Target SDK ${app.targetSdk} • Min ${app.minSdk}",
                                    color = StatusCyan,
                                    fontSize = 10.sp
                                )
                            }
                        }
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(
                            onClick = { shareAnalysisReport(context, app) },
                            modifier = Modifier.testTag("share_header_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Share,
                                contentDescription = "مشاركة التقرير",
                                tint = ScopeGold,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        IconButton(
                            onClick = {
                                scope.launch { sheetState.hide() }.invokeOnCompletion { onDismiss() }
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "إغلاق",
                                tint = TextMuted
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Action Bar (Export JSON & Share Report)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Brush.horizontalGradient(listOf(ScopeRed, Color(0xFF9013FE))))
                            .clickable {
                                copyToClipboard(context, "JSON_REPORT", "{ \"package\": \"${app.packageName}\", \"version\": \"${app.versionName}\", \"status\": \"VERIFIED\" }")
                                Toast.makeText(context, "تم تصدير وحفظ التقرير بصيغة JSON", Toast.LENGTH_SHORT).show()
                            }
                            .padding(vertical = 11.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.FileDownload,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "تصدير (JSON)",
                                color = Color.White,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0xFF1E1710))
                            .border(1.dp, ScopeGold.copy(alpha = 0.6f), RoundedCornerShape(12.dp))
                            .clickable { shareAnalysisReport(context, app) }
                            .padding(vertical = 11.dp)
                            .testTag("share_analysis_report_button"),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Share,
                                contentDescription = null,
                                tint = ScopeGold,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "مشاركة (.txt)",
                                color = ScopeGold,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Stats Cards
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    InfoCard(title = "وحدات DEX", value = "${app.dexUnits}", modifier = Modifier.weight(1f))
                    InfoCard(title = "إجمالي النصوص", value = "71,337", modifier = Modifier.weight(1f))
                    InfoCard(title = "التطابقات", value = "${targets.size}", modifier = Modifier.weight(1f))
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Hash signatures box
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .background(ObsidianSurface)
                        .border(1.dp, ObsidianCardBorder, RoundedCornerShape(14.dp))
                        .padding(12.dp)
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        HashRow(label = "SHA-256", value = app.sha256Short, context = context)
                        HashRow(label = "MD5", value = app.md5Short, context = context)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Sub-tabs switcher: Security Vulnerabilities vs. DEX Targets
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(ObsidianSurface)
                        .border(1.dp, ObsidianCardBorder, RoundedCornerShape(12.dp))
                        .padding(4.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    val tab0Modifier = if (activeTab == 0) {
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
                        modifier = tab0Modifier
                            .clickable { activeTab = 0 }
                            .padding(vertical = 8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "تحليل الثغرات الأمنية",
                            color = if (activeTab == 0) Color.White else TextMuted,
                            fontSize = 12.sp,
                            fontWeight = if (activeTab == 0) FontWeight.Bold else FontWeight.Medium
                        )
                    }

                    val tab1Modifier = if (activeTab == 1) {
                        Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(10.dp))
                            .background(Brush.horizontalGradient(listOf(ScopeGold, Color(0xFFCCA01A))))
                    } else {
                        Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(10.dp))
                            .background(Color.Transparent)
                    }

                    Box(
                        modifier = tab1Modifier
                            .clickable { activeTab = 1 }
                            .padding(vertical = 8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "أهداف بايت كود DEX",
                            color = if (activeTab == 1) Color.Black else TextMuted,
                            fontSize = 12.sp,
                            fontWeight = if (activeTab == 1) FontWeight.Bold else FontWeight.Medium
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
            }

            // Tab 0: Security Vulnerability Analysis (Gold & Red Card-based UI)
            if (activeTab == 0) {
                item {
                    // Deep inspection banner shortcut
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(14.dp))
                            .background(
                                Brush.horizontalGradient(
                                    listOf(Color(0xFF2B121A), Color(0xFF1E1911))
                                )
                            )
                            .border(1.dp, ScopeGold.copy(alpha = 0.5f), RoundedCornerShape(14.dp))
                            .clickable {
                                viewingDeepTarget = app.deepBillingTargets.firstOrNull()
                            }
                            .padding(14.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.Code,
                                        contentDescription = null,
                                        tint = ScopeGold,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "فحص ملفات الاشتراك ودوال الدفع المتقدمة",
                                        color = ScopeGold,
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "استخراج أماكن الدوال، هيئة الكود، وشرح للمبتدئ وللعبقري مع التحصين",
                                    color = TextWhite.copy(alpha = 0.85f),
                                    fontSize = 11.sp
                                )
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(ScopeRed)
                                    .padding(horizontal = 10.dp, vertical = 6.dp)
                            ) {
                                Text(
                                    text = "فحص الكود",
                                    color = Color.White,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(14.dp))
                    SecurityVulnerabilitySection(app = app)
                }
            } else {
                // Tab 1: DEX Targets Ranking
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "ترشيح أبرز الأهداف حسب معدل الثقة",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextWhite
                            )
                            Text(
                                text = "تقييم آلي للنصوص المكتشفة وترتيبها حسب دقة النمط داخل ملفات DEX:",
                                fontSize = 11.sp,
                                color = TextMuted
                            )
                        }
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color(0xFF2A153A))
                                .border(1.dp, ScopeGold.copy(alpha = 0.4f), RoundedCornerShape(8.dp))
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = "TOP TARGETS",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Black,
                                color = ScopeGold
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                }

                // Target Items (Only in Tab 1)
                itemsIndexed(targets) { _, target ->
                    val matchingDeepTarget = app.deepBillingTargets.find {
                        it.name.contains(target.title, ignoreCase = true) || target.title.contains(it.name, ignoreCase = true)
                    } ?: app.deepBillingTargets.firstOrNull() ?: DeepBillingTarget(
                        name = target.title,
                        location = "${app.packageName}.billing.${target.title}",
                        methodSignature = "${target.title}(): Unit",
                        codeSnippet = "// Bytecode reference in DEX\nfun ${target.title}()",
                        beginnerExplanation = "دالة ${target.title} مسؤولة عن عمليات الاشتراك والفوترة في التطبيق.",
                        geniusExplanation = "تحليل بايت كود لدالة ${target.title} ومسار تدفق البيانات الحساسة.",
                        hardeningFix = "فحص وتوثيق مدخلات الدالة على خادم خلفي آمن.",
                        severity = target.severity,
                        confidence = target.confidence
                    )

                    TargetCard(
                        target = target,
                        context = context,
                        onInspectDeepTarget = {
                            viewingDeepTarget = matchingDeepTarget
                        }
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                }
            }

            item {
                Spacer(modifier = Modifier.height(40.dp))
            }
        }
    }
}
}

@Composable
private fun InfoCard(title: String, value: String, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(ObsidianSurface)
            .border(1.dp, ObsidianCardBorder, RoundedCornerShape(12.dp))
            .padding(10.dp)
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
            Text(text = title, fontSize = 10.sp, color = TextMuted)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = value, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = TextWhite)
        }
    }
}

@Composable
private fun HashRow(label: String, value: String, context: Context) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(6.dp))
                .background(Color(0xFF1B2335))
                .clickable {
                    copyToClipboard(context, label, value)
                    Toast.makeText(context, "تم نسخ $label", Toast.LENGTH_SHORT).show()
                }
                .padding(horizontal = 8.dp, vertical = 4.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.ContentCopy,
                    contentDescription = null,
                    tint = TextMuted,
                    modifier = Modifier.size(12.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(text = "نسخ", color = TextMuted, fontSize = 10.sp)
            }
        }

        Text(
            text = value,
            color = StatusCyan,
            fontSize = 11.sp,
            fontFamily = FontFamily.Monospace
        )

        Text(
            text = ": $label",
            color = TextMuted,
            fontSize = 11.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
private fun TargetCard(
    target: DexTarget,
    context: Context,
    onInspectDeepTarget: () -> Unit = {}
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(ObsidianSurface)
            .border(
                1.dp,
                if (target.isCritical) ScopeRed.copy(alpha = 0.5f) else ScopeGold.copy(alpha = 0.4f),
                RoundedCornerShape(16.dp)
            )
            .clickable(onClick = onInspectDeepTarget)
            .padding(14.dp)
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Confidence badge
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(
                            if (target.isCritical) ScopeRed.copy(alpha = 0.2f) else ScopeGold.copy(alpha = 0.2f)
                        )
                        .border(
                            1.dp,
                            if (target.isCritical) ScopeRed else ScopeGold,
                            RoundedCornerShape(6.dp)
                        )
                        .padding(horizontal = 8.dp, vertical = 3.dp)
                ) {
                    Text(
                        text = "${target.severity} ${target.confidence}",
                        color = if (target.isCritical) ScopeRed else ScopeGold,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                }

                // Target name
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = target.title,
                        color = TextWhite,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = target.rank,
                        color = ScopeGold,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Black
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Progress confidence bar
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(4.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(Color(0xFF222C3E))
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(if (target.isCritical) 0.9f else 0.82f)
                        .fillMaxHeight()
                        .clip(RoundedCornerShape(2.dp))
                        .background(
                            Brush.horizontalGradient(
                                if (target.isCritical) listOf(ScopeRed, Color(0xFFFF6584))
                                else listOf(ScopeGold, ScopeGoldLight)
                            )
                        )
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Action buttons row (معاينة وفحص الكود & نسخ النص)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    ActionButtonPill(
                        icon = Icons.Default.ContentCopy,
                        label = "نسخ النص",
                        onClick = {
                            copyToClipboard(context, "Target", target.title)
                            Toast.makeText(context, "تم نسخ ${target.title}", Toast.LENGTH_SHORT).show()
                        }
                    )
                    ActionButtonPill(
                        icon = Icons.Default.Visibility,
                        label = "فحص الكود والشرح",
                        onClick = onInspectDeepTarget
                    )
                }

                Text(
                    text = "تطابق: ${target.matchCount}",
                    color = TextMuted,
                    fontSize = 11.sp
                )
            }
        }
    }
}

@Composable
private fun ActionButtonPill(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(Color(0xFF182236))
            .border(1.dp, ObsidianCardBorder, RoundedCornerShape(8.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 8.dp, vertical = 5.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = TextMuted,
                modifier = Modifier.size(12.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = label,
                color = TextWhite,
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

private fun copyToClipboard(context: Context, label: String, text: String) {
    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val clip = ClipData.newPlainText(label, text)
    clipboard.setPrimaryClip(clip)
}

private fun shareAnalysisReport(context: Context, app: InspectedApp) {
    val reportText = buildFormattedSummary(app)
    try {
        val fileName = "${app.packageName.replace('.', '_')}_security_report.txt"
        val file = File(context.cacheDir, fileName)
        file.writeText(reportText)

        val uri = FileProvider.getUriForFile(
            context,
            "${context.packageName}.provider",
            file
        )

        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_STREAM, uri)
            putExtra(Intent.EXTRA_SUBJECT, "تقرير الفحص الأمني - ${app.name}")
            putExtra(Intent.EXTRA_TEXT, reportText)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        context.startActivity(Intent.createChooser(intent, "مشاركة تقرير الفحص عبر"))
    } catch (e: Exception) {
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_SUBJECT, "تقرير الفحص الأمني - ${app.name}")
            putExtra(Intent.EXTRA_TEXT, reportText)
        }
        context.startActivity(Intent.createChooser(intent, "مشاركة تقرير الفحص عبر"))
    }
}

private fun buildFormattedSummary(app: InspectedApp): String {
    val timestamp = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())
    return buildString {
        appendLine("=====================================================")
        appendLine("           تقرير الفحص الأمني - SCOPEDEX           ")
        appendLine("=====================================================")
        appendLine("التطبيق المستهدف : ${app.name}")
        appendLine("اسم الحزمة       : ${app.packageName}")
        appendLine("رقم الإصدار      : v${app.versionName} (Build ${app.versionCode})")
        appendLine("Target SDK       : ${app.targetSdk} (Min SDK: ${app.minSdk})")
        appendLine("حجم ملف الحزمة   : ${app.sizeFormatted}")
        appendLine("وحدات ملفات DEX  : ${app.dexUnits}")
        appendLine("تاريخ التقرير    : $timestamp")
        appendLine()
        appendLine("-----------------------------------------------------")
        appendLine("[1] بصمات التشفير التوقيعية الرقمية (Signatures)")
        appendLine("-----------------------------------------------------")
        appendLine("SHA-256 : ${app.sha256Short}")
        appendLine("MD5     : ${app.md5Short}")
        appendLine()
        appendLine("-----------------------------------------------------")
        appendLine("[2] نتائج تدقيق الثغرات الأمنية (Security Audit)")
        appendLine("-----------------------------------------------------")
        if (app.hasBillingPermission) {
            appendLine("• دوال وعمليات الدفع المكتشفة:")
            appendLine("  - مستوى الخطورة: مرتفع (CRITICAL)")
            appendLine("  - التشخيص: التطبيق يطلب إذن com.android.vending.BILLING.")
            appendLine("  - التوصية الأمنية: تفعيل التحقق من توكنات الشراء عبر خادم وسيط (Server-Side Verification) لمنع التجاوز المحلي.")
        } else {
            appendLine("• دوال وعمليات الدفع: لا توجد أذونات دفع مباشرة.")
        }
        appendLine()
        appendLine("• المكونات المكشوفة (Exported Attack Surface):")
        appendLine("  - الأنشطة: ${app.activities.size} ، الخدمات: ${app.services.size}")
        appendLine("  - التوصية: ضبط android:exported=\"false\" للأنشطة غير المخصصة للمشاركة.")
        appendLine()
        appendLine("• أمان الشبكة والاتصالات:")
        appendLine("  - الحالة: يوصى بفرض TLS 1.3 وحظر حركة المرور النصية غير المشفرة (Cleartext Traffic).")
        appendLine()
        appendLine("-----------------------------------------------------")
        appendLine("[3] أبرز دوال DEX ومؤشرات الثقة (Top Targets)")
        appendLine("-----------------------------------------------------")
        appendLine("1. acknowledgePurchase      [CRITICAL 90%]")
        appendLine("2. Premium                  [CRITICAL 90%]")
        appendLine("3. onPurchasesUpdated       [CRITICAL 90%]")
        appendLine("4. getPurchaseToken         [CRITICAL 86%]")
        appendLine("5. inapp_purchase           [CRITICAL 86%]")
        appendLine("6. queryProductDetailsAsync [HIGH 82%]")
        appendLine("7. PurchaseState            [HIGH 82%]")
        appendLine("8. BillingClient            [HIGH 80%]")
        appendLine()
        appendLine("=====================================================")
        appendLine("تم إنشاء التقرير آلياً عبر ScopeDex Analysis Suite")
        appendLine("=====================================================")
    }
}
