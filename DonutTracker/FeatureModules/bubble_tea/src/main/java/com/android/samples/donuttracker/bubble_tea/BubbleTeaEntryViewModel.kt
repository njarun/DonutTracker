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

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.android.samples.donuttracker.core.model.BubbleTea
import com.android.samples.donuttracker.core.storage.BubbleTeaDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class BubbleTeaEntryViewModel(private val bubbleTeaDao: BubbleTeaDao) : ViewModel() {

    private var bubbleTeaLiveData: LiveData<BubbleTea>? = null

    fun get(id: Long): LiveData<BubbleTea> {
        return bubbleTeaLiveData ?: liveData {
            emit(bubbleTeaDao.get(id))
        }.also {
            bubbleTeaLiveData = it
        }
    }

    fun addData(
        id: Long,
        name: String,
        description: String,
        rating: Int,
        setupNotification: (Long) -> Unit
    ) {
        val bubbleTea = BubbleTea(id, name, description, rating)

        CoroutineScope(Dispatchers.IO).launch {
            var actualId = id

            if (id > 0) {
                update(bubbleTea)
            } else {
                actualId = insert(bubbleTea)
            }

            setupNotification(actualId)
        }
    }

    private suspend fun insert(donut: BubbleTea) = bubbleTeaDao.insert(donut)

    private fun update(donut: BubbleTea) = viewModelScope.launch(Dispatchers.IO) {
        bubbleTeaDao.update(donut)
    }
}
