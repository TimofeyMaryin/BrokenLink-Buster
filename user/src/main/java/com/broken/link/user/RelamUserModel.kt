package com.broken.link.user

import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import org.mongodb.kbson.ObjectId

class RealmUserModel : RealmObject {
    @PrimaryKey var _uId: ObjectId = ObjectId()
    var user: String = ""
    var colorType: Int = -1
    var about: String = ""
}