package com.jesse.simpleblogappmvvm.model.repository.network

import com.jesse.simpleblogappmvvm.model.Comment
import com.jesse.simpleblogappmvvm.model.Post
import retrofit2.Response
import retrofit2.http.*

interface PostApiService {
    @GET("posts")
    suspend fun getPosts(): Response<List<Post>>

    @GET("comments")
    suspend fun getComments(@Query("postId") id: Int) : Response<List<Comment>>

    @POST("posts")
    suspend fun pushPost(@Body post: Post): Response<Post>

    @POST("comments")
    suspend fun pushComment(@Body comment: Comment) : Response<Comment>
}