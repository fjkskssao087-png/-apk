package com.example.model

import android.graphics.drawable.Drawable

enum class AppFilter {
    ALL, USER, SYSTEM, BILLING_ONLY
}

data class DeepBillingTarget(
    val name: String,
    val location: String,
    val methodSignature: String,
    val codeSnippet: String,
    val beginnerExplanation: String,
    val geniusExplanation: String,
    val hardeningFix: String,
    val severity: String = "CRITICAL",
    val confidence: String = "92%",
    val category: String = "Google Play Billing Engine"
)

data class PackageFileItem(
    val name: String,
    val path: String,
    val category: String, // DEX Bytecode, Manifest, Resources, Native SO, Assets
    val sizeFormatted: String,
    val status: String,
    val riskLevel: String = "آمن",
    val isCritical: Boolean = false
)

data class DeepScanProgress(
    val isScanning: Boolean = false,
    val currentFile: String = "",
    val scannedFilesCount: Int = 0,
    val totalFilesCount: Int = 48,
    val progressFraction: Float = 0f,
    val currentStage: String = "",
    val recentLogs: List<String> = emptyList()
)

data class AiAnalysisResult(
    val understanding: String,
    val deepAnalysis: String,
    val autoCorrection: String,
    val securePatchCode: String,
    val confidenceScore: Int = 98,
    val isAiGenerated: Boolean = true
)

data class InspectorSettings(
    val autoAiAnalysis: Boolean = true,
    val deepDecompileSmali: Boolean = true,
    val scanNativeLibs: Boolean = true,
    val scanResources: Boolean = true,
    val generateDiffPatch: Boolean = true,
    val safetyMode: Boolean = true,
    val aiReasoningLevel: String = "خبير معماري (Deep Reasoning)"
)

data class InspectedApp(
    val name: String,
    val packageName: String,
    val versionName: String,
    val versionCode: Long,
    val isSystemApp: Boolean,
    val apkPath: String,
    val sizeFormatted: String,
    val targetSdk: Int,
    val minSdk: Int,
    val permissions: List<String>,
    val activities: List<String>,
    val services: List<String>,
    val dexUnits: Int = 1,
    val hasBillingPermission: Boolean = false,
    val sha256Short: String = "",
    val md5Short: String = "",
    val iconDrawable: Drawable? = null,
    val isAnalyzable: Boolean = true,
    val analyzabilityScore: Int = 100,
    val analysisReason: String = "جاهز لفحص بايت كود DEX واستخراج دوال الشراء",
    val deepBillingTargets: List<DeepBillingTarget> = emptyList(),
    val packageFiles: List<PackageFileItem> = emptyList(),
    val aiAnalysis: AiAnalysisResult? = null
)

data class InspectionStat(
    val title: String,
    val count: String,
    val highlightColorHex: Long
)

