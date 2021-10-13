package com.kwsilence.topmovies.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.kwsilence.topmovies.databinding.ScheduledMovieRowBinding
import com.kwsilence.topmovies.fragment.MainFragmentDirections
import com.kwsilence.topmovies.model.Movie

class ScheduledListAdapter : RecyclerView.Adapter<ScheduledListAdapter.ScheduleViewHolder>() {
  private var nothingToShowText: String? = null
  private var displayedList = ArrayList<Movie>()
  val notificationToDelete: MutableLiveData<Movie?> = MutableLiveData(null)

  class ScheduleViewHolder(val binding: ScheduledMovieRowBinding) :
    RecyclerView.ViewHolder(binding.root)

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScheduleViewHolder {
    val inflater = LayoutInflater.from(parent.context)
    val binding = ScheduledMovieRowBinding.inflate(inflater, parent, false)
    return ScheduleViewHolder(binding)
  }

  override fun onBindViewHolder(holder: ScheduleViewHolder, position: Int) {
    if (displayedList.size == 0) {
      holder.binding.scheduleControlBlock.visibility = View.GONE
      holder.binding.title.text = nothingToShowText
    } else {
      holder.binding.scheduleControlBlock.visibility = View.VISIBLE
      val movie = displayedList[position]
      holder.binding.title.text = movie.title
      holder.binding.scheduleButton.text = movie.schedule
      holder.binding.root.setOnClickListener {
        val action = MainFragmentDirections.letMovieDetail(movie)
        holder.itemView.findNavController().navigate(action)
      }
      holder.binding.scheduleButton.setOnClickListener {
        val action = MainFragmentDirections.listToScheduling(movie)
        holder.itemView.findNavController().navigate(action)
      }
      holder.binding.deleteButton.setOnClickListener {
        notificationToDelete.value = movie
      }
    }
  }

  override fun getItemCount(): Int = if (displayedList.size == 0) 1 else displayedList.size

  fun setData(movies: List<Movie>) {
    displayedList = ArrayList(movies)
    notifyDataSetChanged()
  }

  fun setNothingToShowText(text: String?) {
    nothingToShowText = text
  }

  fun resetNotificationToDelete() {
    notificationToDelete.value = null
  }
}
