package base.fintechapplication

import com.fasterxml.jackson.annotation.JsonProperty

data class MoviePageInfo(
    @JsonProperty("kinopoiskId")
    val id: Long,
    @JsonProperty("nameRu")
    val name: String,
    @JsonProperty("posterUrl")
    val poster: String,
    @JsonProperty("description")
    val description: String,
    @JsonProperty("genres")
    val Genres: MutableList<Genre>,
    @JsonProperty("countries")
    val Countries: MutableList<Country>

)

class Country(
    @JsonProperty("country")
    private val country: String
) {
    override fun toString(): String {
        return country
    }
}