package com.numq.androidgrpcimageboard.platform.extension.mapping

import com.numq.androidgrpcimageboard.data.board.local.BoardDataModel
import com.numq.androidgrpcimageboard.domain.entity.Board
import proto.BoardOuterClass

val Board.proto: BoardOuterClass.Board
    get() = BoardOuterClass.Board.newBuilder()
        .setId(id)
        .setTitle(title)
        .setDescription(description)
        .setImageUrl(imageUrl)
        .setIsAdult(isAdult)
        .build()

val BoardOuterClass.Board.entity: Board
    get() = Board(id, title, description, imageUrl, isAdult)

val Board.model: BoardDataModel
    get() = BoardDataModel(id, title, description, imageUrl, isAdult)

val BoardDataModel.entity: Board
    get() = Board(id, title, description, imageUrl, isAdult)