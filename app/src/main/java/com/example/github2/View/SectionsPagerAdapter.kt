package com.example.github2.View

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter


class SectionsPagerAdapter(activity: AppCompatActivity) : FragmentStateAdapter(activity) {

    private val typeUser = arrayListOf(
        "followers",
        "following"
    )

    var name: String? = null
    override fun createFragment(position: Int): Fragment {
        return UserDetailFragment.newInstance(name, typeUser[position])
    }

    override fun getItemCount(): Int {
        return typeUser.size
    }

}

