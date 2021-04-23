package com.example.github2.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.GenericTransitionOptions
import com.bumptech.glide.Glide
import com.example.github2.R
import com.example.github2.databinding.ItemRowsDetailBinding
import com.example.github2.model.User
import com.example.github2.view.fragment.UserDetailFragment

class DetailAdapter(private val context: Context) :
    RecyclerView.Adapter<DetailAdapter.ListViewHolder>() {

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
        val mView = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.item_rows_detail, viewGroup, false)
        return ListViewHolder(mView)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.binding.imgItemPhoto.startAnimation(
            AnimationUtils.loadAnimation(
                context,
                R.anim.fade_transition_animation
            )
        )
        holder.itemView.startAnimation(
            AnimationUtils.loadAnimation(
                context,
                R.anim.fade_scale_animation
            )
        )
        holder.bind(mData[position])
    }

    override fun getItemCount(): Int = mData.size

    inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding = ItemRowsDetailBinding.bind(itemView)
        fun bind(user: User) {
            Glide.with(itemView.context)
                .load(user.avatar)
                .transition(GenericTransitionOptions.with(R.anim.fragment_open_enter))
                .into(binding.imgItemPhoto)
            binding.tvUsername.text = user.username

            itemView.setOnClickListener {
                onItemClickCallback.onItemClicked(user)
            }
        }
    }

}