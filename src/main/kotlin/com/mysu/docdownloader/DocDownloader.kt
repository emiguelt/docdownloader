package com.mysu.docdownloader

import kotlin.streams.asStream

interface DocDownloader {
    fun start()
}

class DocDownloaderImpl(
    private val source: SourceReader,
    private val processor: SourceProcessor,
    private val downloader: FileDownloader
) : DocDownloader {
    override fun start() {
        source.read().toList()
            .stream()
            .parallel()
            .map { processSource(it) }
            .map { downloadFiles(it) }
            .forEach {
                it.forEach { file ->
                    println("FileName: ${file.first}, downloaded: ${file.second}")
                }
            }
    }

    private fun downloadFiles(urls: List<Pair<String, String>>): List<Pair<String, Boolean>> =
        urls.map { downloader.get(it.first, it.second) }

    private fun processSource(source: Source): List<Pair<String, String>> = processor.process(source)
}