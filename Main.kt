const val INVALID_RESPONSE: String = "Invalid response.\n"
const val PLAYER_NAME: String = "Enter player name?"

fun isNumber(text: String): Boolean {

    for(index in 0..text.length - 1) {
        if(!text[index].isDigit()){
            return false
        }
    }
    return true
}

fun makeMenu(): String = "\nWelcome to DEISI Minesweeper\n\n1 - Start New Game\n0 - Exit Game\n"

fun isNameValid(name: String?, minLength: Int = 3): Boolean {
    if (name != null) {
        var hasSpace = false
        var spaceIndex = -1
        var iterations = 0
        while (iterations < name.length) {
            if(iterations == 0 && !name[iterations].isUpperCase()) {
                return false
            } else {
                if (name[iterations] == ' ') {
                    hasSpace = true
                    spaceIndex = iterations
                    if(iterations < minLength) return false
                } else if (hasSpace && iterations == spaceIndex + 1) {
                    if (!name[iterations].isUpperCase()) {
                        return false
                    }
                }
            }
            iterations++
        }
        return hasSpace
    }
    return false
}

fun isValidGameMinesConfiguration(numLines: Int, numColumns: Int, numMines: Int): Boolean {
    return (numMines > 0) && (numMines <=  numLines * numColumns - 2)
}

fun calculateNumMinesForGameConfiguration(numLines: Int, numColumns: Int): Int? {
    val emptySlots = numLines * numColumns - 2
    return if (emptySlots >= 14 && emptySlots <= 20) { 6 }
    else if (emptySlots >= 21 && emptySlots <= 40) { 9 }
    else if (emptySlots >= 41 && emptySlots <= 60) { 12 }
    else if (emptySlots >= 61 && emptySlots <= 79) { 19 }
    else { null }
}

fun createLegend(numColumns: Int): String {
    var drawedLetters = 0
    var legend = ""
    val letters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
    while (drawedLetters < numColumns) {
        if (drawedLetters != numColumns - 1) {
            if(drawedLetters == 0) {
                legend += "${letters[drawedLetters]}  "
            } else {
                legend += " ${letters[drawedLetters]}  "
            }
        }
        else {
            legend += " ${letters[drawedLetters]}"
        }

        drawedLetters++
    }
    return legend
}

fun makeTerrain(matrixTerrain: Array<Array<Pair<String, Boolean>>>, showLegend: Boolean = true,  withColor: Boolean = true,
                showEverything: Boolean = false): String {
    val numColumns = matrixTerrain[0].size
    val numLines = matrixTerrain.size
    var terrain = ""

    val esc: String = "\u001B"
    var legendColor = "$esc[97;44m"
    var endLegendColor = "$esc[0m"

    if (!withColor) {
        legendColor = ""
        endLegendColor = ""
    }

    if (showLegend) {
        terrain += "$legendColor    ${createLegend(numColumns)}    $endLegendColor\n"
        for (linha in 0 until numLines) {
            for (coluna in 0 until numColumns) {
                if (matrixTerrain[linha][coluna].second || showEverything) {
                    if (coluna == 0) {
                        terrain += "$legendColor ${linha+1} $endLegendColor ${matrixTerrain[linha][coluna].first} "
                    } else if (coluna == numColumns - 1) {
                        terrain += "| ${matrixTerrain[linha][coluna].first} $legendColor   $endLegendColor\n"
                    } else {
                        terrain += "| ${matrixTerrain[linha][coluna].first} "
                    }
                } else {
                    if (coluna == 0) {
                        terrain += "$legendColor ${linha+1} $endLegendColor   "
                    } else if (coluna == numColumns - 1) { terrain += "|   $legendColor   $endLegendColor\n"
                    } else { terrain += "|   "
                    }
                }
            }
            if (linha != numLines - 1) {
                terrain += "$legendColor   $endLegendColor" + "---+".repeat(numColumns-1) + "---$legendColor   $endLegendColor\n"
            }
        }
        terrain += "$legendColor" + "    ".repeat(numColumns) + "     $endLegendColor"
    } else {
        for (linha in 0 until numLines) {
            for (coluna in 0 until numColumns) {
                val terrainInVal = "| ${matrixTerrain[linha][coluna].first} "
                if (matrixTerrain[linha][coluna].second || showEverything) {
                    if (coluna == 0) {
                        terrain += " ${matrixTerrain[linha][coluna].first} "
                    } else if (coluna == numColumns - 1) {
                        if(linha == numLines - 1){
                            terrain += terrainInVal
                        } else {
                            terrain += "| ${matrixTerrain[linha][coluna].first} \n"
                        }
                    } else {
                        terrain += terrainInVal
                    }
                } else {
                    if (coluna == 0) {
                        terrain += "   "
                    } else if (coluna == numColumns - 1) {
                        terrain += "|   \n"
                    } else {
                        terrain += "|   "
                    }
                }
            }
            if (linha != numLines - 1) {
                terrain += "---+".repeat(numColumns-1) + "---\n"
            }
        }
    }
    return terrain
}

