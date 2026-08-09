package com.tgq.app.data

/**
 * Port of the Luna Parse logic from the web UI (UI/index.html).
 *
 * Parses a pasted result page into structured entries:
 *  - Named POOL markets (block separated by blank lines)
 *  - Orphan (banner) markets detected by fixed position after named markets
 */
object LunaParser {

    data class ExtractedItem(val market: String, val result: String, val period: String)

    private val uiArtifacts = listOf(
        "Play Now", "btn_live", "labelthumbnail", "thumbnail", "label"
    )

    private val aliasMap = mapOf(
        "TOTOMACAU" to "4DTOTOMACAU", "TOTOMACAO" to "4DTOTOMACAU",
        "TOTOMACAU4D" to "4DTOTOMACAU", "4DTOTOMACAU" to "4DTOTOMACAU",
        "TOTOMACAU5D" to "5DTOTOMACAU", "5DTOTOMACAU" to "5DTOTOMACAU",
        "TOTOMACAU6D" to "6DTOTOMACAU", "6DTOTOMACAU" to "6DTOTOMACAU",
        "KINGKONG4D" to "KINGKONG4D"
    )

    private val orphanSlots = listOf(
        OrphanSlot("KING KONG 4D POOL", 0, "HOKIDRAW"),
        OrphanSlot("KING KONG 4D POOL", 1, "HUAHIN0100"),
        OrphanSlot("KENTUCKYEVE POOL", 0, "CAMBODIALOTTO"),
        OrphanSlot("BULLSEYE POOL", 0, "POIPET12"),
        OrphanSlot("OREGON12 POOL", 0, "SYDNEYLOTTO"),
        OrphanSlot("CHELSEA 15 POOL", 0, "POIPET15"),
        OrphanSlot("CHELSEA 15 POOL", 1, "TOTOMALI1530"),
        OrphanSlot("CHELSEA 15 POOL", 2, "HUAHIN1630"),
        OrphanSlot("CHELSEA 19 POOL", 0, "POIPET19"),
        OrphanSlot("PCSO POOL", 0, "TOTOMALI2030"),
        OrphanSlot("PCSO POOL", 1, "HUAHIN2100"),
        OrphanSlot("BRUNEI 21 POOL", 0, "POIPET22"),
        OrphanSlot("BRUNEI 21 POOL", 1, "HONGKONGLOTTO"),
        OrphanSlot("BRUNEI 21 POOL", 2, "TOTOMALI2330")
    )

    private data class OrphanSlot(val after: String, val idx: Int, val market: String)

    fun parse(text: String): List<ExtractedItem> {
        val rawLines = text.split("\n")
        val stripped = rawLines.map { it.trim() }

        // Sanitize: drop artifact lines, keep blank lines as separators
        val sanitized = rawLines
            .map { line ->
                val s = line.trim()
                when {
                    s.isEmpty() -> ""
                    isArtifact(s) -> null
                    else -> line
                }
            }
            .filterNotNull()
            .joinToString("\n")

        val extracted = mutableListOf<ExtractedItem>()

        // ---- Named POOL markets ----
        sanitized.split(Regex("\\n{2,}")).filter { it.isNotBlank() }.forEach { block ->
            val blockLines = block.split("\n").map { it.trim() }.filter { it.isNotEmpty() }
            if (blockLines.size < 2) return@forEach

            val poolIdx = blockLines.indexOfFirst { it.contains("POOL", ignoreCase = true) }
            if (poolIdx == -1) return@forEach

            val rawName = blockLines[poolIdx]
                .split(Regex("POOL"), ignoreCase = true)[0]
                .trim().uppercase().replace(Regex("\\s+"), "")
            if (rawName.isEmpty()) return@forEach

            var result = ""
            for (j in (poolIdx + 1) until minOf(poolIdx + 5, blockLines.size)) {
                val digitLine = blockLines[j].replace(Regex("[^0-9]"), "")
                if (digitLine.isNotEmpty() && (digitLine.length == 4 || digitLine.length == 5)) {
                    result = digitLine
                    break
                }
            }

            var period = ""
            for (j in (poolIdx + 1) until minOf(poolIdx + 5, blockLines.size)) {
                val m = Regex("PERIODE\\s*:\\s*(\\d+)", RegexOption.IGNORE_CASE).find(blockLines[j])
                if (m != null) {
                    period = m.groupValues[1]
                    break
                }
            }

            if (result.isEmpty()) return@forEach
            extracted.add(ExtractedItem(aliasMap[rawName] ?: rawName, result, period))
        }

        // ---- Orphan detection ----
        val orphans = mutableMapOf<String, List<String>>()
        var i = 0
        while (i < stripped.size) {
            val line = stripped[i]
            if (line.isEmpty() || isArtifact(line)) { i++; continue }
            if (line.contains("POOL", ignoreCase = true)) {
                val marketHeader = line
                var j = i + 1
                var skipped = 0
                while (j < stripped.size && skipped < 3) {
                    val l = stripped[j]
                    when {
                        l.isNotEmpty() && Regex("^\\d{4,5}$").matches(l) -> skipped++
                        l.isNotEmpty() && Regex("^\\[?PERIODE", RegexOption.IGNORE_CASE).matches(l) -> skipped++
                        l == "Play Now" -> skipped++
                    }
                    j++
                }
                val found = mutableListOf<String>()
                var k = j
                while (k < stripped.size) {
                    val l = stripped[k]
                    if (l.contains("POOL", ignoreCase = true) && !isArtifact(l)) break

                    if (k + 2 < stripped.size &&
                        stripped[k].isEmpty() &&
                        Regex("^\\d{4,5}$").matches(stripped[k + 1]) &&
                        stripped[k + 2].isEmpty()
                    ) {
                        found.add(stripped[k + 1]); k += 3; continue
                    }
                    if (k + 2 < stripped.size &&
                        Regex("^\\d{4,5}$").matches(stripped[k]) &&
                        stripped[k + 1].contains(":") &&
                        stripped[k + 2] == "btn_live"
                    ) {
                        found.add(stripped[k]); k += 3; continue
                    }
                    k++
                }
                if (found.isNotEmpty()) orphans[marketHeader] = found
                i = j
                continue
            }
            i++
        }

        orphanSlots.forEach { slot ->
            val list = orphans[slot.after]
            if (list != null && slot.idx < list.size) {
                extracted.add(ExtractedItem(slot.market, list[slot.idx], ""))
            }
        }

        return extracted
    }

    private fun isArtifact(s: String): Boolean =
        s in uiArtifacts || uiArtifacts.any { s.contains(it, ignoreCase = true) }
}
