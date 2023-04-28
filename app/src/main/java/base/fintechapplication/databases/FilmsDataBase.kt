package base.fintechapplication.databases

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase

abstract class FilmsDataBase : RoomDatabase() {
    abstract fun moviesDAO(): DAO

    companion object {

        @Volatile
        private var INSTANCE: FilmsDataBase? = null

        fun getDatabase(context: Context): FilmsDataBase {
            if (INSTANCE == null) {
                synchronized(this) {
                    INSTANCE = buildDatabase(context)
                }
            }
            return INSTANCE!!
        }

        private fun buildDatabase(context: Context): FilmsDataBase {
            return Room.databaseBuilder(
                context.applicationContext,
                FilmsDataBase::class.java,
                "FilmsDataBase"
            ).build()
        }
    }
}