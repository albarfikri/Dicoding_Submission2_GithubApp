package com.example.github2.view.activity

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.GenericTransitionOptions
import com.bumptech.glide.Glide
import com.example.github2.R
import com.example.github2.databinding.DetailUserBinding
import com.example.github2.model.User
import com.example.github2.view.adapter.SectionsPagerAdapter
import com.example.github2.viewmodel.DetailViewModel
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DetailUserActivity : AppCompatActivity() {
    companion object {
        const val Data = "User"

        @StringRes
        private val TAB = intArrayOf(
            R.string.followers,
            R.string.following
        )
    }

    private lateinit var binding: DetailUserBinding
    private lateinit var detailViewModel: DetailViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DetailUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayHomeAsUpEnabled(true)
        }

        tabLayoutSetting()

        detailViewModel = ViewModelProvider(
            this
        ).get(DetailViewModel::class.java)

        binding.progressBar.visibility = View.VISIBLE

        val id = intent.getParcelableExtra<User>(Data)?.id as Int
        val username = validatingData(intent.getParcelableExtra<User>(Data)?.username as String)
        val avatar = intent.getParcelableExtra<User>(Data)?.avatar as String

        var isFavChecked = false

        detailViewModel.setDetailUser(username)
        showLoading(true)
        setData()

        CoroutineScope(Dispatchers.IO).launch {
            val count = detailViewModel.checkUserFav(id)
            withContext(Dispatchers.Main) {
                binding.apply {
                    if (count != null) {
                        if (count > 0) {
                            btnFavorite.setImageResource(R.drawable.ic_favorite_like)
                            isFavChecked = true
                        } else {
                            btnFavorite.setImageResource(R.drawable.ic_favorite_dislike)
                            isFavChecked = false
                        }
                    }
                }
            }
        }

        binding.btnFavorite.setOnClickListener {
            binding.apply {
                isFavChecked = !isFavChecked
                if (isFavChecked) {
                    detailViewModel.addToFav(id, avatar, username)
                    btnFavorite.setImageResource(R.drawable.ic_favorite_like)
                    Snackbar.make(it, "Favorite user is saved !", Snackbar.LENGTH_SHORT).show()
                } else {
                    detailViewModel.removeFav(id)
                    btnFavorite.setImageResource(R.drawable.ic_favorite_dislike)
                    Snackbar.make(it, "Favorite user is removed !", Snackbar.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun setData() {
        detailViewModel.getDetailUser().observe(this, { userData ->
            if (userData != null) {
                with(binding) {
                    Glide.with(this@DetailUserActivity)
                        .load(userData.avatar)
                        .transition(GenericTransitionOptions.with(R.anim.fragment_open_enter))
                        .into(imgItemPhoto)

                    tvCompany.text = validatingData(userData.company as String)
                    tvLocation.text = validatingData(userData.location as String)
                    tvFollower.text = validatingData(userData.followers as String)
                    tvFollowing.text = validatingData(userData.following as String)
                    tvRepository.text = validatingData(userData.repository as String)
                    collapsingToolbar.title = validatingData(userData.name as String)
                    collapsingToolbar.setExpandedTitleTextColor(getColorStateList(R.color.white))
                }
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

    private fun tabLayoutSetting() {
        val dataSentFromUsername = intent.getParcelableExtra<User>(Data)?.username as String

        val sectionsPagerAdapter = SectionsPagerAdapter(this)
        sectionsPagerAdapter.name = dataSentFromUsername

        val viewPager: ViewPager2 = binding.viewPager2

        viewPager.adapter = sectionsPagerAdapter

        val tabs: TabLayout = binding.tabLayout

        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB[position])
        }.attach()

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun showLoading(state: Boolean) {
        binding.apply {
            if (state) {
                progressBar.visibility = View.VISIBLE
            } else {
                progressBar.visibility = View.GONE
            }
        }
    }

    private fun validatingData(data: String): String {
        return if (data != "null") {
            data
        } else {
            resources.getString(R.string.dataNotSet)
        }
    }
}