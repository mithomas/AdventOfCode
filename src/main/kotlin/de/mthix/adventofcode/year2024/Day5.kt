package de.mthix.adventofcode.year2024

import de.mthix.adventofcode.linesOfDay

fun main() {
    val example = 0
    val order = PrintOrder(example > 0)

    println(order.exampleOrder)
    val pages = linesOfDay(2024,5, example).map { it.split(",").map { it.toInt() } }

    val orderedPages = pages.filter { order.isOrdered(it) }
    println(orderedPages.sumOf { it[it.size / 2] })

    val unorderedPages = pages
        .filter { !order.isOrdered(it) }
        .map { it.toMutableList() }
        .onEach { pageSet ->
            while(!order.isOrdered(pageSet)) {
                var i = 0
                while(i < pageSet.size) {
                    if (!order.firstPageInCorrectPlace(pageSet.subList(i, pageSet.size))) {
                        var idx = pageSet.indexOfFirst { order.order().getOrDefault(pageSet[i], emptySet()).contains(it) }

                        if(idx == -1) idx = pageSet.size-1

                        val page = pageSet.removeAt(i)
                        pageSet.add(idx, page)
                        i = 0
                    } else i++
                }
            }
        }
    println(unorderedPages.sumOf { it[it.size / 2] })
}

class PrintOrder(val example: Boolean=false)  {

    val exampleOrder = toOrder("""
                                47|53
                                97|13
                                97|61
                                97|47
                                75|29
                                61|13
                                75|53
                                29|13
                                97|29
                                53|29
                                61|53
                                97|53
                                61|29
                                47|13
                                75|47
                                97|75
                                47|61
                                75|61
                                47|29
                                75|13
                                53|13 
                                """)

