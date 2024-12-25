package com.example.studentsdb2.ui.groups

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.example.studentsdb2.R
import com.example.studentsdb2.data.model.Group
import com.google.android.material.textfield.TextInputEditText

class GroupDialog(
    private val group: Group? = null,
    private val onSave: (Group) -> Unit
) : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireContext())
        val inflater = requireActivity().layoutInflater
        val view = inflater.inflate(R.layout.dialog_group, null)

        val groupNumberEdit = view.findViewById<TextInputEditText>(R.id.groupNumberEditText)
        val facultyNameEdit = view.findViewById<TextInputEditText>(R.id.facultyNameEditText)

        group?.let {
            groupNumberEdit.setText(it.number)
            facultyNameEdit.setText(it.facultyName)
        }

        builder.setView(view)
            .setTitle(if (group == null) R.string.add_group else R.string.edit)
            .setPositiveButton(R.string.save) { _, _ ->
                val newGroup = Group(
                    id = group?.id ?: 0,
                    number = groupNumberEdit.text.toString(),
                    facultyName = facultyNameEdit.text.toString()
                )
                onSave(newGroup)
            }
            .setNegativeButton(R.string.cancel, null)

        return builder.create()
    }
} 