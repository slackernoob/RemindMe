package com.example.remindme.di

import com.example.remindme.data.TaskDao
import com.example.remindme.viewmodel.TaskViewModel
import org.junit.Before
import io.mockk.*
import junit.framework.TestCase.assertNotNull
import org.junit.Test


class DatabaseModuleTest {
    private lateinit var mockDao: TaskDao
    private lateinit var viewModel: TaskViewModel

    @Before
    fun setup() {
        mockDao = mockk(relaxed = true)
        viewModel = TaskViewModel(mockDao)
    }

    @Test
    fun dao_is_injected_into_viewModel() {
        assertNotNull(viewModel)
        assertNotNull(mockDao)
    }
}