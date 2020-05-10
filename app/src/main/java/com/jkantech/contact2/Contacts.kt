/*
 * Athor="Jonas Kaninda"
 * Android Studio
 * 2019
 *
 */

package com.jkantech.contact2

class Contacts {
    var id = 0
    var name: String
    var phno: String

    constructor(name: String, phno: String) {
        this.name = name
        this.phno = phno
    }

    constructor(id: Int, name: String, phno: String) {
        this.id = id
        this.name = name
        this.phno = phno
    }

}