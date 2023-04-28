package base.fintechapplication

import com.fasterxml.jackson.annotation.JsonProperty

data class Movies(
    @JsonProperty("pagesCount")
    val number: Int,
    @JsonProperty("films")
    val movies: MutableList<MovieCard>
)