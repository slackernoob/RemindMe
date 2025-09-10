package com.example.remindme.di

import android.content.Context
import com.example.remindme.data.TaskDao
import com.example.remindme.data.TaskDatabase
import com.example.remindme.ui.viewmodel.TaskViewModel
import org.junit.Before
import io.mockk.*
import junit.framework.TestCase.assertNotNull
import org.junit.Test


class DatabaseModuleTest {
    private lateinit var mockDao: TaskDao
    private lateinit var viewModel: TaskViewModel
    private lateinit var context: Context

    @Before
    fun setup() {
        mockDao = mockk(relaxed = true)
        viewModel = TaskViewModel(mockDao)
        context = mockk(relaxed = true)
    }

    @Test
    fun dao_is_injected_into_viewModel() {
        assertNotNull(viewModel)
        assertNotNull(mockDao)
    }

    @Test
    fun provideDatabase_returns_taskDatabase() {
        val db = DatabaseModule.provideDatabase(context)
        assert(db is TaskDatabase)
    }

    @Test
    fun provideTaskDao_returns_taskDao() {
        val db = DatabaseModule.provideDatabase(context)
//        every { db.taskDao() } returns mockDao
        val dao = DatabaseModule.provideTaskDao(db)
        assert(dao == db.taskDao())
    }
}