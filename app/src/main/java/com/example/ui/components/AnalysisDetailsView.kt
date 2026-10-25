package com.example.ui.components

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.ReportProblem
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
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
import com.example.model.InspectedApp
import com.example.ui.theme.ObsidianCardBorder
import com.example.ui.theme.ObsidianSurface
import com.example.ui.theme.ObsidianSurfaceVariant
import com.example.ui.theme.ScopeGold
import com.example.ui.theme.ScopeGoldContainer
import com.example.ui.theme.ScopeGoldLight
import com.example.ui.theme.ScopeRed
import com.example.ui.theme.ScopeRedContainer
import com.example.ui.theme.StatusCyan
import com.example.ui.theme.StatusReady
import com.example.ui.theme.TextDarker
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextWhite

data class SecurityFinding(
    val id: String,
    val title: String,
    val category: String,
    val severity: VulnerabilitySeverity,
    val description: String,
    val remediation: String,
    val cweReference: String = "CWE-311"
)

enum class VulnerabilitySeverity {
    CRITICAL, HIGH, MEDIUM, SECURE
}

@Composable
fun SecurityVulnerabilitySection(
    app: InspectedApp,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    // Dynamically derive security posture based on package properties
    val findings = remember(app) {
        listOf(
            SecurityFinding(
                id = "SEC-01",
                title = if (app.hasBillingPermission)
                    "التحقق من صحة عمليات الشراء (Server-Side Verification)"
                else "فحص تكامل واجهات الدفع المعتمدة",
                category = "In-App Billing & Licensing",
                severity = if (app.hasBillingPermission) VulnerabilitySeverity.CRITICAL else VulnerabilitySeverity.SECURE,
                description = if (app.hasBillingPermission)
                    "التطبيق يستخدم صلاحية BILLING. يجب التحقق من Purchase Tokens عبر الخادم الخلفي (Server-to-Server) وتجنب الاعتماد على التحقق المحلي فقط."
                else "لا توجد أذونات دفع مباشرة تتطلب فحص خادم المشتريات.",
                remediation = "قم بربط واجهة Google Play Developer API على خادم وسيط موثوق للتحقق من حالة الاشتراك وتفادي التحايل المحلي.",
                cweReference = "CWE-602: Client-Side Enforcement of Server-Side Security"
            ),
            SecurityFinding(
                id = "SEC-02",
                title = "تدقيق المكونات المكشوفة (Exported Components)",
                category = "Manifest Attack Surface",
                severity = if (app.activities.size > 2) VulnerabilitySeverity.MEDIUM else VulnerabilitySeverity.SECURE,
                description = "تم رصد ${app.activities.size} أنشطة و ${app.services.size} خدمات. قد يسمح المكون المكشوف بتشغيل واجهات حساسة خارج سياق المصادقة.",
                remediation = "تأكد من ضبط android:exported=\"false\" لجميع الأنشطة والخدمات غير الموجهة للمشاركة مع تطبيقات خارجية.",
                cweReference = "CWE-926: Improper Export of Android Components"
            ),
            SecurityFinding(
                id = "SEC-03",
                title = "سياسة تشفير الاتصالات الشبكية (Network Security)",
                category = "Transport Layer Security",
                severity = VulnerabilitySeverity.SECURE,
                description = "جميع قنوات الاتصال والاتصال بالخوادم يجب أن تكون مقفلة على بروتوكول TLS 1.3 مع منع حركة المرور الصريحة (Cleartext Traffic).",
                remediation = "تضمين ملف res/xml/network_security_config.xml مع إعداد cleartextTrafficPermitted=\"false\".",
                cweReference = "CWE-319: Cleartext Transmission of Sensitive Information"
            ),
            SecurityFinding(
                id = "SEC-04",
                title = "حماية الكود وتعتيم الدوال (R8 / ProGuard Obfuscation)",
                category = "Binary Hardening",
                severity = VulnerabilitySeverity.HIGH,
                description = "تواجد الرموز الصريحة في جداول DEX يسهل قراءة المعاملات ومسارات التحقق.",
                remediation = "تفعيل minifyEnabled = true في build.gradle وقواعد حماية مكتبات الدفع.",
                cweReference = "CWE-656: Reliance on Security Through Obscurity"
            )
        )
    }

    Column(modifier = modifier.fillMaxWidth()) {
        // Section Header with Luxury Red & Gold accent
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
                    text = "تحليل الثغرات الأمنية وتأمين الحزمة",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextWhite
                )
            }
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(ScopeRedContainer)
                    .border(1.dp, ScopeRed.copy(alpha = 0.5f), RoundedCornerShape(8.dp))
                    .padding(horizontal = 8.dp, vertical = 3.dp)
            ) {
                Text(
                    text = "SECURITY AUDIT",
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Black,
                    color = ScopeRed
                )
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = "فحص معماري لمستوى أمان الحزمة، تحصين دوال الدفع، والمكونات المكشوفة:",
            fontSize = 11.sp,
            color = TextMuted
        )

        Spacer(modifier = Modifier.height(14.dp))

        // Security Findings Cards
        findings.forEach { finding ->
            SecurityVulnerabilityCard(finding = finding, context = context)
            Spacer(modifier = Modifier.height(10.dp))
        }
    }
}

