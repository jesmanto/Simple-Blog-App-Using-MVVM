package com.jesse.simpleblogappmvvm.model.repository

import com.jesse.simpleblogappmvvm.model.Comment
import com.jesse.simpleblogappmvvm.model.Post
import com.jesse.simpleblogappmvvm.model.repository.network.RetrofitInstance
import retrofit2.Response


class Repository {

    suspend fun getPosts() : Response<List<Post>> {
        return RetrofitInstance.api.getPosts()
    }

    suspend fun getComments(postId : Int) : Response<List<Comment>> {
        return RetrofitInstance.api.getComments(postId)
    }

    suspend fun pushPost(post: Post) : Response<Post> {
        return RetrofitInstance.api.pushPost(post)
    }

    suspend fun poshComment(comment: Comment) : Response<Comment>{
        return RetrofitInstance.api.pushComment(comment)
    }
}