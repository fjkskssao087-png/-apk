package com.example.ui.components

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.ui.theme.ObsidianBg
import com.example.ui.theme.ObsidianCardBorder
import com.example.ui.theme.ObsidianSurface
import com.example.ui.theme.ScopeGold
import com.example.ui.theme.ScopeRed
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextWhite

@Composable
fun GuideDialog(
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(24.dp))
                .background(ObsidianBg)
                .border(
                    width = 1.dp,
                    brush = Brush.verticalGradient(
                        listOf(ScopeRed.copy(alpha = 0.6f), ScopeGold.copy(alpha = 0.6f))
                    ),
                    shape = RoundedCornerShape(24.dp)
                )
                .padding(20.dp)
                .testTag("guide_dialog")
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
                            imageVector = Icons.AutoMirrored.Filled.MenuBook,
                            contentDescription = null,
                            tint = ScopeGold,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "دليل التشغيل والفحص",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextWhite
                        )
                    }
                    IconButton(onClick = onDismiss) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "إغلاق",
                            tint = TextMuted
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                GuideSection(
                    title = "1. فحص التطبيقات المثبتة",
                    description = "يقوم التطبيق بقراءة حزم التطبيقات الموجودة على الهاتف واستخراج بيانات الإصدار، الحجم، والأذونات الممنوحة عبر الـ PackageManager."
                )

                Spacer(modifier = Modifier.height(12.dp))

                GuideSection(
                    title = "2. فحص ملفات APK الخارجية",
                    description = "يمكنك اختيار أي ملف .apk مخزن في جهازك عبر زر الفحص لاستخراج تفاصيل الحزمة وشهادات التوقيع وتفاصيل SDK."
                )

                Spacer(modifier = Modifier.height(12.dp))

                GuideSection(
                    title = "3. آلية اكتشاف خدمات الشراء (Billing Detection)",
                    description = "يتم التحقق من وجود إذن com.android.vending.BILLING ومكتبات Google Play Billing وفحص دوال المعاملات القياسية مثل acknowledgePurchase و onPurchasesUpdated لتقييم بنية الشراء داخل التطبيق."
                )

                Spacer(modifier = Modifier.height(12.dp))

                GuideSection(
                    title = "4. أفضل الممارسات الأمنية للمطورين",
                    description = "لتأمين المشتريات والاشتراكات، يُنصح دائماً بالتحقق من الـ Purchase Tokens عبر الخادم (Server-Side Verification) باستخدام واجهة برمجة Google Play Developer API بدلاً من الاعتماد على التحقق المحلي فقط."
                )

                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = onDismiss,
                    colors = ButtonDefaults.buttonColors(containerColor = ScopeRed),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("guide_close_button")
                ) {
                    Text(
                        text = "فهمت ذلك",
                        color = Color.White,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
private fun GuideSection(title: String, description: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(ObsidianSurface)
            .border(1.dp, ObsidianCardBorder, RoundedCornerShape(14.dp))
            .padding(12.dp)
    ) {
        Column {
            Text(
                text = title,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = ScopeGold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = description,
                fontSize = 12.sp,
                color = TextWhite.copy(alpha = 0.85f),
                lineHeight = 18.sp
            )
        }
    }
}
