package com.example

import android.app.Application
import androidx.test.core.app.ApplicationProvider
import com.example.model.AppFilter
import com.example.viewmodel.AppInspectorViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.shadows.ShadowLooper

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class AppInspectorViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `viewModel can be instantiated with single application parameter by AndroidViewModelFactory`() {
        val app = ApplicationProvider.getApplicationContext<Application>()
        val viewModel = AppInspectorViewModel(app)
        assertNotNull(viewModel)
    }

    @Test
    fun `viewModel loads and filters only analyzable applications`() = runTest(testDispatcher) {
        val app = ApplicationProvider.getApplicationContext<Application>()
        val viewModel = AppInspectorViewModel(app, testDispatcher)

        // Advance coroutines and shadow looper to allow IO dispatch and state update
        advanceUntilIdle()
        ShadowLooper.idleMainLooper()
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertNotNull(state)

        // All filtered apps must be valid and analyzable
        val filtered = state.filteredApps
        assertTrue("Expected non-empty list of analyzable apps", filtered.isNotEmpty())
        assertTrue("All displayed apps must be analyzable by ScopeDex", filtered.all { it.isAnalyzable })

        // Check search query filtering
        viewModel.onSearchQueryChanged("Proton")
        advanceUntilIdle()
        val searchResults = viewModel.uiState.value.filteredApps
        assertTrue(searchResults.all { it.name.contains("Proton", ignoreCase = true) })

        // Check billing filter
        viewModel.onFilterChanged(AppFilter.BILLING_ONLY)
        advanceUntilIdle()
        val billingResults = viewModel.uiState.value.filteredApps
        assertTrue(billingResults.all { it.hasBillingPermission })
    }
}