@Composable
fun SecurityVulnerabilityCard(
    finding: SecurityFinding,
    context: Context
) {
    var isExpanded by remember { mutableStateOf(false) }

    val (badgeBg, badgeBorder, badgeText, cardBorder) = when (finding.severity) {
        VulnerabilitySeverity.CRITICAL -> listOf(
            ScopeRedContainer,
            ScopeRed,
            ScopeRed,
            ScopeRed.copy(alpha = 0.5f)
        )
        VulnerabilitySeverity.HIGH -> listOf(
            ScopeGoldContainer,
            ScopeGold,
            ScopeGold,
            ScopeGold.copy(alpha = 0.5f)
        )
        VulnerabilitySeverity.MEDIUM -> listOf(
            Color(0xFF261D12),
            Color(0xFFFF9800),
            Color(0xFFFF9800),
            Color(0xFFFF9800).copy(alpha = 0.4f)
        )
        VulnerabilitySeverity.SECURE -> listOf(
            Color(0xFF0F261C),
            StatusReady,
            StatusReady,
            StatusReady.copy(alpha = 0.35f)
        )
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(ObsidianSurface)
            .border(1.dp, cardBorder, RoundedCornerShape(16.dp))
            .clickable { isExpanded = !isExpanded }
            .padding(14.dp)
            .testTag("vuln_card_${finding.id}")
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Severity Pill
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(badgeBg)
                        .border(1.dp, badgeBorder, RoundedCornerShape(6.dp))
                        .padding(horizontal = 8.dp, vertical = 3.dp)
                ) {
                    Text(
                        text = finding.severity.name,
                        color = badgeText,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                }

                // Title & ID
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = finding.title,
                        color = TextWhite,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = finding.id,
                        color = ScopeGold,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Short Description
            Text(
                text = finding.description,
                fontSize = 11.sp,
                color = TextWhite.copy(alpha = 0.85f),
                lineHeight = 16.sp
            )

            // Expanded Remediation & Advisory Details
            AnimatedVisibility(
                visible = isExpanded,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                Column(modifier = Modifier.padding(top = 10.dp)) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .background(Color(0xFF0A0E17))
                            .border(1.dp, ScopeGold.copy(alpha = 0.25f), RoundedCornerShape(10.dp))
                            .padding(10.dp)
                    ) {
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Shield,
                                    contentDescription = null,
                                    tint = ScopeGold,
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "توصية التحصين والمعالجة (Remediation):",
                                    fontSize = 11.sp,
                                    color = ScopeGoldLight,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = finding.remediation,
                                fontSize = 11.sp,
                                color = TextMuted,
                                lineHeight = 15.sp
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = finding.cweReference,
                                fontSize = 9.sp,
                                color = StatusCyan,
                                fontFamily = FontFamily.Monospace
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Footer info / tap prompt
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (isExpanded) "انقر للطي ▲" else "انقر لعرض حلول الأمان ▼",
                    fontSize = 10.sp,
                    color = ScopeGold,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = finding.category,
                    fontSize = 10.sp,
                    color = TextDarker
                )
            }
        }
    }
}
