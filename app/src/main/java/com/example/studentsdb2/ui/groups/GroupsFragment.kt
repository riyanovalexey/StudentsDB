package com.example.studentsdb2.ui.groups

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.example.studentsdb2.R
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.launch

class GroupsFragment : Fragment(R.layout.fragment_groups) {
    private val viewModel: GroupsViewModel by viewModels()
    private lateinit var adapter: GroupsAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView(view)
        setupFab(view)
        observeGroups()
    }

    private fun setupRecyclerView(view: View) {
        adapter = GroupsAdapter(
            onEditClick = { group ->
                showGroupDialog(group)
            },
            onDeleteClick = { group ->
                deleteGroup(group)
            }
        )
        view.findViewById<RecyclerView>(R.id.groupsRecyclerView).adapter = adapter
    }

    private fun setupFab(view: View) {
        view.findViewById<FloatingActionButton>(R.id.addGroupFab).setOnClickListener {
            showGroupDialog(null)
        }
    }

    private fun observeGroups() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.groups.collect { groups ->
                adapter.submitList(groups)
            }
        }
    }

    private fun showGroupDialog(group: com.example.studentsdb2.data.model.Group?) {
        GroupDialog(
            group = group,
            onSave = { newGroup ->
                if (group == null) {
                    viewModel.addGroup(newGroup)
                } else {
                    viewModel.updateGroup(newGroup)
                }
            }
        ).show(childFragmentManager, "group_dialog")
    }

    private fun deleteGroup(group: com.example.studentsdb2.data.model.Group) {
        viewLifecycleOwner.lifecycleScope.launch {
            if (viewModel.canDeleteGroup(group.id)) {
                viewModel.deleteGroup(group)
            } else {
                Toast.makeText(
                    requireContext(),
                    R.string.cannot_delete_group_with_students,
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
} 