package com.example.myapplication.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.model.MovieDto
import com.example.myapplication.R

class MoviesAdapter(
	private var listener: OnItemClickListener
	) : RecyclerView.Adapter<MoviesViewHolder>() {

	private var movies: List<MovieDto> = listOf()

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MoviesViewHolder {
		val inflater : LayoutInflater = LayoutInflater.from(parent.context)
		return MoviesViewHolder(inflater.inflate(R.layout.item_movie, parent, false))
	}

	override fun onBindViewHolder(holder: MoviesViewHolder, position: Int) {
		holder.bind(getItem(position))
		holder.itemView.setOnClickListener {
			listener.onItemClick(getItem(position))
		}
	}

	private fun getItem(position: Int): MovieDto = movies[position]

	override fun getItemCount(): Int = movies.size

	fun updateData(newList: List<MovieDto> ) {
		val callback = MoviesCallback(movies , newList)
		val diff = DiffUtil.calculateDiff(callback)
		movies = newList
		diff.dispatchUpdatesTo(this)
	}

	interface OnItemClickListener {
		fun onItemClick(movie: MovieDto)
	}
}

