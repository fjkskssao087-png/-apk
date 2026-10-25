package com.example

import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.togetherWith
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.FileOpen
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
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
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AppFilter
import com.example.ui.components.AppDetailSheet
import com.example.ui.components.AppListItem
import com.example.ui.components.GuideDialog
import com.example.ui.components.HeaderBar
import com.example.ui.components.ShimmerAppListPlaceholder
import com.example.ui.components.StatsOverview
import com.example.ui.theme.ObsidianBg
import com.example.ui.theme.ObsidianCardBorder
import com.example.ui.theme.ObsidianSurface
import com.example.ui.theme.ScopeDexTheme
import com.example.ui.theme.ScopeGold
import com.example.ui.theme.ScopeGoldLight
import com.example.ui.theme.ScopeRed
import com.example.ui.theme.ScopeRedDark
import com.example.ui.theme.StatusCyan
import com.example.ui.theme.TextDarker
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextWhite
import com.example.viewmodel.AppInspectorViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ScopeDexTheme {
                MainScreen()
            }
        }
    }
}

@Composable
fun MainScreen(viewModel: AppInspectorViewModel = viewModel()) {
    val state by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current

    // File picker launcher for APKs
    val apkPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri: Uri? ->
        uri?.let { viewModel.inspectCustomApkUri(it) }
    }

    LaunchedEffect(state.statusMessage) {
        state.statusMessage?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            viewModel.clearStatusMessage()
        }
    }

    // Easy Back navigation for system back button
    BackHandler(enabled = state.selectedApp != null || state.showGuide || state.searchQuery.isNotEmpty()) {
        when {
            state.selectedApp != null -> viewModel.selectApp(null)
            state.showGuide -> viewModel.toggleGuide(false)
            state.searchQuery.isNotEmpty() -> viewModel.onSearchQueryChanged("")
        }
    }

    Scaffold(
        containerColor = ObsidianBg,
        contentWindowInsets = WindowInsets(0, 0, 0, 0)
    ) { _ ->
        val statusBarPadding = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()
        val navBarPadding = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = statusBarPadding + 10.dp, bottom = navBarPadding + 16.dp)
                .padding(horizontal = 16.dp)
                .testTag("main_screen_list")
        ) {
            // Header Bar
            item {
                HeaderBar(
                    onRefresh = { viewModel.loadInstalledApps() },
                    onOpenGuide = { viewModel.toggleGuide(true) }
                )
                Spacer(modifier = Modifier.height(14.dp))
            }

            // 2. Search Bar at Top of Application List Screen (Quick Filter by Name)
            item {
                Column {
                    OutlinedTextField(
                        value = state.searchQuery,
                        onValueChange = { viewModel.onSearchQueryChanged(it) },
                        placeholder = {
                            Text(
                                text = "ابحث عن تطبيق بالاسم (مثال: Proton, Photoshop)...",
                                color = TextDarker,
                                fontSize = 13.sp
                            )
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = "بحث",
                                tint = ScopeGold,
                                modifier = Modifier.size(20.dp)
                            )
                        },
                        trailingIcon = {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(end = 4.dp)
                            ) {
                                if (state.searchQuery.isNotEmpty()) {
                                    IconButton(
                                        onClick = { viewModel.onSearchQueryChanged("") },
                                        modifier = Modifier.size(28.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Clear,
                                            contentDescription = "مسح البحث",
                                            tint = ScopeRed,
                                            modifier = Modifier.size(16.dp)
                                        )
                                    }
                                }
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(Color(0xFF1B2436))
                                        .padding(horizontal = 7.dp, vertical = 3.dp)
                                ) {
                                    Text(
                                        text = "${state.filteredApps.size}",
                                        color = ScopeGold,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                        keyboardActions = KeyboardActions(onSearch = { focusManager.clearFocus() }),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = ObsidianSurface,
                            unfocusedContainerColor = ObsidianSurface,
                            focusedBorderColor = ScopeGold,
                            unfocusedBorderColor = ObsidianCardBorder,
                            focusedTextColor = TextWhite,
                            unfocusedTextColor = TextWhite,
                            cursorColor = ScopeRed
                        ),
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("top_search_bar")
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // iOS Segmented Control Tabs
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(14.dp))
                            .background(ObsidianSurface)
                            .border(1.dp, ObsidianCardBorder, RoundedCornerShape(14.dp))
                            .padding(4.dp),
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        SegmentTab(
                            title = "الكل (${state.displayTotalApps})",
                            isSelected = state.activeFilter == AppFilter.ALL,
                            onClick = { viewModel.onFilterChanged(AppFilter.ALL) },
                            modifier = Modifier.weight(1f)
                        )
                        SegmentTab(
                            title = "المثبتة (${state.displayUserApps})",
                            isSelected = state.activeFilter == AppFilter.USER,
                            onClick = { viewModel.onFilterChanged(AppFilter.USER) },
                            modifier = Modifier.weight(1f)
                        )
                        SegmentTab(
                            title = "دوال الشراء (${state.displayBillingApps})",
                            isSelected = state.activeFilter == AppFilter.BILLING_ONLY,
                            onClick = { viewModel.onFilterChanged(AppFilter.BILLING_ONLY) },
                            modifier = Modifier.weight(1.1f)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(14.dp))
            }

            // Target Package / File Input Section
            item {
                TargetSelectorCard(
                    selectedAppName = state.selectedApp?.name,
                    onPickFileClick = {
                        try {
                            apkPickerLauncher.launch(arrayOf("application/vnd.android.package-archive", "*/*"))
                        } catch (e: Exception) {
                            Toast.makeText(context, "الرجاء تحديد ملف APK", Toast.LENGTH_SHORT).show()
                        }
                    },
                    onInspectTargetClick = {
                        val firstApp = state.filteredApps.firstOrNull()
                        if (firstApp != null) {
                            viewModel.selectApp(firstApp)
                        } else {
                            Toast.makeText(context, "الرجاء اختيار تطبيق من القائمة أولاً", Toast.LENGTH_SHORT).show()
                        }
                    }
                )
                Spacer(modifier = Modifier.height(14.dp))
            }

            // Stats Overview with Radial Gauge & Zero-State Audit Activation
            item {
                StatsOverview(
                    totalApps = state.displayTotalApps,
                    userApps = state.displayUserApps,
                    billingApps = state.displayBillingApps,
                    isScanStarted = state.isScanStarted,
                    onStartScan = { viewModel.startComprehensiveAudit() },
                    onResetZero = { viewModel.resetStatisticsToZero() }
                )
                Spacer(modifier = Modifier.height(16.dp))
            }

            // List Title & Counter
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "قائمة الحزم المتاحة للفحص",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextWhite
                    )
                    Text(
                        text = if (state.isScanStarted) "${state.filteredApps.size} حزمة" else "0 حزمة (في وضع الاستعداد)",
                        fontSize = 12.sp,
                        color = ScopeGold,
                        fontWeight = FontWeight.SemiBold
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
            }

            // Loading Shimmer or Filtered App List with Smooth Transition Animations
            item {
                AnimatedContent(
                    targetState = state.activeFilter,
                    transitionSpec = {
                        fadeIn(animationSpec = tween(280)) + slideInVertically(animationSpec = tween(280)) { 35 } togetherWith
                                fadeOut(animationSpec = tween(180))
                    },
                    label = "filterStateTransition"
                ) { _ ->
                    Column {
                        if (state.isLoading) {
                            ShimmerAppListPlaceholder()
                        } else if (state.filteredApps.isEmpty()) {
                            EmptyAppsPlaceholder(searchQuery = state.searchQuery)
                        } else {
                            state.filteredApps.forEach { app ->
                                AppListItem(
                                    app = app,
                                    onInspectClick = { viewModel.selectApp(it) }
                                )
                                Spacer(modifier = Modifier.height(10.dp))
                            }
                        }
                    }
                }
            }
        }
    }

    // Detail Bottom Sheet when an app is selected
    state.selectedApp?.let { app ->
        AppDetailSheet(
            app = app,
            onDismiss = { viewModel.selectApp(null) }
        )
    }

    // Guide Dialog
    if (state.showGuide) {
        GuideDialog(onDismiss = { viewModel.toggleGuide(false) })
    }
}

