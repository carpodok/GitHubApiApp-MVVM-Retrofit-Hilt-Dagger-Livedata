package com.example.githubapp.view

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
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

    private var userName = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        avatarImageView = binding.userAvatarImageView
        userNameTextInputLayout = binding.userNameET
        searchBtn = binding.searchBtn

        searchBtn.setOnClickListener {

            userName = userNameTextInputLayout.editText?.text.toString()

            Log.d(TAG, "onCreate: $userName")
            if (userName.isNotEmpty()) {
                initViewModel()
            } else {
                Snackbar.make(
                    it,
                    "You must type user name to make any search",
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        }

        rv = binding.recyclerView
        rv.layoutManager = LinearLayoutManager(this)

    }

    private fun initViewModel() {

        val viewModel = ViewModelProvider(this)[MainActivityViewModel::class.java]

        viewModel.getLiveData().observe(this) {

            if (it != null) {
                if (it.isEmpty()) {
                    Snackbar.make(
                        searchBtn,
                        "$userName has no repository",
                        Snackbar.LENGTH_SHORT
                    ).show()
                }

                adapter = RecyclerViewAdapter(this, it)
                rv.adapter = adapter
                adapter.notifyDataSetChanged()

                Glide
                    .with(this)
                    .load(it[0].owner.avatar_url)
                    .centerCrop()
                    .into(avatarImageView)


            } else {
                Snackbar.make(
                    searchBtn,
                    "There is no user named $userName",
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        }

        viewModel.loadData(userName)

    }

    companion object {
        private const val TAG = "MainActivity"
    }
}