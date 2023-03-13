package me.askandola.complaintmanagementsystem

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import me.askandola.complaintmanagementsystem.daos.UserDao
import me.askandola.complaintmanagementsystem.models.User

class UsersListActivity : AppCompatActivity(), UserClickInterface {

    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var adapter: UserAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_users_list)

        title = "Users"

        recyclerView = findViewById(R.id.usersRecyclerView)
        progressBar = findViewById(R.id.usersListProgressBar)

        setRecyclerView()
    }

    private fun setRecyclerView() {
        val query = UserDao().db.collection("users")
            .whereEqualTo("admin", "false")

        val options = FirestoreRecyclerOptions.Builder<User>()
            .setQuery(query, User::class.java)
            .build()

        adapter = UserAdapter(options, this)

        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        progressBar.visibility  =View.GONE
    }

    override fun onStart() {
        super.onStart()
        adapter.startListening()
    }

    override fun onResume() {
        super.onResume()
        adapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        adapter.stopListening()
    }

    override fun onItemClicked(userId: String) {
        val intent = Intent(this, AdminUserViewActivity::class.java)
        intent.putExtra("uid", userId)
        startActivity(intent)
        finish()
    }
}