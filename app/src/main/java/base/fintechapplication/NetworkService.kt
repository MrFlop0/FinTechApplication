package base.fintechapplication

import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Binder
import android.os.IBinder
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.MapperFeature
import com.fasterxml.jackson.databind.json.JsonMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import kotlinx.coroutines.*
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException

class NetworkService : Service() {


    var movies = mutableListOf<MovieCard>()
    private val networkScope = CoroutineScope(Dispatchers.IO)
    private val retrofit = Retrofit.Builder()
        .baseUrl(Constants.endpoint)
        .addConverterFactory(ScalarsConverterFactory.create())
        .addConverterFactory(
            JacksonConverterFactory.create(
                JsonMapper
                    .builder()
                    .serializationInclusion(JsonInclude.Include.NON_NULL)
                    .configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true)
                    .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                    .build()
                    .registerModule(KotlinModule.Builder().build())
            )
        )
        .build()

    private val networkWalker = retrofit.create(RetrofitInterface::class.java)

    private val getCertainMoviePageInfo: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent) {
            val id = intent.getLongExtra("id", 0)
            networkScope.launch { withContext(Dispatchers.IO) { receiveFilmInfo(id) } }
        }
    }

    fun receiveFilmInfo(id: Long) {
        val response =
            kotlin.runCatching {
                val call = networkWalker.getMovieInfo(id).execute()


                val info = call.body()!!
                val byteArray =
                    networkWalker.getByteArrayForImage(info.poster)
                        .execute().body()!!.bytes()
                val highResBitmap =
                    BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)

                val imageName = id.toString()
                val image = File(this@NetworkService.cacheDir, imageName)
                image.createNewFile()
                try {
                    val out = FileOutputStream(image)
                    highResBitmap.compress(Bitmap.CompressFormat.PNG, 0, out)

                    out.flush()
                    out.close()
                } catch (e: FileNotFoundException) {
                    e.printStackTrace()

                } catch (e: IOException) {
                    e.printStackTrace()
                }

                val launch = Intent(applicationContext, MoviePage::class.java)
                applicationContext?.startActivity(launch)

                val newIntent = Intent(Constants.loadedInfo)
                newIntent.putExtra("name", info.name)
                newIntent.putExtra("link", imageName)
                newIntent.putExtra("desc", info.description)
                newIntent.putExtra("genres", info.Genres.joinToString(", "))
                newIntent.putExtra("countries", info.Countries.joinToString(", "))
                LocalBroadcastManager.getInstance(this).sendBroadcast(newIntent)

            }
        if (response.isFailure) {
            val fintent = Intent(Constants.intentFailure)
            LocalBroadcastManager.getInstance(this).sendBroadcast(fintent)
        }
    }


    fun receiveMovies() {
        networkScope.launch {
            withContext(Dispatchers.IO) {
                val response = kotlin.runCatching {
                    repeat(5) {
                        val call = networkWalker.getMovies(Constants.type, it + 1).execute()


                        call.body()!!.movies.forEach {
                            val byteArray =
                                networkWalker.getByteArrayForImage(it.link)
                                    .execute().body()!!.bytes()
                            val highResBitmap =
                                BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
                            it.Image = highResBitmap
                        }
                        movies += call.body()!!.movies

                    }
                    val intent = Intent(Constants.intentPopular)
                    LocalBroadcastManager.getInstance(this@NetworkService).sendBroadcast(intent)
                }

                if (response.isFailure) {
                    val fintent = Intent(Constants.intentFailure)
                    LocalBroadcastManager.getInstance(this@NetworkService).sendBroadcast(fintent)
                }
            }
        }

    }


    override fun onCreate() {
        super.onCreate()

        receiveMovies()

        LocalBroadcastManager.getInstance(this).registerReceiver(
            getCertainMoviePageInfo, IntentFilter(Constants.getMovieInfo)
        )
    }


    override fun onDestroy() {
        super.onDestroy()
        networkScope.cancel()
        LocalBroadcastManager.getInstance(this).unregisterReceiver(getCertainMoviePageInfo)
    }

    override fun onBind(p0: Intent?): IBinder {
        return MyBinder()
    }

    inner class MyBinder : Binder() {
        fun getMyService() = this@NetworkService
    }
}