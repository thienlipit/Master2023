package com.example.rsa_android.Utils

import android.content.Context
import android.net.Uri
import android.widget.Toast
import java.io.*
import java.math.BigInteger

class MyFile {
    fun loadMyKey(context: Context, fileName: String?): Triple<BigInteger, BigInteger, BigInteger> {
        var n: BigInteger = BigInteger.ZERO
        var eNum: BigInteger = BigInteger.ZERO
        var dNum: BigInteger = BigInteger.ZERO
        var reader: BufferedReader? = null
        try {
            val fis = context.openFileInput(fileName)
            reader = BufferedReader(InputStreamReader(fis))
            var line: String? = null
            if (null != reader.readLine().also { line = it }) eNum = line!!.toBigInteger()
            if (null != reader.readLine().also { line = it }) dNum = line!!.toBigInteger()
            if (null != reader.readLine().also { line = it }) n = line!!.toBigInteger()
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
            Toast.makeText(context, "You doesn't generate key", Toast.LENGTH_SHORT).show()
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            if (null != reader) {
                try {
                    reader.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
            return Triple(n, eNum, dNum)
        }
//        return  Triple(n, eNum, dNum)

    }

    fun saveKey(context: Context, fileName: String?, n: String?, eNum: String?, dNum: String?) {
        var writer: PrintWriter? = null
        try {
            val fos = context.openFileOutput(fileName, Context.MODE_PRIVATE)
            writer = PrintWriter(BufferedWriter(OutputStreamWriter(fos)))
            writer.println(eNum)
            writer.println(dNum)
            writer.println(n)
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            writer?.close()
        }
    }

    // Save Encrypted text to file
    fun saveEncryptFile(
        context: Context?,
        filePath: File,
        fileName: String,
        listEncryptText: List<BigInteger>
    ) {
        var writer: BufferedWriter? = null
        try {
            // Open file
            val file = File(filePath, fileName)
            writer = BufferedWriter(FileWriter(file))

            // write value to file
            for (number in listEncryptText) {
                writer.write(number.toString())
                writer.newLine()
            }
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            if (null != writer) {
                Toast.makeText(context, "Save successfully", Toast.LENGTH_SHORT).show()
                writer.close()
            }
        }
    }

    fun loadEncryptedTextFile(context: Context, uri: Uri): List<BigInteger> {
        var reader: BufferedReader? = null
        val numbers = mutableListOf<BigInteger>()
        try {
            val inputStream: InputStream? = context.contentResolver?.openInputStream(uri)
            reader = BufferedReader(InputStreamReader(inputStream))
            var line: String? = reader.readLine()
            while (line != null) {
                numbers.add(BigInteger(line))
                line = reader.readLine()
            }

        } catch (e: FileNotFoundException) {
            e.printStackTrace()
            Toast.makeText(context, "Not found file", Toast.LENGTH_SHORT).show()
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            reader!!.close()
            Toast.makeText(context, "Load successfully", Toast.LENGTH_SHORT).show()
            return numbers
        }
    }

    // Export Generate key to file
    fun exportGenerateKey(
        context: Context?,
        filePath: File,
        fileName: String,
        key: String?,
        n: String?
    ) {
        var writer: PrintWriter? = null
        try {
            val fos = FileOutputStream(File(filePath, fileName))
            writer = PrintWriter(BufferedWriter(OutputStreamWriter(fos)))
            writer.println(key)
            writer.print(n)
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            if (null != writer) {
                Toast.makeText(context, "Save successfully", Toast.LENGTH_SHORT).show()
                writer.close()
            }
        }
    }
}