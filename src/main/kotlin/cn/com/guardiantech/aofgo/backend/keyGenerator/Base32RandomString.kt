package cn.com.guardiantech.aofgo.backend.keyGenerator

import org.apache.commons.codec.binary.Base32
import java.util.*


/**
 * http://stackoverflow.com/questions/41107/how-to-generate-a-random-alpha-numeric-string
 * Created by dummy on 4/29/17.
 */
class Base32RandomString(val length: Int, private val random: Random) {
    fun nextString(): String = with(CharArray(length)) {
        for (idx in this.indices)
            this[idx] = symbols[random.nextInt(symbols.size)]
        String(this)
    }

    fun decode(secret: String): ByteArray = base32.decode(secret)

    companion object {
        private val symbols: CharArray
        private val base32 = Base32()

        init {
            val tmp = StringBuilder()

            var ch = '2'
            while (ch <= '7') {
                tmp.append(ch)
                ++ch
            }
            ch = 'A'

            while (ch <= 'Z') {
                tmp.append(ch)
                ++ch
            }
            symbols = tmp.toString().toCharArray()
        }

    }
}