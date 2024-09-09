package com.broken.link.buster.data._const

const val TAG = "tag"

enum class UserStatusSignIn {
    GUEST, GOOGLE, DEVELOPER, GUEST_NO_FILLING
}

fun getUserStatusSignInToInt(status: UserStatusSignIn?) : Int {
    return when(status) {
        UserStatusSignIn.GUEST -> 1
        UserStatusSignIn.GOOGLE -> 2
        UserStatusSignIn.DEVELOPER -> 3
        UserStatusSignIn.GUEST_NO_FILLING -> 4
        null -> -1
    }
}