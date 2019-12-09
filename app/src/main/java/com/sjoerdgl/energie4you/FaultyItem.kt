package com.sjoerdgl.energie4you

class FaultyItem constructor(var name: String, var category: String){
    companion object {
        fun createContactsList(numContacts: Int): ArrayList<FaultyItem> {
            val contacts = ArrayList<FaultyItem>()
            var index = 0

            for (i in 1..numContacts) {
                contacts.add(FaultyItem("Person $i", "Test category $i"))
            }

            return contacts
        }
    }
}