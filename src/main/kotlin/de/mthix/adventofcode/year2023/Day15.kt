package de.mthix.adventofcode.year2023

import de.mthix.adventofcode.textOfDay

class LensStep(val step: String) {

    val hash = hash(step, 0)
    val label = Regex("[a-z]+").find(step)!!.value
    val labelHash = hash(label,0)
    val op = Regex("[=-]").find(step)!!.value
    val focalLength = if(op == "=") Regex("[0-9]+").find(step)!!.value.toInt() else 0

    override fun toString(): String {
        return step
    }
    companion object {
        fun hash(remainingStep:String, currentHash:Int): Int {
            return if(remainingStep.isEmpty())
                currentHash
            else
                hash(remainingStep.substring(1), ((currentHash + remainingStep.first().code)*17) % 256)
        }
    }
}
fun main() {
    val lensSteps = textOfDay(2023, 15,0)
        .split(",")
        .map { LensStep(it) }

    println(lensSteps)
    println(lensSteps.sumOf { it.hash })

    val boxes = List(256) { LinkedHashMap<String,Int>() }
    lensSteps.forEach { step ->
        val box = boxes[step.labelHash]
        when(step.op) {
            "=" -> { box[step.label] = step.focalLength }
            "-" -> { box.remove(step.label)}
        }
    }

    println(boxes.mapIndexed { bi,box ->
            if(box.isNotEmpty()) box.values.mapIndexed { li, focalLength -> (bi+1)*(li+1)*focalLength }.sum()
            else 0
        }.sum()
    )
}

