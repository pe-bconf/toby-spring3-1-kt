package springbook.learntest.template

import java.io.BufferedReader
import java.io.FileNotFoundException
import java.io.FileReader
import java.io.IOException

class Calculator {
    fun calcSum(filepath: String): Int {
        val sumCallback = object : LineCallback<Int> {
            override fun doSomethingWithLine(line: String, value: Int): Int {
                return value + Integer.valueOf(line)
            }
        }

        return lineReaderTemplate(filepath, sumCallback, 0)
    }

    fun calcMultiply(filepath: String): Int {
        val multiplyCallback = object: LineCallback<Int> {
            override fun doSomethingWithLine(line: String, value: Int): Int {
                return value * Integer.valueOf(line)
            }
        }

        return lineReaderTemplate(filepath, multiplyCallback, 1)
    }

    fun concatenate(filepath: String): String {
        val concatenateCallback = object : LineCallback<String> {
            override fun doSomethingWithLine(line: String, value: String): String {
                return value + line
            }
        }
        return lineReaderTemplate(filepath, concatenateCallback, "")
    }

    fun fileReaderTemplate(filepath: String,
                           readerCallback: BufferedReaderCallback): Int {
        var br: BufferedReader? = null
        try {
            br = BufferedReader(FileReader(filepath))
            return readerCallback.doSomethingWithReader(br)
        } catch (e: FileNotFoundException) {
            println(e.message)
            throw e
        } finally {
            if (br != null) {
                try{ br.close() }
                catch (e: IOException) { println(e.message) }
            }
        }
    }

    fun <T> lineReaderTemplate(filepath: String,
                           lineCallback: LineCallback<T>,
                           initVal: T): T {
        var br: BufferedReader? = null
        try {
            br = BufferedReader(FileReader(filepath))
            var res: T = initVal
            var line: String
            while (br.readLine().also { line = it } != null) {
                res = lineCallback.doSomethingWithLine(line, res)
            }
            return res
        } catch (e: FileNotFoundException) {
            println(e.message)
            throw e
        } finally {
            if (br != null) {
                try{ br.close() }
                catch (e: IOException) { println(e.message) }
            }
        }
    }

}