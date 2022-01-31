package com.numq.androidgrpcimageboard.domain.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Post(
    val id: String = "",
    val threadId: String,
    val description: String = "Anon",
    var quoteIds: List<String> = emptyList(),
    val text: String,
    val imageUrl: String = "",
    val videoUrl: String = "",
    val createdAt: Long = System.currentTimeMillis()
) : Parcelable
