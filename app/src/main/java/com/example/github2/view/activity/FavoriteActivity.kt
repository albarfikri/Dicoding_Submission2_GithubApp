package com.example.github2.view.activity

import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.github2.databinding.ActivityFavoriteBinding
import com.example.github2.db.UserFavorite
import com.example.github2.model.User
import com.example.github2.view.adapter.UserAdapter
import com.example.github2.viewmodel.DetailViewModel

class FavoriteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFavoriteBinding
    private lateinit var adapter: UserAdapter
    private lateinit var viewModel: DetailViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)
        recyclerView()

        viewModel = ViewModelProvider(this).get(DetailViewModel::class.java)
        viewModel.getFavUser()?.observe(this, {
            if (it != null) {
                val list = convertList(it)
                adapter.setData(list)
                if (adapter.itemCount > 0) {
                    dataNotFound(false)
                } else {
                    dataNotFound(true)
                }
            }
        })

        supportActionBar?.title = "List Favorite User"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun dataNotFound(state: Boolean) {
        if (state) {
            binding.dataNotFound.visibility = View.VISIBLE
        } else {
            binding.dataNotFound.visibility = View.GONE
        }
    }


    private fun convertList(user: List<UserFavorite>): ArrayList<User> {
        val listUsers = ArrayList<User>()
        for (data in user) {
            val userMapped = User(
                data.id,
                data.avatar,
                data.username
            )
            listUsers.add(userMapped)
        }
        return listUsers
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

        adapter.setOnItemClickCallback(object : UserAdapter.OnItemClickCallback {
            override fun onItemClicked(data: User) {
                Toast.makeText(this@FavoriteActivity, "Loading..", Toast.LENGTH_SHORT).show()
                val moveToDetail = Intent(this@FavoriteActivity, DetailUserActivity::class.java)
                moveToDetail.putExtra(DetailUserActivity.Data, data)
                startActivity(moveToDetail)
            }
        })
    }
}