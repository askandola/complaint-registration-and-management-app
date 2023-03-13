package me.askandola.complaintmanagementsystem

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import android.view.MenuInflater
import android.view.MenuItem
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    private lateinit var addComplaintButton: Button
    private lateinit var pendingComplaintsButton: Button
    private lateinit var resolvedComplaintsButton: Button
    private lateinit var allComplaintsButton: Button
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        title = "Dashboard"

        auth = Firebase.auth

        addComplaintButton = findViewById(R.id.addComplaintButton)
        pendingComplaintsButton = findViewById(R.id.pendingComplaitsButton)
        resolvedComplaintsButton = findViewById(R.id.resolvedComplaintsButton)
        allComplaintsButton = findViewById(R.id.allComplaintsButton)

        addComplaintButton.setOnClickListener {
            val intent2 = Intent(this, AddComplaintActivity::class.java)
            startActivity(intent2)
        }

        val intent = Intent(this, ComplaintsListActivity::class.java)

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