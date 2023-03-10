/*
package com.example.rsa_android

package com.example.rsa_android

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.rsa_android.databinding.ActivityMainBinding
import kotlin.math.sqrt
import kotlin.random.Random

const val BIT_LENGTH = 4
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var mainP = 0
    private var mainQ = 0
    private var mainN = 0
    private  var mPublicKey: Pair<Int, Int> = Pair(0 , 0)
    private  var mPrivateKey: Pair<Int, Int> = Pair(0 , 0)
    private var mEncryptedMsg: List<Int> = listOf()

    private var generateClick = false
    private var encryptClick = false
    private var decryptClick = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val tvN = binding.textViewN
        val tvP = binding.textViewP
        val tvQ = binding.textViewQ
        val tvPublicKey = binding.publicKey
        val tvPrivateKey = binding.privateKey
        val tvMsgEncrypt = binding.tvMsgEncrypt
        val btnEncrypt = binding.btnEncrypt
        val btnDecrypt = binding.btnDecrypt
        val linearLayout = binding.lnl
        val edtMsg = binding.edtMsg
        val tvDecrypt = binding.tvDecrypt

        btnEncrypt.visibility = View.GONE
        btnDecrypt.visibility = View.GONE
        edtMsg.visibility = View.GONE
        linearLayout.visibility = View.GONE
        tvDecrypt.visibility = View.GONE


        binding.btnGeneratePq.setOnClickListener {
            val (public, private) = generate_keypair( Math.pow(2.0, BIT_LENGTH.toDouble()).toInt())
            tvP.text = mainP.toString()
            tvQ.text = mainQ.toString()
            tvN.text = mainN.toString()
            mPublicKey = public
            mPrivateKey = private
            tvPublicKey.text = mPublicKey.toString()
            tvPrivateKey.text = mPrivateKey.toString()

            generateClick = true
            btnEncrypt.visibility = View.VISIBLE
            edtMsg.visibility = View.VISIBLE
        }

        btnEncrypt.setOnClickListener {

            val msg = edtMsg.text.toString()
            if(msg != ""){
                linearLayout.visibility = View.VISIBLE
                btnDecrypt.visibility = View.VISIBLE
                tvDecrypt.visibility = View.VISIBLE
                val encryptedMsg = encrypt(msg, mPublicKey)
                mEncryptedMsg = encryptedMsg
                var encryptMsg = ""
                for(char in mEncryptedMsg){
                    encryptMsg += char
                }
                tvMsgEncrypt.text = encryptMsg
            }
            else {
                Toast.makeText(this, "Please input message...", Toast.LENGTH_SHORT).show()
            }
        }

        btnDecrypt.setOnClickListener {
//            btnDecrypt.showLoading()
            val desc = decrypt(mEncryptedMsg, mPrivateKey)
            tvDecrypt.text = desc
//            btnDecrypt.hideLoading()
        }
    }

    private fun gcd(a: Int, b: Int): Int {
        return if (b == 0) a else gcd(b, a % b)
    }

    private fun modInverse(a: Int, m: Int): Int {
        for (x in 1 until m) {
            if (a * x % m == 1) {
                return x
            }
        }
        return -1
    }

    fun isPrime(n: Int): Boolean {
        if (n < 2) {
            return false
        } else if (n == 2) {
            return true
        } else {
            for (i in 2..sqrt(n.toDouble()).toInt() + 1 step 2) {
                if (n % i == 0) {
                    return false
                }
            }
        }
        return true
    }

    private fun generate_keypair( keysize: Int): Pair<Pair<Int, Int>, Pair<Int, Int>> {
        // keysize is the bit length of n so it must be in range(nMin,nMax+1).
        // shl is a bitwise operator
        // x shl y is same as multiplying x by 2**y
        // i am doing this so that p and q values have similar bit-length.
        // this will generate an n value that's hard to factorize into p and q.

        val nMin = 1 shl (keysize - 1)
        val nMax = (1 shl keysize) - 1
        val primes = mutableListOf(2)
        // we choose two prime numbers in range(start, stop) so that the difference of bit lengths is at most 2.
        val start = 1 shl (keysize / 2 - 1)
        val stop = 1 shl (keysize / 2 + 1)

        if (start >= stop) {
            return Pair(Pair(0, 0), Pair(0, 0))
        }

        for (i in 3..stop step 2) {
            var isPrime = true
            for (p in primes) {
                if (i % p == 0) {
                    isPrime = false
                    break
                }
            }
            if (isPrime) {
                primes.add(i)
            }
        }

        while (primes.isNotEmpty() && primes[0] < start) {
            primes.removeAt(0)
        }

        // choosing p and q from the generated prime numbers.
        var p = 0
        var q = 0
        while (primes.isNotEmpty()) {
            p = primes.random()
            primes.remove(p)
            val q_values = primes.filter { q -> nMin <= p * q && p * q <= nMax }
            if (q_values.isNotEmpty()) {
                q = q_values.random()
                break
            }
        }
        println("$p $q")
        mainP = p
        mainQ = q

        Log.d("TAG", "generate_keypair: $p and $q")
        val n = p * q
        mainN = n
        val phi = (p - 1) * (q - 1)

        // generate public key 1 < e < phi(n)
        var e = Random.nextInt(1, phi)
        var g = gcd(e, phi)


        while (g != 1) {
            // as long as gcd(1,phi(n)) is not 1, keep generating e
            e = Random.nextInt(1, phi)
            g = gcd(e, phi)
        }

        // generate private key
        val d = modInverse(e, phi)

        // public key (e,n)
        // private key (d,n)
        return Pair(Pair(e, n), Pair(d, n))
    }

    private fun encrypt(msgPlaintext: String, packages: Pair<Int, Int>): List<Int> {
        val (e, n) = packages
        val ciphertext: MutableList<Int> = mutableListOf()
        for (c in msgPlaintext) {
            val temp = c.hashCode().toBigInteger().pow(e) % n.toBigInteger()
            ciphertext.add(temp.toInt() )
        }
        return ciphertext
    }

    private fun decrypt(msg: List<Int>, packages: Pair<Int, Int>): String {
        val (d, n) = packages
        var msgPlaintext = ""
        for (c in msg){
            val char = c.toBigInteger().pow(d) % n.toBigInteger()
            msgPlaintext += char.toInt().toChar().toString()
        }
        return msgPlaintext
    }
}*/
