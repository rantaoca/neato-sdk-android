package com.neatorobotics.sdk.android

import android.util.Log

import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.net.URL

/**
 * Created by Marco on 21/03/16.
 */
object MockJSON {

    fun getFileFromPath(obj: Any, fileName: String): File? {
        val classLoader = obj.javaClass.classLoader
        val resource = classLoader!!.getResource("test/$fileName")
        return if (resource == null)
            null
        else
            File(resource.path)
    }

    fun loadJSON(obj: Any, filename: String): String? {
        val text = StringBuilder()
        try {
            val br = BufferedReader(FileReader(getFileFromPath(obj, filename)))
            var line: String? = null

            while ({ line = br.readLine(); line }() != null) {
                text.append(line)
                text.append('\n')
            }
            br.close()
        } catch (e: Exception) {
            Log.e("Exception", "Exception", e)
            return null
        }

        return text.toString()
    }
}