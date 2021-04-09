package com.example.github2.view


import android.content.Intent
import android.content.pm.ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
import android.os.Bundle
import android.os.Handler
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.github2.R
import com.example.github2.databinding.ActivityMainBinding
import com.example.github2.model.User
import com.example.github2.viewmodel.MainViewModel

@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: UserAdapter
    private lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mainViewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        ).get(MainViewModel::class.java)
        recyclerView()
        showLoading(true)
        search()
        mainViewModel.getSearchLiveData().observe(this, { query ->
            if (query != "") {
                mainViewModel.setDataByUsername(query)
                setData()
            } else {
                mainViewModel.getAll()
                setData()
            }
        })

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.option_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.changeLanguage) {
            val mIntent = Intent(Settings.ACTION_LOCALE_SETTINGS)
            startActivity(mIntent)
        }
        return super.onOptionsItemSelected(item)
    }

    private fun recyclerView() {
        val orientation = resources.configuration.orientation
        adapter = UserAdapter()
        adapter.notifyDataSetChanged()
        binding.rvUser.setHasFixedSize(true)
        if (orientation == SCREEN_ORIENTATION_PORTRAIT) {
            binding.rvUser.layoutManager = GridLayoutManager(this, 2)
        } else {
            binding.rvUser.layoutManager = GridLayoutManager(this, 3)
        }
        binding.rvUser.adapter = adapter

        adapter.setOnItemClickCallback(object : UserAdapter.OnItemClickCallback {
            override fun onItemClicked(data: User) {
                Toast.makeText(this@MainActivity, "Loading..", Toast.LENGTH_SHORT).show()
                val moveToDetail = Intent(this@MainActivity, DetailUserActivity::class.java)
                moveToDetail.putExtra(DetailUserActivity.Data, data)
                startActivity(moveToDetail)
            }
        })
    }

    private fun search() {
        mainViewModel.setSearchLiveData("")
        binding.searchView.apply {
            setOnClickListener {
                this.onActionViewExpanded()
            }
            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String): Boolean {
                    showLoading(true)
                    mainViewModel.setDataByUsername(query)
                    mainViewModel.setSearchLiveData(query)
                    setData()
                    return true
                }

                override fun onQueryTextChange(newText: String): Boolean {
                    showLoading(true)
                    if (newText.isNotEmpty()) {
                        showLoading(true)
                        mainViewModel.setDataByUsername(newText)
                        mainViewModel.setSearchLiveData(newText)
                        setData()
                    } else {
                        mainViewModel.getAll()
                        setData()
                    }
                    return true
                }
            })
        }
    }

    private fun showLoading(state: Boolean) {
        val delayTime = 1000L
        if (state) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            Handler().postDelayed({
                binding.progressBar.visibility = View.GONE
            }, delayTime)
        }
    }

    private fun dataNotFound(state: Boolean) {
        if (state) {
            binding.dataNotFound.visibility = View.VISIBLE
        } else {
            binding.dataNotFound.visibility = View.GONE
        }
    }

    private fun setData() {
        mainViewModel.getAvailabilityState().observe(this, { state ->
            if (state) {
                mainViewModel.getListUser().observe(this, { userItems ->
                    if (userItems != null) {
                        adapter.setData(userItems)
                        dataNotFound(false)
                        showLoading(false)
                    }
                })
            } else {
                mainViewModel.getListUser().observe(this, { userItems ->
                    adapter.setData(userItems)
                    showLoading(false)
                    dataNotFound(true)
                })
            }
        })
    }
}