package springbook.learntest.template

import java.io.BufferedReader

interface BufferedReaderCallback {
    fun doSomethingWithReader(br: BufferedReader): Int
}