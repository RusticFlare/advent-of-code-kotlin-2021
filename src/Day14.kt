fun main() {
    fun part1(input: List<String>): Long {
        return input.size.toLong()
    }

    fun part2(input: String): Long {
        val (initial, transforms) = input.split("\n\n")
        val mappings = transforms.lines().associate { (it[0] to it[1]) to ((it[0] to it[6]) to (it[6] to it[1])) }

        fun Map<Pair<Char, Char>, Long>.nextState(): Map<Pair<Char, Char>, Long> {
            val result = mutableMapOf<Pair<Char, Char>, Long>()
            forEach { (key, count) ->
                val (a, b) = mappings.getValue(key)
                result.compute(a) { _, oldCount -> oldCount?.plus(count) ?: count }
                result.compute(b) { _, oldCount -> oldCount?.plus(count) ?: count }
            }
            return result
        }

        var state = initial.zipWithNext().groupingBy { it }.eachCount().mapValues { (_, count) -> count.toLong() }

        repeat(40) { state = state.nextState() }

        val counts = state.asSequence()
            .groupingBy { (key) -> key.second }
            .aggregate { _, oldCount: Long?, (_, count), _ -> oldCount?.plus(count) ?: count }
            .toMutableMap()
            .apply { compute(initial.first()) { _, oldCount -> oldCount?.plus(1) ?: 1 } }
            .values

        return counts.max() - counts.min()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readText("Day14_test")
//    check(part1(testInput) == 1)
    check(part2(testInput) == 2188189693529)

    val input = readText("Day14")
//    println(part1(input))
    check(part2(input) == 2875665202438)
    println(part2(input))
}
