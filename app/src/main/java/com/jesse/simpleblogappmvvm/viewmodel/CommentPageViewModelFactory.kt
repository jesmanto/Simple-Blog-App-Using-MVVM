package com.jesse.simpleblogappmvvm.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.jesse.simpleblogappmvvm.model.Post
import com.jesse.simpleblogappmvvm.model.repository.Repository

class CommentPageViewModelFactory(private val post:Post, private val repository: Repository) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return CommentPageViewModel(post,repository) as T
    }
}