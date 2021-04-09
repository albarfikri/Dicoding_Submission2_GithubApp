package com.example.github2.view

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.github2.model.User
import com.example.github2.viewmodel.MainViewModel
import com.example.github2.databinding.FragmentUserDetailBinding


@Suppress("DEPRECATION")
class UserDetailFragment : Fragment() {
    private lateinit var adapter: DetailAdapter
    private lateinit var mainViewModel: MainViewModel
    private lateinit var binding: FragmentUserDetailBinding

    companion object {
        private const val Username = "username"
        private const val TypeUser = "typeUser"

        @JvmStatic
        fun newInstance(username: String?, typeUser: String) = UserDetailFragment().apply {
            arguments = Bundle().apply {
                putString(Username, username)
                putString(TypeUser, typeUser)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUserDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = DetailAdapter()
        mainViewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        ).get(MainViewModel::class.java)

        recyclerView()

        val username = arguments?.getString(Username)
        val type = arguments?.getString(TypeUser)


        if (type != null) {
            mainViewModel.setFollowerAndFollowing(username, type)
        }

        showLoading(true)
        inflateData()
    }

    private fun recyclerView() {
        adapter.notifyDataSetChanged()
        binding.rvDetail.setHasFixedSize(true)
        binding.rvDetail.layoutManager = LinearLayoutManager(context)
        binding.rvDetail.adapter = adapter

        adapter.setOnItemClickCallback(object : DetailAdapter.OnItemClickCallback {
            override fun onItemClicked(data: User) {
                val moveToDetail = Intent(context, DetailUserActivity::class.java)
                moveToDetail.putExtra(DetailUserActivity.Data, data)
                startActivity(moveToDetail)

            }
        })
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

    private fun inflateData() {
        mainViewModel.getAvailabilityState().observe(viewLifecycleOwner, { state ->
            if (state) {
                mainViewModel.getListUser().observe(viewLifecycleOwner, { listUser ->
                    adapter.setData(listUser)
                    dataNotFound(false)
                    showLoading(false)
                })
            } else {
                mainViewModel.getListUser().observe(viewLifecycleOwner, { userItems ->
                    adapter.setData(userItems)

                    showLoading(false)
                    val delayTime = 1000L
                    Handler().postDelayed({
                        dataNotFound(true)
                    }, delayTime)
                })
            }
        })
    }
}