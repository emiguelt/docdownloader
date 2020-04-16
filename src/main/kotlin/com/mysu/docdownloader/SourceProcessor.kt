package com.mysu.docdownloader

import org.jsoup.Jsoup
import java.net.URL

interface SourceProcessor {
    fun process(source: Source): List<Pair<String, String>>
}

class RemoteHtmlProcessor : SourceProcessor {
    private val selectors = listOf("pdf" to ".test-bookpdf-link", "epub" to ".test-bookepub-link")
    override fun process(source: Source): List<Pair<String, String>> {
        val doc = Jsoup.connect(source.url)
            .get();

        val host = URL(doc.location()).let { "${it.protocol}://${it.host}" }
        return selectors.map { selector ->
            doc.select(selector.second)
                .map { entry -> entry.attr("href") }
                .map { href -> "${source.title}.${selector.first}" to host + href }
                .firstOrNull()
        }.filterNotNull()
    }
}