package com.mysu.docdownloader

import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import java.net.URL
import java.text.Normalizer
import java.util.regex.Pattern


interface SourceProcessor {
    fun process(source: Source): List<Pair<String, String>>
}

class RemoteHtmlProcessorSingleFile(private val selectors: List<Pair<String, String>>) : SourceProcessor {
    override fun process(source: Source): List<Pair<String, String>> {
        return getLinksFromHtmlWithSelector(source.url, selectors){
            _, ext -> "${source.title}.$ext"
        }.distinctBy { e -> e.title }.map { s -> s.title to s.url }
    }
}
class RemoteHtmlProcessorMultipleFile(private val selectors: List<Pair<String, String>>) : SourceProcessor {
    override fun process(source: Source): List<Pair<String, String>> {
        return getLinksFromHtmlWithSelector(source.url, selectors){
            e,_ -> source.title + "/" + URL(e.baseUri() + e.attr("href")).path.substringAfterLast("/")
        }.distinctBy { e -> e.title }.map { s -> s.title to s.url }
    }
}

fun getLinksFromHtmlWithSelector(url: String,selectors: List<Pair<String, String>>,
                                 key: (e: Element, ext: String)-> String):
        List<Source> {
    val doc = Jsoup.connect(url).get();

    val host = URL(doc.location()).let { "${it.protocol}://${it.host}" }
    return selectors.map { selector ->
        doc.select(selector.second)
            .map { entry -> Source(key(entry, selector.first), host + entry.attr("href")) }
    }.fold(emptyList()){ acc, list ->  acc + list}

}


private val pattern: Pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+")
fun String.unAccent(): String {
    val temp: String = Normalizer.normalize(this, Normalizer.Form.NFD)
    return pattern.matcher(temp).replaceAll("")
}