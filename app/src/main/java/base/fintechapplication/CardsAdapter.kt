package base.fintechapplication

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.RecyclerView

class CardsAdapter(private val context: Context, private val values: List<MovieCard>) :
    RecyclerView.Adapter<CardsAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val preview = view.findViewById<ImageView>(R.id.MovieImage)!!
        val name = view.findViewById<TextView>(R.id.MovieName)!!
        val extra = view.findViewById<TextView>(R.id.MovieGenre)!!
        val wholeCard = view.findViewById<CardView>(R.id.wholeCard)!!
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(context).inflate(R.layout.movie_card, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val movie = values[position]

        holder.name.text = movie.Name
        val extra = "${movie.Genres[0]} (${movie.Year ?: "?"})"
        holder.extra.text = extra
        holder.preview.setImageBitmap(movie.Image)

        holder.wholeCard.setOnClickListener {

            val intent = Intent(Constants.getMovieInfo)
            intent.putExtra("id", movie.id)
            LocalBroadcastManager.getInstance(context).sendBroadcast(intent)

        }
    }

    override fun getItemCount(): Int {
        return values.size
    }
}