fun askName(result: String): String {
    var playerName: String
    while (true) {
        playerName = readLine().toString()
        if (isNameValid(playerName)) {
            return playerName
        } else {
            println(INVALID_RESPONSE)
            println(result)
        }
    }
}

fun askLegend(): Boolean {
    while (true) {
        println("Show legend (y/n)?")
        val showLegendStr = readLine().toString()
        if (showLegendStr != "y" && showLegendStr != "n") {
            println(INVALID_RESPONSE)
        } else {
            return showLegendStr == "y"
        }
    }
}

fun askLinesNum(): Int {
    var howManyLines: Int?
    while (true) {
        println("How many lines?")
        howManyLines = readLine()?.toIntOrNull()
        if (howManyLines != null && (howManyLines in 4..9)) {
            return howManyLines
        } else {
            println(INVALID_RESPONSE)
        }
    }
}

fun askColumnsNum(): Int {
    var howManyColumns: Int?
    while (true){
        println("How many columns?")
        howManyColumns = readLine()?.toIntOrNull()
        if (howManyColumns != null && (howManyColumns in 4..9)) {
            return howManyColumns
        } else {
            println(INVALID_RESPONSE)
        }
    }
}

fun askMinesNum(linesNum: Int, columnsNum: Int): Int? {
    var howManyMines: Int?
    while (true) {
        println("How many mines (press enter for default value)?")
        howManyMines = readLine()?.toIntOrNull()
        if (howManyMines == null) {
            howManyMines = calculateNumMinesForGameConfiguration(linesNum, columnsNum)
            return howManyMines
        } else {
            if (!isValidGameMinesConfiguration(linesNum, columnsNum, howManyMines)) {
                println(INVALID_RESPONSE)
            } else {
                return howManyMines
            }
        }
    }
}

fun createMatrixTerrain(numLines: Int, numColumns: Int, numMines: Int, ensurePathToWin: Boolean = false): Array<Array<Pair<String,Boolean>>>{

    val arrayPairs = Array(numLines){Array(numColumns){Pair(" ", false)}}
    arrayPairs[0][0] = Pair("P", true)
    arrayPairs[numLines-1][numColumns-1] = Pair("f", true)

    if(!ensurePathToWin){
        for(i in 0 until numMines){
            var pos = true
            while(pos) {
                val randomNumLine = (0 until numLines).random()
                val randomNumColumn = (0 until numColumns).random()
                val placingMine = arrayPairs[randomNumLine][randomNumColumn]

                if(placingMine == Pair(" ", false)) {
                    arrayPairs[randomNumLine][randomNumColumn] = Pair("*", false)
                    pos = false
                }
            }
        }
    } else {
        for(i in 0 until numMines){
            var pos = true
            while(pos) {
                val randomNumLine = (0 until numLines).random()
                val randomNumColumn = (0 until numColumns).random()
                val placingMine = arrayPairs[randomNumLine][randomNumColumn]
                val terrain = getSquareAroundPoint(randomNumLine, randomNumColumn, numLines = arrayPairs.size, numColumns = arrayPairs[0].size)
                val yLeft = terrain.first.first
                val xLeft = terrain.first.second
                val yRight = terrain.second.first
                val xRight = terrain.second.second

                if (placingMine.first == " " && isEmptyAround(matrixTerrain = arrayPairs, randomNumLine, randomNumColumn,
                        yLeft, xLeft, yRight, xRight)) {
                    arrayPairs[randomNumLine][randomNumColumn] = Pair("*", false)
                    pos = false
                }
            }
        }
    }
    return arrayPairs
}

