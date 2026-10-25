package com.example.ui.components

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import androidx.compose.foundation.Image
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
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Android
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.InspectedApp
import com.example.ui.theme.ObsidianCardBorder
import com.example.ui.theme.ObsidianSurface
import com.example.ui.theme.ScopeGold
import com.example.ui.theme.ScopeRed
import com.example.ui.theme.ScopeRedDark
import com.example.ui.theme.StatusCyan
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextWhite

@Composable
fun AppListItem(
    app: InspectedApp,
    onInspectClick: (InspectedApp) -> Unit,
    modifier: Modifier = Modifier
) {
    val bitmap = remember(app.iconDrawable) {
        app.iconDrawable?.let { drawableToBitmap(it) }
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .background(ObsidianSurface)
            .border(
                width = 1.dp,
                color = if (app.hasBillingPermission) ScopeRed.copy(alpha = 0.45f) else ObsidianCardBorder,
                shape = RoundedCornerShape(18.dp)
            )
            .clickable { onInspectClick(app) }
            .padding(14.dp)
            .testTag("app_item_${app.packageName.replace('.', '_')}")
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // App Icon or Monogram
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(Color(0xFF1B2335))
                        .border(
                            1.dp,
                            Brush.linearGradient(listOf(ScopeGold.copy(alpha = 0.6f), ScopeRed.copy(alpha = 0.6f))),
                            RoundedCornerShape(14.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    if (bitmap != null) {
                        Image(
                            bitmap = bitmap.asImageBitmap(),
                            contentDescription = app.name,
                            modifier = Modifier
                                .size(40.dp)
                                .clip(RoundedCornerShape(10.dp))
                        )
                    } else {
                        Text(
                            text = app.name.take(1).uppercase(),
                            color = ScopeGold,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Black
                        )
                    }
                }

                Spacer(modifier = Modifier.width(12.dp))

                // App details
                Column(modifier = Modifier.weight(1f)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = app.name,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextWhite,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.weight(1f, fill = false)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = app.sizeFormatted,
                            fontSize = 11.sp,
                            color = ScopeGold,
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    Spacer(modifier = Modifier.height(3.dp))

                    Text(
                        text = app.packageName,
                        fontSize = 11.sp,
                        color = TextMuted,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    // Badges row
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        // Billing Badge
                        if (app.hasBillingPermission) {
                            BadgePill(
                                label = "BILLING DETECTED",
                                bgColor = ScopeRed.copy(alpha = 0.15f),
                                textColor = ScopeRed,
                                borderColor = ScopeRed.copy(alpha = 0.4f)
                            )
                        }

                        // Type Badge
                        BadgePill(
                            label = if (app.isSystemApp) "SYSTEM" else "USER",
                            bgColor = if (app.isSystemApp) Color(0xFF222B3D) else StatusCyan.copy(alpha = 0.12f),
                            textColor = if (app.isSystemApp) TextMuted else StatusCyan,
                            borderColor = if (app.isSystemApp) ObsidianCardBorder else StatusCyan.copy(alpha = 0.3f)
                        )

                        // Analyzability Badge
                        BadgePill(
                            label = "${app.analyzabilityScore}% جاهز للفحص",
                            bgColor = ScopeGold.copy(alpha = 0.12f),
                            textColor = ScopeGold,
                            borderColor = ScopeGold.copy(alpha = 0.35f)
                        )

                        // Version Badge
                        BadgePill(
                            label = "v${app.versionName}",
                            bgColor = Color(0xFF1B2232),
                            textColor = TextMuted,
                            borderColor = ObsidianCardBorder
                        )
                    }
                }

                Spacer(modifier = Modifier.width(8.dp))

                // Inspect button (iOS Pill with red gradient)
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(
                            Brush.horizontalGradient(
                                listOf(ScopeRed, ScopeRedDark)
                            )
                        )
                        .padding(horizontal = 10.dp, vertical = 7.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "فحص",
                        color = Color.White,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
fun BadgePill(
    label: String,
    bgColor: Color,
    textColor: Color,
    borderColor: Color
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(6.dp))
            .background(bgColor)
            .border(1.dp, borderColor, RoundedCornerShape(6.dp))
            .padding(horizontal = 6.dp, vertical = 2.dp)
    ) {
        Text(
            text = label,
            color = textColor,
            fontSize = 9.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

private fun drawableToBitmap(drawable: Drawable): Bitmap {
    if (drawable is BitmapDrawable && drawable.bitmap != null) {
        return drawable.bitmap
    }
    val width = if (drawable.intrinsicWidth > 0) drawable.intrinsicWidth else 48
    val height = if (drawable.intrinsicHeight > 0) drawable.intrinsicHeight else 48
    val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)
    drawable.setBounds(0, 0, canvas.width, canvas.height)
    drawable.draw(canvas)
    return bitmap
}
