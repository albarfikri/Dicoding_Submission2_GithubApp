package com.example.github2.View


import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.pm.ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.provider.Settings
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
import android.content.pm.ActivityInfo.SCREEN_ORIENTATION_PORTRAIT as SCREEN_ORIENTATION_PORTRAIT

@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding


    private lateinit var adapter: UserAdapter


    private lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        recyclerView()

        mainViewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        ).get(MainViewModel::class.java)

        showLoading(true)
        mainViewModel.getAll()
        search()
        setData()

        mainViewModel.getSearchLiveData().observe(this, { query ->
            if (query != "") {
                mainViewModel.setDataByUsername(query)
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.option_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    private fun recyclerView() {
        var orientation = resources.configuration.orientation
        if(orientation == SCREEN_ORIENTATION_PORTRAIT) {
            adapter = UserAdapter()
            adapter.notifyDataSetChanged()
            binding.rvUser.setHasFixedSize(true)

            binding.rvUser.layoutManager = GridLayoutManager(this, 2)
            binding.rvUser.adapter = adapter
        }else{
            adapter = UserAdapter()
            adapter.notifyDataSetChanged()
            binding.rvUser.setHasFixedSize(true)
            binding.rvUser.layoutManager = GridLayoutManager(this, 3)
            binding.rvUser.adapter = adapter
        }


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
                    return true
                }

                override fun onQueryTextChange(newText: String): Boolean {
                    if (newText.isNotEmpty()) {
                        mainViewModel.setDataByUsername(newText)
                        mainViewModel.setSearchLiveData(newText)
                        showLoading(true)
                    } else {
                        mainViewModel.getAll()
                    }
                    dataNotFound(false)
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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.changeLanguage) {
            val mIntent = Intent(Settings.ACTION_LOCALE_SETTINGS)
            startActivity(mIntent)
        }
        return super.onOptionsItemSelected(item)
    }

    fun setData() {
        mainViewModel.getAvailabilityState().observe(this, { state ->
            if (state) {
                mainViewModel.getListUser().observe(this, { userItems ->
                    if (userItems != null) {
                        adapter.setData(userItems)
                        showLoading(false)
                        dataNotFound(false)
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