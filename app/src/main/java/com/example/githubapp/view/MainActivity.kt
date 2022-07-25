package com.example.githubapp.view

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.githubapp.adapter.RecyclerViewAdapter
import com.example.githubapp.databinding.ActivityMainBinding
import com.example.githubapp.viewmodel.MainActivityViewModel
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import dagger.hilt.android.AndroidEntryPoint
import de.hdodenhof.circleimageview.CircleImageView

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var rv: RecyclerView
    private lateinit var adapter: RecyclerViewAdapter
    private lateinit var avatarImageView: CircleImageView
    private lateinit var userNameTextInputLayout: TextInputLayout
    private lateinit var searchBtn: Button
    private lateinit var viewModel: MainActivityViewModel
    private lateinit var pb: ProgressBar

    private var userName = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        avatarImageView = binding.userAvatarImageView
        userNameTextInputLayout = binding.userNameET
        searchBtn = binding.searchBtn
        pb = binding.progressBar

        rv = binding.recyclerView
        rv.layoutManager = LinearLayoutManager(this)

        searchBtn.setOnClickListener {

            userName = userNameTextInputLayout.editText?.text.toString()

            Log.d(TAG, "onCreate: $userName")
            if (userName.isNotEmpty()) {

                rv.visibility = View.INVISIBLE
                avatarImageView.visibility = View.INVISIBLE

                pb.visibility = View.VISIBLE

                initViewModel()
            } else {
                Snackbar.make(
                    it,
                    "You must type user name to make any search",
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        }


    }

    private fun initViewModel() {

        viewModel = ViewModelProvider(this)[MainActivityViewModel::class.java]

        viewModel.loadData(userName)

        observeData()

    }

    private fun observeData() {

        viewModel.getLiveData().observe(this) {

            pb.visibility = View.INVISIBLE

            if (it != null) {
                if (it.isEmpty()) {

                    rv.visibility = View.INVISIBLE
                    avatarImageView.visibility = View.INVISIBLE


                    Snackbar.make(
                        searchBtn,
                        "$userName has no repository",
                        Snackbar.LENGTH_SHORT
                    ).show()
                } else {

                    rv.visibility = View.VISIBLE
                    avatarImageView.visibility = View.VISIBLE


                    adapter = RecyclerViewAdapter(this, it)
                    rv.adapter = adapter
                    adapter.notifyDataSetChanged()


                    Glide
                        .with(this)
                        .load(it[0].owner.avatar_url)
                        .centerCrop()
                        .into(avatarImageView)

                }

            } else {
                rv.visibility = View.INVISIBLE
                avatarImageView.visibility = View.INVISIBLE


                Snackbar.make(
                    searchBtn,
                    "There is no user named $userName",
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        }

    }

    companion object {
        private const val TAG = "MainActivity"
    }
}