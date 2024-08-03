package com.project.populartvseries.common

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.project.populartvseries.apiViewModels.SeriesViewModel
import com.project.populartvseries.repositories.SeriesRepository

class SeriesViewModelFactory (private val repository: SeriesRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SeriesViewModel(repository) as T
    }
}