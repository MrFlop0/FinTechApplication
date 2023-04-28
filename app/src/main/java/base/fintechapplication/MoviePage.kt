package base.fintechapplication

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.PersistableBundle
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException

class MoviePage : AppCompatActivity() {


    private val getData: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent) {
            name.text = intent.getStringExtra("name")
            description.text = intent.getStringExtra("desc")
            genres.text = intent.getStringExtra("genres")
            countries.text = intent.getStringExtra("countries")

            link = intent.getStringExtra("link")!!
            uploadImage()
        }
    }

    private fun uploadImage() {
        val picture = File(this@MoviePage.cacheDir, link)

        var fis: FileInputStream? = null
        try {
            fis = FileInputStream(picture)
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }

        val bitmap = BitmapFactory.decodeStream(fis)
        preview.setImageBitmap(bitmap)
    }

    lateinit var link: String
    lateinit var preview: ImageView
    lateinit var name: TextView
    lateinit var description: TextView
    lateinit var genres: TextView
    lateinit var countries: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.movie_page)

        WindowCompat.setDecorFitsSystemWindows(window, false)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )

        preview = findViewById(R.id.Preview)
        name = findViewById(R.id.name)
        description = findViewById(R.id.desc)
        genres = findViewById(R.id.genres)
        countries = findViewById(R.id.countries)

        LocalBroadcastManager.getInstance(this).registerReceiver(
            getData, IntentFilter(Constants.loadedInfo)
        )
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        name.text = savedInstanceState.getString("name")
        description.text = savedInstanceState.getString("desc")
        genres.text = savedInstanceState.getString("genres")
        countries.text = savedInstanceState.getString("countries")
        link = savedInstanceState.getString("link")!!
        uploadImage()

    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString("name", name.text.toString())
        outState.putString("link", link)
        outState.putString("desc", description.text.toString())
        outState.putString("countries", countries.text.toString())
        outState.putString("genres", genres.text.toString())
        super.onSaveInstanceState(outState)
    }

    override fun onDestroy() {
        super.onDestroy()
        LocalBroadcastManager.getInstance(this).unregisterReceiver(getData)
    }

}