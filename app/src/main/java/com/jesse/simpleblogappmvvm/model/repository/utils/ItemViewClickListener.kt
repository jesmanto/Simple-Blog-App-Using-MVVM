package com.jesse.simpleblogappmvvm.model.repository.utils

import android.view.View
import com.jesse.simpleblogappmvvm.model.Post

interface ItemViewClickListener {
    fun onClickListener(position:Int, view: View, post: Post)
}