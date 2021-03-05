package com.jesse.simpleblogappmvvm.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jesse.simpleblogappmvvm.model.Comment
import com.jesse.simpleblogappmvvm.model.Post
import com.jesse.simpleblogappmvvm.model.repository.Repository
import kotlinx.coroutines.launch
import retrofit2.Response


class CommentPageViewModel (post: Post,val repository:Repository) : ViewModel() {

    private var _commentList = MutableLiveData<Response<List<Comment>>>()
    val commentList : LiveData<Response<List<Comment>>>
        get() = _commentList

    private var _comment = MutableLiveData<Response<Comment>>()
    val comment : LiveData<Response<Comment>>
        get() = _comment

    fun getComments(postId: Int){
        viewModelScope.launch {
            val response = repository.getComments(postId)
            _commentList.value = response
        }
    }

    fun pushComment(comment: Comment){
        viewModelScope.launch {
            val response = repository.poshComment(comment)
            _comment.value = response
        }
    }
}