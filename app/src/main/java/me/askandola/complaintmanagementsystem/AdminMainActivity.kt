package me.askandola.complaintmanagementsystem

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Button
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class AdminMainActivity : AppCompatActivity() {

    private lateinit var pendingComplaintsButton: Button
    private lateinit var resolvedComplaintsButton: Button
    private lateinit var allComplaintsButton: Button
    private lateinit var manageUsersButton: Button
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_main)

        title = "Admin Dashboard"

        auth = Firebase.auth

        pendingComplaintsButton = findViewById(R.id.adminPendingCompButton)
        resolvedComplaintsButton = findViewById(R.id.adminResolvedCompButton)
        allComplaintsButton = findViewById(R.id.adminAllCompButton)
        manageUsersButton = findViewById(R.id.manageUsersButton)

        val intent = Intent(this, ComplaintsListActivity::class.java)
        intent.putExtra("admin", "true")

        pendingComplaintsButton.setOnClickListener {
            intent.putExtra("filter", "Pending")
            startActivity(intent)
        }

        resolvedComplaintsButton.setOnClickListener {
            intent.putExtra("filter", "Resolved")
            startActivity(intent)
        }

        allComplaintsButton.setOnClickListener {
            intent.putExtra("filter", "all")
            startActivity(intent)
        }

        manageUsersButton.setOnClickListener {
            val uIntent = Intent(this, UsersListActivity::class.java)
            startActivity(uIntent)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        MenuInflater(this).inflate(R.menu.menu_logout, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId==R.id.logoutOption) {
            auth.signOut()
            val logoutIntent = Intent(this, SignInActivity::class.java)
            startActivity(logoutIntent)
            finish()
        }
        return super.onOptionsItemSelected(item)
    }
}