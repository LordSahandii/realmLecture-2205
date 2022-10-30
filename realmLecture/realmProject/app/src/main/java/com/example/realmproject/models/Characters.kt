package com.example.realmproject.models

import io.realm.kotlin.types.ObjectId
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey

/**
 * Character model
 */
class Characters: RealmObject{
    @PrimaryKey
    var _id: ObjectId = ObjectId.create()
    var name: String = ""
    var age: Int = 0
    var isEvil: Boolean = false
    var specs: Enum<Specs> = Specs.Unknown
}

/**
 * Middle-Earth Race in Enum format
 */
enum class Specs{
    HOBBITS,
    HUMANS,
    ELVES,
    DWARFS,
    ORCS,
    GOBLINS,
    DarkElves,
    Unknown
}