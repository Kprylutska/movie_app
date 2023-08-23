package com.kkrnvvv.movies.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.size.Scale
import com.kkrnvvv.movies.DetailesMovieActivity
import com.kkrnvvv.movies.R
import com.kkrnvvv.movies.databinding.ItemRowBinding
import com.kkrnvvv.movies.response.Result
import com.kkrnvvv.movies.utils.Constants.POSTER_BASEURL

class MovieAdapter : RecyclerView.Adapter<MovieAdapter.ViewHolder>()  {

    private lateinit var binding: ItemRowBinding
    private lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        binding= ItemRowBinding.inflate(inflater,parent,false)
        context=parent.context
        return ViewHolder()
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(differ.currentList[position])
    }

    inner class ViewHolder : RecyclerView.ViewHolder(binding.root){
        fun bind(item: Result){
            binding.apply {
                tvMovieName.text = item.title
                textRate.text=item.vote_average.toString()
                val moviePosterURL = POSTER_BASEURL + item.poster_path
                imgMovie.load(moviePosterURL) {
                    crossfade(true)
                    placeholder(R.drawable.poster_placeholder)
                    scale(Scale.FILL)
                }
                dateReleased.text=item.release_date

                root.setOnClickListener{
                    val intent = Intent(context, DetailesMovieActivity::class.java)
                    intent.putExtra("id", item.id)
                    context.startActivity(intent)
                }

            }
        }
    }

    private val differCallback=object : DiffUtil.ItemCallback<Result>(){
        override fun areItemsTheSame(
            oldItem: Result,
            newItem: Result
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: Result,
            newItem: Result
        ): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this,differCallback)
}