package com.example.userapp.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.GenericTransitionOptions
import com.bumptech.glide.Glide
import com.example.userapp.R
import com.example.userapp.databinding.ItemRowsBinding
import com.example.userapp.model.User

class UserAdapter() :
    RecyclerView.Adapter<UserAdapter.ListViewHolder>() {

    private val mData = ArrayList<User>()

    fun setData(items: ArrayList<User>) {
        mData.clear()
        mData.addAll(items)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        viewGroup: ViewGroup,
        viewType: Int
    ): ListViewHolder {
        val mView =
            LayoutInflater.from(viewGroup.context).inflate(R.layout.item_rows, viewGroup, false)
        return ListViewHolder(mView)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(mData[position])
    }

    override fun getItemCount(): Int = mData.size

    inner class ListViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        private val binding = ItemRowsBinding.bind(itemView)
        fun bind(user: User) {
            Glide.with(itemView.context)
                .load(user.avatar)
                .transition(GenericTransitionOptions.with(R.anim.fragment_open_enter))
                .into(binding.imgItemPhoto)
            binding.tvName.text = user.username
        }
    }
}