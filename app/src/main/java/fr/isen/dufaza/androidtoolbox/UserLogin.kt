package fr.isen.dufaza.androidtoolbox

class User {
    var name: String = ""
    var firstname: String = ""
    var birthDate: String = ""

    constructor()

    constructor(name: String, firstname: String, date: String)
    {
        this.name = name
        this.firstname = firstname
        this.birthDate = date
    }
}