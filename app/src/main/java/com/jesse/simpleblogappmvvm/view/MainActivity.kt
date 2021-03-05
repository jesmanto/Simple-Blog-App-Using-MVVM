package com.jesse.simpleblogappmvvm.view

import android.app.AlertDialog
import android.content.Intent
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
import com.jesse.simpleblogappmvvm.adapters.PostAdapter
import com.jesse.simpleblogappmvvm.databinding.ActivityMainBinding
import com.jesse.simpleblogappmvvm.model.Post
import com.jesse.simpleblogappmvvm.model.repository.Repository
import com.jesse.simpleblogappmvvm.viewmodel.PostViewModel
import com.jesse.simpleblogappmvvm.viewmodel.PostViewModelFactory
import com.jesse.simpleblogappmvvm.model.repository.utils.*

class MainActivity : AppCompatActivity(), ItemViewClickListener {

    lateinit var binding: ActivityMainBinding

    lateinit var viewModel: PostViewModel

    private val adapter = PostAdapter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val repository = Repository()
        val viewModelFactory = PostViewModelFactory(repository)
        viewModel = ViewModelProvider(this, viewModelFactory)
            .get(PostViewModel::class.java)

        // Observing postList live data from the viewModel
        viewModel.postList.observe(this, Observer { response ->
            if (response.isSuccessful) {
                Log.d(TAG, "$response")
                response.body()?.let { adapter.loadPostList(it) }
                response.body()?.let { adapter.reloadPostList(it) }
                initRecyclerView()
            } else {
                Log.d(TAG, response.code().toString())
            }

        })

        // Observing the post live data from the viewModel
        viewModel.post.observe(this, Observer { response ->
            if (response.isSuccessful) {
                Log.d(TAG, "${response.body()}")
                response.body()?.let { adapter.addCustomPost(it) }
            }
        })
    }

    /**
     * Sets an adapter to the recyclerView
     */
    private fun initRecyclerView() {
        binding.recyclerView.postRecyclerView.adapter = adapter
        hideProgressBar()
    }

    /**
     * Opens a dialog box that takes user inputs
     * to add a new post to the server
     */
    private fun addPost() {
        val postDialog = AlertDialog.Builder(this)
        val rootView = layoutInflater.inflate(R.layout.post_alert_dialog, null)

        postDialog.setView(rootView)
        val dialog = postDialog.create()
        dialog.show()

        // When the positive button is clicked, it gets the values of the
        // post fields and sends the data to the server
        // as well as populates the list of posts locally
        rootView.findViewById<TextView>(R.id.addBtn).setOnClickListener {
            try {
                val userId = rootView.findViewById<EditText>(R.id.userId).text.toString().toInt()
                val postTitle = rootView.findViewById<EditText>(R.id.commentTitle).text.toString()
                val postBody = rootView.findViewById<EditText>(R.id.bodyComment).text.toString()
                val newPost = Post(userId, 1, postTitle, postBody)
                viewModel.pushPost(newPost)
                dialog.dismiss()
            } catch (e: NumberFormatException) {
                Toast.makeText(
                    this, TRY_AGAIN, Toast.LENGTH_LONG
                ).show()
            }
        }

        // The cancel button closes the AlertDialog box
        rootView.findViewById<TextView>(R.id.cancelBtn).setOnClickListener {
            dialog.dismiss()
        }
    }

    /**
     * Sets the progressBar to GONE
     */
    private fun hideProgressBar() {
        binding.postProgressBar.progressBar.visibility = View.GONE
    }

    /**
     * Opens the up the view that provides the
     * user the opportunity to filter by userId
     */
    fun openFilterSection(view: View) {
        binding.recyclerView.filterContainer.visibility = View.VISIBLE
    }

    /**
     * Initiates the filtering features of the application
     */
    fun filterResults(view: View) {
        binding.recyclerView.filterContainer.visibility = View.GONE
        try {
            val id = binding.recyclerView.enterId.text.toString().toInt()
            adapter.loadFilteredPosts(id)
        } catch (e: NumberFormatException) {
            Toast.makeText(this, "Please enter a valid digit", Toast.LENGTH_LONG)
                .show()
        }
    }

    /**
     * Determines what happens when a post is clicked on
     */
    override fun onClickListener(position: Int, view: View, post: Post) {
        val intent = Intent(this, CommentsActivity::class.java).putExtra(POST, post)
        startActivity(intent)
    }

    /**
     * Called in by a view in the xml to show the
     * alert dialog box to add new post
     */
    fun addBtnOnClick(view: View) {
        if (binding.recyclerView.filterContainer.visibility == View.VISIBLE) {
            binding.recyclerView.filterContainer.visibility = View.GONE
        }
        addPost()
    }
}