package com.example.rsa_android.fragment

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Environment
import android.text.TextUtils
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.*
import androidx.activity.result.ActivityResultLauncher
import com.example.rsa_android.*
import com.example.rsa_android.Utils.MyFile
import java.math.BigInteger

class DialogFileChoose(
    private val title: String,
    val context: Context,
    private val listPlainText: List<BigInteger>?,
    private val resultLauncher: ActivityResultLauncher<Intent>?,
    private val requestTask: Int,
    private val view: View
) {
    @SuppressLint("SetTextI18n")
    fun showExportDialog() {
        val dialog = Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.export_file_text_dialog)
        dialog.window!!.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
        dialog.setCanceledOnTouchOutside(true)
        val titleTV: TextView = dialog.findViewById(R.id.titleExportDialogTextView)
        titleTV.text = title
        val btnClose: ImageView = dialog.findViewById(R.id.closeExportDialogImageView)
        val edtFileNameDialog: EditText = dialog.findViewById(R.id.fileNameExportEditText)
        val edtFilePathDialog: EditText = dialog.findViewById(R.id.filePathExportEditText)
        val tvFileNameDialog: TextView = dialog.findViewById(R.id.textView2)
        val btn: Button = dialog.findViewById(R.id.saveTextFileExportButton)
        val folder =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)

        edtFilePathDialog.setText(folder.toString(), TextView.BufferType.EDITABLE)

        btnClose.setOnClickListener { dialog.dismiss() }

        when (requestTask) {
            //SAVE_PUBLIC_KEY
            SAVE_PUBLIC_KEY -> {
                btn.setOnClickListener {
                    if (TextUtils.isEmpty(edtFileNameDialog.text.toString())) {
                        Toast.makeText(context, "File name is empty", Toast.LENGTH_SHORT).show()
                        return@setOnClickListener
                    }
                    var fileName: String = edtFileNameDialog.text.toString()
                    val arr = fileName.split("\\.").toTypedArray()
                    if (arr.size == 1) fileName += ".txt"

                    val key = view.findViewById<TextView>(R.id.eNumberTextView).text.toString()
                    val myFile = MyFile()
                    myFile.exportGenerateKey(
                        context,
                        folder!!,
                        fileName,
                        key,
                        view.findViewById<TextView>(R.id.nNumberTextView).text.toString()

                    )
                    dialog.dismiss()
                }
            }

            //SAVE_PRIVATE_KEY
            SAVE_PRIVATE_KEY -> {
                btn.setOnClickListener {
                    if (TextUtils.isEmpty(edtFileNameDialog.text.toString())) {
                        Toast.makeText(context, "File name is empty", Toast.LENGTH_SHORT).show()
                        return@setOnClickListener
                    }

                    var fileName: String = edtFileNameDialog.text.toString()
                    val arr = fileName.split("\\.").toTypedArray()
                    if (arr.size == 1) fileName += ".txt"

                    val key = view.findViewById<TextView>(R.id.dNumberTextView).text.toString()
                    val myFile = MyFile()
                    myFile.exportGenerateKey(
                        context,
                        folder!!,
                        fileName,
                        key,
                        view.findViewById<TextView>(R.id.nNumberTextView).text.toString()
                    )
                    dialog.dismiss()
                }
            }

            //LOAD_OTHER_PUBLIC_KEY
            LOAD_OTHER_PUBLIC_KEY -> {
                edtFileNameDialog.visibility = View.GONE
                tvFileNameDialog.visibility = View.GONE
                btn.text = "Open Folder"
                btn.setOnClickListener {
                    val intent = Intent(Intent.ACTION_GET_CONTENT)
                    intent.type = "text/plain"
                    resultLauncher!!.launch(intent)
                    dialog.dismiss()
                }
            }

            //EXPORT_ENCRYPTED_TEXT
            EXPORT_ENCRYPTED_TEXT -> {
                btn.setOnClickListener {
                    if (TextUtils.isEmpty(edtFileNameDialog.text.toString())) {
                        Toast.makeText(context, "File name is empty", Toast.LENGTH_SHORT).show()
                        return@setOnClickListener
                    }

                    var fileName: String = edtFileNameDialog.text.toString()
                    val arr = fileName.split("\\.").toTypedArray()
                    if (arr.size == 1) fileName += ".txt"

                    val myFile = MyFile()

                    myFile.saveEncryptFile(context, folder, fileName, listPlainText!!)
                    dialog.dismiss()
                }
            }

            //LOAD_OTHER_PRIVATE_KEY
            LOAD_OTHER_PRIVATE_KEY -> {
                edtFileNameDialog.visibility = View.GONE
                tvFileNameDialog.visibility = View.GONE
                btn.text = "Open Folder"
                btn.setOnClickListener {
                    val intent = Intent(Intent.ACTION_GET_CONTENT)
                    intent.type = "text/plain"
                    resultLauncher!!.launch(intent)
                    dialog.dismiss()
                }
            }

            //LOAD_ENCRYPTED_TEXT
            LOAD_ENCRYPTED_TEXT -> {
                edtFileNameDialog.visibility = View.GONE
                tvFileNameDialog.visibility = View.GONE
                btn.text = "Open Folder"
                btn.setOnClickListener {
                    val intent = Intent(Intent.ACTION_GET_CONTENT)
                    intent.type = "text/plain"
                    resultLauncher!!.launch(intent)
                    dialog.dismiss()
                }
            }
        }

        dialog.show()
    }
}