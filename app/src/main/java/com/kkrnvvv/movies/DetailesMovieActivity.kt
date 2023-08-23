package com.kkrnvvv.movies

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import coil.load
import coil.size.Scale
import com.kkrnvvv.movies.api.ApiClient
import com.kkrnvvv.movies.api.ApiService
import com.kkrnvvv.movies.databinding.ActivityDetailesMovieBinding
import com.kkrnvvv.movies.response.DetailesMovieResponse
import com.kkrnvvv.movies.utils.Constants.POSTER_BASEURL
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailesMovieActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailesMovieBinding
    private val api : ApiService by lazy {
        ApiClient().getClient().create(ApiService::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityDetailesMovieBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val movieId = intent.getIntExtra("id", 1)

        binding.apply {
            val callDetailMovieApi = api.getMovieDetails(movieId)
            callDetailMovieApi.enqueue(object : Callback<DetailesMovieResponse> {
                override fun onResponse(
                    call: Call<DetailesMovieResponse>,
                    response: Response<DetailesMovieResponse>
                ) {
                    when(response.code()){
                        in 200..299 ->{
                            response.body().let { itBody ->
                                val imagePoster = POSTER_BASEURL + itBody!!.poster_path
                                imgMovie.load(imagePoster){
                                    crossfade(true)
                                    placeholder(R.drawable.poster_placeholder)
                                    scale(Scale.FILL)
                                }
                                imgBackground.load(imagePoster){
                                    crossfade(true)
                                    placeholder(R.drawable.poster_placeholder)
                                    scale(Scale.FILL)
                                }
                                tvMovieName.text=itBody.title
                                tvTagLine.text=itBody.tagline
                                dateReleased.text=itBody.release_date
                                tvMovieRating.text=itBody.vote_average.toString()
                                tvMovieRuntime.text=itBody.runtime.toString()
                                tvMovieBudget.text=itBody.budget.toString()
                                tvMovieRevenue.text=itBody.revenue.toString()
                                tvMovieOverview.text=itBody.overview
                            }
                        }
                        in 300..399 ->{
                            Log.d("Response Code", " success messages : ${response.code()}")
                        }
                        in 400..499 ->{
                            Log.d("Response Code", " success messages : ${response.code()}")
                        }
                        in 500..599 ->{
                            Log.d("Response Code", " success messages : ${response.code()}")
                        }
                    }
                }

                override fun onFailure(call: Call<DetailesMovieResponse>, t: Throwable) {
                    TODO("Not yet implemented")
                }

            })
        }
    }
}