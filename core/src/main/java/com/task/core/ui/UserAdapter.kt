package com.task.core.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.task.core.databinding.ItemUserListBinding
import com.task.core.domain.model.User
import com.task.core.utils.UserDiffCallback

class UserAdapter(private val user: ArrayList<User>, private val clickListener: (String, View) -> Unit):
    RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    fun setData(items: List<User>?) {
        val diffCallback = items?.let { UserDiffCallback(this.user, it) }
        val diffResult = diffCallback?.let { DiffUtil.calculateDiff(it) }

        this.user.apply {
            clear()
            items?.let { addAll(it) }
        }

        diffResult?.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder =
        UserViewHolder(ItemUserListBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) = holder.bind(user[position], clickListener)

    override fun getItemCount(): Int = user.size

    inner class UserViewHolder(private val binding: ItemUserListBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(user: User, click: (String, View) -> Unit) {
            binding.data = user
            binding.root.transitionName = user.login
            binding.root.setOnClickListener { user.login?.let { it1 -> click(it1, binding.root) } }
        }
    }
}