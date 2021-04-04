package com.example.github2.View

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.example.github2.Model.User
import com.example.github2.R
import com.example.github2.ViewModel.DetailViewModel
import com.example.github2.databinding.DetailUserBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import java.lang.System.load

@Suppress("DEPRECATION")
class DetailUser : AppCompatActivity() {

    // nama untuk tab
    companion object {
        const val Data = "User"

        @StringRes
        private val TAB = intArrayOf(
            R.string.followers,
            R.string.following
        )
    }

    private lateinit var binding: DetailUserBinding

    // Live data
    private lateinit var detailViewModel: DetailViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DetailUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // setting Back Button
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayHomeAsUpEnabled(true)
        }

        tabLayoutSetting()

        //set Live data
        detailViewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        ).get(DetailViewModel::class.java)

        binding.progressBar.visibility = View.VISIBLE

        // lempar data dari main ke detailViewModel
        val dataSentFromUsername = intent.getParcelableExtra<User>(Data)?.username as String

        detailViewModel.setDetailUser(dataSentFromUsername)
        showLoading(true)
        setData()
    }

    private fun setData() {
        detailViewModel.getDetailUser().observe(this, { userData ->
            if (userData != null) {
                with(binding) {
                    Glide.with(this@DetailUser)
                        .load(userData.avatar)
                        .into(imgItemPhoto)

                    tvName.text = validatingData(userData.name as String)
                    tvCompany.text = validatingData(userData.company as String)
                    tvLocation.text = validatingData(userData.location as String)
                    tvFollower.text = validatingData(userData.followers as String)
                    tvFollowing.text = validatingData(userData.following as String)
                    tvRepository.text = validatingData(userData.repository as String)
                }
            }
        })
    }

    private fun tabLayoutSetting() {
        // Pasang Tab Layout dan View pager
        val dataSentFromUsername = intent.getParcelableExtra<User>(Data)?.username as String

        val sectionsPagerAdapter = SectionsPagerAdapter(this)
        //sending data to sectionspager from detail user
        sectionsPagerAdapter.name = dataSentFromUsername

        val viewPager: ViewPager2 = binding.viewPager2

        viewPager.adapter = sectionsPagerAdapter

        val tabs: TabLayout = binding.tabLayout

        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB[position])
        }.attach()
    }

    // Setting back button
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
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

    private fun validatingData(data: String): String {
        var message: String
        if (data != "null") {
            message = data
        } else {
            var language:String = resources.getString(R.string.dataNotSet)
            message = language
        }
        return message
    }
}