@Composable
fun TargetSelectorCard(
    selectedAppName: String?,
    onPickFileClick: () -> Unit,
    onInspectTargetClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .background(ObsidianSurface)
            .border(1.dp, ObsidianCardBorder, RoundedCornerShape(18.dp))
            .padding(14.dp)
            .testTag("target_selector_card")
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Security,
                        contentDescription = null,
                        tint = StatusCyan,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "مسار الحزمة المستهدفة",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextWhite
                    )
                }
                Text(
                    text = "APK / DEX",
                    fontSize = 10.sp,
                    color = ScopeGold,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Inspect Button (Purple / Red Gradient)
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(
                            Brush.horizontalGradient(
                                listOf(Color(0xFF6B46C1), Color(0xFF553C9A))
                            )
                        )
                        .clickable(onClick = onInspectTargetClick)
                        .padding(horizontal = 14.dp, vertical = 12.dp)
                        .testTag("inspect_target_button"),
                    contentAlignment = Alignment.Center
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.PlayArrow,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "فحص الهدف",
                            color = Color.White,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                // File path display & pick button
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFF0F1522))
                        .border(1.dp, ObsidianCardBorder, RoundedCornerShape(12.dp))
                        .clickable(onClick = onPickFileClick)
                        .padding(horizontal = 12.dp, vertical = 10.dp)
                        .testTag("pick_apk_button")
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = selectedAppName ?: "أدخل مسار الملف (.apk / .dex)...",
                            color = if (selectedAppName != null) TextWhite else TextDarker,
                            fontSize = 12.sp,
                            maxLines = 1
                        )
                        Icon(
                            imageVector = Icons.Default.Folder,
                            contentDescription = "اختيار ملف",
                            tint = ScopeGold,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun SegmentTab(
    title: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val tabModifier = if (isSelected) {
        modifier
            .clip(RoundedCornerShape(10.dp))
            .background(Brush.horizontalGradient(listOf(ScopeRed, ScopeRedDark)))
    } else {
        modifier
            .clip(RoundedCornerShape(10.dp))
            .background(Color.Transparent)
    }

    Box(
        modifier = tabModifier
            .clickable(onClick = onClick)
            .padding(vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = title,
            color = if (isSelected) Color.White else TextMuted,
            fontSize = 12.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
        )
    }
}

@Composable
fun EmptyAppsPlaceholder(searchQuery: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .background(ObsidianSurface)
            .border(1.dp, ObsidianCardBorder, RoundedCornerShape(18.dp))
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                imageVector = Icons.Default.FileOpen,
                contentDescription = null,
                tint = ScopeGold,
                modifier = Modifier.size(36.dp)
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = if (searchQuery.isNotEmpty()) "لا توجد نتائج تطابق \"$searchQuery\""
                else "لا توجد تطبيقات في هذه الفئة",
                color = TextWhite,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "يمكنك النقر على زر المجلد لاختيار ملف APK خارجي وفحصه مباشرة",
                color = TextMuted,
                fontSize = 11.sp
            )
        }
    }
}
