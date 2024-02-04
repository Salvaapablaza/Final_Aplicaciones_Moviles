package com.example.parcialdispositvos.Entities.Mascotas




class Mascota (identifier:String,edad: Int, nombre: String, raza: String, nombredueno: String) {


    var identifier: String

    var edad: Int

    var nombre: String

    var nombredueno: String

    var raza: String

    constructor() : this("",0,"","","")

    init{
        this.identifier = identifier
        this.edad = edad
        this.nombre = nombre
        this.nombredueno = nombredueno
        this.raza = raza
    }


}