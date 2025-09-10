package com.example.remindme.di

import android.content.Context
import androidx.room.Room
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.example.remindme.data.TaskDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


//@Module
//@InstallIn(SingletonComponent::class)
//object DatabaseModule {
//    @Provides
//    @Singleton
//    fun provideDatabase(@ApplicationContext context: Context): TaskDatabase =
//        Room.databaseBuilder(context, TaskDatabase::class.java, "task_database")
//            .build()
//
//    @Provides
//    fun provideTaskDao(database: TaskDatabase) = database.taskDao()
//}

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): TaskDatabase =
        TaskDatabase(
            driver = AndroidSqliteDriver(TaskDatabase.Schema, context, "task_database")
        )

    @Provides
    fun provideTaskQueries(db: TaskDatabase) = db.taskQueries
}
