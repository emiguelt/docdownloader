package com.mysu.docdownloader

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