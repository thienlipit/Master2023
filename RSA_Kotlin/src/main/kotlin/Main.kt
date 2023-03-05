import java.math.BigInteger
import java.util.*

/*
 *   Date: Mar 05 2023
 */
const val numBit = 64  // Edit here to change

/**
 * Generates a random large prime number of specified bitlength
 *
 */
fun largePrime(bits: Int): BigInteger? {
    val randomInteger = Random()
    return BigInteger.probablePrime(bits, randomInteger)
}

/** 3. Compute Phi(n) (Euler's totient function)
 * Phi(n) = (p-1)(q-1)
 * BigIntegers are objects and must use methods for algebraic operations
 */
fun getPhi(p: BigInteger, q: BigInteger): BigInteger? {
    return p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE))
}

/**
 * Recursive implementation of Euclidian algorithm to find greatest common denominator
 * Note: Uses BigInteger operations
 */
fun gcd(a: BigInteger, b: BigInteger): BigInteger {
    return if (b == BigInteger.ZERO) {
        a
    } else {
        gcd(b, a.mod(b))
    }
}

/** Recursive EXTENDED Euclidean algorithm, solves Bezout's identity (ax + by = gcd(a,b))
 * and finds the multiplicative inverse which is the solution to ax ≡ 1 (mod m)
 * returns [d, p, q] where d = gcd(a,b) and ap + bq = d
 * Note: Uses BigInteger operations
 */
fun extEuclid(a: BigInteger, b: BigInteger): Array<BigInteger> {
    if (b == BigInteger.ZERO) return arrayOf(
        a, BigInteger.ONE, BigInteger.ZERO
    ) // { a, 1, 0 }
    val vals = extEuclid(b, a.mod(b))
    val d = vals[0]
    val p = vals[2]
    val q = vals[1].subtract(a.divide(b).multiply(vals[2]))
    return arrayOf(
        d, p, q
    )
}

/**
 * generate e by finding a Phi such that they are coprimes (gcd = 1)
 *
 */
fun genE(phi: BigInteger): BigInteger {
    val rand = Random()
    var e = BigInteger(1024, rand)
    do {
        e = BigInteger(numBit * 2, rand)
        while (e.min(phi) == phi) { // while phi is smaller than e, look for a new e
            e = BigInteger(numBit * 2, rand)
        }
    } while (gcd(e, phi) != BigInteger.ONE) // if gcd(e,phi) isnt 1 then stay in loop
    return e
}

fun encrypt(msgPlaintext: String, e: BigInteger?, n: BigInteger?): List<BigInteger> {
    val ciphertext: MutableList<BigInteger> = ArrayList()
    for (c in msgPlaintext.toCharArray()) {
        val temp = BigInteger.valueOf(c.code.toLong()).modPow(e, n)
        ciphertext.add(temp)
    }
    return ciphertext
}

fun decrypt(msg: List<BigInteger>, d: BigInteger?, n: BigInteger?): String {
    val msgPlaintext = StringBuilder()
    for (c in msg) {
        val charCode = c.modPow(d, n)
        msgPlaintext.append(charCode.toInt().toChar())
    }
    return msgPlaintext.toString()
}

fun n(p: BigInteger, q: BigInteger?): BigInteger? {
    return p.multiply(q)
}

fun main() {
    // 1. Find large primes p and q
    val p: BigInteger = largePrime(numBit)!!
    val q: BigInteger = largePrime(numBit)!!

    // 2. Compute n from p and q
    // n is mod for private & public keys, n bit length is key length
    val n: BigInteger = n(p, q)!!

    // 3. Compute Phi(n) (Euler's totient function)
    // Phi(n) = (p-1)(q-1)
    // BigIntegers are objects and must use methods for algebraic operations
    val phi: BigInteger = getPhi(p, q)!!

    // 4. Find an int e such that 1 < e < Phi(n) 	and gcd(e,Phi) = 1
    val e: BigInteger = genE(phi)

    // 5. Calculate d where  d ≡ e^(-1) (mod Phi(n))
    val d: BigInteger = extEuclid(e, phi)[1]

    // Print generated values for reference
    println("p: $p")
    println("q: $q")
    println("n: $n")
    println("Phi: $phi")
    println("e: $e")
    println("d: $d")

    // encryption / decryption example
    val message = "Tran thien 123@@,./"

    // Encrypt the ciphered message
    val encrypted: List<BigInteger> = encrypt(message, e, n)
    // Decrypt the encrypted message
    val decrypted: String = decrypt(encrypted, d, n)

    println("Original message: $message")
    println("Encrypted: $encrypted")
    println("Decrypted: $decrypted")
}



