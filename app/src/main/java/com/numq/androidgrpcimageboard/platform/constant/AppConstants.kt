package com.numq.androidgrpcimageboard.platform.constant

object AppConstants {

    object Prefs {
        const val APP_PREFS = "APP_PREFERENCES"
    }

    object Nav {
        const val HOME = "home"
        const val BOARD = "board"
        const val THREAD = "thread"
    }

    object Network {
//        const val GRPC_ADDRESS = "127.0.0.1 || 0.0.0.0 || 192.168.41.2 || 10.0.3.2 || 10.0.2.2"
        const val GRPC_ADDRESS = "10.0.3.2"
        const val GRPC_PORT = 8080
    }

    object Args {
        const val ACTIVE_BOARD_ID = "ACTIVE_BOARD_ID"
        const val ACTIVE_THREAD_ID = "ACTIVE_THREAD_ID"
    }

    object Paging {
        const val DEFAULT_SKIP = 0L
        const val DEFAULT_LIMIT = 10L
    }

    object Database {
        const val DB_NAME = "imageboard"
        const val TABLE_BOARDS = "boards"
        const val TABLE_THREADS = "threads"
        const val TABLE_POSTS = "posts"
    }

    object UI {
        const val IMAGE_TEMPLATE_URI = "https://i1.ytimg.com/vi/%s/default.jpg"
    }

}