package com.mysu.docdownloader

fun main(args: Array<String>) {
    InstanceFactory.buildDocDownloader(args[0]).start()
}


object InstanceFactory {
    fun buildDocDownloader(path: String): DocDownloader =
        DocDownloaderImpl(
            CSVSourceReader(path),
            RemoteHtmlProcessor(),
            HttpDownloader(path)
        )
}