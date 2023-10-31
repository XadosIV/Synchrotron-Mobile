package com.dubert.synchrotron.model

import com.dubert.synchrotron.R

class Line(
    var name : Char,
    var arrets : ArrayList<String>,
    var logo : Int
) {
    companion object {

        fun charToLineLogo(line : Char): Int {
            return when (line.uppercaseChar()){
                'A' -> R.drawable.line_a
                'B' -> R.drawable.line_b
                'C' -> R.drawable.line_c
                'D' -> R.drawable.line_d
                else -> R.drawable.ic_star //debug case
            }
        }
    }
}