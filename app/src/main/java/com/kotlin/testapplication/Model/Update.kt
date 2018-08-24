package com.kotlin.testapplication.Model

abstract class Update(
        val updateType: String,
        val updateUser: Person
        ) {

    class TYPE {
        companion object {
            val STANDARD = "STANDARD"
            val PREMIUM = "PREMIUM"
        }
    }
}

data class StandardUpdate(val user: Person) : Update(Update.TYPE.STANDARD, user)

data class PremiumUpdate(val user: Person) : Update(Update.TYPE.PREMIUM, user)