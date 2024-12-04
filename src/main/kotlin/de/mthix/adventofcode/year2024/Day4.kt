package de.mthix.adventofcode.year2024

import de.mthix.adventofcode.linesOfDay

fun main() {
    val wordSearch = linesOfDay(2024,4 ).map { it.toList().map { it.toString() } }

    // build nodes
    val wordNodes = LinkedHashMap<Pair<Int,Int>,WordNode>()
    for(y in wordSearch.indices) {
        for(x in wordSearch[y].indices) {
            wordNodes[Pair(x,y)] = WordNode(wordSearch[y][x])
        }
    }
    // set neighbours
    wordNodes.forEach { (k,n) ->
        n.tl = wordNodes[Pair(k.first-1,k.second-1)]
        n.t  = wordNodes[Pair(k.first,k.second-1)]
        n.tr = wordNodes[Pair(k.first+1,k.second-1)]
        n.l  = wordNodes[Pair(k.first-1,k.second)]
        n.r  = wordNodes[Pair(k.first+1,k.second)]
        n.bl = wordNodes[Pair(k.first-1,k.second+1)]
        n.b  = wordNodes[Pair(k.first,k.second+1)]
        n.br = wordNodes[Pair(k.first+1,k.second+1)]
    }

    val words = wordNodes.values.map { listOf(
        it.lookTL(3),
        it.lookT(3),
        it.lookTR(3),
        it.lookL(3),
        it.lookR(3),
        it.lookBL(3),
        it.lookB(3),
        it.lookBR(3),
    )}

    println(words.flatten().filter { it == "XMAS" }.size)
    println(wordNodes.values.count { it.isXmas() })
}

data class WordNode(val v:String) {
    var tl:WordNode? = null
    var t:WordNode?  = null
    var tr:WordNode? = null
    var l:WordNode?  = null
    var r:WordNode?  = null
    var bl:WordNode? = null
    var b:WordNode?  = null
    var br:WordNode? = null

    fun lookTL(depth:Int = 1):String = v + if(depth > 0 && tl != null) tl!!.lookTL(depth-1) else ""
    fun lookT (depth:Int = 1):String = v + if(depth > 0 && t  != null) t!!.lookT(depth-1) else ""
    fun lookTR(depth:Int = 1):String = v + if(depth > 0 && tr != null) tr!!.lookTR(depth-1) else ""
    fun lookL (depth:Int = 1):String = v + if(depth > 0 && l  != null) l!!.lookL(depth-1) else ""
    fun lookR (depth:Int = 1):String = v + if(depth > 0 && r  != null) r!!.lookR(depth-1) else ""
    fun lookBL(depth:Int = 1):String = v + if(depth > 0 && bl != null) bl!!.lookBL(depth-1) else ""
    fun lookB (depth:Int = 1):String = v + if(depth > 0 && b  != null) b!!.lookB(depth-1) else ""
    fun lookBR(depth:Int = 1):String = v + if(depth > 0 && br != null) br!!.lookBR(depth-1) else ""

    fun isXmas() = (
                    (lookTL() == "AM" && lookTR() == "AM" && lookBL() == "AS" && lookBR() == "AS") ||
                    (lookTL() == "AM" && lookTR() == "AS" && lookBL() == "AM" && lookBR() == "AS") ||
                    (lookTL() == "AS" && lookTR() == "AS" && lookBL() == "AM" && lookBR() == "AM") ||
                    (lookTL() == "AS" && lookTR() == "AM" && lookBL() == "AS" && lookBR() == "AM")
            )
}