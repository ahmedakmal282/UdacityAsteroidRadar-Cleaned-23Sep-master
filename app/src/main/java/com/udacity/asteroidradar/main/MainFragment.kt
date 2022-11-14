package com.udacity.asteroidradar.main

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.database.AsteroidDatabase
import com.udacity.asteroidradar.databinding.FragmentMainBinding
import com.udacity.asteroidradar.network.getToday
import com.udacity.asteroidradar.network.getWeekAfter
import com.udacity.asteroidradar.repository.AsteroidListDatabaseFilter
import com.udacity.asteroidradar.repository.AsteroidsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


@Suppress("DEPRECATION")
class MainFragment : Fragment() {



    lateinit var viewModel: MainViewModel


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val application = requireNotNull(this.activity).application
        val dataSource = AsteroidDatabase.getInstance(application).asteroidDao
        val viewModelFactory = MainViewModelFactory(dataSource,application)
        val binding = FragmentMainBinding.inflate(inflater)

        viewModel = ViewModelProvider(this, viewModelFactory)[MainViewModel::class.java]

        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        val adapter = MainViewAdapter(AsteroidListener { asteroid ->
            viewModel.onAsteroidClicked(asteroid)
        })

        binding.asteroidRecycler.adapter = adapter

        viewModel._asteroids.observe(viewLifecycleOwner, Observer {
            Log.i("DODO","$it")
            it?.let {
                adapter.submitList(it)
                binding.executePendingBindings()
            }
        })

        viewModel.navigateToAsteroidDetails.observe(viewLifecycleOwner, Observer {it->
            it?.let {
                this.findNavController().navigate(MainFragmentDirections.actionShowDetail(it))
                viewModel.onAsteroidClickedDone()
            }
        })
        setHasOptionsMenu(true)

        return binding.root
    }
    @Deprecated("Deprecated in Java")
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_overflow_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }
    @Deprecated("Deprecated in Java")
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        lifecycleScope.launch {
            when (item.itemId) {
                R.id.show_today_menu -> {
                    viewModel.updateDatabaseFilter(AsteroidListDatabaseFilter.TODAY)
                }
                R.id.show_week_menu -> {
                    viewModel.updateDatabaseFilter(AsteroidListDatabaseFilter.WEEK)
                }
                else -> {
                    viewModel.updateDatabaseFilter(AsteroidListDatabaseFilter.ALL)
                }
            }
        }
        return true
    }
}
