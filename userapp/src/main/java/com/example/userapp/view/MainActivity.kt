package com.example.userapp.view

import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.userapp.databinding.ActivityFavoriteBinding
import com.example.userapp.viewmodel.DetailViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFavoriteBinding
    private lateinit var adapter: UserAdapter
    private lateinit var viewModel: DetailViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        recyclerView()
        showLoading(true)
        viewModel = ViewModelProvider(this).get(DetailViewModel::class.java)

        viewModel.setUserFav(this)
        viewModel.getLoadingState().observe(this, { loadingState ->
            if (loadingState) {
                viewModel.getFavUser().observe(this, {
                    if (it != null) {
                        Log.d("inputanResult", it.toString())
                        adapter.setData(it)
                        dataNotFound(false)
                        showLoading(false)
                    }
                })
            } else {
                viewModel.getFavUser().observe(this, {
                    if (it != null) {
                        Log.d("inputanResult", it.toString())
                        adapter.setData(it)
                        dataNotFound(true)
                        showLoading(false)
                    }
                })
            }

        })

        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);

        supportActionBar?.title = "User Github App"
    }

    private fun dataNotFound(state: Boolean) {
        if (state) {
            binding.dataNotFound.visibility = View.VISIBLE
        } else {
            binding.dataNotFound.visibility = View.GONE
        }
    }

    private fun showLoading(state: Boolean) {
        val delayTime = 1000L
        if (state) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            Handler(mainLooper).postDelayed({
                binding.progressBar.visibility = View.GONE
            }, delayTime)
        }
    }

    private fun recyclerView() {
        val orientation = resources.configuration.orientation
        adapter = UserAdapter()
        adapter.notifyDataSetChanged()
        binding.rvDetailFavorite.setHasFixedSize(true)
        if (orientation == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
            binding.rvDetailFavorite.layoutManager = GridLayoutManager(this, 2)
        } else {
            binding.rvDetailFavorite.layoutManager = GridLayoutManager(this, 3)
        }
        binding.rvDetailFavorite.adapter = adapter
    }
}