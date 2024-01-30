package com.example.alphafilms.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.alphafilms.R
import com.example.alphafilms.api.DetailsAPI
import com.example.alphafilms.api.DetailsResponse
import com.example.alphafilms.api.Movie
import com.example.alphafilms.api.SearchResponse
import com.example.alphafilms.databinding.ActivityDetailsBinding
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

private const val TAG = "item"


class DetailsActivity : AppCompatActivity() {

    //id = tt0372784
    private lateinit var binding: ActivityDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val imdbId = intent.getStringExtra(TAG).toString()

        searchCall2(imdbId)

    }

    /*private fun searchCall(imdbId: String) {

        val httpClient = OkHttpClient()
        val retrofit = Retrofit.Builder()
            .client(httpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("http://www.omdbapi.com").build()

        val detailsAPI = retrofit.create(DetailsAPI::class.java)
        val call = detailsAPI.search("4039981a", imdbId)

        call.enqueue(object : retrofit2.Callback<DetailsResponse> {
            override fun onResponse(
                call: Call<DetailsResponse>,
                response: Response<DetailsResponse>
            ) {
                val detailResponse = response.body()
                if (detailResponse != null) {
                    binding.titleTv.text = detailResponse.title
                    binding.yearTv.text = detailResponse.year
                    binding.genreTv.text = detailResponse.genre
                    binding.awardsTv.text = detailResponse.awards
                    Glide.with(this@DetailsActivity).load(detailResponse.poster)
                        .into(binding.posterIv)
                }
                binding.seconeWaitIv.visibility = View.GONE
            }

            override fun onFailure(call: Call<DetailsResponse>, t: Throwable) {
                TODO("Not yet implemented")
                binding.seconeWaitIv.visibility = View.GONE
            }
        })
    }*/

    private fun searchCall2(imdbId: String) {
        val httpClient = OkHttpClient()
        val retrofit = Retrofit.Builder()
            .client(httpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .baseUrl("http://www.omdbapi.com").build()

        val detailsAPI = retrofit.create(DetailsAPI::class.java)
        val disposable = detailsAPI.search("4039981a", imdbId)
            .subscribeOn(Schedulers.io())
            .doOnSuccess({
                Log.d("Thread", Thread.currentThread().name)
            })
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSuccess({
                Log.d("Thread", Thread.currentThread().name)
            })
            .subscribe({
                binding.titleTv.text = it.title
                binding.yearTv.text = it.year
                binding.genreTv.text = it.genre
                binding.awardsTv.text = it.awards
                Glide.with(this@DetailsActivity).load(it.poster)
                    .into(binding.posterIv)
                binding.seconeWaitIv.visibility = View.GONE
            }, {
                Toast.makeText(this@DetailsActivity, "Fail", Toast.LENGTH_SHORT).show()
            })
    }
}