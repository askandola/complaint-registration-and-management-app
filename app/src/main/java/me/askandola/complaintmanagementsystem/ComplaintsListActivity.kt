package me.askandola.complaintmanagementsystem

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.Query
import com.google.firebase.ktx.Firebase
import me.askandola.complaintmanagementsystem.daos.ComplaintDao
import me.askandola.complaintmanagementsystem.models.Complaint

class ComplaintsListActivity : AppCompatActivity(), ClickInterface {

    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var filter: String
    private lateinit var adapter:ComplaintAdapter
    private var admin: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_complaints_list)

        recyclerView = findViewById(R.id.recyclerView)
        progressBar = findViewById(R.id.complaintsListProgressBar)
        filter = intent.getStringExtra("filter")!!
        admin = intent.getStringExtra("admin")

        title = if(filter=="all") {
            "Complaints"
        }
        else {
            "$filter Complaints"
        }

        setRecyclerView()
    }

    private fun setRecyclerView() {

        val complaintDao = ComplaintDao()

        val query = if(admin!=null) {
                if (filter == "all") {
                    complaintDao.db.collection("complaints")
                        .orderBy("date", Query.Direction.DESCENDING)
                } else {
                    complaintDao.db.collection("complaints")
                        .whereEqualTo("status", filter)
                        .orderBy("date", Query.Direction.DESCENDING)
                }
            }
            else {
                if (filter == "all") {
                    complaintDao.db.collection("complaints")
                        .whereEqualTo(FieldPath.of("user", "uid"), Firebase.auth.currentUser!!.uid)
                        .orderBy("date", Query.Direction.DESCENDING)
                } else {
                    complaintDao.db.collection("complaints")
                        .whereEqualTo(FieldPath.of("user", "uid"), Firebase.auth.currentUser!!.uid)
                        .whereEqualTo("status", filter)
                        .orderBy("date", Query.Direction.DESCENDING)
                }
            }
        val recyclerViewOptions = FirestoreRecyclerOptions.Builder<Complaint>()
            .setQuery(query, Complaint::class.java)
            .build()

        adapter = ComplaintAdapter(recyclerViewOptions, this)

        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        progressBar.visibility = View.GONE
    }



    override fun onItemClicked(complaintId: String) {
        val intent = if(admin!=null) {
            Intent(this, AdminComplaintViewActivity::class.java)
        }
        else {
            Intent(this, ViewComplaintActivity::class.java)
        }
        intent.putExtra("id", complaintId)
        startActivity(intent)
        finish()
    }

    override fun onStart() {
        super.onStart()
        adapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        adapter.stopListening()
    }
}