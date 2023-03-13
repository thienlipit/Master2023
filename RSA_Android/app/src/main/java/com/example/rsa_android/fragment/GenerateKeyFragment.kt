package com.example.rsa_android.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.rsa_android.R
import com.example.rsa_android.RSA
import com.example.rsa_android.SAVE_PRIVATE_KEY
import com.example.rsa_android.SAVE_PUBLIC_KEY
import com.example.rsa_android.Utils.MyFile
import com.example.rsa_android.databinding.FragmentGenerateKeyBinding
import java.math.BigInteger


const val FILE_KEY_STORE = "FILE_KEY_STORE.txt"

class GenerateKeyFragment : Fragment() {
    var numBit: Int = 64
    private var _binding: FragmentGenerateKeyBinding? = null
    private val binding get() = _binding!!
    lateinit var rsa: RSA

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGenerateKeyBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val btnGenerateKey = binding.generateKeyButton
        val edtP = binding.pNumberEditText
        val edtQ = binding.qNumberEditText
        val tvN = binding.nNumberTextView
        val tvPhi = binding.phiNNumberTextView
        val tvE = binding.eNumberTextView
        val tvD = binding.dNumberTextView
        val tvEN = binding.publicKeyTextView
        val tvDN = binding.privateKeyTextView

        val radioGroup = binding.radioGroup
        val radioBtn64 = binding.radio64bits
        val radioBtn128 = binding.radio128bits
        val radioBtn256 = binding.radio256bits
        val radioBtn512 = binding.radio512bits
        val radioBtn1024 = binding.radio1024bits

        radioGroup.setOnCheckedChangeListener { _, p1 ->
            when (p1) {
                R.id.radio_64bits -> {
                    radioBtn64.isChecked = true
                    numBit = 64
                    Log.d("aa", radioBtn64.text.toString())
                }
                R.id.radio_128bits -> {
                    radioBtn128.isChecked = true
                    numBit = 128
                    Log.d("GEN", radioBtn128.text.toString())
                }
                R.id.radio_256bits -> {
                    radioBtn256.isChecked = true
                    numBit = 256
                    Log.d("GEN", radioBtn256.text.toString())
                }
                R.id.radio_512bits -> {
                    radioBtn512.isChecked = true
                    numBit = 512
                    Log.d("GEN", radioBtn512.text.toString())
                }
                R.id.radio_1024bits -> {
                    radioBtn1024.isChecked = true
                    numBit = 1024
                    Log.d("GEN", radioBtn1024.text.toString())
                }
            }
        }
        rsa = RSA()

        btnGenerateKey.setOnClickListener {
            Log.d("GENERATE", "btn generate clicked")
            var p: BigInteger?
            var q: BigInteger?
            var n: BigInteger?
            var phi: BigInteger?
            var e: BigInteger?
            var d: BigInteger?

            do {
                // 1. Find large primes p and q
                p = rsa.largePrime(numBit)!!
                q = rsa.largePrime(numBit)!!

                // 2. Compute n from p and q
                // n is mod for private & public keys, n bit length is key length
                n = rsa.n(p, q)!!

                // 3. Compute Phi(n) (Euler's totient function)
                // Phi(n) = (p-1)(q-1)
                // BigIntegers are objects and must use methods for algebraic operations
                phi = rsa.getPhi(p, q)!!

                // 4. Find an int e such that 1 < e < Phi(n) 	and gcd(e,Phi) = 1
                e = rsa.genE(phi, numBit)

                // 5. Calculate d where  d ≡ e^(-1) (mod Phi(n))
                d = rsa.extEuclid(e, phi)[1]
                val boolean: Boolean = (d > BigInteger.ZERO)
                Log.d("TEST", boolean.toString())
            } while (!boolean)

            edtP.text = p.toString()
            edtQ.text = q.toString()
            tvN.text = n.toString()
            tvPhi.text = phi.toString()
            tvE.text = e.toString()
            tvD.text = d.toString()
            tvEN.text = "($e, $n)"
            tvDN.text = "($d, $n)"
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.generate_key_fragment_option_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.saveKeyGeneOptionMenu -> {
                if (binding.qNumberEditText.text.equals("")) {
                    Toast.makeText(context, "Please press GENERATE KEY button", Toast.LENGTH_SHORT)
                        .show()

                } else {
                    val myFile = MyFile()
                    myFile.saveKey(
                        requireContext(),
                        FILE_KEY_STORE,
                        binding.nNumberTextView.text.toString(),
                        binding.eNumberTextView.text.toString(),
                        binding.dNumberTextView.text.toString()
                    )
                }
            }

            R.id.exportPubKeyGeneOptionMenu -> {

                if (binding.qNumberEditText.text.equals("")) {
                    Toast.makeText(context, "Please press GENERATE KEY button", Toast.LENGTH_SHORT)
                        .show()

                } else {
                    val dialogFileChoose = DialogFileChoose("Export public key file", requireContext(), null,
                        null, SAVE_PUBLIC_KEY, requireView())
                    dialogFileChoose.showExportDialog()

                }

            }
            R.id.exportPriKeyGeneOptionMenu -> {
                if (binding.qNumberEditText.text.equals("")) {
                    Toast.makeText(context, "Please press GENERATE KEY button", Toast.LENGTH_SHORT)
                        .show()

                } else {
                    val dialogFileChoose = DialogFileChoose("Export private key file", requireContext(), null,
                        null, SAVE_PRIVATE_KEY, requireView())
                    dialogFileChoose.showExportDialog()

                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}