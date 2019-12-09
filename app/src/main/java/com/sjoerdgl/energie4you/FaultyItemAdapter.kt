package com.sjoerdgl.energie4you

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.widget.Toast
import android.content.Intent



class FaultyItemAdapter(val faultyItems: ArrayList<FaultyItem>) : RecyclerView.Adapter<FaultyItemAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)

        // Inflate the custom layout
        val contactView = inflater.inflate(R.layout.row_layout, parent, false)

        // Return a new holder instance
        return ViewHolder(contactView)
    }

    override fun getItemCount(): Int {
        return faultyItems.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // Get the data model based on position
        val faultyItem = faultyItems.get(position)

        // Set item views based on your views and data model
        val textView = holder.nameTextView
        val categoryTextView = holder.categoryTextView

        textView.text = faultyItem.name
        categoryTextView.text = faultyItem.category
        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, TestActivity::class.java)
            (holder.itemView.context as Activity).startActivity(intent)

            Toast.makeText(holder.itemView.context, position.toString(), Toast.LENGTH_SHORT).show()
        }
    }

    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access
    inner class ViewHolder// We also create a constructor that accepts the entire item row
    // and does the view lookups to find each subview
        (itemView: View) : RecyclerView.ViewHolder(itemView) {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        var nameTextView: TextView = itemView.findViewById(R.id.employee_name)
        var categoryTextView: TextView = itemView.findViewById(R.id.category_name)

        // to access the context from any ViewHolder instance.
    }
}