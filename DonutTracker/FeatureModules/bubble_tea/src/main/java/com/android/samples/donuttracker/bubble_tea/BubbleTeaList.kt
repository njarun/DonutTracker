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

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.NotificationManagerCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.android.samples.donuttracker.coffee.databinding.BubbleTeaListBinding
import com.android.samples.donuttracker.core.storage.SnackDatabase

/**
 * Fragment containing the RecyclerView which shows the current list of coffees being tracked.
 */
class BubbleTeaList : Fragment() {

    private lateinit var bubbleTeaListViewModel: BubbleTeaListViewModel

    private val adapter = BubbleTeaListAdapter(
        onEdit = { bubbleTea ->
            findNavController().navigate(
                BubbleTeaListDirections.actionCoffeeListToCoffeeEntryDialogFragment(bubbleTea.id)
            )
        },
        onDelete = { bubbleTea ->
            NotificationManagerCompat.from(requireContext()).cancel(bubbleTea.id.toInt())
            bubbleTeaListViewModel.delete(bubbleTea)
        }
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return BubbleTeaListBinding.inflate(inflater, container, false).root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val binding = BubbleTeaListBinding.bind(view)
        val bubbleTeaDao = SnackDatabase.getDatabase(requireContext()).bubbleTeaDao()

        bubbleTeaListViewModel = ViewModelProvider(this, BubbleTeaViewModelFactory(bubbleTeaDao))
            .get(BubbleTeaListViewModel::class.java)

        bubbleTeaListViewModel.bubbleTeaList.observe(viewLifecycleOwner) { coffees ->
            adapter.submitList(coffees)
        }

        binding.recyclerView.adapter = adapter

        binding.fab.setOnClickListener { fabView ->
            fabView.findNavController().navigate(
                BubbleTeaListDirections.actionCoffeeListToCoffeeEntryDialogFragment()
            )
        }
    }
}
