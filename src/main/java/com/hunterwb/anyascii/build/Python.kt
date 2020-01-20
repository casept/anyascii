package com.hunterwb.anyascii.build

import java.nio.file.Files
import java.nio.file.Path

fun python(table: Table) {
    val dirPath = Path.of("python/anyascii/_data")
    dirPath.toFile().deleteRecursively()
    Files.createDirectories(dirPath)
    Files.createFile(dirPath.resolve("__init__.py"))
    for (b in 0..0xFFF) {
        val block = List(256) { table[(b shl 8) or it] }.dropLastWhile { it == null }
        if (block.isEmpty()) continue
        writeBlock(block, dirPath.resolve("_%03x.py".format(b)))
    }
}

private fun writeBlock(block: List<String?>, path: Path) {
    Files.newBufferedWriter(path).use { writer ->
        writer.write("b=(")
        for (c in block.indices) {
            var s = block[c] ?: ""
            s = s.replace("\\", "\\\\").replace("'", "\\'")
            s = "'$s'"
            s += if (c == block.lastIndex) ')' else ','
            writer.write(s)
        }
    }
}