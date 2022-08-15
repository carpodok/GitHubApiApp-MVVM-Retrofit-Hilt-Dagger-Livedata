package com.example.githubapp.view

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.ProgressBar
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

    private lateinit var adapter: RecyclerViewAdapter
    private lateinit var viewModel: MainActivityViewModel

    private var userName = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.recyclerView.layoutManager = LinearLayoutManager(this)

        binding.searchBtn.setOnClickListener {

            val view = this.currentFocus
            if (view != null) {
                val imm: InputMethodManager =
                    getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(view.windowToken, 0)
            }

            userName = binding.userNameET.editText?.text.toString()

            Log.d(TAG, "onCreate: $userName")
            if (userName.isNotEmpty()) {

                binding.recyclerView.visibility = View.INVISIBLE
                binding.userAvatarImageView.visibility = View.INVISIBLE

                binding.progressBar.visibility = View.VISIBLE

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

            binding.progressBar.visibility = View.INVISIBLE

            if (it != null) {
                if (it.isEmpty()) {

                    binding.recyclerView.visibility = View.INVISIBLE
                    binding.userAvatarImageView.visibility = View.INVISIBLE


                    Snackbar.make(
                        binding.searchBtn,
                        "$userName has no repository",
                        Snackbar.LENGTH_SHORT
                    ).show()
                } else {

                    binding.recyclerView.visibility = View.VISIBLE
                    binding.userAvatarImageView.visibility = View.VISIBLE


                    adapter = RecyclerViewAdapter(it)
                    binding.recyclerView.adapter = adapter
                    adapter.notifyDataSetChanged()


                    Glide
                        .with(this)
                        .load(it[0].owner.avatarUrl)
                        .centerCrop()
                        .into(binding.userAvatarImageView)

                }

            } else {
                binding.recyclerView.visibility = View.INVISIBLE
                binding.userAvatarImageView.visibility = View.INVISIBLE


                Snackbar.make(
                    binding.searchBtn,
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