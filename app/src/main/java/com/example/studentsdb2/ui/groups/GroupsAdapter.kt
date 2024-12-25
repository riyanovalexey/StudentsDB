package com.example.studentsdb2.ui.groups

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.studentsdb2.R
import com.example.studentsdb2.data.model.Group

class GroupsAdapter(
    private val onEditClick: (Group) -> Unit,
    private val onDeleteClick: (Group) -> Unit
) : ListAdapter<Group, GroupsAdapter.GroupViewHolder>(GroupDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_group, parent, false)
        return GroupViewHolder(view)
    }

    override fun onBindViewHolder(holder: GroupViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class GroupViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val groupNumberTextView: TextView = itemView.findViewById(R.id.groupNumberTextView)
        private val facultyNameTextView: TextView = itemView.findViewById(R.id.facultyNameTextView)
        private val editButton: ImageButton = itemView.findViewById(R.id.editGroupButton)
        private val deleteButton: ImageButton = itemView.findViewById(R.id.deleteGroupButton)

        fun bind(group: Group) {
            groupNumberTextView.text = group.number
            facultyNameTextView.text = group.facultyName

            editButton.setOnClickListener { onEditClick(group) }
            deleteButton.setOnClickListener { onDeleteClick(group) }
        }
    }

    private class GroupDiffCallback : DiffUtil.ItemCallback<Group>() {
        override fun areItemsTheSame(oldItem: Group, newItem: Group): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Group, newItem: Group): Boolean {
            return oldItem == newItem
        }
    }
} 