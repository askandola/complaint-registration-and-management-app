package me.askandola.complaintmanagementsystem.daos

import android.content.Context
import android.widget.Toast
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import me.askandola.complaintmanagementsystem.models.Complaint
import me.askandola.complaintmanagementsystem.models.User
import java.text.SimpleDateFormat
import java.util.*

class ComplaintDao {
    val db = FirebaseFirestore.getInstance()

    fun addComplaint(context:Context, title: String, desc: String) {
        GlobalScope.launch(Dispatchers.IO) {
            val uid = Firebase.auth.currentUser!!.uid
            val user = UserDao().getUserById(uid).await().toObject(User::class.java)!!
            val sdf = SimpleDateFormat("yyyy/MM/dd", Locale.US)
            val date = sdf.format(Date())
            val complaint = Complaint(title, desc, date, "", "Pending", user)
            db.collection("complaints").document().set(complaint).addOnCompleteListener {
                if(it.isSuccessful) {
                    Toast.makeText(context, "Complaint filed successfully", Toast.LENGTH_SHORT).show()
                }
                else {
                    Toast.makeText(context, "Something went wrong. Please try again.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    fun updateComplaint(id: String, status: String, verdict: String) {
        val mp = hashMapOf("status" to status, "verdict" to verdict)
        GlobalScope.launch(Dispatchers.IO) {
            db.collection("complaints").document(id).set(mp, SetOptions.merge())
        }
    }

    fun getComplaintById(complaintId: String): Task<DocumentSnapshot> {
        return db.collection("complaints").document(complaintId).get()
    }

    fun deleteComplaintById(complaintid: String) {
        GlobalScope.launch(Dispatchers.IO) {
            db.collection("complaints").document(complaintid).delete()
        }
    }
}