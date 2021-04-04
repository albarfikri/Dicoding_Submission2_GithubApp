package com.example.github2.View

import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.github2.ViewModel.MainViewModel
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
        adapter = DetailAdapter()
        mainViewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        ).get(MainViewModel::class.java)

        recyclerView()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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

    private fun inflateData() {
        mainViewModel.getListUser().observe(viewLifecycleOwner,{ listUser ->
            Log.d("datayang", listUser.toString())
            adapter.setData(listUser)
            showLoading(false)
        }
        )
    }

}