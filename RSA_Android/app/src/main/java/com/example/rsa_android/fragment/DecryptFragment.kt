package com.example.rsa_android.fragment

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.view.*
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.example.rsa_android.R
import com.example.rsa_android.RSA
import com.example.rsa_android.Utils.MyFile
import com.example.rsa_android.databinding.FragmentDecryptBinding
import java.io.*
import java.math.BigInteger

class DecryptFragment : Fragment() {
    private var _binding: FragmentDecryptBinding? = null
    private val binding get() = _binding!!
    lateinit var rsa: RSA

    private lateinit var encryptN: BigInteger
    private lateinit var encryptD: BigInteger
    private lateinit var listEncryptBigInteger: List<BigInteger>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDecryptBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val edtD = binding.dNumberEditText
        val edtN = binding.nNumberEditText
        val edtPlainText = binding.plainTextEditText
        val btnDecrypt = binding.decryptButton
        btnDecrypt.setOnClickListener {
            rsa = RSA()
            if (edtD.text.isNullOrEmpty() || edtN.text.isNullOrEmpty()) {
                Toast.makeText(context, "Please Load private key", Toast.LENGTH_SHORT).show()
            } else {
                edtPlainText.setText(
                    rsa.decrypt(listEncryptBigInteger, encryptD, encryptN),
                    TextView.BufferType.EDITABLE
                )
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.decrypt_fragment_option_menu, menu)
    }

    @SuppressLint("SetTextI18n")
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.loadMyPriKeyDecOptionMenu -> {
                val myFile = MyFile()
                val (n, e, d) = myFile.loadMyKey(requireContext(), FILE_KEY_STORE)
                val edtD = binding.dNumberEditText
                val edtN = binding.nNumberEditText
                encryptD = d
                encryptN = n
                edtD.setText(d.toString(), TextView.BufferType.EDITABLE)
                edtN.setText(n.toString(), TextView.BufferType.EDITABLE)
            }
            R.id.loadEncryptedTextDecOptionMenu -> {
                showExportDialog("Load Encrypted Text", requireContext())
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
        val tvFileNameDialog: TextView = dialog.findViewById(R.id.textView2)
        val edtFilePathDialog: EditText = dialog.findViewById(R.id.filePathExportEditText)
        val btnSave: Button = dialog.findViewById(R.id.saveTextFileExportButton)

        edtFileNameDialog.visibility = View.GONE
        tvFileNameDialog.visibility = View.GONE
        btnSave.text = "Open Folder"

        val folder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)
        edtFilePathDialog.setText(folder.toString(), TextView.BufferType.EDITABLE)

        btnClose.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                dialog.dismiss()
            }
        })

        btnSave.setOnClickListener(object : View.OnClickListener {
            @RequiresApi(Build.VERSION_CODES.R)
            override fun onClick(v: View?) {
                val intent = Intent(Intent.ACTION_GET_CONTENT)
                intent.type = "text/plain"
                resultLauncher.launch(intent)
                dialog.dismiss()
            }
        })
        dialog.show()
    }

    var resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data
                val uri = data!!.data
                if (uri != null) {
                    val myFile = MyFile()
                    listEncryptBigInteger = myFile.loadEncryptedTextFile(requireContext(), uri)

                    binding.encryptedTextEditText.setText(
                        listEncryptBigInteger.toString(),
                        TextView.BufferType.EDITABLE
                    )

                }
            }
        }
}