package com.example.github2.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.GenericTransitionOptions
import com.bumptech.glide.Glide
import com.example.github2.R
import com.example.github2.databinding.ItemRowsBinding
import com.example.github2.model.User

class UserAdapter() :
    RecyclerView.Adapter<UserAdapter.ListViewHolder>() {

    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: User)
    }

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
            with(itemView) {
                Glide.with(itemView.context)
                    .load(user.avatar)
                    .transition(GenericTransitionOptions.with(R.anim.fragment_open_enter))
                    .into(binding.imgItemPhoto)
                binding.tvName.text = user.username

                setOnClickListener {
                    onItemClickCallback.onItemClicked(user)
                }
            }
        }
    }
}