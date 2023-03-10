package com.example.rsa_android.fragment

import android.Manifest
import android.os.Bundle
import android.text.TextUtils
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.rsa_android.R


class GenerateKeyFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_generate_key, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.generate_key_fragment_option_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
//            R.id.autoGeneKeyGeneOptionMenu -> autoGenerateKey()
//            R.id.saveKeyGeneOptionMenu -> requestPermissions(
//                arrayOf(
//                    Manifest.permission.READ_EXTERNAL_STORAGE,
//                    Manifest.permission.WRITE_EXTERNAL_STORAGE
//                )
//            )
//            R.id.exportPubKeyGeneOptionMenu -> {
//                if (TextUtils.isEmpty(txtN.getText().toString()) || TextUtils.isEmpty(
//                        txtE.getText().toString()
//                    )
//                ) {
//                    Toast.makeText(context, "Public key is empty", Toast.LENGTH_SHORT).show()
//                    break
//                }
//                showExportDialog(
//                    "Export public key file",
//                    GenerateKeyFragment.REQUEST_CODE_EXPORT_PUBLIC_KEY
//                )
//            }
//            R.id.exportPriKeyGeneOptionMenu -> {
//                if (TextUtils.isEmpty(txtN.getText().toString()) || TextUtils.isEmpty(
//                        txtD.getText().toString()
//                    )
//                ) {
//                    Toast.makeText(context, "Private key is empty", Toast.LENGTH_SHORT).show()
//                    break
//                }
//                showExportDialog(
//                    "Export private key file",
//                    GenerateKeyFragment.REQUEST_CODE_EXPORT_PRIVATE_KEY
//                )
//            }
        }
        return super.onOptionsItemSelected(item)
    }

}