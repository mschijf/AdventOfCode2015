package tool.math

/* This code is contributed by Devesh Agrawal (and altered by myself) */
private fun combinationUtil(arr: IntArray, data: IntArray, start: Int, end: Int, index: Int, r: Int, allCombinationsList: MutableList<List<Int>>) {
    if (index == r) {
        allCombinationsList.add(data.toList())
        return
    }

    var i = start
    while (i <= end && end - i + 1 >= r - index) {
        data[index] = arr[i]
        combinationUtil(arr, data, i + 1, end, index + 1, r, allCombinationsList)
        i++
    }
}

// return all combinations of split in (split, splitSize)
//   (   split   )
//   ( splitSize )
//
fun getCombinationList(size: Int, splitSize: Int): List<List<Int>> {
    val allCombinationsList = mutableListOf<List<Int>>()
    val data = IntArray(splitSize)
    val arr = IntArray(size){it}
    allCombinationsList.clear()
    combinationUtil(arr, data, 0, size - 1, 0, splitSize, allCombinationsList)
    return allCombinationsList
}


/**
 * returns all permutations of the given list.
 * each 'list' in the returned list of 'list's, conatins one of the permutations.
 * be aware of the combinatorial explosion!
 *
 */

fun <T> makeAllPermutations(elements: List<T>): List<List<T>> {
    return makeAllPermutations(elements.size, elements.toMutableList())
}

private fun <T> swap(elements: MutableList<T>, a: Int, b: Int) {
    val tmp = elements[a]
    elements[a] = elements[b]
    elements[b] = tmp
}

private fun <T> makeAllPermutations(n: Int, elements: MutableList<T>): List<List<T>> {
    if (n == 1) {
        return listOf(elements.toList())
    } else {
        val localList = mutableListOf<List<T>>()
        for (i in 0.. n - 2) {
            localList.addAll(makeAllPermutations(n - 1, elements))
            if (n % 2 == 0) {
                swap(elements, i, n - 1)
            } else {
                swap(elements, 0, n - 1)
            }
        }
        localList.addAll(makeAllPermutations(n - 1, elements))
        return localList
    }
}

fun Long.fac():Long {
    if (this == 0L)
        return 1L
    return (1..this).reduce { acc, i ->  acc*i}
}

fun Int.fac():Long {
    return this.toLong().fac()
}
