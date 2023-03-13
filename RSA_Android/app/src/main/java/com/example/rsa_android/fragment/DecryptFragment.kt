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
import com.example.rsa_android.LOAD_ENCRYPTED_TEXT
import com.example.rsa_android.LOAD_OTHER_PRIVATE_KEY
import com.example.rsa_android.R
import com.example.rsa_android.RSA
import com.example.rsa_android.Utils.MyFile
import com.example.rsa_android.databinding.FragmentDecryptBinding
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
        val edtEncryptedText = binding.encryptedTextEditText
        val edtPlainText = binding.plainTextEditText
        val btnDecrypt = binding.decryptButton
        btnDecrypt.setOnClickListener {
            rsa = RSA()
            if (edtD.text.isNullOrEmpty() || edtN.text.isNullOrEmpty()) {
                Toast.makeText(context, "Please Load private key", Toast.LENGTH_SHORT).show()
            } else {
                if(edtEncryptedText.text.isNullOrEmpty()){
                    Toast.makeText(context, "Please Load encrypted text", Toast.LENGTH_SHORT).show()
                } else{
                    edtPlainText.setText(
                        rsa.decrypt(listEncryptBigInteger, encryptD, encryptN),
                        TextView.BufferType.EDITABLE
                    )
                }
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
            R.id.loadOtherPrivateKeyOptionMenu -> {
                val dialogFileChoose = DialogFileChoose("Load other private key", requireContext(), null,
                    resultLauncherLoadOtherPrivateKey, LOAD_OTHER_PRIVATE_KEY, requireView())
                dialogFileChoose.showExportDialog()
            }
            R.id.loadEncryptedTextDecOptionMenu -> {
                val dialogFileChoose = DialogFileChoose("Load Encrypted Text", requireContext(), null,
                    resultLauncher, LOAD_ENCRYPTED_TEXT, requireView())
                dialogFileChoose.showExportDialog()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    @SuppressLint("SetTextI18n")
    private var resultLauncherLoadOtherPrivateKey =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data
                val uri = data!!.data
                if (uri != null) {
                    val myFile = MyFile()
                    val (d, n) = myFile.loadOtherPublicKeyFile(requireContext(), uri)
                    val edtD = binding.dNumberEditText
                    val edtN = binding.nNumberEditText
                    encryptD = d
                    encryptN = n

                    edtD.setText(d.toString(), TextView.BufferType.EDITABLE)
                    edtN.setText(n.toString(), TextView.BufferType.EDITABLE)

                }
            }
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