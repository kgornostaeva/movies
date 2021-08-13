package com.example.myapplication.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.RoundedCornersTransformation
import com.example.myapplication.model.MovieDto
import com.example.myapplication.adapters.MovieItemDecoration
import com.example.myapplication.R
import com.example.myapplication.adapters.ActorsAdapter
import com.example.myapplication.helpers.TAG_MOVIE
import com.example.myapplication.viewModel.MovieDetailsViewModel

class MovieDetailsFragment : Fragment() {

	private lateinit var moviePoster: ImageView
	private lateinit var movieGenre: TextView
	private lateinit var movieReleaseDate: TextView
	private lateinit var movieName: TextView
	private lateinit var movieAnnotation: TextView
	private lateinit var movieRating: RatingBar
	private lateinit var movieAgeRestriction: TextView
	private lateinit var viewModel: MovieDetailsViewModel
	private lateinit var liveData: LiveData<MovieDto>
	private lateinit var actorRecycler: RecyclerView

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		viewModel = ViewModelProvider(this).get(MovieDetailsViewModel::class.java)
		val id = requireArguments().getInt(TAG_MOVIE)
		viewModel.uploadMovie(id)
	}

	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		val view: View = inflater.inflate(R.layout.activity_movie_details, container, false)
		initViewAttributes(view)

		setActorsRecycler(view)
		liveData = viewModel.getData()
		liveData.observe(viewLifecycleOwner, {
			setViewAttributes(it)
			(actorRecycler.adapter as ActorsAdapter).updateData(it.actors)
		})

		return view
	}

	private fun initViewAttributes(view: View) {
		moviePoster = view.findViewById(R.id.ivMovieDetailsPoster)
		movieGenre = view.findViewById(R.id.tvMovieDetailsGenre)
		movieReleaseDate = view.findViewById(R.id.tvMovieDetailsReleaseDate)
		movieName = view.findViewById(R.id.tvMovieDetailsName)
		movieAnnotation = view.findViewById(R.id.tvMovieDetailsAnnotation)
		movieRating = view.findViewById(R.id.rbMovieDetailsRating)
		movieAgeRestriction = view.findViewById(R.id.tvMovieDetailsAgeRestriction)
	}

	private fun setViewAttributes(movie: MovieDto?) {
		moviePoster.load(movie?.imageUrl) {
			transformations(RoundedCornersTransformation(30f))
		}
		movieName.text = movie?.title
		movieAnnotation.text = movie?.fullDescription
		movieAgeRestriction.text = StringBuilder(movie?.ageRestriction.toString() + "+")
		movieGenre.text = movie?.genre
		movieReleaseDate.text = movie?.release
		movieRating.rating = movie?.rateScore!!.toFloat()
	}

	private fun setActorsRecycler(view: View) {
		actorRecycler = view.findViewById(R.id.rvActors)
		actorRecycler.adapter = ActorsAdapter()
		actorRecycler.layoutManager =
			LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
		val itemDecoration = MovieItemDecoration(
			resources.getDimensionPixelSize(R.dimen.item_actor_left_offset),
			resources.getDimensionPixelSize(R.dimen.item_actor_top_offset),
			resources.getDimensionPixelSize(R.dimen.item_actor_right_offset),
			resources.getDimensionPixelSize(R.dimen.item_actor_bottom_offset)
		)
		actorRecycler.addItemDecoration(itemDecoration)
	}

	companion object {
		fun newInstance(movie: MovieDto): MovieDetailsFragment {
			val args = Bundle()
			val fragment = MovieDetailsFragment()
			args.putInt(TAG_MOVIE, movie.id)
			fragment.arguments = args
			return fragment
		}
	}
}