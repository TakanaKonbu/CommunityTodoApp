package com.takanakonbu.communitytodoapp.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(entities = [Todo::class], version = 2, exportSchema = false)
@TypeConverters(Converters::class)
abstract class TodoDatabase : RoomDatabase() {
    abstract fun todoDao(): TodoDao

    companion object {
        @Volatile
        private var INSTANCE: TodoDatabase? = null

        // バージョン1から2へのマイグレーション定義
        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // 完了予定日カラムの追加
                database.execSQL("ALTER TABLE todos ADD COLUMN dueDate TEXT")
            }
        }

        fun getDatabase(context: Context): TodoDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    TodoDatabase::class.java,
                    "todo_database.db"
                )
                    .addMigrations(MIGRATION_1_2) // マイグレーションを追加
                    .fallbackToDestructiveMigration() // 最悪の場合のフォールバック
                    .build()

                INSTANCE = instance
                instance
            }
        }
    }
}