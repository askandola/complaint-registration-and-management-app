package me.askandola.complaintmanagementsystem

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.Query
import me.askandola.complaintmanagementsystem.daos.ComplaintDao
import me.askandola.complaintmanagementsystem.models.Complaint

class AdminUserViewActivity : AppCompatActivity(), ClickInterface {

    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var adapter: ComplaintAdapter
    private lateinit var uid: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_user_view)

        title = "User's Complaints"

        uid = intent.getStringExtra("uid")!!
        recyclerView = findViewById(R.id.adminUsersComplaintsRecyclerView)
        progressBar = findViewById(R.id.adminUserViewProgressBar)

        setUpRecyclerView()
    }

    private fun setUpRecyclerView() {
        val query = ComplaintDao().db.collection("complaints")
            .whereEqualTo(FieldPath.of("user", "uid"), uid)
            .orderBy("date", Query.Direction.DESCENDING)

        val options = FirestoreRecyclerOptions.Builder<Complaint>()
            .setQuery(query, Complaint::class.java)
            .build()

        adapter = ComplaintAdapter(options, this)

        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        progressBar.visibility = View.GONE
    }

    override fun onStart() {
        super.onStart()
        adapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        adapter.stopListening()
    }

    override fun onItemClicked(complaintId: String) {
        val intent2 = Intent(this, AdminComplaintViewActivity::class.java)
        intent2.putExtra("id", complaintId)
        startActivity(intent2)
        finish()
    }
}