package com.example.alphafilms.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.alphafilms.R
import com.example.alphafilms.activity.MainActivity
import com.example.alphafilms.api.Movie

class FilmsAdapter(private val listener: OnMovieClickListener) :
    RecyclerView.Adapter<FilmsAdapter.FilmsHolder>() {

     private val mList: ArrayList<Movie> = ArrayList()

    fun getList() : List<Movie> = mList

    fun updateList(list: List<Movie>?) {
        mList.clear()
        mList.addAll(list ?: emptyList())
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilmsHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.movie_item, parent, false)
        return FilmsHolder(view)
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    override fun onBindViewHolder(holder: FilmsHolder, position: Int) {
        val filmItem = mList[position]

        holder.titleView.text = filmItem.title
        holder.yearView.text = filmItem.year
        Glide.with(holder.imageView.context).load(filmItem.poster).into(holder.imageView);
    }

    inner class FilmsHolder(itemView: View) : RecyclerView.ViewHolder(itemView), OnClickListener {

        val titleView: TextView = itemView.findViewById(R.id.title_tv)
        val yearView: TextView = itemView.findViewById(R.id.year_tv)
        val imageView: ImageView = itemView.findViewById(R.id.poster_v)

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(p0: View?) {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                listener.onMovieClick(position)
            }
        }
    }


    fun interface OnMovieClickListener {
        fun onMovieClick(position: Int)
    }

}