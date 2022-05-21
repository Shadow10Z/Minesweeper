
fun main(){
    println(printMatrixOfPairs(createMatrixTerrain(5, 5, 4)))
}

fun printMatrixOfPairs(array: Array<Array<Pair<String, Boolean>>>): String {

    var striguified_array: String = ""
    for (line in array) {
        for (slot in line) {
            striguified_array += "Pair(" + slot.first + ", " + slot.second + "),"
        }
        striguified_array += "\n"
    }

    return striguified_array
}