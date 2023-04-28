package base.fintechapplication

import com.fasterxml.jackson.annotation.JacksonAnnotation
import com.fasterxml.jackson.annotation.JsonProperty


class Genre(
    @JsonProperty("genre")
    private val genre: String
) {
    override fun toString(): String {
        return genre
    }
}