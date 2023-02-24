package uz.rakhmonov.passportapp.myDB

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class MyUser {
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null
    var name: String? = null
    var surname: String? = null
    var image: String? = null
    var position: Int? = 0
    var number_passport: String? = null

    constructor(
        id: Int?,
        name: String?,
        surname: String?,
        image: String?,
        number_passport: String?
    ) {
        this.id = id
        this.name = name
        this.surname = surname
        this.image = image
        this.number_passport = number_passport
    }

    constructor(name: String?, surname: String?, image: String?, number_passport: String?) {
        this.name = name
        this.surname = surname
        this.image = image
        this.number_passport = number_passport
    }

    constructor()
    constructor(
        name: String?,
        surname: String?,
        image: String?,
        position: Int?,
        number_passport: String?
    ) {
        this.name = name
        this.surname = surname
        this.image = image
        this.position = position
        this.number_passport = number_passport
    }


}
