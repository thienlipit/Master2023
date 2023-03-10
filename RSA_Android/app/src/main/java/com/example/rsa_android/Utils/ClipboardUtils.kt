package com.example.rsa_android.Utils

import android.content.ClipData
import android.content.ClipboardManager
import android.view.View
import android.widget.TextView
import androidx.core.content.getSystemService
import com.example.rsa_android.R
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText

object ClipboardUtils {
    @JvmStatic
    fun copyTextView(view: View) {
        val data = when (view) {
            is TextInputEditText -> Pair(view.editableText, view.hint)
            is TextView -> Pair(view.text, view.contentDescription)
            else -> return
        }
        if (data.first == null || data.first.isEmpty()) {
            return
        }
        val service = view.context.getSystemService<ClipboardManager>() ?: return
        service.setPrimaryClip(ClipData.newPlainText(data.second, data.first))
        Snackbar.make(view, view.context.getString(R.string.copied_to_clipboard, data.second), Snackbar.LENGTH_LONG).show()
    }
}
