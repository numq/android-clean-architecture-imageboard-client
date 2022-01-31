package com.numq.androidgrpcimageboard.domain.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Thread(
    val id: String = "",
    val boardId: String,
    val postCount: Long = 1L,
    val title: String,
    val createdAt: Long = System.currentTimeMillis(),
    val bumpedAt: Long = System.currentTimeMillis()
) : Parcelable