fun fillNumberOfMines(matrixTerrain: Array<Array<Pair<String, Boolean>>>){

    for(i in 0 until matrixTerrain.size){
        for(j in 0 until matrixTerrain[0].size){
            val numberOfMines = countNumberOfMinesCloseToCurrentCell(matrixTerrain, i, j)
            if(matrixTerrain[i][j].first == " " && numberOfMines > 0){
                matrixTerrain[i][j] = Pair(numberOfMines.toString(), false)
            }
        }
    }
}

fun countNumberOfMinesCloseToCurrentCell(matrixTerrain: Array<Array<Pair<String, Boolean>>>, centerY: Int, centerX: Int): Int{

    val square = getSquareAroundPoint(centerY, centerX, numLines = matrixTerrain.size, numColumns = matrixTerrain[0].size)

    val yLeft = square.first.first
    val xLeft = square.first.second
    val yRight = square.second.first
    val xRight = square.second.second

    var mines = 0

    for(i in yLeft..yRight){
        for(j in xLeft..xRight){
            if(!(i == centerY && j == centerX)){
                if(matrixTerrain[i][j].first == "*"){
                    mines++
                }
            }
        }
    }
    return mines
}

fun getSquareAroundPoint(linha: Int, coluna: Int, numLines: Int, numColumns: Int): Pair<Pair<Int, Int>, Pair<Int, Int>>{

    val yLeft = if(linha - 1 !in 0 until numLines) linha else linha - 1
    val xLeft = if(coluna - 1 !in 0 until numColumns) coluna else coluna - 1
    val yRight = if(linha + 1 !in 0 until numLines) linha else linha + 1
    val xRight = if(coluna + 1 !in 0 until numColumns) coluna else coluna + 1

    val coordenates = Pair(Pair(yLeft, xLeft), Pair(yRight, xRight))

    return coordenates

}

fun revealMatrix(matrixTerrain: Array<Array<Pair<String, Boolean>>>, coordY: Int, coordX: Int, endGame: Boolean = false){

    val coordenates = getSquareAroundPoint(coordY, coordX, matrixTerrain.size, matrixTerrain[0].size)

    val yLeft = coordenates.first.first
    val xLeft = coordenates.first.second
    val yRight = coordenates.second.first
    val xRight = coordenates.second.second

    for(i in yLeft..yRight){
        for(j in xLeft..xRight){
            if(!endGame){
                if(matrixTerrain[i][j].first != "*"){
                    matrixTerrain[i][j] = Pair(matrixTerrain[i][j].first, true)
                }
            } else {
                matrixTerrain[i][j] = Pair(matrixTerrain[i][j].first, true)
            }
        }
    }
}

fun isEmptyAround(matrixTerrain: Array<Array<Pair<String, Boolean>>>, centerY: Int, centerX: Int, yl: Int, xl: Int, yr: Int, xr: Int): Boolean{

    for(i in yl..yr){
        for(j in xl..xr){
            if(!(centerY == i && centerX == j) && (matrixTerrain[i][j].first == "*" || matrixTerrain[i][j].first == "P"
                        || matrixTerrain[i][j].first == "f")){
                return false
            }
        }
    }
    return true
}

fun isMovementPValid(currentCoord : Pair<Int, Int>, targetCoord : Pair<Int, Int>): Boolean{

    return targetCoord.first in currentCoord.first - 1..currentCoord.first + 1
            && targetCoord.second in currentCoord.second - 1..currentCoord.second + 1
}

fun isCoordinateInsideTerrain(coord: Pair<Int, Int>, numColumns: Int, numLines: Int): Boolean{

    return (coord.first < numColumns && coord.second < numLines) && (coord.first >= 0 && coord.second >= 0)

}

