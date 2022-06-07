package com.nikfen.asynctask

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.nikfen.asynctask.databinding.StringItemBinding

class StringAdapter() :
    ListAdapter<Int, StringAdapter.StringViewHolder>(StringDiffUtilCallBack()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StringViewHolder {
        return StringViewHolder(
            StringItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: StringViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class StringViewHolder(private val binding: StringItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(num: Int) {
            binding.itemString.text = num.toString()
        }
    }
}

class StringDiffUtilCallBack : DiffUtil.ItemCallback<Int>() {
    override fun areItemsTheSame(oldItem: Int, newItem: Int): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Int, newItem: Int): Boolean {
        return oldItem == newItem
    }


}
