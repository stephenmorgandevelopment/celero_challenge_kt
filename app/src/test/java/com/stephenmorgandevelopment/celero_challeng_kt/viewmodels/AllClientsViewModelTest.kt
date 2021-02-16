package com.stephenmorgandevelopment.celero_challeng_kt.viewmodels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.stephenmorgandevelopment.celero_challeng_kt.repos.FakeClientRepo
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class AllClientsViewModelTest {

    private lateinit var viewModel: AllClientsViewModel

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        viewModel = AllClientsViewModel(FakeClientRepo())
    }

    @Test
    fun testRefreshListUpdatesDataReturnsSuccess() {


    }

}