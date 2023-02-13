package com.vismay.makeanote.ui.notes

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import com.vismay.makeanote.data.local.db.entity.NoteEntity
import com.vismay.makeanote.databinding.ActivityMainBinding
import com.vismay.makeanote.ui.base.BaseActivity
import com.vismay.makeanote.ui.createupdatenote.CreateUpdateNoteActivity
import com.vismay.makeanote.utils.Constants.KEY_NOTE_BUNDLE
import com.vismay.makeanote.utils.extensions.ActivityExtension.launchActivity
import com.vismay.makeanote.utils.extensions.ActivityExtension.showDeleteDialog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding, MainActivityViewModel>() {

    override fun getBinding(): ActivityMainBinding = ActivityMainBinding.inflate(layoutInflater)

    override val viewModel: MainActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mViewBinding.fab.setOnClickListener {
            showNoteInDetail()
        }

        viewModel.getNotes.observe(this) { notes ->
            mViewBinding.notesRecycler.adapter = NotesAdapter(notes) { noteClick ->
                if (noteClick.second) {
                    this@MainActivity.showDeleteDialog { isDeleteClicked ->
                        if (isDeleteClicked)
                            viewModel.deleteNote(noteClick.first)
                    }
                    return@NotesAdapter
                }
                showNoteInDetail(noteClick.first)
            }

        }
    }

    private fun showNoteInDetail(note: NoteEntity? = null) {
        Log.d("TAG","${note?.note}")
        launchActivity<CreateUpdateNoteActivity> {
            putExtra(KEY_NOTE_BUNDLE, note)
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.getAllNotes()
    }


}