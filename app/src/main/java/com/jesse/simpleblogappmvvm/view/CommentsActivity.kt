package com.jesse.simpleblogappmvvm.view

import android.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.jesse.simpleblogappmvvm.R
import com.jesse.simpleblogappmvvm.adapters.CommentsAdapter
import com.jesse.simpleblogappmvvm.databinding.ActivityCommentsBinding
import com.jesse.simpleblogappmvvm.model.Comment
import com.jesse.simpleblogappmvvm.model.Post
import com.jesse.simpleblogappmvvm.model.repository.Repository
import com.jesse.simpleblogappmvvm.model.repository.utils.POST
import com.jesse.simpleblogappmvvm.viewmodel.CommentPageViewModel
import com.jesse.simpleblogappmvvm.viewmodel.CommentPageViewModelFactory
import com.jesse.simpleblogappmvvm.model.repository.utils.*

class CommentsActivity : AppCompatActivity() {
    lateinit var viewModel: CommentPageViewModel
    lateinit var binding: ActivityCommentsBinding
    lateinit var bundle: Post
    private val adapter = CommentsAdapter()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCommentsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        bundle = intent.getParcelableExtra(POST)!!
        setPostName()
        setPostBody()

        val repository = Repository()
        val viewModelFactory = CommentPageViewModelFactory(bundle, repository)
        viewModel = ViewModelProvider(this, viewModelFactory)
            .get(CommentPageViewModel::class.java)

        viewModel.getComments(bundle.id)

        // Observing the commentList live data from the viewModel
        viewModel.commentList.observe(this, Observer { response ->
            if (response.isSuccessful) {
                response.body()?.let { adapter.loadComments(it) }
                initRecyclerView()
            }

        })

        // Observing the comment live data from the viewModel
        viewModel.comment.observe(this, Observer {
            if (it.isSuccessful) {
                Log.d(TAG, "${it.body()}")
                it.body()?.let { it1 -> adapter.addCustomComment(it1) }
            } else {
                Log.d(TAG, "${it.errorBody()}")
            }
        })
    }

    /**
     * Sets an adapter to the recyclerView
     */
    private fun initRecyclerView() {
        binding.commentList.adapter = adapter
    }

    /**
     * Gets the incoming [bundle: Post] and extracts its
     * title field to display on the comments page
     */
    private fun setPostName() {
        binding.heading.text = bundle.title
    }

    /**
     * Gets the incoming [bundle: Post] and extracts its
     * body field to display on the comments page
     */
    private fun setPostBody() {
        binding.commentBody.text = bundle.body
    }

    /**
     * Displays the dialog box to allow users add new
     * comments to the list of comments
     */
    private fun addComment() {
        val postDialog = AlertDialog.Builder(this)
        val rootView = layoutInflater.inflate(R.layout.comment_alert_dialog, null)

        postDialog.setView(rootView)
        val dialog = postDialog.create()
        dialog.show()

        // When the positive button is clicked, it gets the values of the
        // post fields and sends the data to the server
        // as well as populates the list of posts locally
        rootView.findViewById<TextView>(R.id.addBtn).setOnClickListener {

            val commentTitle =
                rootView.findViewById<EditText>(R.id.commentTitle).text.toString()
            val commentBody = rootView.findViewById<EditText>(R.id.bodyComment).text.toString()

            viewModel.pushComment(validateComment(commentTitle, commentBody))
            dialog.dismiss()

        }
        rootView.findViewById<TextView>(R.id.cancelBtn).setOnClickListener { dialog.dismiss() }
    }

    /**
     * This method checks if the user is try to add an empty
     * comment to the data source
     */
    private fun validateComment(title: String, body: String): Comment {

        var commentTitle = ""
        var commentBody = ""

        if (title.isEmpty() || body.isEmpty()) {
            Toast.makeText(
                this, TRY_AGAIN, Toast.LENGTH_LONG
            ).show()
        } else {
            commentTitle = title
            commentBody = body
        }

        return Comment(1, commentTitle, commentBody)
    }

    /**
     * This method displays the dialog box
     * to allow the user add new comment
     */
    fun openDialog(view: View) {
        addComment()
    }
}