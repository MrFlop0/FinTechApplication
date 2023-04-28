package base.fintechapplication.databases

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query

@Dao
interface DAO {

    @Insert(onConflict = REPLACE)
    fun insert(item: Entity)

    @Delete
    fun delete(item: Entity)

    @Insert
    fun insert(list: List<Entity>)

    @Delete
    fun delete(list: List<Entity>)

    @Query("SELECT * FROM DB")
    fun getAll(): List<Entity>
}