fun getCoordinates(readText: String?): Pair<Int, Int>?{
    if (readText != null && readText.length == 2){
        var occurrences = 0
        if(readText[0].isDigit()){
            val value = readText[0].toInt() - 49
            if(readText[1].isLetter()){
                val legendLetter = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
                val legendLetter2 = "abcdefghijklmnopqrstuvwxyz"
                while((readText[1] != legendLetter[occurrences]) && (readText[1] != legendLetter2[occurrences])){
                    if(occurrences != legendLetter.length - 1){
                        occurrences++
                    }
                }
                if (value >= 0){
                    return Pair(value, occurrences)
                } else {
                    return null
                }
            } else {
                return null
            }
        } else {
            return null
        }
    }
    return null
}

fun coordinatesImput(matrix: Array<Array<Pair<String, Boolean>>>,
                     playerCoordinates: Pair<Int, Int>, howManyColumns: Int, howManyLines: Int, showLegend: Boolean): Pair<Int, Int>?{

    var coordinatesTemp : Pair<Int, Int>? = Pair(0, 0)
    do {
        println("Choose the Target cell (e.g 2D)")
        val targetCell = readLine()
        coordinatesTemp = getCoordinates(targetCell)
        if (coordinatesTemp == null || !isMovementPValid(playerCoordinates, coordinatesTemp) ||
            !isCoordinateInsideTerrain(coordinatesTemp, howManyColumns, howManyLines)
        ) {
            if(targetCell != null && targetCell == "abracadabra"){
                println(makeTerrain(matrix, showLegend, showEverything = true))
            } else if(targetCell != null && targetCell == "exit")
            {return null} else {
                print(INVALID_RESPONSE)
                println(makeTerrain(matrix, showLegend))
            }
        }
    } while (coordinatesTemp == null || !isMovementPValid(playerCoordinates, coordinatesTemp) ||
        !isCoordinateInsideTerrain(coordinatesTemp, howManyColumns, howManyLines))
    return coordinatesTemp
}

fun main() {
    do {
        var validOption = false
        var result = ""
        while (!validOption) {
            println(makeMenu())

            val start = readLine().toString()
            if (isNumber(start)) {
                if (start.toInt() == 1) {
                    validOption = true
                    result = PLAYER_NAME
                } else if (start.toInt() == 0) {
                    validOption = true
                    return
                } else {
                    println(INVALID_RESPONSE)
                }
            } else {
                println(INVALID_RESPONSE)
            }
        }
        println(result)

        var endGame = false

        if (result == PLAYER_NAME) {
            val playerName: String = askName(result)
            val showLegend = askLegend()
            val howManyLines = askLinesNum()
            val howManyColumns = askColumnsNum()
            val howManyMines: Int? = askMinesNum(howManyLines, howManyColumns)

            if (howManyMines != null) {
                val matrix = createMatrixTerrain(howManyLines, howManyColumns, howManyMines)
                fillNumberOfMines(matrix)
                revealMatrix(matrix, 0, 0)
                println(makeTerrain(matrix, showLegend))

                var coordinates: Pair<Int, Int>?
                var playerCoordinates = Pair(0, 0)
                var lastPosition = Pair(" ", true)
                var lastPositionCoordinates = Pair(0, 0)

                do {
                    coordinates = coordinatesImput(matrix, playerCoordinates, howManyColumns, howManyLines, showLegend)
                    if(coordinates == null) return
                    if (matrix[coordinates!!.first][coordinates.second].first == "*") {
                        println(makeTerrain(matrix, showLegend, showEverything = true))
                        println("You lost the game!")
                        endGame = true
                    } else if (matrix[coordinates.first][coordinates.second].first == "f") {
                        println(makeTerrain(matrix, showLegend, showEverything = true))
                        println("You win the game")
                        endGame = true
                    } else {
                        matrix[lastPositionCoordinates.first][lastPositionCoordinates.second] = lastPosition
                        lastPosition = matrix[coordinates.first][coordinates.second]
                        lastPositionCoordinates = Pair(coordinates.first, coordinates.second)
                        matrix[coordinates.first][coordinates.second] = Pair("P", true)
                        playerCoordinates = Pair(coordinates.first, coordinates.second)
                        revealMatrix(matrix, coordinates.first, coordinates.second)
                        println(makeTerrain(matrix, showLegend))
                    }
                } while (matrix[coordinates!!.first][coordinates.second].first != "*" && matrix[coordinates.first][coordinates.second].first != "f")

            }
        }
    } while(result != PLAYER_NAME || endGame == true)
}