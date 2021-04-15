package com.example.github2.view.adapter

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.github2.view.fragment.UserDetailFragment


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

