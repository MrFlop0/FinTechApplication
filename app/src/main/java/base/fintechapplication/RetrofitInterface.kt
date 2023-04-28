package base.fintechapplication

import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface RetrofitInterface {

    @Headers("X-API-KEY: ${Constants.key}")
    @GET("films/top")
    fun getMovies(
        @Query("type") type: String,
        @Query("page") id: Int = 1
    ): Call<Movies>

    @GET("{path}")
    fun getByteArrayForImage(@Path("path") path: String): Call<ResponseBody>

    @Headers("X-API-KEY: ${Constants.key}")
    @GET("films/{id}")
    fun getMovieInfo(@Path("id") id: Long): Call<MoviePageInfo>
}