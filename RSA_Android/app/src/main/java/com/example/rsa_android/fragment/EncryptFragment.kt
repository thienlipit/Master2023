package com.example.rsa_android.fragment

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.text.TextUtils
import android.view.*
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.example.rsa_android.R
import com.example.rsa_android.RSA
import com.example.rsa_android.Utils.MyFile
import com.example.rsa_android.databinding.FragmentEncryptBinding
import java.io.*
import java.math.BigInteger

class EncryptFragment : Fragment() {
    private var _binding: FragmentEncryptBinding? = null
    private val binding get() = _binding!!
    lateinit var rsa: RSA
    private lateinit var encryptN: BigInteger
    private lateinit var encryptE: BigInteger
    private lateinit var listPlainText: List<BigInteger>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEncryptBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val edtE = binding.eNumberEditText
        val edtPlainText = binding.plainTextEditText
        val edtEncryptText = binding.encryptedTextEditText
        val btnEncrypt = binding.encryptButton

        btnEncrypt.setOnClickListener {
            rsa = RSA()
            if (edtPlainText.text.isNullOrEmpty()) {
                Toast.makeText(context, "Plain text is empty", Toast.LENGTH_SHORT).show()
            } else {
                if (edtE.text.isNullOrEmpty()) {
                    Toast.makeText(context, "Please Load Generate Key", Toast.LENGTH_SHORT).show()
                } else {
                    listPlainText = rsa.encrypt(edtPlainText.text.toString(), encryptE, encryptN)
                    edtEncryptText.setText(listPlainText.toString(), TextView.BufferType.EDITABLE)
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.encrypt_fragment_option_menu, menu)
    }

    @SuppressLint("SetTextI18n")
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.loadMyKeyPubEncOptionMenu -> {
                val myFile = MyFile()
                val (n, e, d) = myFile.loadMyKey(requireContext(), FILE_KEY_STORE)
                val edtE = binding.eNumberEditText
                val edtN = binding.nNumberEditText
                encryptE = e
                encryptN = n

                edtE.setText(e.toString(), TextView.BufferType.EDITABLE)
                edtN.setText(n.toString(), TextView.BufferType.EDITABLE)
            }
            R.id.exportEncryptedTextEncOptionMenu -> {
                showExportDialog("Export Encrypted Text", requireContext())
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun showExportDialog(title: String, context: Context) {
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
        val btnSave: Button = dialog.findViewById(R.id.saveTextFileExportButton)
        val folder =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)

        edtFilePathDialog.setText(folder.toString(), TextView.BufferType.EDITABLE)

        btnClose.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                dialog.dismiss()
            }
        })

        btnSave.setOnClickListener(object : View.OnClickListener {
            @RequiresApi(Build.VERSION_CODES.R)
            override fun onClick(v: View?) {

                if (TextUtils.isEmpty(edtFileNameDialog.getText().toString())) {
                    Toast.makeText(context, "File name is empty", Toast.LENGTH_SHORT).show()
                    return
                }

                var fileName: String = edtFileNameDialog.getText().toString()
                val arr = fileName.split("\\.").toTypedArray()
                if (arr.size == 1) fileName += ".txt"

                val myFile = MyFile()

                myFile.saveEncryptFile(context, folder, fileName, listPlainText)
                dialog.dismiss()
            }
        })
        dialog.show()
    }
}