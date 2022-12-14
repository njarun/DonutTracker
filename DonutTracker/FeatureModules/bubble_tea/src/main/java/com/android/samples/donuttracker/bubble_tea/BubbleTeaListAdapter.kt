/*
 * Copyright (C) 2021 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.android.samples.donuttracker.bubble_tea

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.android.samples.donuttracker.coffee.R
import com.android.samples.donuttracker.coffee.databinding.BubbleTeaItemBinding
import com.android.samples.donuttracker.core.model.BubbleTea

/**
 * The adapter used by the RecyclerView to display the current list of Bubble Tea
 */
class BubbleTeaListAdapter(
    private var onEdit: (BubbleTea) -> Unit,
    private var onDelete: (BubbleTea) -> Unit
) : ListAdapter<BubbleTea, BubbleTeaListAdapter.CoffeeListViewHolder>(CoffeeDiffCallback()) {

    class CoffeeListViewHolder(
        private val binding: BubbleTeaItemBinding,
        private var onEdit: (BubbleTea) -> Unit,
        private var onDelete: (BubbleTea) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        private var coffeeId: Long = -1
        private var nameView = binding.name
        private var description = binding.description
        private var thumbnail = binding.thumbnail
        private var rating = binding.rating
        private var bubbleTea: BubbleTea? = null

        fun bind(bubbleTea: BubbleTea) {
            coffeeId = bubbleTea.id
            nameView.text = bubbleTea.name
            description.text = bubbleTea.description
            rating.text = bubbleTea.rating.toString()
            thumbnail.setImageResource(R.drawable.coffee_cup)
            this.bubbleTea = bubbleTea
            binding.deleteButton.setOnClickListener {
                onDelete(bubbleTea)
            }
            binding.root.setOnClickListener {
                onEdit(bubbleTea)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =  CoffeeListViewHolder(
        BubbleTeaItemBinding.inflate(LayoutInflater.from(parent.context), parent, false),
        onEdit,
        onDelete
    )

    override fun onBindViewHolder(holder: CoffeeListViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

class CoffeeDiffCallback : DiffUtil.ItemCallback<BubbleTea>() {
    override fun areItemsTheSame(oldItem: BubbleTea, newItem: BubbleTea): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: BubbleTea, newItem: BubbleTea): Boolean {
        return oldItem == newItem
    }
}
