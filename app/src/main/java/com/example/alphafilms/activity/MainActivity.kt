package com.example.alphafilms.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.alphafilms.adapter.FilmsAdapter
import com.example.alphafilms.api.Movie
import com.example.alphafilms.api.MovieAPI
import com.example.alphafilms.api.SearchResponse
import com.example.alphafilms.databinding.ActivityMainBinding
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.*
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory


private const val TAG = "item"

class MainActivity : AppCompatActivity(), FilmsAdapter.OnMovieClickListener {

    private val adapter = FilmsAdapter({ position ->
        onMovieClick(position)
    })

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.loadingIv.visibility = View.GONE

        binding.button.setOnClickListener {
            binding.loadingIv.visibility = View.VISIBLE
            callInternet2()


        }
        val recyclerView = binding.recyclerV
        recyclerView.layoutManager = LinearLayoutManager(this)

        recyclerView.adapter = adapter
    }

    /*private fun callInternet() {

        val httpClient = OkHttpClient()
        val retrofit = Retrofit.Builder()
            .client(httpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("http://www.omdbapi.com").build()

        val movieAPI = retrofit.create(MovieAPI::class.java)

        val text: String = binding.searchText.text.toString()

        if (text != null) {
            val call = movieAPI.search("4039981a", text, 1)

            call.enqueue(object : retrofit2.Callback<SearchResponse> {
                override fun onResponse(
                    call: Call<SearchResponse>,
                    response: retrofit2.Response<SearchResponse>
                ) {
                    val searchResponse = response.body()
                    val searchList = searchResponse?.list?: emptyList()

                   val call2 = movieAPI.search("4039981a", text, 2)
                    call2.enqueue(object : retrofit2.Callback<SearchResponse> {
                        override fun onResponse(
                            call: Call<SearchResponse>,
                            response: retrofit2.Response<SearchResponse>
                        ) {
                            val searchResponse = response.body()
                            if (searchResponse != null) {
                                val newSearchList = searchResponse.list ?: emptyList()
                                adapter.updateList(searchList+newSearchList)
                            }
                            binding.loadingIv.visibility = View.GONE
                        }

                        override fun onFailure(call: Call<SearchResponse>, t: Throwable) {
                            Toast.makeText(this@MainActivity, "Fail", Toast.LENGTH_SHORT).show()
                        }
                    })
                    binding.loadingIv.visibility = View.GONE
                }

                override fun onFailure(call: Call<SearchResponse>, t: Throwable) {
                    Toast.makeText(this@MainActivity, "Fail", Toast.LENGTH_SHORT).show()
                }
            })
        } else {
            Toast.makeText(this, "Введите минимум 1 символ!", Toast.LENGTH_SHORT).show()
            binding.loadingIv.visibility = View.GONE
        }
    }*/


    private fun callInternet2() {

        val httpClient = OkHttpClient()
        val retrofit = Retrofit.Builder()
            .client(httpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .baseUrl("http://www.omdbapi.com").build()

        val movieAPI = retrofit.create(MovieAPI::class.java)

        val text: String = binding.searchText.text.toString()

        if (text != null) {
            val disposable =  movieAPI.search("4039981a", text, 1).flatMap( {response->
                 movieAPI.search("4039981a", text, 2).map( { newResponse->
                     val list = ArrayList<Movie>()
                     list.addAll(response.list?: emptyList())
                     list.addAll(newResponse.list?: emptyList())
                     return@map list
                 })
             })
                 .subscribeOn(Schedulers.io())
                 .observeOn(AndroidSchedulers.mainThread())
                 .subscribe({
                     adapter.updateList(it)
                     binding.loadingIv.visibility = View.GONE
                 }, {
                     Toast.makeText(this@MainActivity, "Fail", Toast.LENGTH_SHORT).show()
                 })

        } else {
            Toast.makeText(this, "Введите минимум 1 символ!", Toast.LENGTH_SHORT).show()
            binding.loadingIv.visibility = View.GONE
        }
    }
    override fun onMovieClick(position: Int) {

        var movieId = adapter.getList().get(position).id
        val intent = Intent(this, DetailsActivity::class.java)
        intent.putExtra(TAG, movieId)
        startActivity(intent)
    }
}