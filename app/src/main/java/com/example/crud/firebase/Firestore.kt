package com.example.crud.firebase

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class Firestore private constructor() {
    val db = Firebase.firestore

    companion object {
        val instance: Firestore by lazy { Firestore() }
    }

    fun getCollection(): CollectionReference {
        return db.collection("users")
    }

    fun getDocument(userid: String): DocumentReference {
        return getCollection().document(userid)
    }
}