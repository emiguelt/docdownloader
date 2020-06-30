package com.mysu.docdownloader

import org.jsoup.Jsoup
import java.io.FileReader

interface SourceReader {
    fun read(): Sequence<Source>
}

class CSVSourceReader(private val path: String) : SourceReader {
    override fun read(): Sequence<Source> {
        return FileReader(path).readLines()
            .asSequence()
            .mapNotNull { it.split(";") }
            .filter { it.size == 2 }
            .map { it[0] to it[1] }
            .map { Source(it.first.replace(" ", "_"), it.second) }

    }

}

class HtmlSourceReader(private val url: String, private val urlSelector: String): SourceReader {
    override fun read(): Sequence<Source> {
        return getLinksFromHtmlWithSelector(url, listOf("pdf" to urlSelector)){
                e, _ -> e.text().unAccent().replace(" ", "")
        }.asSequence()
    }
}