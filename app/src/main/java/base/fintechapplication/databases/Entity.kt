package base.fintechapplication.databases

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "FilmsDataBase")
class Entity(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val name: String,
    val year: String?,
    val genres: String,
    val link: String,
    val pathToImage: String
)
