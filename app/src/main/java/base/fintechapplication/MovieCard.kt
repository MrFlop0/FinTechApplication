package base.fintechapplication

import android.graphics.Bitmap
import com.fasterxml.jackson.annotation.JsonProperty

data class MovieCard(

    @JsonProperty("filmId")
    val id: Long,
    var Image: Bitmap? = null,
    @JsonProperty("nameRu")
    val Name: String,
    @JsonProperty("year")
    val Year: String?,
    @JsonProperty("genres")
    val Genres: MutableList<Genre>,
    @JsonProperty("posterUrl")
    val link: String
)


