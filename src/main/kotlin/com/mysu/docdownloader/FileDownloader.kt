package com.mysu.docdownloader

import java.io.File
import java.io.FileOutputStream
import java.lang.Exception
import java.net.URL
import java.nio.channels.Channels

interface FileDownloader {
    fun get(fileName: String, url: String): Pair<String, Boolean>
}

class HttpDownloader(path: String) : FileDownloader {
    private val folder: String

    init {
        val file = File(path)
        folder = (if (file.isDirectory) path else file.parent) + "/docs/"

        File(folder).let { if (!it.exists()) it.mkdir() }
    }

    override fun get(fileName: String, url: String): Pair<String, Boolean> {
        return fileName to try {
            val fullName = folder + fileName
            if(!File(fullName).exists()) {
                println("Trying to download $url")
                val inChannel = Channels.newChannel(URL(url).openStream())
                FileOutputStream(fullName).channel.transferFrom(inChannel, 0, Long.MAX_VALUE)
                true
            }else {
                println("File $fullName already exists")
                false
            }
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
}