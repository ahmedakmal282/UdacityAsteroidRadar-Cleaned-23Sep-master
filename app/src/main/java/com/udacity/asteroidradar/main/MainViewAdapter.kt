package com.udacity.asteroidradar.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.udacity.asteroidradar.databinding.AsteroidViewItemBinding
import com.udacity.asteroidradar.domain.Asteroid


class MainViewAdapter(private val clickListener: AsteroidListener) : ListAdapter<Asteroid, MainViewAdapter.ViewHolder>(DiffCallBack) {

    companion object DiffCallBack : DiffUtil.ItemCallback<Asteroid>() {
        override fun areItemsTheSame(oldItem: Asteroid, newItem: Asteroid): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: Asteroid, newItem: Asteroid): Boolean {
            return oldItem.id == newItem.id
        }
    }
    override fun onBindViewHolder(holder:  ViewHolder, position: Int) {
       val asteroid = getItem(position)
        holder.bind(asteroid, clickListener)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

 class ViewHolder private constructor(private val binding: AsteroidViewItemBinding):RecyclerView.ViewHolder(binding.root){

        fun bind(asteroid: Asteroid, clickListener: AsteroidListener) {
            binding.asteroid = asteroid
            binding.clickListener = clickListener
            binding.executePendingBindings()

        }
        companion object {
             fun from(parent: ViewGroup): ViewHolder {
                 val layoutInflater = LayoutInflater.from(parent.context)
                 val binding = AsteroidViewItemBinding.inflate(layoutInflater, parent, false)
                 return ViewHolder(binding)
            }
        }
 }

}
class AsteroidListener(val clickListener: (asteroid: Asteroid) -> Unit) {
    fun onClick(asteroid: Asteroid) = clickListener(asteroid)
}
