package com.example.rsa_android.fragment

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.example.rsa_android.EXPORT_ENCRYPTED_TEXT
import com.example.rsa_android.LOAD_OTHER_PUBLIC_KEY
import com.example.rsa_android.R
import com.example.rsa_android.RSA
import com.example.rsa_android.Utils.MyFile
import com.example.rsa_android.databinding.FragmentEncryptBinding
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
            R.id.loadOtherKeyPubEncOptionMenu -> {
                val dialogFileChoose = DialogFileChoose("Load other public key", requireContext(), null,
                    resultLauncher, LOAD_OTHER_PUBLIC_KEY, requireView())
                dialogFileChoose.showExportDialog()

            }
            R.id.exportEncryptedTextEncOptionMenu -> {
                if( binding.encryptedTextEditText.text.isNullOrEmpty()){
                    Toast.makeText(context, "Encrypt text is empty", Toast.LENGTH_SHORT).show()
                } else {
                    val dialogFileChoose = DialogFileChoose("Export Encrypted Text", requireContext(), listPlainText,
                        null, EXPORT_ENCRYPTED_TEXT, requireView())
                    dialogFileChoose.showExportDialog()
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    @SuppressLint("SetTextI18n")
    private var resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data
                val uri = data!!.data
                if (uri != null) {
                    val myFile = MyFile()
                    val (e, n) = myFile.loadOtherPublicKeyFile(requireContext(), uri)
                    val edtE = binding.eNumberEditText
                    val edtN = binding.nNumberEditText
                    encryptE = e
                    encryptN = n

                    edtE.setText(e.toString(), TextView.BufferType.EDITABLE)
                    edtN.setText(n.toString(), TextView.BufferType.EDITABLE)

                }
            }
        }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}