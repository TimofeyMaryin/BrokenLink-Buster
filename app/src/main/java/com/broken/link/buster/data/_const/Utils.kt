package com.broken.link.buster.data._const

const val TAG = "tag"
const val SHARED_USER_STATUS_NAME = "shared_user_data_name" // название БД
const val USER_STATUS_SHARED_NAME = "user_status_shared_name" // имя для поля статуса юзера

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