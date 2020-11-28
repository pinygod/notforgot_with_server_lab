package prog

import java.security.MessageDigest

abstract class MD5HashFromString {
    companion object{
        fun toMD5Hash( text: String ): String {

            val result: String

            result = try {

                val md5 = MessageDigest.getInstance("MD5")
                val md5HashBytes = md5.digest(text.toByteArray()).toTypedArray()

                byteArrayToHexString(md5HashBytes)
            } catch ( e: Exception ) {

                "error: ${e.message}"
            }

            return result
        }

        private fun byteArrayToHexString( array: Array<Byte> ): String {

            val result = StringBuilder(array.size * 2)

            for ( byte in array ) {

                val toAppend =
                    String.format("%2X", byte).replace(" ", "0") // hexadecimal
                result.append(toAppend).append("-")
            }
            result.setLength(result.length - 1) // remove last '-'

            return result.toString()
        }
    }
}

