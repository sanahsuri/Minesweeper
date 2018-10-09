package hu.ait.android.minesweeper.model

import java.util.*


data class Field(var type: Int, var minesAround: Int,
                 var isFlagged: Boolean, var wasClicked: Boolean)

object FieldModel {

    val MINE: Int = 1
    val EMPTY: Int = 0
    var fieldMatrix = Array<Array<Field>>(5) { Array<Field>(5) {Field(EMPTY, 0, false, false)} }
    var counter: Int = 22
    var flagCounter: Int = 3
    var mines = mutableSetOf<Pair<Int, Int>>()

    fun getMines() {
        while (mines.size < 3) {
            var rand = Random()
            mines.add(Pair(rand.nextInt(5), rand.nextInt(5)))
        }
    }

    fun putMines() {
        getMines()
        for (item in mines) {
            var r = item.first
            var c = item.second
            fieldMatrix[r][c].type = MINE
            for (i in r-1 .. r+1) {
                for (j in c-1 .. c+1) {
                    if (i < 5 && i >= 0 && j < 5 && j >= 0) {
                        fieldMatrix[i][j].minesAround++;
                    }
                }
            }
        }
    }

    fun getFieldContent (x: Int, y: Int) = fieldMatrix[x][y]

    fun onWin() : Boolean {
        return (counter <= 0 || flagCounter <= 0)
    }

    fun setFlag (x: Int, y: Int) {
        fieldMatrix[x][y].isFlagged = true
    }

   fun resetModel() {
        for (i in 0..4) {
            for (j in 0..4) {
                fieldMatrix[i][j].type = 0
                fieldMatrix[i][j].wasClicked = false
                fieldMatrix[i][j].minesAround = 0
                fieldMatrix[i][j].isFlagged = false
            }
        }
       mines.clear()
       counter = 22
       flagCounter = 3
    }

}