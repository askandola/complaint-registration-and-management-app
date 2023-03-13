package me.askandola.complaintmanagementsystem.daos

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import me.askandola.complaintmanagementsystem.models.User

class UserDao {
    val db = Firebase.firestore

    fun setUser(user: User?) {
        GlobalScope.launch(Dispatchers.IO) {
            user?.let {
                db.collection("users").document(user.uid).set(it)
            }
        }
    }

    fun getUserById(uid: String): Task<DocumentSnapshot> {
        return db.collection("users").document(uid).get()
    }
}