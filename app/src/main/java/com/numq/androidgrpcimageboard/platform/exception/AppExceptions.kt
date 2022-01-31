package com.numq.androidgrpcimageboard.platform.exception

object AppExceptions {

    const val DEFAULT_MESSAGE = "Something get wrong."

    object ServiceException : Exception(DEFAULT_MESSAGE) {
        private const val PERMISSION_MESSAGE = "No permission."
        private const val NETWORK_MESSAGE = "Connection unavailable."

        object PermissionException : Exception(PERMISSION_MESSAGE)
        object NetworkException : Exception(NETWORK_MESSAGE)
    }

    object DataException : Exception(DEFAULT_MESSAGE) {

        private const val LOCAL_MESSAGE = "Unable to get local data."
        private const val REMOTE_MESSAGE = "Unable to get remote data."

        object LocalException : Exception(LOCAL_MESSAGE)
        object RemoteException : Exception(REMOTE_MESSAGE)

    }

}