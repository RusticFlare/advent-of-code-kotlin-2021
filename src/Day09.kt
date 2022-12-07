fun main() {
    fun part1(input: List<String>): Int {
        return input.size
    }

    fun MutableList<MutableSet<Pair<Int, Int>>>.addPoint(point: Pair<Int, Int>): MutableSet<Pair<Int, Int>>? {
        return if (none { basin -> point in basin }) {
            mutableSetOf(point).also { add(it) }
        } else {
            null
        }
    }

    fun List<List<Int>>.exploreBasin(
        point: Pair<Int,Int>,
        visited: MutableSet<Pair<Int, Int>> = mutableSetOf()
    ): Set<Pair<Int,Int>> {
        listOf(
            point.copy(first = point.first - 1),
            point.copy(first = point.first + 1),
            point.copy(second = point.second - 1),
            point.copy(second = point.second + 1),
        ).filter { (row, col) -> getOrNull(row)?.getOrNull(col)?.let { it < 9 } == true }
            .filter { visited.add(it) }
            .forEach { exploreBasin(it, visited) }
        return visited
    }

    fun part2(input: List<String>): Int {
        val heightmap = input.map { depths -> depths.map { depth -> depth.digitToInt() } }
        val basins = mutableListOf<Set<Pair<Int, Int>>>()
        heightmap.asSequence().forEachIndexed { row, depths ->
            depths.asSequence().withIndex()
                .filter { (_, depth) -> depth < 9 }
                .filter { (col) -> basins.none { (row to col) in it } }
                .forEach { (col) -> basins.add(heightmap.exploreBasin(row to col)) }
        }
        val basinSizes = basins.map { it.size }
        return basinSizes.sortedDescending().take(3).reduce(Int::times)
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readLines("Day09_test")
//    check(part1(testInput) == 1)
    check(part2(testInput) == 1134)

    val input = readLines("Day09")
//    println(part1(input))
    check(part2(input) == 1076922)
    println(part2(input))
}
