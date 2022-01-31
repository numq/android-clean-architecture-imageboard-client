package com.numq.androidgrpcimageboard.domain.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Board(
    val id: String = "",
    val title: String,
    val description: String = "",
    val imageUrl: String = "",
    val isAdult: Boolean = false
) : Parcelable