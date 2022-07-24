package com.example.githubapp.adapter

import android.content.Context
import android.content.Intent
import android.graphics.Paint
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.githubapp.R
import com.example.githubapp.model.Repo


class RecyclerViewAdapter(
    private val context: Context,
    private val dataList: List<Repo>
) :
    RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>() {


    private var list = dataList

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val name: TextView =
            view.findViewById(R.id.nameTW)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycler_view_item, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = list[position]

        holder.name.text = data.name
        holder.name.paintFlags = holder.name.paintFlags or Paint.UNDERLINE_TEXT_FLAG

        holder.name.setOnClickListener {
            goToWebsiteWithUrl(data.html_url)
        }
    }

    private fun goToWebsiteWithUrl(url: String) {

        val properURL = getProperUrl(url)
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(properURL))
        context.startActivity(browserIntent)
    }

    private fun getProperUrl(url: String): String {
        var properURL = url
        if (!properURL.startsWith("http://") && !properURL.startsWith("https://")) {
            properURL = "http://" + properURL;
        }

        return properURL

    }

    override fun getItemCount(): Int {
        return list.size
    }
}