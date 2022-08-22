package com.rkgroup.bookapp.utils

import android.util.Log


fun CharSequence.purify(): String {
    Log.d(TAG, "purify: inputName: $this")
    val findAnySpacialCarList = ArrayList<Int>()

    this.forEachIndexed { index, char ->
        if (char in Constants.fileNameInvalidChars) {
            findAnySpacialCarList.add(index)
        }
    }
    val titleCharsList = this.toMutableList()
    findAnySpacialCarList.forEach {
        titleCharsList[it] = '_'
    }
    val finalName = titleCharsList.joinToString("")
    Log.d(TAG, "purify: outputName: $finalName")
    return finalName
}

private const val TAG = "_String"