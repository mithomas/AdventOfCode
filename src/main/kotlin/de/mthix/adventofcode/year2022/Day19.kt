package de.mthix.adventofcode.year2022

import com.marcinmoskala.math.product
import de.mthix.adventofcode.linesOfDay

fun main() {
    var blueprints = linesOfDay(2022, 19)
        .mapIndexed { i, v ->
            listOf(
                ProductionScenario(
                    i + 1,
                    0, 1,
                    0, 0,
                    0, 0,
                    0, 0,
                    RobotFactory(v)
                )
            )
        }


    (1..24).forEach { blueprints = blueprints.map { generateSuccessorScenarios(it) } }
    println("Puzzle 1: " + blueprints.map { it.maxBy { it.geodes } }.sumOf { it.index * it.geodes })


    blueprints = if (blueprints.size >= 3) blueprints.subList(0, 3) else blueprints
    (1..32).forEach { blueprints = blueprints.map { generateSuccessorScenarios(it) } }
    println("Puzzle 2: " + blueprints.map { it.maxBy { it.geodes } }.map { it.geodes }.product())
}

private fun generateSuccessorScenarios(scenarios: List<ProductionScenario>): List<ProductionScenario> {
    val newScenarios = scenarios.map { it.generateSuccessorScenarios() }.flatten()
    return newScenarios.filter { s -> !newScenarios.any { o -> s !== o && s.hasSameRobotsButLessResourcesAs(o) } }
}


class ProductionScenario(
    val index: Int,
    private val ore: Int,
    private val oreRobots: Int,
    private val clay: Int,
    private val clayRobots: Int,
    private val obsidian: Int,
    private val obsidianRobots: Int,
    val geodes: Int,
    private val geodeRobots: Int,
    private val factory: RobotFactory
) {

    fun generateSuccessorScenarios(): List<ProductionScenario> {
        val scenarios = mutableListOf<ProductionScenario>()

        if (factory.geodeRobotCosts.isEnough(ore, clay, obsidian)) {
            scenarios.add(produceGeodeRobot())
        } else {
            if (factory.obsidianRobotCosts.isEnough(ore, clay, obsidian)
                && obsidianRobots < factory.geodeRobotCosts.obsidian
            ) scenarios.add(produceObsidianRobot())

            if (factory.clayRobotCosts.isEnough(ore, clay, obsidian)
                && clayRobots < factory.obsidianRobotCosts.clay
                && obsidianRobots < factory.geodeRobotCosts.obsidian
            ) scenarios.add(produceClayRobot())

            if (factory.oreRobotCosts.isEnough(ore, clay, obsidian)
                && oreRobots < listOf(
                    factory.clayRobotCosts.ore,
                    factory.obsidianRobotCosts.ore,
                    factory.geodeRobotCosts.ore
                ).max()
            ) scenarios.add(produceOreRobot())

            scenarios.add(produceNoRobot())
        }

        return scenarios
    }

    fun hasSameRobotsButLessResourcesAs(otherScenario: ProductionScenario) =
        this.oreRobots == otherScenario.oreRobots
                && this.clayRobots == otherScenario.clayRobots
                && this.obsidianRobots == otherScenario.obsidianRobots
                && this.geodeRobots == otherScenario.geodeRobots
                && this.ore <= otherScenario.ore
                && this.clay <= otherScenario.clay
                && this.obsidian <= otherScenario.obsidian
                && this.geodes <= otherScenario.geodes

    private fun produceGeodeRobot() = ProductionScenario(
        index,
        ore - factory.geodeRobotCosts.ore + oreRobots,
        oreRobots,
        clay - factory.geodeRobotCosts.clay + clayRobots,
        clayRobots,
        obsidian - factory.geodeRobotCosts.obsidian + obsidianRobots,
        obsidianRobots,
        geodes + geodeRobots,
        geodeRobots + 1,
        factory
    )

    private fun produceObsidianRobot() = ProductionScenario(
        index,
        ore - factory.obsidianRobotCosts.ore + oreRobots,
        oreRobots,
        clay - factory.obsidianRobotCosts.clay + clayRobots,
        clayRobots,
        obsidian - factory.obsidianRobotCosts.obsidian + obsidianRobots,
        obsidianRobots + 1,
        geodes + geodeRobots,
        geodeRobots,
        factory
    )

    private fun produceClayRobot() = ProductionScenario(
        index,
        ore - factory.clayRobotCosts.ore + oreRobots,
        oreRobots,
        clay - factory.clayRobotCosts.clay + clayRobots,
        clayRobots + 1,
        obsidian - factory.clayRobotCosts.obsidian + obsidianRobots,
        obsidianRobots,
        geodes + geodeRobots,
        geodeRobots,
        factory
    )

    private fun produceOreRobot() = ProductionScenario(
        index,
        ore - factory.oreRobotCosts.ore + oreRobots,
        oreRobots + 1,
        clay - factory.oreRobotCosts.clay + clayRobots,
        clayRobots,
        obsidian - factory.oreRobotCosts.obsidian + obsidianRobots,
        obsidianRobots,
        geodes + geodeRobots,
        geodeRobots,
        factory
    )

    private fun produceNoRobot() = ProductionScenario(
        index,
        ore + oreRobots,
        oreRobots,
        clay + clayRobots,
        clayRobots,
        obsidian + obsidianRobots,
        obsidianRobots,
        geodes + geodeRobots,
        geodeRobots,
        factory
    )

    override fun toString() =
        "Scenario[ore=$oreRobots->$ore,clay=$clayRobots->$clay,obsidian=$obsidianRobots->$obsidian,geodes=>$geodeRobots->$geodes]"
}

class RobotFactory(blueprint: String) {

    val oreRobotCosts: Costs
    val clayRobotCosts: Costs
    val obsidianRobotCosts: Costs
    val geodeRobotCosts: Costs

    init {
        val input =
            "Blueprint [0-9]+: (Each ore.*ore.) (Each clay.*ore.) (Each obsidian.*clay.) (Each geode.*obsidian.)".toRegex()
                .find(blueprint)
        oreRobotCosts = Costs(input!!.groupValues[1])
        clayRobotCosts = Costs(input.groupValues[2])
        obsidianRobotCosts = Costs(input.groupValues[3])
        geodeRobotCosts = Costs(input.groupValues[4])
    }
}

class Costs(blueprint: String) {

    val ore: Int
    val clay: Int
    val obsidian: Int

    init {
        ore = ".* ([0-9]+) ore.*".toRegex().find(blueprint)?.groupValues?.get(1)?.toInt() ?: 0
        clay = ".* ([0-9]+) clay.*".toRegex().find(blueprint)?.groupValues?.get(1)?.toInt() ?: 0
        obsidian = ".* ([0-9]+) obsidian.*".toRegex().find(blueprint)?.groupValues?.get(1)?.toInt() ?: 0
    }

    fun isEnough(ore: Int, clay: Int, obsidian: Int) = this.ore <= ore && this.clay <= clay && this.obsidian <= obsidian

    override fun toString() = "Costs[ore=$ore,clay=$clay,obsidian=$obsidian]"
}
