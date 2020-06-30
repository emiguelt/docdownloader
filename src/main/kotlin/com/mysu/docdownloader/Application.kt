package com.mysu.docdownloader

fun main(args: Array<String>) {
    //InstanceFactory.buildSpringerDocDownloader(args[0]).start()
    InstanceFactory.buildUnalDigialLibraryDocDownloader(args[0], args[1]).start()
}


object InstanceFactory {
    fun buildSpringerDocDownloader(path: String): DocDownloader {
        val selectors = listOf("pdf" to ".test-bookpdf-link", "epub" to ".test-bookepub-link")
        return DocDownloaderImpl(
            CSVSourceReader(path),
            RemoteHtmlProcessorSingleFile(selectors),
            HttpDownloader(path)
        )
    }

    fun buildUnalDigialLibraryDocDownloader(urlSource: String, urlSelector: String): DocDownloader {
        val selectors = listOf("pdf" to "strong a")
        return DocDownloaderImpl(HtmlSourceReader(urlSource, urlSelector),
            RemoteHtmlProcessorMultipleFile(selectors), HttpDownloader("/Users/edwintriana/limbo/libros"))
    };
}