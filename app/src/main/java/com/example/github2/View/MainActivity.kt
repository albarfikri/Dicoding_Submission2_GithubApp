package com.example.github2.View


import android.content.Intent
import android.media.audiofx.BassBoost
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.provider.Settings
import android.provider.Settings.ACTION_LOCALE_SETTINGS
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.github2.Model.User
import com.example.github2.R
import com.example.github2.ViewModel.MainViewModel
import com.example.github2.databinding.ActivityMainBinding

@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    // inisialisasi adapter
    private lateinit var adapter: UserAdapter

    private val list = ArrayList<User>()

    // Live Data
    private lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Inisialisasi adapter
        recyclerView()

        //Live Data
        mainViewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        ).get(MainViewModel::class.java)

        search()
        showLoading(true)
        mainViewModel.getAll()
        Log.d("loading",showLoading(true).toString())
        setData()
        // Keeping data while having the different orientation
        mainViewModel.getSearchLiveData().observe(this, { query ->
            if (query != "") {
                mainViewModel.setDataByUsername(query)
                Log.d("LiveDataUsername", query)
            }
        })
    }

    // setting menu and localization
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // menu Points
        val inflater = menuInflater
        inflater.inflate(R.menu.option_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    private fun recyclerView() {
        adapter = UserAdapter()
        adapter.notifyDataSetChanged()
        binding.rvUser.setHasFixedSize(true)
        binding.rvUser.layoutManager = GridLayoutManager(this, 2)
        binding.rvUser.adapter = adapter

        adapter.setOnItemClickCallback(object : UserAdapter.OnItemClickCallback {
            override fun onItemClicked(data: User) {
                Toast.makeText(this@MainActivity, "Loading..", Toast.LENGTH_SHORT).show()
                val moveToDetail = Intent(this@MainActivity, DetailUser::class.java)
                moveToDetail.putExtra(DetailUser.Data, data)
                startActivity(moveToDetail)

            }
        })
    }

    private fun search() {
        mainViewModel.searchLiveData.postValue("")
        binding.searchView.apply {
            setOnClickListener {
                this.onActionViewExpanded()
            }
            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String): Boolean {
                    mainViewModel.setDataByUsername(query)
                    mainViewModel.setSearchLiveData(query)
                    setData()
                    return true
                }

                override fun onQueryTextChange(newText: String): Boolean {
                    if (newText.isNotEmpty()) {
                        mainViewModel.setDataByUsername(newText)
                        mainViewModel.setSearchLiveData(newText)
                        showLoading(true)
                        setData()
                    } else {
                        mainViewModel.getAll()
                    }
                    dataNotFound(false)
                    setData()
                    return false
                }
            })
        }
    }

    //Loading View
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

    // setting localization
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.changeLanguage) {
            val mIntent = Intent(Settings.ACTION_LOCALE_SETTINGS)
            startActivity(mIntent)
        }
        return super.onOptionsItemSelected(item)
    }

    // Set data
    fun setData() {
        mainViewModel.getAvailabilityState().observe(this, { state ->
            if (state) {
                Log.d("state1", state.toString())
                mainViewModel.getListUser().observe(this, { userItems ->
                    if (userItems != null) {
                        Log.d("User", userItems.toString())
                        adapter.setData(userItems)
                        showLoading(false)
                        dataNotFound(false)
                    }
                })
            } else {
                mainViewModel.getListUser().observe(this, { userItems ->
                    Log.d("User", userItems.toString())
                    adapter.setData(userItems)
                    showLoading(false)
                    dataNotFound(true)
                })
            }
        })
    }
}