    val order = toOrder("""
                87|26
                31|18
                31|47
                16|39
                16|74
                16|34
                57|62
                57|87
                57|24
                57|59
                29|57
                29|69
                29|15
                29|43
                29|33
                34|52
                34|89
                34|42
                34|98
                34|29
                34|92
                42|22
                42|98
                42|47
                42|33
                42|29
                42|17
                42|82
                17|31
                17|28
                17|16
                17|34
                17|11
                17|69
                17|71
                17|81
                53|29
                53|52
                53|11
                53|12
                53|73
                53|58
                53|17
                53|89
                53|82
                71|11
                71|43
                71|16
                71|59
                71|75
                71|19
                71|24
                71|37
                71|48
                71|57
                13|17
                13|12
                13|59
                13|68
                13|39
                13|69
                13|58
                13|75
                13|15
                13|71
                13|28
                18|53
                18|94
                18|22
                18|69
                18|98
                18|12
                18|29
                18|47
                18|33
                18|52
                18|83
                18|54
                74|56
                74|12
                74|52
                74|54
                74|86
                74|82
                74|83
                74|26
                74|18
                74|87
                74|94
                74|17
                74|98
                47|77
                47|33
                47|75
                47|17
                47|73
                47|94
                47|29
                47|53
                47|26
                47|89
                47|22
                47|12
                47|98
                47|52
                26|89
                26|57
                26|17
                26|69
                26|53
                26|29
                26|75
                26|94
                26|33
                26|15
                26|98
                26|77
                26|71
                26|73
                26|52
                69|68
                69|56
                69|71
                69|34
                69|28
                69|81
                69|11
                69|32
                69|37
                69|57
                69|19
                69|24
                69|39
                69|59
                69|48
                69|74
                56|17
                56|47
                56|89
                56|82
                56|26
                56|92
                56|54
                56|53
                56|12
                56|73
                56|52
                56|29
                56|94
                56|77
                56|87
                56|32
                56|58
                75|42
                75|59
                75|57
                75|74
                75|16
                75|31
                75|85
                75|81
                75|34
                75|11
                75|48
                75|37
                75|28
                75|38
                75|24
                75|62
                75|68
                75|19
                38|89
                38|47
                38|98
                38|73
                38|92
                38|53
                38|52
                38|17
                38|58
                38|29
                38|22
                38|26
                38|33
                38|77
                38|87
                38|15
                38|94
                38|83
                38|82
                82|94
                82|17
                82|75
                82|28
                82|83
                82|71
                82|12
                82|22
                82|62
                82|89
                82|11
                82|33
                82|15
                82|52
                82|98
                82|92
                82|69
                82|58
                82|54
                82|77
                33|48
                33|37
                33|74
                33|81
                33|68
                33|19
                33|85
                33|69
                33|24
                33|57
                33|39
                33|34
                33|71
                33|31
                33|11
                33|16
                33|59
                33|28
                33|43
                33|62
                33|56
                73|24
                73|19
                73|57
                73|68
                73|33
                73|11
                73|22
                73|59
                73|69
                73|39
                73|15
                73|81
                73|37
                73|85
                73|74
                73|71
                73|43
                73|62
                73|16
                73|31
                73|48
                73|28
                85|29
                85|42
                85|94
                85|34
                85|86
                85|89
                85|26
                85|48
                85|19
                85|53
                85|18
                85|31
                85|38
                85|59
                85|52
                85|82
                85|74
                85|39
                85|87
                85|81
                85|56
                85|32
                85|47
                83|85
                83|68
                83|34
                83|59
                83|48
                83|33
                83|57
                83|37
                83|28
                83|81
                83|69
                83|19
                83|24
                83|43
                83|71
                83|75
                83|15
                83|39
                83|62
                83|73
                83|22
                83|16
                83|11
                83|31
                86|83
                86|29
                86|94
                86|89
                86|54
                86|42
                86|15
                86|53
                86|18
                86|87
                86|82
                86|47
                86|73
                86|38
                86|12
                86|26
                86|17
                86|52
                86|92
                86|22
                86|58
                86|98
                86|77
                86|13
                32|58
                32|15
                32|38
                32|29
                32|98
                32|82
                32|53
                32|26
                32|86
                32|92
                32|17
                32|83
                32|13
                32|54
                32|47
                32|52
                32|94
                32|87
                32|73
                32|18
                32|42
                32|12
                32|89
                32|77
                48|94
                48|12
                48|77
                48|47
                48|54
                48|87
                48|86
                48|42
                48|56
                48|53
                48|52
                48|89
                48|92
                48|82
                48|26
                48|18
                48|17
                48|32
                48|74
                48|13
                48|29
                48|58
                48|98
                48|38
                39|92
                39|32
                39|26
                39|87
                39|53
                39|42
                39|29
                39|56
                39|38
                39|52
                39|18
                39|81
                39|77
                39|86
                39|48
                39|89
                39|54
                39|74
                39|94
                39|47
                39|24
                39|34
                39|82
                39|19
                98|59
                98|62
                98|68
                98|73
                98|16
                98|83
                98|75
                98|11
                98|13
                98|12
                98|71
                98|37
                98|28
                98|31
                98|22
                98|69
                98|39
                98|15
                98|33
                98|57
                98|43
                98|85
                98|58
                98|17
                24|48
                24|77
                24|54
                24|86
                24|74
                24|56
                24|47
                24|53
                24|42
                24|92
                24|38
                24|82
                24|87
                24|19
                24|12
                24|52
                24|98
                24|29
                24|13
                24|94
                24|18
                24|32
                24|26
                24|89
                68|81
                68|38
                68|42
                68|32
                68|19
                68|31
                68|26
                68|59
                68|34
                68|53
                68|47
                68|87
                68|82
                68|86
                68|74
                68|43
                68|48
                68|16
                68|24
                68|56
                68|39
                68|85
                68|37
                68|18
                62|56
                62|47
                62|37
                62|24
                62|26
                62|31
                62|74
                62|86
                62|42
                62|68
                62|87
                62|59
                62|85
                62|19
                62|16
                62|48
                62|81
                62|39
                62|38
                62|32
                62|18
                62|28
                62|43
                62|34
                28|85
                28|74
                28|47
                28|32
                28|16
                28|38
                28|37
                28|34
                28|18
                28|24
                28|42
                28|39
                28|43
                28|68
                28|26
                28|87
                28|31
                28|56
                28|59
                28|81
                28|53
                28|19
                28|86
                28|48
                43|85
                43|74
                43|53
                43|48
                43|26
                43|31
                43|32
                43|37
                43|39
                43|82
                43|16
                43|59
                43|87
                43|94
                43|38
                43|86
                43|81
                43|18
                43|42
                43|24
                43|34
                43|19
                43|56
                43|47
                81|82
                81|47
                81|42
                81|18
                81|92
                81|38
                81|94
                81|34
                81|53
                81|52
                81|48
                81|54
                81|19
                81|98
                81|24
                81|86
                81|26
                81|56
                81|77
                81|32
                81|74
                81|87
                81|29
                81|89
                54|16
                54|17
                54|77
                54|57
                54|85
                54|75
                54|58
                54|73
                54|83
                54|12
                54|13
                54|92
                54|15
                54|11
                54|98
                54|28
                54|62
                54|69
                54|68
                54|43
                54|71
                54|22
                54|33
                54|37
                59|31
                59|24
                59|86
                59|39
                59|87
                59|26
                59|81
                59|94
                59|34
                59|38
                59|32
                59|48
                59|42
                59|89
                59|19
                59|82
                59|56
                59|74
                59|47
                59|18
                59|53
                59|29
                59|52
                59|54
                12|16
                12|34
                12|68
                12|73
                12|39
                12|31
                12|11
                12|58
                12|28
                12|71
                12|81
                12|33
                12|59
                12|22
                12|83
                12|75
                12|17
                12|15
                12|57
                12|69
                12|43
                12|62
                12|37
                12|85
                11|42
                11|48
                11|38
                11|18
                11|74
                11|34
                11|81
                11|24
                11|47
                11|16
                11|56
                11|68
                11|57
                11|37
                11|32
                11|43
                11|39
                11|86
                11|59
                11|28
                11|62
                11|19
                11|31
                11|85
                92|58
                92|28
                92|12
                92|73
                92|68
                92|75
                92|71
                92|33
                92|16
                92|69
                92|17
                92|83
                92|11
                92|62
                92|37
                92|43
                92|59
                92|98
                92|15
                92|85
                92|13
                92|57
                92|22
                92|77
                52|54
                52|13
                52|12
                52|75
                52|29
                52|37
                52|43
                52|92
                52|73
                52|11
                52|15
                52|62
                52|69
                52|22
                52|28
                52|77
                52|83
                52|57
                52|17
                52|71
                52|58
                52|98
                52|68
                52|33
                22|69
                22|48
                22|57
                22|43
                22|33
                22|16
                22|56
                22|74
                22|68
                22|81
                22|59
                22|37
                22|31
                22|71
                22|32
                22|24
                22|75
                22|85
                22|39
                22|34
                22|62
                22|19
                22|11
                22|28
                37|89
                37|39
                37|48
                37|86
                37|59
                37|19
                37|74
                37|53
                37|16
                37|26
                37|82
                37|85
                37|81
                37|24
                37|31
                37|56
                37|42
                37|87
                37|94
                37|18
                37|38
                37|34
                37|47
                37|32
                77|16
                77|59
                77|33
                77|31
                77|22
                77|98
                77|11
                77|13
                77|12
                77|85
                77|75
                77|17
                77|68
                77|57
                77|83
                77|71
                77|15
                77|28
                77|62
                77|37
                77|73
                77|43
                77|58
                77|69
                58|75
                58|19
                58|81
                58|11
                58|37
                58|43
                58|68
                58|71
                58|31
                58|28
                58|62
                58|34
                58|16
                58|69
                58|33
                58|57
                58|85
                58|83
                58|73
                58|39
                58|59
                58|24
                58|15
                58|22
                94|28
                94|52
                94|69
                94|54
                94|11
                94|33
                94|29
                94|83
                94|71
                94|92
                94|58
                94|75
                94|73
                94|17
                94|98
                94|57
                94|12
                94|22
                94|62
                94|68
                94|13
                94|15
                94|89
                94|77
                15|19
                15|57
                15|31
                15|69
                15|16
                15|59
                15|37
                15|48
                15|62
                15|22
                15|56
                15|75
                15|34
                15|74
                15|81
                15|68
                15|85
                15|33
                15|11
                15|24
                15|28
                15|71
                15|43
                15|39
                89|77
                89|11
                89|71
                89|15
                89|54
                89|69
                89|33
                89|83
                89|75
                89|28
                89|29
                89|98
                89|62
                89|57
                89|12
                89|17
                89|13
                89|22
                89|68
                89|92
                89|43
                89|73
                89|58
                89|52
                19|56
                19|29
                19|89
                19|74
                19|98
                19|38
                19|86
                19|52
                19|48
                19|82
                19|87
                19|13
                19|94
                19|32
                19|54
                19|17
                19|12
                19|47
                19|26
                19|18
                19|42
                19|77
                19|92
                19|53
                87|22
                87|83
                87|52
                87|58
                87|92
                87|29
                87|13
                87|15
                87|77
                87|73
                87|89
                87|33
                87|71
                87|69
                87|12
                87|94
                87|53
                87|98
                87|75
                87|82
                87|11
                87|54
                87|17
                31|89
                31|53
                31|19
                31|32
                31|56
                31|86
                31|94
                31|81
                31|48
                31|92
                31|34
                31|24
                31|38
                31|87
                31|42
                31|29
                31|26
                31|54
                31|39
                31|82
                31|52
                31|74
                16|38
                16|53
                16|82
                16|89
                16|24
                16|56
                16|31
                16|32
                16|47
                16|52
                16|26
                16|85
                16|18
                16|48
                16|81
                16|94
                16|87
                16|59
                16|19
                16|42
                16|86
                57|38
                57|48
                57|37
                57|56
                57|68
                57|16
                57|81
                57|43
                57|86
                57|31
                57|32
                57|18
                57|28
                57|74
                57|47
                57|85
                57|39
                57|34
                57|42
                57|19
                29|37
                29|17
                29|73
                29|12
                29|16
                29|77
                29|68
                29|54
                29|83
                29|13
                29|62
                29|58
                29|92
                29|75
                29|11
                29|28
                29|22
                29|98
                29|71
                34|77
                34|38
                34|82
                34|86
                34|18
                34|19
                34|74
                34|56
                34|13
                34|87
                34|26
                34|24
                34|47
                34|94
                34|54
                34|32
                34|48
                34|53
                42|89
                42|18
                42|83
                42|58
                42|87
                42|54
                42|53
                42|38
                42|13
                42|94
                42|77
                42|15
                42|26
                42|12
                42|73
                42|52
                42|92
                17|57
                17|62
                17|68
                17|75
                17|83
                17|59
                17|58
                17|73
                17|43
                17|39
                17|15
                17|22
                17|37
                17|33
                17|85
                17|24
                53|75
                53|22
                53|98
                53|57
                53|13
                53|71
                53|77
                53|54
                53|83
                53|15
                53|94
                53|33
                53|69
                53|92
                53|62
                71|74
                71|86
                71|28
                71|62
                71|34
                71|42
                71|18
                71|39
                71|68
                71|32
                71|56
                71|81
                71|31
                71|85
                13|22
                13|73
                13|43
                13|11
                13|37
                13|16
                13|33
                13|57
                13|85
                13|31
                13|62
                13|83
                13|81
                18|82
                18|38
                18|89
                18|77
                18|87
                18|15
                18|17
                18|58
                18|13
                18|73
                18|26
                18|92
                74|38
                74|53
                74|77
                74|89
                74|47
                74|42
                74|29
                74|32
                74|13
                74|58
                74|92
                47|82
                47|15
                47|13
                47|92
                47|54
                47|69
                47|58
                47|87
                47|83
                47|71
                26|92
                26|12
                26|22
                26|82
                26|54
                26|83
                26|58
                26|11
                26|13
                69|62
                69|43
                69|75
                69|31
                69|42
                69|16
                69|85
                69|86
                56|38
                56|42
                56|86
                56|98
                56|18
                56|13
                56|83
                75|43
                75|32
                75|18
                75|56
                75|86
                75|39
                38|54
                38|71
                38|13
                38|69
                38|12
                82|57
                82|73
                82|13
                82|29
                33|75
                33|32
                33|86
                73|34
                73|75
                85|24
                """)

    fun firstPageInCorrectPlace(l: List<Int>):Boolean {
        return if(l.size > 1) {
            l.subList(1,l.size).all { order().getOrDefault(l[0], emptySet()).contains(it) }
        } else true
    }

    fun isOrdered(l: List<Int>) = l.indices.all { this.firstPageInCorrectPlace(l.subList(it,l.size)) }

    fun order() = if (example) exampleOrder else order

    fun toOrder(s:String): Map<Int, Set<Int>> {
        val result = HashMap<Int,MutableSet<Int>>()

        s.lines()
            .filter { it.isNotBlank() }
            .map { it.trim() }
            .forEach { result.getOrPut(it.substring(0,2).toInt()) { HashSet<Int>()}.add(it.substring(3).toInt()) }

        return result
    }
}