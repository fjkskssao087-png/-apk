package com.example.ui.components

import android.widget.Toast
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.RestartAlt
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.model.InspectorSettings
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

@Composable
fun SettingsDialog(
    currentSettings: InspectorSettings,
    onSaveSettings: (InspectorSettings) -> Unit,
    onResetStatsZero: () -> Unit,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    var settings by remember { mutableStateOf(currentSettings) }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(0.92f)
                .clip(RoundedCornerShape(22.dp))
                .background(ObsidianBg)
                .border(
                    1.dp,
                    Brush.horizontalGradient(listOf(ScopeGold, ScopeRed)),
                    RoundedCornerShape(22.dp)
                )
                .padding(20.dp)
                .testTag("settings_dialog")
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = null,
                            tint = ScopeGold,
                            modifier = Modifier.size(22.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "إعدادات الفحص المتقدمة والذكاء الاصطناعي",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextWhite
                        )
                    }
                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.size(28.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "إغلاق",
                            tint = TextMuted
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Section 1: AI Features
                SettingsSectionHeader(title = "محرك الذكاء الاصطناعي (ScopeAI Core)", icon = Icons.Default.AutoAwesome)
                Spacer(modifier = Modifier.height(8.dp))

                SettingToggleRow(
                    title = "التشغيل التلقائي للذكاء الاصطناعي",
                    description = "يفهم ويحلل ويصحح الكود بمجرد انتهاء فحص الحزمة",
                    checked = settings.autoAiAnalysis,
                    onCheckedChange = { settings = settings.copy(autoAiAnalysis = it) }
                )

                SettingToggleRow(
                    title = "توليد ملفات الباتش المحصنة (.kt)",
                    description = "صياغة كود تصحيح كامل لمنع التجاوز المحلي للاشتراكات",
                    checked = settings.generateDiffPatch,
                    onCheckedChange = { settings = settings.copy(generateDiffPatch = it) }
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Section 2: File & Deep Inspection Features
                SettingsSectionHeader(title = "ملفات الفحص المجهري (Deep Inspection)", icon = Icons.Default.Folder)
                Spacer(modifier = Modifier.height(8.dp))

                SettingToggleRow(
                    title = "تفكيك وتدقيق بايت كود Smali",
                    description = "فحص متعمق لتعليمات DEX ومعاملات الدفع في الذاكرة",
                    checked = settings.deepDecompileSmali,
                    onCheckedChange = { settings = settings.copy(deepDecompileSmali = it) }
                )

                SettingToggleRow(
                    title = "فحص مكتبات لغة الآلة Native (.so)",
                    description = "فحص أمان الدوال المكتوبة بـ C/C++ والتحقق من التشفير",
                    checked = settings.scanNativeLibs,
                    onCheckedChange = { settings = settings.copy(scanNativeLibs = it) }
                )

                SettingToggleRow(
                    title = "فحص الموارد وجداول النصوص (resources.arsc)",
                    description = "استخراج المفاتيح المشفرة، النصوص، وروابط خوادم الدفع",
                    checked = settings.scanResources,
                    onCheckedChange = { settings = settings.copy(scanResources = it) }
                )

                Spacer(modifier = Modifier.height(18.dp))

                // Zero-Stats Reset Button inside Settings
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFF1F1618))
                        .border(1.dp, ScopeRed.copy(alpha = 0.5f), RoundedCornerShape(12.dp))
                        .clickable {
                            onResetStatsZero()
                            Toast.makeText(context, "تم تصفير كافة الإحصائيات (0) بنجاح!", Toast.LENGTH_SHORT).show()
                            onDismiss()
                        }
                        .padding(12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.RestartAlt,
                                contentDescription = null,
                                tint = ScopeRed,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text(
                                    text = "تصفير لوحة المؤشرات (0)",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = ScopeRed
                                )
                                Text(
                                    text = "إعادة ضبط العدادات إلى الصفر قبل بدء الفحص التالي",
                                    fontSize = 10.sp,
                                    color = TextMuted
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                // Save Changes Button
                Button(
                    onClick = {
                        onSaveSettings(settings)
                        Toast.makeText(context, "تم حفظ الإعدادات وتطبيق التعديلات بنجاح!", Toast.LENGTH_SHORT).show()
                        onDismiss()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("save_settings_button"),
                    colors = ButtonDefaults.buttonColors(containerColor = ScopeGold),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = null,
                            tint = Color.Black,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "حفظ وتطبيق التعديلات",
                            color = Color.Black,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun SettingsSectionHeader(title: String, icon: androidx.compose.ui.graphics.vector.ImageVector) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = ScopeGold,
            modifier = Modifier.size(15.dp)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
            text = title,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = ScopeGold
        )
    }
}

@Composable
private fun SettingToggleRow(
    title: String,
    description: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(ObsidianSurface)
            .border(1.dp, ObsidianCardBorder, RoundedCornerShape(10.dp))
            .clickable { onCheckedChange(!checked) }
            .padding(horizontal = 12.dp, vertical = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = TextWhite
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = description,
                fontSize = 10.sp,
                color = TextMuted,
                lineHeight = 14.sp
            )
        }
        Spacer(modifier = Modifier.width(8.dp))
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.Black,
                checkedTrackColor = ScopeGold,
                uncheckedThumbColor = TextMuted,
                uncheckedTrackColor = Color(0xFF1E2838)
            )
        )
    }
    Spacer(modifier = Modifier.height(8.dp))
}
