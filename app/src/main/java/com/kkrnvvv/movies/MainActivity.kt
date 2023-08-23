package com.kkrnvvv.movies

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.kkrnvvv.movies.api.ApiClient
import com.kkrnvvv.movies.api.ApiService
import com.kkrnvvv.movies.databinding.ActivityMainBinding
import com.kkrnvvv.movies.databinding.ItemRowBinding
import com.kkrnvvv.movies.response.MovieListResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.kkrnvvv.movies.adapter.MovieAdapter

class MainActivity : AppCompatActivity() {

    private lateinit var binding:ActivityMainBinding
    private val movieAdapter by lazy { MovieAdapter() }
    private val api : ApiService by lazy {
        ApiClient().getClient().create(ApiService::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            prgBarMovie.visibility=View.VISIBLE

            val callMovieApi = api.getPopularMovie(1)
            callMovieApi.enqueue(object : Callback<MovieListResponse>{
                override fun onResponse(
                    call: Call<MovieListResponse>,
                    response: Response<MovieListResponse>
                ) {
                    prgBarMovie.visibility = View.GONE
                    when (response.code()) {
                        //Successful responses
                        in 200..299 -> {
                            response.body().let { itBody ->
                                itBody?.results.let { itData ->
                                    if (itData!!.isNotEmpty()){
                                        movieAdapter.differ.submitList(itData)
                                        rvMovie.apply {
                                            layoutManager=LinearLayoutManager(this@MainActivity)
                                            adapter=movieAdapter
                                        }
                                    }
                                }
                            }
                        }
                        //Redirection messages
                        in 300..399 -> {
                            Log.d("Response Code", " success messages : ${response.code()}")
                        }
                        //Client error responses
                        in 400..499 -> {
                            Log.d("Response Code", " success messages : ${response.code()}")
                        }
                        //Server error responses
                        in 500..599 -> {
                            Log.d("Response Code", " success messages : ${response.code()}")
                        }

                    }
                }

                override fun onFailure(call: Call<MovieListResponse>, t: Throwable) {
                    binding.prgBarMovie.visibility=View.GONE
                    Log.d("on Failure", "Error: ${t.message}")
                }

            })
        }
    }
}