package com.example.parcialdispositvos.Entities.Login

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
class User (id: Int, name: String, email: String, pass: String){

    @PrimaryKey
    @ColumnInfo(name = "id")
    var id: Int

    @ColumnInfo(name = "name")
    var name: String

    @ColumnInfo(name = "email")
    var email: String

    @ColumnInfo(name = "pass")
    var pass: String

    init{
        this.id = id
        this.name = name
        this.email = email
        this.pass = pass
    }
}